package cn.bobdeng;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bobdeng.TWeibo.OpenIdResult;
import cn.bobdeng.TWeibo.UserInfo;

import com.google.gson.Gson;
import com.sun.istack.internal.logging.Logger;

public class Weibo implements WeiboInterface{
	Logger logger=Logger.getLogger(Weibo.class);
	private String token;
	private String name;
	public Weibo(String token) {
		super();
		this.token = token;
	}

	public void postWeibo(Tweet t) throws Exception {
		String rlt = null;
		if (t.getImages() == null || t.getImages().size() == 0) {
			logger.info("发纯文本微博");
			rlt = new HttpUtils()
					.setUrl("https://api.weibo.com/2/statuses/update.json")
					.addParam("status", t.getContent())
					.addParam("access_token", token).httpsPost();
		}else{
			logger.info("发带图片微博");
			rlt = new HttpUtils()
			
			.setUrl("https://upload.api.weibo.com/2/statuses/upload.json")
			.addParam("status", t.getContent())
			.addFile("pic", new Utils().getOneImage(t.getImages()).getAbsolutePath())
			.addParam("access_token", token).httpsUpload();
		}
		logger.info(rlt);

	}
	
	public static void main(String[] args) throws Exception {
		Weibo weibo = new Weibo("2.00jrXvCGEtKk4B385b3c86361s8yGD");
		List<String> images = new ArrayList<String>();
		images.add("https://pbs.twimg.com/media/B_Ge-L9U0AAZ59k.jpg:large");
		//images.add("https://pbs.twimg.com/media/B_Gm8MqVIAApUr7.jpg:large");
		//images.add("https://pbs.twimg.com/media/B_GJezeU4AEQLJ-.jpg:large");
		//weibo.getOneImage(images);
		weibo.postWeibo(new Tweet().setContent("测试"+System.currentTimeMillis()).setImages(images));
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "新浪微博："+name;
	}

	public Weibo setName(String name) {
		this.name = name;
		return this;
	}

	@Override
	public void updateName() throws Exception{
		String rlt=new HttpUtils().setUrl("https://api.weibo.com/2/account/get_uid.json?access_token="+token).httpsGet();
			OpenIdResult openIdResult=new Gson().fromJson(rlt, OpenIdResult.class);
			updateName(openIdResult.uid);
	}
	private void updateName(String openId)throws Exception{
		String rlt=new HttpUtils().setUrl("https://api.weibo.com/2/users/show.json?access_token="+token+"&uid="+openId)
				.httpsGet();
		logger.info(rlt);
		UserInfo user=new Gson().fromJson(rlt, UserInfo.class);
		this.name=user.getScreen_name();
		
	}
	
	public class OpenIdResult{
		private String uid;

		public String getUid() {
			return uid;
		}

		public void setUid(String uid) {
			this.uid = uid;
		}

		
	}
	public class UserInfo{
		private String screen_name;

		public String getScreen_name() {
			return screen_name;
		}

		public void setScreen_name(String screen_name) {
			this.screen_name = screen_name;
		}

		
	}

}
