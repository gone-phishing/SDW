package sdw;

import java.io.*;
import java.util.*;

class ConfigLoader
{
	Properties properties;
	private String params[] = {"dataset_loc", "valid_types", "common_rdf_format", "hash_format", "hashed_file"};
	public String values[] = new String[params.length];

	// protected ConfigLoader configLoader = null;

	// private ConfigLoader() {}

	// public static ConfigLoader getInstance() {
	// 	if (null == ConfigLoader) {
	// 		/// TODO get path from environment variable!!
	// 		ConfigLoader.configLoader = new ConfigLoader();
	// 	}

	// 	return ConfigLoader.configLoader;
	// }

	public ConfigLoader(String propFileName) throws IOException
	{
		try 
		(
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
		)
		{
			properties = new Properties();
			if (inputStream != null) 
			{
				properties.load(inputStream);
			} 
			else 
			{
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}

			if(propFileName.equals("config.properties"))
			{
				for(int i=0; i<params.length; i++)
				{
					values[i] = properties.getProperty(params[i]);
				}
			}
		} 
		catch (Exception e) 
		{
			System.out.println("Exception: " + e);
		}
	}

	public String getPropertyValue(String key)
	{
		return properties.getProperty(key);
	}

	public String getDatasetLocation()
	{
		return values[0];
	}

	public String getValidInputTypes()
	{
		return values[1];
	}

	public String getDefaultFormat()
	{
		return values[2];
	}

	public String getHashFunction()
	{
		return values[3];
	}

	public String getHashedFile()
	{
		return values[4];
	}
}