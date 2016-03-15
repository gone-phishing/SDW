package org.sdw.ingestion;

import org.sdw.util.HashFilter;
import java.util.ArrayList;
import java.util.HashMap;
import org.sdw.util.ConfigReader;
import org.apache.commons.configuration2.Configuration;

public class DatasetLoader
{
	public ArrayList<Configuration> configurationList = new ArrayList<Configuration>();
	public HashMap<Configuration, String> invalidDatasets = new HashMap<Configuration, String>();
	
	public DatasetLoader()
	{
		IngestionConfig ic = new IngestionConfig();
		String datasetPaths[] = ic.datasetPaths;
		for(String str : datasetPaths)
		{
			ConfigReader cfg = new ConfigReader(str);
			configurationList.add(cfg.getConfig());
		}
		/**
		 * 1. Validate
		 * 2. Filter
		 */
		for(Configuration cfg : configurationList)
		{
			if(validate(cfg, invalidDatasets))
			{
				filter(cfg);
			}
		}
	}
	
	private boolean validate(Configuration cfg, HashMap<Configuration, String> invalidDatasets)
	{
		if(!validateType(cfg))
		{
			invalidDatasets.put(cfg, "Source format not supported");
			return false;
		}
		if(!validatePath(cfg))
		{
			invalidDatasets.put(cfg, "Invalid path"+cfg.getString("dataset_path") );
			return false;
		}
		if(!validateSchema(cfg))
		{
			invalidDatasets.put(cfg, "Given schema not aligned with required schema");
			return false;
		}
		return true;
	}
	
	private boolean validateType(Configuration cfg)
	{
		return true;
	}
	
	private boolean validatePath(Configuration cfg)
	{
		return true;
	}
	
	private boolean validateSchema(Configuration cfg)
	{
		return true;
	}
	
	private void filter(Configuration cfg)
	{
		
	}
}
