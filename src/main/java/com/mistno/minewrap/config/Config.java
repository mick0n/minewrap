package com.mistno.minewrap.config;

import java.io.*;
import java.util.Properties;

public class Config {

	protected static Properties properties = new Properties();

	public static void initialize() throws IOException {
		properties.load(new FileInputStream(new File("minewrap.properties")));
		validateProperties();
	}

	protected static void validateProperties() {
		validateSingleProperty("server.jar");
		validateSingleProperty("server.xmx");
		validateSingleProperty("server.xms");
		validateSingleProperty("backup.sourceDir");
		validateSingleProperty("backup.targetDir");
	}

	private static void validateSingleProperty(String key) {
		if (!properties.containsKey(key)) {
			throw new IllegalArgumentException("Missing the following property in minewrap.properties: " + key);
		}
		if (properties.getProperty(key).equals("")) {
			throw new IllegalArgumentException("Missing value for property in minewrap.properties: " + key);
		}
	}
	
	private static void validateSinglePropertyWithDependency(String key, String depenentOnKey, String dependentOnValue) {
		if(contains(depenentOnKey) && properties.getProperty(key).equals(dependentOnValue)) {
			validateSingleProperty(key);
		}
	}

	public static String get(String key) {
		return properties.getProperty(key);
	}
	
	public static boolean contains(String key) {
		return properties.containsKey(key) && !properties.getProperty(key).equals("");
	}
}
