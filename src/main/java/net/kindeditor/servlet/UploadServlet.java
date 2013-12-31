package net.kindeditor.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import net.kindeditor.generator.PathGenerator;
import net.kindeditor.util.ConstraintChecker;
import net.kindeditor.util.Constants;
import static net.kindeditor.util.Constants.*;

public class UploadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Pattern FILENAME_PATTERN = Pattern
			.compile("filename=\"([^\" ]+)\"");

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json; charset=UTF-8");
		PrintWriter out = response.getWriter();

		final ServletContext servletContext = request.getServletContext();
		Properties config = (Properties) servletContext.getAttribute(Constants.SC_KIND_CONFIG);
		String uploadRoot = config.getProperty(UPLOAD_ROOT);

		String subdir = request.getParameter("dir");
		ConstraintChecker checker = (ConstraintChecker)servletContext.getAttribute(SC_CONSTRAINT_CHECKER);
		File subDirectory = checker.checkSubDirectory(uploadRoot, subdir);

		String destUrl = config.getProperty(DEST_URL_PREFIX);
		destUrl += subdir + "/";

		PathGenerator pathGenerator = (PathGenerator) servletContext.getAttribute(SC_PATH_GENERATOR);
		
		ResourceBundle bundle = ResourceBundle.getBundle("messages", request.getLocale());
		
		Collection<Part> parts = request.getParts();
		for (Part part : parts) {
			String fileName = extractFileName(part);
			if(fileName == null) continue;
			// 检查文件大小
			if (!checker.checkSizeLimit(part.getSize())) {
				out.println(buildErrorMessage(bundle.getString(MSG_UPLOAD_EXCEEDED)));
				return;
			}
			// 检查扩展名
			String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1)
					.toLowerCase();
			if( !checker.checkFileExtension(subdir, fileExt) ) {
				out.println(buildErrorMessage(bundle.getString(MSG_EXT_VIOLATED)));
				return;
			}

			String path = pathGenerator.generate(request, fileName);
			int last = path.lastIndexOf(File.separator);
			if(last > 0) {
				File lastDir = new File(subDirectory, path.substring(0, last));
				if(!lastDir.exists())
					lastDir.mkdirs();
			}
			
			part.write(subDirectory.getAbsolutePath() + File.separator + path);

			out.println(buildSuccessMessage(destUrl +path));

		}
	}

	/**
	 * Extract filename property from Part's Content-Disposition header.
	 * @param part
	 * @return the extracted filename value.
	 */
	private String extractFileName(Part part) {
		String disposition = part.getHeader("Content-Disposition");
		if (disposition == null)
			return null;
		Matcher matcher = FILENAME_PATTERN.matcher(disposition);
		if (!matcher.find())
			return null;
		return matcher.group(1);
	}

	private String buildSuccessMessage(String url) {
		return "{\"error\":0,\"url\":\"" + url+ "\"}";
	}
	
	private String buildErrorMessage(String message) {
		return "{\"error\":1,\"message\":\"" + message + "\"}";
	}
}
