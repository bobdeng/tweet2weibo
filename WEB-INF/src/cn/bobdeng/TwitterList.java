package cn.bobdeng;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TwitterList {

	private String user;
	private Weibo weibo;
	private static final String RSS = "https://twitter.com/";
	private static final String lastIdFile="/usr/local/config.txt";
	public TwitterList(String user, Weibo weibo) {
		super();
		this.user = user;
		this.weibo = weibo;
	}

	public void readPage() throws Exception {
		List<Tweet> newList = readNewTweet();
		for (int i =newList.size()-1; i >=0; i--) {
			Thread.sleep(20000);
			repostContent(newList.get(i));
		}
	}

	private void repostContent(Tweet t) throws Exception {
		System.out.println("repost " + t);
		weibo.postWeibo(t);
		writeLastId(t.getId());

	}

	private void writeLastId(String id) throws Exception {
		File file = new File(lastIdFile);
		if (!file.exists()) {
			file.createNewFile();
		}
		OutputStream output = null;
		try {
			output = new FileOutputStream(file);
			output.write(id.getBytes());
		} finally {
			output.close();
		}

	}

	public List<Tweet> readNewTweet() {
		List<Tweet> ids = readIds();
		System.out.println("read tweet "+ids.size());
		String lastId = getLastId();
		List<Tweet> newIds = new ArrayList<Tweet>();
		for (Tweet tweet : ids) {
			if (tweet.getId().equals(lastId)) {
				break;
			}
			newIds.add(tweet);
		}
		System.out.println("new tweet "+newIds.size());
		return newIds;
	}
	
	private String getLastId() {
		try {
			File file = new File(lastIdFile);
			if (!file.exists()) {
				return null;
			}
			InputStream input = new FileInputStream(file);
			byte[] buffer = new byte[input.available()];
			input.read(buffer);
			return new String(buffer);
		} catch (Exception e) {

		}
		return null;
	}

	private List<Tweet> readIds() {
		List<Tweet> ids = new ArrayList<Tweet>();
		try {
			Document doc = Jsoup.parse(new HttpUtils().setUrl(
					RSS + user + "/with_replies").httpsGet());
			Element root = doc.select(".GridTimeline-items").get(0);
			Elements streamItems = doc.select(".StreamItem");
			for (int i = 0; i < streamItems.size(); i++) {
				Tweet t = new Tweet();
				Element streamItem = streamItems.get(i);
				String id = streamItem.attr("data-item-id");
				t.setId(id);
				Elements elements = streamItem.select(".ProfileTweet-contents");
				Element element = elements.get(0);
				Element p = element.select("p").get(0);
				t.setContent(p.text());
				Elements images = streamItem
						.select(".TwitterPhoto-mediaSource");
				if (images.size() > 0) {
					for (int j = 0; j < images.size(); j++) {
						t.addImage(images.get(j).attr("src"));
					}
				}
				ids.add(t);
				// System.out.println(t);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ids;
	}

	public static void main(String[] args) throws Exception {
		TwitterList tl = new TwitterList("fangshimin", new Weibo(
				"2.00jrXvCGEtKk4B385b3c86361s8yGD"));
		tl.readPage();
	}

	public Weibo getWeibo() {
		return weibo;
	}

	public TwitterList setWeibo(Weibo weibo) {
		this.weibo = weibo;
		return this;
	}
}
