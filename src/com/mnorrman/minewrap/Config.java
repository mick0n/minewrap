package com.mnorrman.minewrap;

import java.io.*;
import java.util.Properties;

public class Config {

	private static Properties properties = new Properties();

	public static void initialize() throws IOException {
		properties.load(new FileInputStream(new File("minewrap.properties")));
		validateProperties();
	}

	private static void validateProperties() {
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
	}

	public static String get(String key) {
		return properties.getProperty(key);
	}
}
