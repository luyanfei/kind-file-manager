package net.kindeditor.listener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.kindeditor.generator.PathGenerator;
import net.kindeditor.util.ConstraintChecker;
import static net.kindeditor.util.Constants.*;

/**
 * This is the kind-file-manager's initialize entrance. When ServletContext is initialized, some things will happen:
 * <ol>
 * <li> A properties file named "kindmanager.properties" will be searched in classpath, configuration properties
 * in this file will be merged with default configuration properties. The Properties object will be kept
 * in ServletContext with the attribute name {@link Constants.SC_KIND_CONFIG}, other servlet will need these configuration
 * properties.</li>
 * <li> The directory for upload files will be checked, if it does not exist or cann't be written, a RuntimeException
 * will be thrown. These allowed subdirectories in upload root directory will be checked for existence, and
 * will be created if necessary.</li>
 * <li> PathGenerator object will be created from class name configured in properties file, and this object will be 
 * kept in ServletContext with the attribute name {@link Constants.SC_PATH_GENERATOR}.</li>
 * <li> A Jackson ObjectMapper object will be initialized, and this object will be kept in ServletContext with the 
 * attribute name {@link Constants.SC_OBJECT_MAPPER}.</li>
 * </ol>
 * @author luyanfei
 * @see net.kindeditor.util.Constants
 */

@WebListener
public class FileManagerInitializer implements ServletContextListener {
	
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext context = sce.getServletContext();
		Properties properties = readFromConfigFile(context);
		//for test
		properties.list(System.err);
		context.setAttribute(SC_KIND_CONFIG, properties);
		checkUploadDirectories(properties);
		PathGenerator pathGenerator = loadPathGenerator(properties);
		context.setAttribute(SC_PATH_GENERATOR, pathGenerator);
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(DATE_FORMAT);
		context.setAttribute(SC_OBJECT_MAPPER, mapper);
		
		ConstraintChecker checker = new ConstraintChecker(properties);
		context.setAttribute(SC_CONSTRAINT_CHECKER, checker);
	}

	private PathGenerator loadPathGenerator(Properties properties) {
		String className = properties.getProperty(PATH_GENERATOR);
		className = className == null ? DEFAULT_PATH_GENERATOR_CLASS : className;
		
		Class<?> clazz = null;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		//check whether this class implemented PathGenerator interface 
		boolean foundInterface = false;
		for(Class<?> clz : clazz.getInterfaces()) {
			if(PathGenerator.class.isAssignableFrom(clz)) {
				foundInterface = true;
				break;
			}
		}
		if(!foundInterface)
			throw new RuntimeException("Cann't assign to PathGenerator interface, "
					+ "the " + PATH_GENERATOR + " in configure file is wrong.");
		PathGenerator pathGenerator = null;
		try {
			pathGenerator = (PathGenerator)clazz.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		return pathGenerator;
	}

	private void checkUploadDirectories(Properties properties) {
		String root = properties.getProperty(UPLOAD_ROOT);
		if(!root.endsWith(File.separator))
			root += File.separator;
		File rootDir = new File(root);
		rootDir.mkdir();
		if(!rootDir.isAbsolute() || !rootDir.exists() || !rootDir.canWrite())
			throw new RuntimeException("Upload root directory: " + root + "does not exists or can not be written.");
		//create allowed dirs
		String[] allowedDirs = properties.getProperty(ALLOWED_DIRS).split(",");
		for(String dir : allowedDirs) {
			File t = new File(rootDir, dir);
			if(!t.exists()) t.mkdir();
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

		String uploadRoot = properties.getProperty(UPLOAD_ROOT);
		if( uploadRoot == null) {
			String defaultUploadRoot = context.getRealPath("/") + "attached/";
			properties.setProperty(UPLOAD_ROOT, defaultUploadRoot);
		}
		else {
			//make sure upload root is ended with "/"
			if(!uploadRoot.endsWith("/"))
				properties.setProperty(UPLOAD_ROOT, uploadRoot + "/");
		}
		
		String destUrlPrefix = properties.getProperty(DEST_URL_PREFIX);
		if(destUrlPrefix == null) {
			String defaultPrefix = context.getContextPath() + "/attached/";
			properties.setProperty(DEST_URL_PREFIX, defaultPrefix);
		}
		else {
			//make sure prefix end with "/"
			if(!destUrlPrefix.endsWith("/")) {
				properties.setProperty(DEST_URL_PREFIX, destUrlPrefix + "/");
			}
		}
		return properties;
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

}
