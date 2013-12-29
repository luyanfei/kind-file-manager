package net.kindeditor.bean;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represent file or directory in kind editor. This class is used to generate json response to kind editor.
 * @author luyanfei
 *
 */
public class FileItem {

	@JsonProperty("filename")
	private String fileName;
	@JsonProperty("filesize")
	private int fileSize;
	@JsonProperty("filetype")
	private String fileType;
	@JsonProperty("has_file")
	private boolean hasFile;
	@JsonProperty("is_dir")
	private boolean isDir;
	@JsonProperty("is_photo")
	private boolean isPhoto;
	@JsonProperty("datetime")
	private Date dateTime;
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public int getFileSize() {
		return fileSize;
	}
	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public boolean isHasFile() {
		return hasFile;
	}
	public void setHasFile(boolean hasFile) {
		this.hasFile = hasFile;
	}
	public boolean isDir() {
		return isDir;
	}
	public void setDir(boolean isDir) {
		this.isDir = isDir;
	}
	public boolean isPhoto() {
		return isPhoto;
	}
	public void setPhoto(boolean isPhoto) {
		this.isPhoto = isPhoto;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
}
