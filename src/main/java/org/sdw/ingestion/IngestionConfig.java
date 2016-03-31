package org.sdw.ingestion;

import org.sdw.util.ConfigReader;
import org.apache.commons.configuration2.Configuration;

public class IngestionConfig extends ConfigReader
{
	public final String commonRdfFormat;
	public final Configuration config;
	public final String[] datasetPaths;
	public final String hashFile;
	public final String hashFunction;
	public final String[] mappingLanguage;
	public final String[] updateIntervals;
	public final String[] validTypes;
	public static IngestionConfig instance = null;
	/**
	 * Default constructor to set the fields
	 */
	private IngestionConfig()
	{
		super("ingestion.properties");
		config = getConfig();
		datasetPaths = config.getString("paths").split(",");
		validTypes = config.getString("validTypes").split(",");
		commonRdfFormat = config.getString("commonRdfFormat");
		hashFunction = config.getString("hashFunction");
		hashFile = config.getString("hashFile");
		mappingLanguage = config.getString("mappingLanguage").split(",");
		updateIntervals = config.getString("updateIntervals").split(",");
	}
	
	public static IngestionConfig getInstance()
	{
		if(instance == null)
		{
			instance = new IngestionConfig();
		}
		return instance;
	}
	
}
