package org.sdw.ingestion;

import org.sdw.util.HashFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.sdw.Main;
import org.sdw.util.ConfigReader;
import org.apache.commons.configuration2.Configuration;

public class DatasetLoader
{
	public static Logger LOG = LoggerFactory.getLogger(DatasetLoader.class);
	public ArrayList<Configuration> configurationList = new ArrayList<Configuration>();
	public HashMap<Configuration, String> invalidDatasets = new HashMap<Configuration, String>();
	public HashMap<Configuration, String> validDatasets = new HashMap<Configuration, String>();
	public IngestionConfig ic = new IngestionConfig();
	public HashFilter hf = new HashFilter("MD5");
	public HashSet<String> oldHashes = new HashSet<String>();
	public HashSet<String> newHashes = new HashSet<String>();
	public int countNewDatasets = 0;
	
	/**
	 * Default constructor that separates the valid datasets from invalid ones
	 */
	public DatasetLoader()
	{
		LOG.info("In dataset loader class");
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
		LOG.info("Old file hashes loaded");
		loadOldHashes(ic.hashFile, oldHashes);
		for(Configuration cfg : configurationList)
		{
			if(validate(cfg, invalidDatasets))
			{
				if(filter(cfg.getString("sourceFile")))
				{
					validDatasets.put(cfg, cfg.getString("sourceFile"));
					countNewDatasets++;
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
		if(!validateType(cfg.getString("sourceFormat")))
		{
			invalidDatasets.put(cfg, "Source format not supported");
			return false;
		}
		if(!validatePath(cfg.getString("sourceFile")))
		{
			
			invalidDatasets.put(cfg, "Invalid path: "+cfg.getString("sourceFile") );
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
		if(type == null)
		{
			return false;
		}
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
	private boolean validatePath(String sourceFile)
	{
		File file = new File(sourceFile);
		if(file.exists())
		{
			return true;
		}
		else 
		{
			return false;
		}
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
	
	/**
	 * Filter datasets already hashed
	 * @param path : Path of the dataset whose hash is to be found
	 * @return : Boolean indicating the hash is already scanned or not
	 */
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
	
	/**
	 * Load old content hashes stored in src/main/resources/hash_file
	 * @param path : Path of the hash_file
	 * @param oldHashes : Hashset to load the hashes
	 */
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
	
	/**
	 * Write the new found content hashes
	 * @param path : path of the hash_file
	 * @param newHashes : hashset to add the new hash values
	 */
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