package org.sdw.ingestion;

import org.sdw.util.ConfigReader;
import org.apache.commons.configuration2.Configuration;

public class IngestionConfig extends ConfigReader
{
	public final Configuration config;
	public final String[] validTypes;
	public final String hashFunction;
	public final String commonRdfFormat;
	public final String hashFile;
	public final String[] datasetPaths;
	public final String[] mappingLanguage;
	
	public IngestionConfig()
	{
		super("ingestion.properties");
		config = getConfig();
		datasetPaths = config.getStringArray("paths");
		validTypes = config.getStringArray("validTypes");
		commonRdfFormat = config.getString("commonRDfFormaat");
		hashFunction = config.getString("hashFunction");
		hashFile = config.getString("hashFile");
		mappingLanguage = config.getStringArray("mappingLanguage");
	}
	
}
