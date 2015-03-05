package cn.bobdeng;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.sun.istack.internal.logging.Logger;

public class Twitter {
	Logger logger=Logger.getLogger(Twitter.class);
	private String user;
	private String name;
	private WeiboInterface weibo;
	private static final String RSS = "https://twitter.com/";
	private static final String lastIdFile="/usr/local/";
	public Twitter(String name,String user, WeiboInterface weibo) {
		super();
		this.name=name;
		this.user = user;
		this.weibo = weibo;
	}

	public void readPage() throws Exception {
		List<Tweet> newList = readNewTweet();
		for (int i =newList.size()-1; i >=0; i--) {
			repostContent(newList.get(i));
			if(!Constant.DEBUG)
			{
				Thread.sleep(20000);
			}
		}
	}

	private void repostContent(Tweet t) throws Exception {
		System.out.println("repost " + t);
		if(!Constant.DEBUG)
		{
			weibo.postWeibo(t);
		}
		writeLastId(t.getId());

	}
	private File getLastIdFile(){
		File file= new File("/usr/local/"+user+weibo.getName()+".txt");
		logger.info("last id file"+file.getAbsolutePath());
		return file;
	}
	private void writeLastId(String id) throws Exception {
		logger.info("write last id "+lastIdFile+user +","+id+".txt");
		File file = getLastIdFile();
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
		List<Tweet> ids = readTweets();
		logger.info("read tweet "+ids.size());
		String lastId = getLastId();		
		List<Tweet> newIds = new ArrayList<Tweet>();
		for (Tweet tweet : ids) {
			if (tweet.getId().equals(lastId)) {
				break;
			}
			newIds.add(tweet);
		}
		logger.info("new tweet "+newIds.size()+",lastid="+lastId);
		return newIds;
	}
	
	private String getLastId() {
		BufferedReader input=null;
		try {
			File file = getLastIdFile();
			if (!file.exists()) {
				return null;
			}
			input = new BufferedReader(new FileReader(file));
			return input.readLine();
		} catch (Exception e) {

		}finally{
			if(input!=null){
				try{
					input.close();
				}catch(Exception e){}
			}
		}
		return null;
	}

	private List<Tweet> readTweets() {
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
		Twitter tl = new Twitter("fangshimin1","fangshimin", new Weibo(
				"2.00jrXvCGEtKk4B385b3c86361s8yGD"));
		tl.readPage();
	}

	public WeiboInterface getWeibo() {
		return weibo;
	}

	public Twitter setWeibo(Weibo weibo) {
		this.weibo = weibo;
		return this;
	}

	public String getName() {
		return name;
	}

	public Twitter setName(String name) {
		this.name = name;
		return this;
	}

	public String getUser() {
		return user;
	}

	public Twitter setUser(String user) {
		this.user = user;
		return this;
	}
	
}
