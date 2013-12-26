package net.kindeditor.util;


public interface Constants {

	//attribute name in ServletContext for Properties object.
	public static final String KIND_CONFIG = "KIND_CONFIG";
	
	//property names of the config file
	public static final String UPLOAD_ROOT = "upload_root";
	public static final String UPLOAD_SIZE_LIMIT = "upload_size_limit";
	public static final String IMG_DIR = "img_dir";
	public static final String IMG_DIR_EXT = "img_dir_ext";
	public static final String FLASH_DIR = "flash_dir";
	public static final String FLASH_DIR_EXT = "flash_dir_ext";
	public static final String MEDIA_DIR = "media_dir";
	public static final String MEDIA_DIR_EXT = "media_dir_ext";
	public static final String FILE_DIR = "file_dir";
	public static final String FILE_DIR_EXT = "file_dir_ext";
	public static final String ALLOWED_DIRS = "allowed_dirs";
	/**
	 * after upload successed, server should return uploaded file's reference url,
	 * this property is used for config url prefix.
	 */
	public static final String DEST_URL_PREFIX = "dest_url_prefix";

}
