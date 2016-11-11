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
package org.sdw;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.sdw.ingestion.DatasetConfig;
import org.sdw.ingestion.DatasetLoader;
import org.sdw.ingestion.FileProcessor;
import org.sdw.ingestion.IngestionConfig;
import org.sdw.mapping.RMLmapper;
import org.sdw.model.JenaModel;
import org.sdw.scheduler.PeriodicScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ritesh Kumar Singh
 *
 */
public class Main
{
	public static final Logger LOG = LoggerFactory.getLogger(Main.class);
	
	/**
	 * Default constructor
	 */
	private Main()
	{
		
	}

	/**
	 * Main method to execute the code sequentially
	 * @param args Arguments to the main method
	 * @throws Exception Exceptions generated during execution
	 */
	public static void main(String[] args) throws Exception 
	{
		// Check for project setup
		Bootstrap bs = new Bootstrap();
		
		// Get instance of IngestionConfig singleton class
		IngestionConfig ingestionConfig = IngestionConfig.getInstance();
		
		// Load configs for all the datasets
		DatasetLoader datasetLoader = new DatasetLoader(ingestionConfig);
		
		// Display stats for Dataset Loader
		bs.printStats(datasetLoader.validDatasets.size(), datasetLoader.invalidDatasets.size());
		
		// Break the datasets to fit into memory
		FileProcessor fileProcessor = new FileProcessor(ingestionConfig.maxFileSize);
		
		// Push to queue for later processing
		PeriodicScheduler periodicScheduler = new PeriodicScheduler();
		
		for(DatasetConfig datasetConfig : datasetLoader.validDatasets.keySet())
		{
			fileProcessor.processConfig(datasetConfig.getConfiguration());
			datasetConfig.setPartFileAbsolutePaths(fileProcessor.getPartFileAbsolutePaths());
			periodicScheduler.pushToQueue(datasetConfig);
		}
		
		// Make a class with dataset config properties and arraylist getters. Push that to schedule queue and parallel executeor for RML
		//new Thread(new QueueProcessor()).start();
		
		//Convert datasets to RDF format using RML mapper
		RMLmapper rmlMapper = new RMLmapper(ingestionConfig.commonRdfFormat);
		for (DatasetConfig datasetConfig : PeriodicScheduler.scheduleQueue)
		{
			rmlMapper.parallelExecutor(datasetConfig, datasetConfig.getPartFileAbsolutePaths().size());
		}
		LOG.info("Mapping stage complete!");

		/// TODO km: Clean up here!!
		// Load datasets to memory using Jena model
		for (DatasetConfig datasetConfig : PeriodicScheduler.scheduleQueue)
		{
			JenaModel jenaModel = new JenaModel(ingestionConfig.jenaTDBDatabase);
			jenaModel.loadDirectory(datasetConfig.getConfiguration().getString("outputFile"));
			String[] flowOperators = datasetConfig.getConfiguration().getStringArray("flow");
			
			// Use java reflection api to resolve flow operators directly at run time
			Class params[] = new Class[2];
			params[0] = String.class;
			params[1] = jenaModel.getClass();
			
			for(String str : flowOperators)
			{
				try
				{
					Class c = Class.forName("org.sdw.plugins."+str);
					Object obj = c.newInstance();
					Method method = c.getDeclaredMethod("run", params);
					method.invoke(obj, ingestionConfig.queryDumpDirectory, jenaModel);
				}
				catch(SecurityException | NoSuchMethodException | IllegalArgumentException | IllegalAccessException | InvocationTargetException ex)
				{
					LOG.error(ex.getMessage(), ex);
				}
			}
		}
		LOG.info("Finished!!");
		System.exit(0);
	}
}
