package br.com.labuonapasta.util;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

public class ConfigProperties implements Serializable {
	
	private static final long serialVersionUID = -5241964956536838433L;
	
	private Properties getPropertiesConfig() throws Exception {
		String propFileName = "config.properties";
		
		try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName)) {
			if (inputStream != null) {
				Properties properties = new Properties();
				properties.load(inputStream);
				return properties;
			} else {
				throw new FileNotFoundException("Property file " + propFileName + "not found in the classpath.");
			}
		} catch (Exception e) {
			System.out.println("Error when get config file...");
			throw new Exception(e.getCause());
		}	
	}
	
	public String getValuePropertie(String propertie) throws Exception {
		return this.getPropertiesConfig().getProperty(propertie);
	}

}
