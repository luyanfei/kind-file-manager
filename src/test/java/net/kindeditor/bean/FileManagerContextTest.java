package net.kindeditor.bean;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FileManagerContextTest {

	private String rootPath;
	private String path;
	private File testDir;
	
	@Before
	public void setUp() throws IOException {
		rootPath = "/tmp/";
		path = "image/20130202/";
		testDir = new File(rootPath, path);
		if(!testDir.isDirectory())
			testDir.mkdirs();
		File a = new File(testDir,"a.png");
		a.createNewFile();
		File b = new File(testDir,"b.jpg");
		b.createNewFile();
		File dir = new File(testDir,"mydir");
		dir.mkdir();
	}
	
	@Test
	public void testConstructor() throws JsonProcessingException {
		FileManagerContext context = 
				new FileManagerContext(rootPath, "http://localhost:8080/demo/tmp/", path);
		ObjectMapper mapper = new ObjectMapper();
//		mapper.setVisibility(PropertyAccessor.ALL, Visibility.NONE);
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		System.out.println(mapper.writeValueAsString(context));
	}
}
