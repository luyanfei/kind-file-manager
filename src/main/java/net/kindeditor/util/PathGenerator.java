package net.kindeditor.util;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @author luyanfei
 *
 */
public interface PathGenerator {

	public String generate(HttpServletRequest request, String originalName);
}
