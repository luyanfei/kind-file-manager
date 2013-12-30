package net.kindeditor.util;

import java.util.Comparator;

import net.kindeditor.bean.FileItem;

/**
 * Note: all these comparator class have natural ordering that are inconsistent with equals.
 * @author luyanfei
 *
 */
public class FileItemComparator {

	public static final class SizeComparator implements Comparator<FileItem> {
		@Override
		public int compare(FileItem item1, FileItem item2) {
			if(item1.isDir() && !item2.isDir())
				return -1;
			else if(!item1.isDir() && item2.isDir())
				return 1;
			else
				return new Long(item1.getFileSize()).compareTo(new Long(item2.getFileSize()));
		}
	}

	public static final class NameComparator implements Comparator<FileItem> {

		@Override
		public int compare(FileItem item1, FileItem item2) {
			if(item1.isDir() && !item2.isDir())
				return -1;
			else if(!item1.isDir() && item2.isDir())
				return 1;
			else
				return item1.getFileName().compareTo(item2.getFileName());
		}
		
	}
	
	public static final class TypeComparator implements Comparator<FileItem> {

		@Override
		public int compare(FileItem item1, FileItem item2) {
			if(item1.isDir() && !item2.isDir())
				return -1;
			else if(!item1.isDir() && item2.isDir())
				return 1;
			else
				return item1.getFileType().compareTo(item2.getFileType());
		}
		
	}
}
