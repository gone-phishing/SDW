/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.sdw.util.ConfigReader;
import org.apache.commons.configuration2.Configuration;

/**
 * @author Ritesh Kumar Singh
 *
 */
public class DatasetLoader
{
	public static final Logger LOG = LoggerFactory.getLogger(DatasetLoader.class);
	
	private final HashFilter hf = new HashFilter("MD5");
	private final List<Configuration> configurationList = new ArrayList<>();
	public final Map<Configuration, String> invalidDatasets = new HashMap<>();
	public final Map<DatasetConfig, String> validDatasets = new HashMap<>();
	private final Set<String> oldHashes = new HashSet<>();
	private final Set<String> newHashes = new HashSet<>();
	private final IngestionConfig ic;
	
	/**
	 * Default constructor that separates the valid datasets from invalid ones
	 * @param ic : Ingestion configuration parameters
	 */
	public DatasetLoader(IngestionConfig ic)
	{
		this.ic = ic;
		String[] datasetPaths = ic.datasetPaths;
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
		for (Configuration cfg : configurationList) 
		{
			if(validate(cfg, invalidDatasets))
			{
				if(ic.datasetHashing.equals("true"))
				{
					if(filter(cfg.getString("sourceFile")))
					{
						validDatasets.put(new DatasetConfig(cfg), cfg.getString("sourceFile"));
					}
					else
					{
						invalidDatasets.put(cfg, "Dataset already loaded");
					}	
				}
				else if (ic.datasetHashing.equals("false"))
				{
					validDatasets.put(new DatasetConfig(cfg), cfg.getString("sourceFile"));
				}
				else
				{
					LOG.error("Invalid value for datasetHashing in ingestion config file");
				}
				
			}
		}
		if(invalidDatasets.size() > 0) 
		{
			for(Configuration cfg : invalidDatasets.keySet())
			{
				LOG.warn("[WARN] "+cfg.getString("name")+" : "+ invalidDatasets.get(cfg));
			}
		}
		
		if(!newHashes.isEmpty()) 
		{
			writeNewHashes(ic.hashFile, newHashes);
			LOG.info("New hashes written");
		}
	}
	
	/**
	 * Validate all property values
	 * @param cfg : Configuration of the dataset
	 * @param invalidDatasets : Invalid datasets will be added to this hashmap with their error message
	 * @return : Boolean indicating validity of the dataset config file
	 */
	private boolean validate(Configuration cfg, Map<Configuration, String> invalidDatasets)
	{
		if(!validateType(cfg.getString("sourceFormat")))
		{
			invalidDatasets.put(cfg, "Source format not supported");
			return false;
		}
		String[] filePaths = {cfg.getString("sourceFile"), cfg.getString("mappingFile")};
		if(!validatePath(filePaths))
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
	private boolean validatePath(String[] filePaths)
	{
		for(String str : filePaths)
		{
			File file = new File(str);
			if(!file.exists())
			{
				return false;
			}
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
	private void loadOldHashes(String path, Set<String> oldHashes)
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
			LOG.error(iex.getMessage(), iex);
		}
	}
	
	/**
	 * Write the new found content hashes
	 * @param path : path of the hash_file
	 * @param newHashes : hashset to add the new hash values
	 */
	private void writeNewHashes(String path, Set<String> newHashes)
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
			LOG.error(iex.getMessage(), iex);;
		}
	}
}