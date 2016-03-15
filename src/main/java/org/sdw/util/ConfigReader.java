package org.sdw.util;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;

public class ConfigReader
{
	private final FileBasedConfigurationBuilder<FileBasedConfiguration> builder;
	private Configuration config;
	
	/**
	 * Single parameter constructor
	 * @param propertyFile : Absolute/ Relative path of property file to be read
	 */
	public ConfigReader(String propertyFile)
	{
		Parameters params = new Parameters();
		builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class).configure(params.properties().setFileName(propertyFile));
	}
	
	/**
	 * @return Configuration object
	 */
	public Configuration getConfig()
	{
		try
		{
			config = builder.getConfiguration();
		}
		catch(ConfigurationException cex)
		{
			cex.printStackTrace();
		}
		return config;
	}
}