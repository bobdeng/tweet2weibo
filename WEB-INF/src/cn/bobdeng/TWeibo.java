package cn.bobdeng;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.sun.istack.internal.logging.Logger;

public class TWeibo implements WeiboInterface {

	Logger logger = Logger.getLogger(Weibo.class);
	private String token;
	private String name;
	private String openid;

	public TWeibo(String token) {
		super();
		this.token = token;
	}

	public void postWeibo(Tweet t) throws Exception {
		String rlt = null;
		if (t.getImages() == null || t.getImages().size() == 0) {
			logger.info("发纯文本微博");
			rlt = new HttpUtils().setUrl("https://graph.qq.com/t/add_t")
					.addParam("content", t.getContent())
					.addParam("access_token", token)
					.addParam("oauth_consumer_key", "101197156")
					.addParam("openid", openid).addParam("format", "json")
					.httpsPost();
		} else {
			logger.info("发带图片微博");
			rlt = new HttpUtils()
					.setUrl("https://graph.qq.com/t/add_pic_t")
					.addParam("content", t.getContent())
					.addParam("access_token", token)
					.addParam("oauth_consumer_key", "101197156")
					.addParam("openid", openid)
					.addParam("content", t.getContent())
					.addParam("format", "json")
					.addFile(
							"pic",
							new Utils().getOneImage(t.getImages())
									.getAbsolutePath()).httpsUpload();
		}
		logger.info(rlt);

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "腾讯微博:" + name;
	}

	public TWeibo setName(String name) {
		this.name = name;
		return this;
	}

	@Override
	public void updateName() throws Exception {
		String rlt = new HttpUtils().setUrl("https://graph.qq.com/oauth2.0/me")
				.addParam("access_token", token)
				.addParam("oauth_consumer_key", "101197156").httpsPost();
		Pattern pattern = Pattern.compile("callback\\(\\s*(\\S+)\\s*\\);\\n",
				Pattern.MULTILINE);
		Matcher mather = pattern.matcher(rlt);
		logger.info(rlt);
		if (mather.matches()) {
			OpenIdResult openIdResult = new Gson().fromJson(mather.group(1),
					OpenIdResult.class);
			updateName(openIdResult.openid);
			this.openid = openIdResult.openid;

		} else {
			logger.warning("parse fail");
		}
	}

	private void updateName(String openId) throws Exception {
		String rlt = new HttpUtils()
				.setUrl("https://graph.qq.com/user/get_user_info")
				.addParam("access_token", token)
				.addParam("oauth_consumer_key", "101197156")
				.addParam("openid", openId).httpsPost();
		logger.info(rlt);
		UserInfo user = new Gson().fromJson(rlt, UserInfo.class);
		this.name = user.getNickname();

	}

	public static void main(String[] args) throws Exception {
		TWeibo weibo = new TWeibo("5828B6F0EC4B7D6153D56C6AAB66527E");
		weibo.updateName();
		System.out.println(weibo.openid);
		weibo.postWeibo(new Tweet().setContent("hello中文").addImage("https://pbs.twimg.com/media/B_TCsMbVEAEirYW.jpg:large"));
	}

	public class OpenIdResult {
		private String openid;

		public String getOpenid() {
			return openid;
		}

		public void setOpenid(String openid) {
			this.openid = openid;
		}

	}

	public class UserInfo {
		private String nickname;

		public String getNickname() {
			return nickname;
		}

		public void setNickname(String nickname) {
			this.nickname = nickname;
		}

	}

}
