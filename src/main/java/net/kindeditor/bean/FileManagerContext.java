package net.kindeditor.bean;

import java.io.File;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents json object return to kind editor from FileManagerServlet.
 * @author luyanfei
 *
 */
public class FileManagerContext {
	@JsonProperty("current_url")
	private String currentUrl;
	@JsonProperty("current_dir_path")
	private String currentDirPath;
	@JsonProperty("moveup_dir_path")
	private String moveupDirPath;
	@JsonProperty("file_list")
	private List<FileItem> fileList;
	@JsonProperty("total_count")
	private int tocalCount;

	@JsonIgnore
	private File currentDir;
	
	public FileManagerContext(String rootPath, String urlPrefix, String path) {
		super();
		path = path != null ? path : "";
		//.. is not allowed
		if (path.indexOf("..") >= 0) {
			throw new RuntimeException("Request to .. is not allowed.");
		}
		//the end char in path should be "/"
		if (!"".equals(path) && !path.endsWith("/")) {
			throw new RuntimeException("Invalid path parameter.");
		}
		currentDir = new File(rootPath, path);
		if(!currentDir.isDirectory())
			throw new RuntimeException("Wrong path parameter, " 
					+ currentDir.getAbsolutePath() + " is not a directory.");
		
		this.currentUrl = urlPrefix + path;
		this.currentDirPath = path;
		//calculate moveupDirPath
		if(path.length() == 0) {
			this.moveupDirPath = "";
		}
		else {
			path = path.substring(0, path.length() - 1);
			int index = path.lastIndexOf("/");
			this.moveupDirPath = index >= 0 ? path.substring(0, index+1) : "";
		}
		
		initFileList(currentDir);
	}

	private void initFileList(File dir) {
		
	}

	public String getCurrentUrl() {
		return currentUrl;
	}

	public void setCurrentUrl(String currentUrl) {
		this.currentUrl = currentUrl;
	}

	public String getCurrentDirPath() {
		return currentDirPath;
	}

	public void setCurrentDirPath(String currentDirPath) {
		this.currentDirPath = currentDirPath;
	}

	public String getMoveupDirPath() {
		return moveupDirPath;
	}

	public void setMoveupDirPath(String moveupDirPath) {
		this.moveupDirPath = moveupDirPath;
	}

	public List<FileItem> getFileList() {
		return fileList;
	}

	public void setFileList(List<FileItem> fileList) {
		this.fileList = fileList;
	}

	public int getTocalCount() {
		return tocalCount;
	}

	public void setTocalCount(int tocalCount) {
		this.tocalCount = tocalCount;
	}

	public File getCurrentDir() {
		return currentDir;
	}

	public void setCurrentDir(File currentDir) {
		this.currentDir = currentDir;
	}
	
	
}
