package net.kindeditor.bean;

import java.text.SimpleDateFormat;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FileManagerContextTest {

	@Test
	public void testConstructor() throws JsonProcessingException {
		FileManagerContext context = 
				new FileManagerContext("/tmp/", "http://localhost:8080/demo/tmp/", "image/20130202/");
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		System.out.println(mapper.writeValueAsString(context));
	}
}
