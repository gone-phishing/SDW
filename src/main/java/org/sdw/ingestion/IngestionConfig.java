package org.sdw.ingestion;

import org.sdw.util.ConfigReader;
import org.apache.commons.configuration2.Configuration;

public class IngestionConfig extends ConfigReader
{
	private final Configuration config;
	private final String[] validTypes;
	private final String hashFunction;
	private final String commonRdfFormat;
	private final String hashFile;
	
	public IngestionConfig()
	{
		super("ingestion.properties");
		config = getConfig();
		validTypes = config.getStringArray("validTypes");
		commonRdfFormat = config.getString("commonRDfFormaat");
		hashFunction = config.getString("hashFunction");
		hashFile = config.getString("hashFile");
	}
	
	public String get(String key)
	{
		return key;
	}
}
