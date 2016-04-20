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

import org.apache.commons.configuration2.Configuration;
import org.sdw.ingestion.DatasetLoader;
import org.sdw.ingestion.IngestionConfig;
import org.sdw.mapping.RMLmapper;
import org.sdw.model.JenaModel;
import org.sdw.scheduler.PeriodicScheduler;
import org.sdw.scheduler.QueueProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception 
	{
		Bootstrap bs = new Bootstrap();
		IngestionConfig ingestionConfig = IngestionConfig.getInstance();
		DatasetLoader datasetLoader = new DatasetLoader(ingestionConfig);
		bs.printStats(datasetLoader.validDatasets.size(), datasetLoader.invalidDatasets.size());
		
		PeriodicScheduler periodicScheduler = new PeriodicScheduler();
		periodicScheduler.pushToQueue(datasetLoader.validDatasets);
		new Thread(new QueueProcessor()).start();
		
		RMLmapper rmlMapper = new RMLmapper(ingestionConfig.commonRdfFormat);
		for (Configuration cfg : PeriodicScheduler.scheduleQueue)
		{
			rmlMapper.execute(cfg);
		}
		LOG.info("Mapping stage complete!");
		
		for (Configuration cfg : PeriodicScheduler.scheduleQueue)
		{
			JenaModel jenaModel = new JenaModel("/home/kilt/datasets/database");
			jenaModel.loadDirectory(cfg.getString("outputFile"));
			//jenaModel.execQuery("SELECT * WHERE {?s <http://schema.org/name> ?o}");
			String[] flowOperators = cfg.getStringArray("flow");
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
					method.invoke(obj, "/home/kilt/Desktop/res.csv", jenaModel);
				}
				catch(SecurityException | NoSuchMethodException | IllegalArgumentException | IllegalAccessException | InvocationTargetException ex)
				{
					LOG.error(ex.getMessage(), ex);
				}
			}
		}
	}
}
