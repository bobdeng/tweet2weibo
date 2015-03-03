package cn.bobdeng;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class Weibo {

	private String token;

	public Weibo(String token) {
		super();
		this.token = token;
	}

	public void postWeibo(Tweet t) throws Exception {
		String rlt = null;
		if (t.getImages() == null || t.getImages().size() == 0) {
			rlt = new HttpUtils()
					.setUrl("https://api.weibo.com/2/statuses/update.json")
					.addParam("status", t.getContent())
					.addParam("access_token", token).httpsPost();
		}else{
			rlt = new HttpUtils()
			.setUrl("https://upload.api.weibo.com/2/statuses/upload.json")
			.addParam("status", t.getContent())
			.addFile("pic", getOneImage(t.getImages()).getAbsolutePath())
			.addParam("access_token", token).httpsUpload();
		}
		System.out.println(rlt);

	}
	private File getOneImage(List<String> images)throws Exception{
		List<BufferedImage> listImages=new ArrayList<BufferedImage>();
		for(String img:images){
			listImages.add(readImage(img));
		}
		int width=1024;
		int height=0;
		for(BufferedImage img:listImages){
			height+=(img.getHeight()*width)/img.getWidth();
		}
		BufferedImage rlt=new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2=rlt.createGraphics();
		g2.setColor(new Color(255,0,0));
		g2.fillRect(0, 0, width, height);
		g2.drawString("hello", 100, 100);
		height=0;
		for(BufferedImage img:listImages){
			int drawHeight=(img.getHeight()*width)/img.getWidth();
			g2.drawImage(img, 0, height, width, drawHeight,null);
			height+=drawHeight;
			//ImageIO.write(img, "jpg", new FileOutputStream("a"+height+"a.jpg"));
		}
		g2.dispose();
		File tempJpg=new File("temp.jpg");
		ImageIO.write(rlt, "jpg", tempJpg);
		return tempJpg;
	}
	private BufferedImage readImage(String url)throws Exception{
		byte[] data=new HttpUtils().setUrl(url).httpsDown();
		return ImageIO.read(new ByteArrayInputStream(data));
		
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

}
