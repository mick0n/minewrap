package com.mistno.minewrap.config;

import java.io.*;
import java.util.Properties;

public class Config {

	protected static Properties properties = new Properties();

	public static void initialize(InputStream inputStream) throws IOException {
		properties.load(inputStream);
		validateProperties();
	}

	protected static void validateProperties() {
		validateSingleProperty("server.jar");
		validateSingleProperty("server.xmx");
		validateSingleProperty("server.xms");
		validateSinglePropertyWithDependency("backup.zip.sourceDir", "backup.zip.enabled", "true");
		validateSinglePropertyWithDependency("backup.zip.targetDir", "backup.zip.enabled", "true");
		validateSinglePropertyWithDependency("backup.zip.exclude", "backup.zip.enabled", "true");
		validateSinglePropertyWithDependency("backup.external.path", "backup.external.enabled", "true");
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
		if(contains(depenentOnKey) && properties.getProperty(depenentOnKey).equalsIgnoreCase(dependentOnValue)) {
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
