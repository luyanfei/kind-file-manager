package net.kindeditor.bean;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import net.kindeditor.util.FileItemComparator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents json object return to kind editor from FileManagerServlet.
 * @author luyanfei
 *
 */
public class FileManagerContext {
	@JsonIgnore
	private static final List<String> IMG_EXTS = Arrays.asList("gif", "jpg", "jpeg", "png", "bmp");
	
	@JsonProperty("current_url")
	private String currentUrl;
	@JsonProperty("current_dir_path")
	private String currentDirPath;
	@JsonProperty("moveup_dir_path")
	private String moveupDirPath;
	@JsonProperty("file_list")
	private List<FileItem> fileList = new ArrayList<FileItem>();
	@JsonProperty("total_count")
	private int tocalCount;

	@JsonIgnore
	private File currentDir;

	@JsonIgnore
	private static final FileItemComparator.SizeComparator sizeComparator = new FileItemComparator.SizeComparator();
	@JsonIgnore
	private static final FileItemComparator.TypeComparator typeComparator = new FileItemComparator.TypeComparator();
	@JsonIgnore
	private static final FileItemComparator.NameComparator nameComparator = new FileItemComparator.NameComparator();
	
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
		this.tocalCount = fileList.size();
	}

	private void initFileList(File dir) {
		for(File file : dir.listFiles()) {
			FileItem item = new FileItem();
			String name = file.getName();
			item.setFileName(name);
			item.setDateTime(new Date(file.lastModified()));
			if(file.isDirectory()) {
				item.setDir(true);
				item.setHasFile(file.listFiles() != null);
				item.setFileSize(0L);
				item.setPhoto(false);
				item.setFileType("");
			}
			else {
				item.setDir(false);
				item.setHasFile(false);
				item.setFileSize(file.length());
				String ext = name.substring(name.lastIndexOf(".") + 1).toLowerCase();
				item.setPhoto(IMG_EXTS.contains(ext));
				item.setFileType(ext);
			}
			fileList.add(item);
		}
	}
	
	public void sortFileList(String order) {
		order = order != null ? order.toLowerCase() : "name";
		if("size".equals(order)) {
			Collections.sort(fileList, sizeComparator);
		}
		else if("type".equals(order)) {
			Collections.sort(fileList, typeComparator);
		}
		else {
			Collections.sort(fileList, nameComparator);
		}
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
