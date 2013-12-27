package net.kindeditor.util;

/**
 * Constants used in kind-file-manager, most of them are configure file's property names.
 * @author luyanfei
 *
 */
public interface Constants {

	/**
	 * Attribute name in ServletContext used for keep configure Properties object.
	 */
	public static final String SC_KIND_CONFIG = "king_config";
	/**
	 * Attribute name in ServletContex used for keep PathGenerator Object.
	 */
	public static final String SC_PATH_GENERATOR = "kind_path_generator";
	
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
	 * After upload successed, server should return uploaded file's reference url,
	 * this property is used for config url prefix.
	 */
	public static final String DEST_URL_PREFIX = "dest_url_prefix";
	/**
	 * Configure file's property, the value will be class name of the implemented PathGenerator.
	 */
	public static final String PATH_GENERATOR = "path_generator";
	public static final String DEFAULT_PATH_GENERATOR_CLASS = "net.kindeditor.util.DateBasedPathGenerator";

}