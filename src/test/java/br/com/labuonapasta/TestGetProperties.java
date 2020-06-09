package br.com.labuonapasta;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.Test;

public class TestGetProperties {
	
	@Test
	public void main() {
		InputStream inputStream = null;
		
		try {
			Properties prop = new Properties();
			String propFileName = "config.properties";
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
			
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("Property file " + propFileName + "not found in the classpath");
			}
			
			String user = prop.getProperty("user");
			
			System.out.println(user);
		} catch (Exception e) {
			System.out.println("Error when get config file...");
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
}
