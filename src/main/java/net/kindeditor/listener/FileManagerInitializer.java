package net.kindeditor.listener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.naming.directory.DirContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import static net.kindeditor.util.Constants.*;


@WebListener
public class FileManagerInitializer implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext context = sce.getServletContext();
		Properties properties = readFromConfigFile(context);
		//for test
		properties.list(System.err);
		context.setAttribute(KIND_CONFIG, properties);
		checkUploadDirectories(properties);
	}

	private void checkUploadDirectories(Properties properties) {
		String root = properties.getProperty(UPLOAD_ROOT);
		if(!root.endsWith("/"))
			root += "/";
		File rootDir = new File(root);
		if(!rootDir.isAbsolute() || !rootDir.canWrite())
			throw new RuntimeException("Upload root directory: " + root + "does not exists or can not be written.");
		//create allowed dirs
		String[] allowedDirs = properties.getProperty(ALLOWED_DIRS).split(",");
		for(String dir : allowedDirs) {
			File t = new File(root + dir);
			t.mkdir();
		}
	}

	//TODO: image,flash,media,file should not be configured by client's property file.
	private Properties readFromConfigFile(ServletContext context) {
		ClassLoader loader = context.getClassLoader();
		InputStream defaultConfig = loader.getResourceAsStream("kindmanager_default.properties");
		InputStream config = loader.getResourceAsStream("kindmanager.properties");
		Properties properties = new Properties();
		Properties userProperties = new Properties();
		try {
			properties.load(defaultConfig);
			userProperties.load(config);
			properties.putAll(userProperties);
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if(defaultConfig != null) 
					defaultConfig.close();
				if(config != null)
					config.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if(properties.getProperty(UPLOAD_ROOT) == null) {
			String defaultUploadRoot = context.getRealPath("/") + "attached/";
			properties.setProperty(UPLOAD_ROOT, defaultUploadRoot);
		}
		if(properties.getProperty(DEST_URL_PREFIX) == null) {
			String defaultPrefix = context.getContextPath() + "/attached/";
			properties.setProperty(DEST_URL_PREFIX, defaultPrefix);
		}
		return properties;
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

}
