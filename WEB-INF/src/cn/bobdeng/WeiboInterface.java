package cn.bobdeng;

public interface WeiboInterface {
	public void postWeibo(Tweet t) throws Exception;
	public String getName();
	public void updateName()throws Exception;
}
