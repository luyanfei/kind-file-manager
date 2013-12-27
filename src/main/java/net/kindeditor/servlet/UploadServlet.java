package net.kindeditor.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Random;
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

import org.json.simple.JSONObject;

import static net.kindeditor.util.Constants.*;

@WebServlet(urlPatterns="/kindeditor/upload.do")
@MultipartConfig
public class UploadServlet extends HttpServlet {
	
	private static final Pattern FILENAME_PATTERN = Pattern.compile("filename=\"([^\" ]+)\"");

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		Properties config = (Properties) request.getServletContext()
				.getAttribute(Constants.SC_KIND_CONFIG);
		
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();

		String savePath = config.getProperty(UPLOAD_ROOT);

		String dirName = request.getParameter("dir");
		if (dirName == null) {
			dirName = "image";
		}
		String[] dirs = config.getProperty(ALLOWED_DIRS).split(",");
		List<String> dirList = Arrays.asList(dirs);
		if(!dirList.contains(dirName)){
			out.println(getError("目录名不正确。"));
			return;
		}
		//创建文件夹
		savePath += "/" + dirName + "/";
		String saveUrl = config.getProperty(DEST_URL_PREFIX);
		saveUrl += dirName + "/";

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String ymd = sdf.format(new Date());
		savePath += ymd + "/";
		saveUrl += ymd + "/";
		File dirFile = new File(savePath);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		
		String destPath = dirName + "/" + ymd + "/";
		
		Collection<Part> parts = request.getParts();
		Iterator itr = parts.iterator();
		while (itr.hasNext()) {
			Part part = (Part) itr.next();
			String disposition = part.getHeader("Content-Disposition");
			if(disposition == null) continue;
			Matcher matcher = FILENAME_PATTERN.matcher(disposition);
			if(!matcher.find()) continue;
			String fileName = matcher.group(1);
			
			long fileSize = part.getSize();
		
				//检查文件大小
				if(part.getSize() > new Integer(config.getProperty(UPLOAD_SIZE_LIMIT).trim())){
					out.println(getError("上传文件大小超过限制。"));
					return;
				}
				//检查扩展名
				String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
//				if(!Arrays.<String>asList(extMap.get(dirName).split(",")).contains(fileExt)){
//					out.println(getError("上传文件扩展名是不允许的扩展名。\n只允许" + extMap.get(dirName) + "格式。"));
//					return;
//				}

				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
				String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
//				try{
//					File uploadedFile = new File(savePath, newFileName);
//					item.write(uploadedFile);
					part.write(savePath + newFileName);
					System.err.println("savePath=" + savePath + ",newFileName=" + newFileName);
//				}catch(Exception e){
//					out.println(getError("上传文件失败。"));
//					return;
//				}

				JSONObject obj = new JSONObject();
				obj.put("error", 0);
				obj.put("url", saveUrl + newFileName);
				out.println(obj.toJSONString());
			
		}
	}
	
	@SuppressWarnings("unchecked")
	private String getError(String message) {
		JSONObject obj = new JSONObject();
		obj.put("error", 1);
		obj.put("message", message);
		return obj.toJSONString();
	}
}
