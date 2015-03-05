package cn.bobdeng;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.google.gson.Gson;
import com.sun.istack.internal.logging.Logger;

public class RepostThread implements Runnable {
	Logger logger = Logger.getLogger(RepostThread.class);
	private static RepostThread instance = new RepostThread();
	public static RepostThread getInstance() {
		return instance;
	}

	private Map<String, Twitter> mapRepost = new HashMap<String, Twitter>();
	public Collection<Twitter> getMission(){
		return mapRepost.values();
	}
	private RepostThread() {
		new Thread(this).start();
	}

	public void run() {
		while (true) {
			if (mapRepost.size() > 0) {
				Collection<Twitter> twitters = mapRepost.values();
				for (Twitter t : twitters) {
					try {
						logger.warning("开始转发Twitter "+t.getName()+","+t.getUser());
						t.readPage();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				try {
					Thread.sleep(10 * 1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				logger.warning("没有任务");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void weiboLogin(String code, String name, String twitterName)
			throws Exception {
		if(name==null){
			name=UUID.randomUUID().toString();
		}
		logger.warning("登陆新浪微博");
		String rlt = new HttpUtils()
				.setUrl("https://api.weibo.com/oauth2/access_token")
				.addParam("client_id", "1266993502")
				.addParam("client_secret", "be57576737b13978fdeb64d4988111d8")
				.addParam("grant_type", "authorization_code")
				.addParam("redirect_uri", "http://bobdeng.cn/weibo.jsp")
				.addParam("code", code).httpsPost();
		AccessToken token = new Gson().fromJson(rlt, AccessToken.class);
		System.out.println(token.getAccess_token());
		String tokenStr = token.getAccess_token();
		WeiboInterface weibo = new Weibo(tokenStr);
		weibo.updateName();
		Twitter tr = new Twitter(name, twitterName, weibo);
		this.mapRepost.put(name, tr);

	}

	public void qqLogin(String code, String name, String twitterName)
			throws Exception {
		if(name==null){
			name=UUID.randomUUID().toString();
		}
		logger.warning("登陆qq");
		String rlt = new HttpUtils()
				.setUrl("https://graph.qq.com/oauth2.0/token")
				.addParam("client_id", "101197156")
				.addParam("client_secret", "645f26fc887e7257d41506a9475e5e15")
				.addParam("grant_type", "authorization_code")
				.addParam("redirect_uri", "http://bobdeng.cn/qq.jsp")
				.addParam("code", code).httpsPost();
		String[] params=rlt.split("&");
		String token=null;
		for(String param:params){
			String[] nv=param.split("=");
			if(nv.length==2){
				if(nv[0].equals("access_token")){
					token=nv[1];
					break;
				}
			}
		}
		if(token==null){
			throw new Exception("login fail:"+rlt);
		}
		System.out.println(token);
		WeiboInterface weibo = new TWeibo(token);
		weibo.updateName();
		Twitter tr = new Twitter(name, twitterName, weibo);
		this.mapRepost.put(name, tr);
	}

}
