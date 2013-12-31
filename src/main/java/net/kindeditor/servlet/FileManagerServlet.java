package net.kindeditor.servlet;

import static net.kindeditor.util.Constants.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.kindeditor.bean.FileManagerContext;
import net.kindeditor.util.ConstraintChecker;

import com.fasterxml.jackson.databind.ObjectMapper;

public class FileManagerServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		final ServletContext context = request.getServletContext();
		Properties config = (Properties) context.getAttribute(SC_KIND_CONFIG);
		String rootPath = config.getProperty(UPLOAD_ROOT);
		String rootUrl  = config.getProperty(DEST_URL_PREFIX);
		
		response.setContentType("application/json; charset=UTF-8");
		PrintWriter out = response.getWriter();

		String subdir = request.getParameter("dir");
		String path = request.getParameter("path");
		//排序形式，name or size or type
		String order = request.getParameter("order");
		
		ConstraintChecker checker = (ConstraintChecker)context.getAttribute(SC_CONSTRAINT_CHECKER);
		checker.checkSubDirectory(rootPath, subdir);


		FileManagerContext fmc = new FileManagerContext(rootPath, rootUrl, path);
		fmc.sortFileList(order);
		ObjectMapper mapper = (ObjectMapper)context.getAttribute(SC_OBJECT_MAPPER);
		out.println(mapper.writeValueAsString(fmc));
	}

}
