package cn.bobdeng;

import com.google.gson.Gson;
import com.sun.istack.internal.logging.Logger;

public class RepostThread implements Runnable{
	Logger logger=Logger.getLogger(RepostThread.class);
	private static RepostThread instance=new RepostThread();
	private String token=null;
	public static RepostThread getInstance(){
		return instance;
	}
	private RepostThread(){
		new Thread(this).start();
	}
	public void run(){
		while(true){
			if(token!=null){
				Weibo weibo=new Weibo(token);
				Twitter tr=new Twitter("fangshimin", weibo);
				try{
					logger.warning("开始转发Twitter");
					tr.readPage();
				}catch(Exception e){
					e.printStackTrace();
				}
				try {
					Thread.sleep(10*1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				logger.warning("还没有登陆新浪微博");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	public String getToken() {
		return token;
	}
	public RepostThread setToken(String token) {
		this.token = token;
		return this;
	}
	public void login(String code)throws Exception{
		logger.warning("登陆新浪微博");
		String rlt = new HttpUtils()
		.setUrl("https://api.weibo.com/oauth2/access_token")
		.addParam("client_id", "1266993502")
		.addParam("client_secret", "be57576737b13978fdeb64d4988111d8")
		.addParam("grant_type", "authorization_code")
		.addParam("redirect_uri", "http://bobdeng.cn/twitter.jsp")
		.addParam("code", code)
		.httpsPost();
		AccessToken token=new Gson().fromJson(rlt, AccessToken.class);
		System.out.println(token.getAccess_token());
		this.token=token.getAccess_token();
	}
	
}
