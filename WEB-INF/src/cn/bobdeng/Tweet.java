package cn.bobdeng;

import java.util.ArrayList;
import java.util.List;

public class Tweet {

	private String id;
	private String content;
	private List<String> images=new ArrayList<String>();
	public String getId() {
		return id;
	}
	public String getContent() {
		return content;
	}
	public Tweet setId(String id) {
		this.id = id;
		return this;
	}
	public Tweet setContent(String content) {
		this.content = content;
		return this;
	}
	public List<String> getImages() {
		return images;
	}
	public Tweet setImages(List<String> images) {
		this.images = images;
		return this;
	}
	public void addImage(String img){
		images.add(img);
	}
	@Override
	public String toString() {
		return "Tweet [id=" + id + ", content=" + content + ", images="
				+ images + "]";
	}
	
}
