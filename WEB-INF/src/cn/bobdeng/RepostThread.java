package cn.bobdeng;

import com.google.gson.Gson;

public class RepostThread implements Runnable{

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
				TwitterList tr=new TwitterList("fangshimin", weibo);
				try{
					tr.readPage();
				}catch(Exception e){
					e.printStackTrace();
				}
				try {
					Thread.sleep(60*1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
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
