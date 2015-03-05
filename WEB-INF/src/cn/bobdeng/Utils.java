package cn.bobdeng;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class Utils {
	public File getOneImage(List<String> images)throws Exception{
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
}
