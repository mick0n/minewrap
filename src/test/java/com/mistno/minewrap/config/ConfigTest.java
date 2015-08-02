package com.mistno.minewrap.config;

import org.junit.*;
import org.junit.rules.ExpectedException;

import com.mistno.minewrap.config.Config;

public class ConfigTest {

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Before
	public void setup() {
		Config.properties.setProperty("server.jar", "randomValue");
		Config.properties.setProperty("server.xmx", "randomValue");
		Config.properties.setProperty("server.xms", "randomValue");
		Config.properties.setProperty("backup.sourceDir", "randomValue");
		Config.properties.setProperty("backup.targetDir", "randomValue");
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

	@After
	public void teardown() {
		Config.properties.clear();
	}
}
