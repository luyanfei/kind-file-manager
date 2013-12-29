package net.kindeditor.servlet;

import static net.kindeditor.util.Constants.DEST_URL_PREFIX;
import static net.kindeditor.util.Constants.SC_KIND_CONFIG;
import static net.kindeditor.util.Constants.UPLOAD_ROOT;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.kindeditor.bean.FileManagerContext;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebServlet(urlPatterns="/kindeditor/filemanager.do")
public class FileManagerServlet extends HttpServlet {

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Properties config = (Properties) request.getServletContext()
				.getAttribute(SC_KIND_CONFIG);
		String rootPath = config.getProperty(UPLOAD_ROOT);
		String rootUrl  = config.getProperty(DEST_URL_PREFIX);
		
		PrintWriter out = response.getWriter();

		String dirName = request.getParameter("dir");
		
		String path = request.getParameter("path");
		
		//排序形式，name or size or type
		String order = request.getParameter("order");
		order = order != null ? order.toLowerCase() : "name";
		
		checkDir(rootPath, dirName);

		FileManagerContext fmc = new FileManagerContext(rootPath, rootUrl, path);
		fmc.sortFileList(order);
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(DATE_FORMAT);
		response.setContentType("application/json; charset=UTF-8");
		
		out.println(mapper.writeValueAsString(fmc));
	}
	
	private void checkDir(String rootPath, String dirName)
		throws ServletException{
		if (dirName != null) {
			File saveDirFile = new File(rootPath, dirName);
			if(!saveDirFile.exists()){
				throw new ServletException("Invalid Directory name.");
			}
		}

	}

}
