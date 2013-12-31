package net.kindeditor.generator;

import javax.servlet.http.HttpServletRequest;

/**
 * Developer can provide his own PathGenerator through implement this interface.
 * For self provided PathGenerator to work, an entry of "path_generator" should be added 
 * in kindmanager.properties.
 * @author luyanfei
 *
 */
public interface PathGenerator {

	/**
	 * 
	 * @param request
	 * 		HttpServletRequest object for dynamic information.
	 * @param originalName
	 * 		this will be local file name in file system before upload in most cases. 
	 * @return
	 * 		relative path denote the path generated in server. 
	 * 		<em>Caution:</em> returned string should not begin with "/".
	 */
	public String generate(HttpServletRequest request, String originalName);
}
