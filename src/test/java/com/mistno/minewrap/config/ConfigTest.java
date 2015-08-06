package com.mistno.minewrap.config;

import org.junit.*;
import org.junit.rules.ExpectedException;

import com.mistno.minewrap.config.Config;

public class ConfigTest {

	private static final String RANDOM_STRING = "bivNPiEWgf";
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Before
	public void setupDefaults() {
		Config.properties.setProperty("server.jar", RANDOM_STRING);
		Config.properties.setProperty("server.xmx", RANDOM_STRING);
		Config.properties.setProperty("server.xms", RANDOM_STRING);
		Config.properties.setProperty("backup.zip.enabled", "true");
		Config.properties.setProperty("backup.zip.sourceDir", RANDOM_STRING);
		Config.properties.setProperty("backup.zip.exclude", RANDOM_STRING);
		Config.properties.setProperty("backup.zip.targetDir", RANDOM_STRING);
		Config.properties.setProperty("backup.external.enabled", "false");
		Config.properties.setProperty("backup.external.path", "");
	}

	@Test
	public void allPropertiesValid() throws Exception {
		Config.validateProperties();
	}

	@Test
	public void missingPropertyThrowsException() {
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("Missing the following property in minewrap.properties: server.jar");

		Config.properties.remove("server.jar");
		Config.validateProperties();
	}

	@Test
	public void missingValueThrowsException() {
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("Missing value for property in minewrap.properties: server.jar");

		Config.properties.setProperty("server.jar", "");
		Config.validateProperties();
	}
	
	@Test
	public void ignorePropertyIfDependentPropertyIsMissing() {
		Config.properties.setProperty("backup.zip.enabled", "");
		Config.properties.remove("backup.zip.sourceDir");
		Config.validateProperties();
	}

	@Test
	public void missingPropertyWithFulfilledDependencyThrowsException() {
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("Missing the following property in minewrap.properties: backup.zip.sourceDir");

		Config.properties.remove("backup.zip.sourceDir");
		Config.validateProperties();
	}
	
	@After
	public void teardown() {
		Config.properties.clear();
	}
}
