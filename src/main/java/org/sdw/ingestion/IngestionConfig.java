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

import org.sdw.util.ConfigReader;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.ex.ConfigurationException;

/**
 * @author Ritesh Kumar Singh
 *
 */
public class IngestionConfig extends ConfigReader
{
	public final long maxFileSize;
	public final String commonRdfFormat;
	public final Configuration config;
	public final String[] datasetPaths;
	public final String hashFile;
	public final String hashFunction;
	public final String datasetHashing;
	public final String[] mappingLanguage;
	public final String[] updateIntervals;
	public final String[] validTypes;
	public final String jenaTDBDatabase;
	public final String queryDumpDirectory;
	private static IngestionConfig instance = null;
	
	/**
	 * Default constructor to set the fields
	 */
	private IngestionConfig()
	{
		super("ingestion.properties");
		
		config = getSourceConfig();
		datasetPaths = config.getStringArray("paths");
		validTypes = config.getStringArray("validTypes");
		commonRdfFormat = config.getString("commonRdfFormat");
		hashFunction = config.getString("hashFunction");
		datasetHashing = config.getString("datasetHashing");
		maxFileSize = config.getLong("maxFileSize");
		hashFile = config.getString("hashFile");
		jenaTDBDatabase = config.getString("jenaTDBDatabase");
		queryDumpDirectory = config.getString("queryDumpDirectory");
		mappingLanguage = config.getStringArray("mappingLanguage");
		updateIntervals = config.getStringArray("updateIntervals");
	}
	
	/**
	 * Making it a singleton class
	 * @return IngestionConfig Can be initialized only once throughout the programme
	 */
	public static IngestionConfig getInstance()
	{
		if(instance == null)
		{
			instance = new IngestionConfig();
		}
		return instance;
	}
}
