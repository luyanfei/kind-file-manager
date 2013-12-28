package net.kindeditor.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import net.kindeditor.util.Constants;
import net.kindeditor.util.PathGenerator;

import org.json.simple.JSONObject;

import static net.kindeditor.util.Constants.*;

@WebServlet(urlPatterns = "/kindeditor/upload.do")
@MultipartConfig
public class UploadServlet extends HttpServlet {

	private static final String DEFAULT_SUBDIRECTORY = "image";
	private static final Pattern FILENAME_PATTERN = Pattern
			.compile("filename=\"([^\" ]+)\"");

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();

		Properties config = (Properties) request.getServletContext()
				.getAttribute(Constants.SC_KIND_CONFIG);
		String uploadRoot = config.getProperty(UPLOAD_ROOT);

		String dirName = request.getParameter("dir");
		File subDirectory = new File(uploadRoot,
				dirName == null ? DEFAULT_SUBDIRECTORY : dirName);
		if (!subDirectory.exists()) {
			out.println(buildErrorMessage("目录名不正确。"));
			return;
		}

		String destUrl = config.getProperty(DEST_URL_PREFIX);
		destUrl += dirName + "/";

		PathGenerator pathGenerator = (PathGenerator) request
				.getServletContext().getAttribute(SC_PATH_GENERATOR);
		
		Collection<Part> parts = request.getParts();
		for (Part part : parts) {
			String fileName = extractFileName(part);
			if(fileName == null) continue;
			// 检查文件大小
			if (part.getSize() > new Integer(config.getProperty(UPLOAD_SIZE_LIMIT).trim())) {
				out.println(buildErrorMessage("上传文件大小超过限制。"));
				return;
			}
			// 检查扩展名
			String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1)
					.toLowerCase();

			String path = pathGenerator.generate(request, fileName);
			int last = path.lastIndexOf(File.separator);
			if(last > 0) {
				File lastDir = new File(subDirectory, path.substring(0, last));
				if(!lastDir.exists())
					lastDir.mkdirs();
			}
			
			part.write(subDirectory.getAbsolutePath() + File.separator + path);

			JSONObject obj = new JSONObject();
			obj.put("error", 0);
			obj.put("url", destUrl + path);
			out.println(obj.toJSONString());

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

	@SuppressWarnings("unchecked")
	private String buildErrorMessage(String message) {
		JSONObject obj = new JSONObject();
		obj.put("error", 1);
		obj.put("message", message);
		return obj.toJSONString();
	}
}
