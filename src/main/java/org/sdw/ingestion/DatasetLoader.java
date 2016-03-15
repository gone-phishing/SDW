package org.sdw.ingestion;

import org.sdw.util.HashFilter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.sdw.util.ConfigReader;
import org.apache.commons.configuration2.Configuration;

public class DatasetLoader
{
	public ArrayList<Configuration> configurationList = new ArrayList<Configuration>();
	public HashMap<Configuration, String> invalidDatasets = new HashMap<Configuration, String>();
	public HashMap<Configuration, String> validDatasets = new HashMap<Configuration, String>();
	public IngestionConfig ic = new IngestionConfig();
	public HashFilter hf = new HashFilter("MD5");
	public HashSet<String> oldHashes = new HashSet<String>();
	public HashSet<String> newHashes = new HashSet<String>();
	
	public DatasetLoader()
	{
		System.out.println("In dataset loader class");
		String datasetPaths[] = ic.datasetPaths;
		for(String str : datasetPaths)
		{
			//System.out.println("path: "+str);
			ConfigReader cfg = new ConfigReader(str);
			configurationList.add(cfg.getConfig());
		}
		/**
		 * 1. Validate
		 * 2. Filter
		 */
		loadOldHashes(ic.hashFile, oldHashes);
		for(Configuration cfg : configurationList)
		{
			if(validate(cfg, invalidDatasets))
			{
				if(filter(cfg.getString("dataset_path")))
				{
					validDatasets.put(cfg, cfg.getString("dataset_path"));
				}
				else
				{
					invalidDatasets.put(cfg, "Dataset already loaded");
				}
			}
		}
		if(invalidDatasets.size() > 0) 
		{
			for(Configuration cfg : invalidDatasets.keySet())
			{
				System.out.println("[ERROR] "+cfg.getString("name")+" : "+invalidDatasets.get(cfg));
			}
		}
		
		if(newHashes.size() > 0) 
		{
			writeNewHashes(ic.hashFile, newHashes);
		}
	}
	
	/**
	 * Validate all property values
	 * @param cfg : Configuration of the dataset
	 * @param invalidDatasets : Invalid datasets will be added to this hashmap with their error message
	 * @return : Boolean indicating validity of the dataset config file
	 */
	private boolean validate(Configuration cfg, HashMap<Configuration, String> invalidDatasets)
	{
		if(!validateType(cfg.getString("type")))
		{
			invalidDatasets.put(cfg, "Source format not supported");
			return false;
		}
		if(!validatePath(cfg.getString("dataset_path")))
		{
			invalidDatasets.put(cfg, "Invalid path"+cfg.getString("dataset_path") );
			return false;
		}
		if(!validateSchema(cfg))
		{
			invalidDatasets.put(cfg, "Given schema not aligned with required schema");
			return false;
		}
		if(!validateUpdateInterval(cfg.getString("reloadInterval")))
		{
			invalidDatasets.put(cfg, "Reload interval is invalid");
			return false;
		}
		return true;
	}
	
	/**
	 * Validate supported types with given type
	 * @param type : Extension of the dataset
	 * @return : Boolean indicating validity of extension type
	 */
	private boolean validateType(String type)
	{
		for(String str : ic.validTypes)
		{
			if(type.equals(str))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Check if dataset exist at the given path
	 * @param dataset_path : Path of the dataset
	 * @return Boolean indicataing path is valid or not
	 */
	private boolean validatePath(String dataset_path)
	{
		try
		{
			Paths.get(dataset_path);
		}
		catch(InvalidPathException | NullPointerException ex)
		{
			return false;
		}
		return true;
	}
	
	/**
	 * Match the schema of given dataset with registered schema
	 * @param cfg : TODO
	 * @return : TODO
	 */
	private boolean validateSchema(Configuration cfg)
	{
		/**
		 * TODO : Add code for this
		 */
		return true;
	}
	
	/**
	 * Reload interval of the dataset
	 * @param interval : Frequency of update
	 * @return : Boolean indicating interval given is valid
	 */
	private boolean validateUpdateInterval(String interval)
	{
		for(String str : ic.updateIntervals)
		{
			if(interval.equals(str))
			{
				return true;
			}
		}
		return false;
	}
	
	private boolean filter(String path)
	{
		String hash = hf.getContentHash(path);
		if(oldHashes.add(hash))
		{
			newHashes.add(hash);
			return true;
		}
		return false;
	}
	
	private void loadOldHashes(String path, HashSet<String> oldHashes)
	{
		String str = null;
		try
		(
			BufferedReader br = new BufferedReader(new FileReader(path));
		)
		{
			while( (str = br.readLine()) != null)
			{
				oldHashes.add(str);
			}
		}
		catch(IOException iex)
		{
			iex.printStackTrace();
		}
	}
	
	private void writeNewHashes(String path, HashSet<String> newHashes)
	{
		try
		(
				BufferedWriter bw = new BufferedWriter(new FileWriter(path, true));
		)
		{
			for(String str : newHashes)
			{
				bw.write(str);
				bw.newLine();
			}
		}
		catch(IOException iex)
		{
			iex.printStackTrace();
		}
	}
}