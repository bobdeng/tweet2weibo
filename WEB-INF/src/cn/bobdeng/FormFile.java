package cn.bobdeng;

public class FormFile {

	private String name;
	private byte[] value;
	private String fileName;
	public String getName() {
		return name;
	}
	public byte[] getValue() {
		return value;
	}
	public String getFileName() {
		return fileName;
	}
	public FormFile setName(String name) {
		this.name = name;
		return this;
	}
	public FormFile setValue(byte[] value) {
		this.value = value;
		return this;
	}
	public FormFile setFileName(String fileName) {
		this.fileName = fileName;
		return this;
	}
	
}
