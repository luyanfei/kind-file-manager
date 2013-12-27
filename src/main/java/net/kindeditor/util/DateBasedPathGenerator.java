package net.kindeditor.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

/**
 * Path generator based on current date.
 * @author luyanfei
 *
 */
public class DateBasedPathGenerator implements PathGenerator {

	private static final SimpleDateFormat YEARMONTH= new SimpleDateFormat("yyyyMM");
	private static final SimpleDateFormat PRECISE= new SimpleDateFormat("yyyyMMddHHmmssSSS");
	
	
	@Override
	public String generate(HttpServletRequest request, String originalName) {
		final Date current = new Date();
		int index = originalName.lastIndexOf(".");
		String ext = index == -1 ? originalName : originalName.substring(index);
		return YEARMONTH.format(current) + File.separator + PRECISE.format(current) + ext;
	}

}
