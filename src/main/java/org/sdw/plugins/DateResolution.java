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
package org.sdw.plugins;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.sdw.model.JenaModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ritesh Kumar Singh
 *
 */
public class DateResolution 
{
	public static final Logger LOG = LoggerFactory.getLogger(DateResolution.class);
	
	/**
	 * Default Constructor
	 */
	public DateResolution()
	{
		
	}
	
	/**
	 * Run method called at runtime using java reflection api
	 * @param directoryPath : Directory location to store the query dump files
	 * @param jenaModel : Jena model to perform SPARQL queries upon
	 * @throws Exception : Exception generated while execution
	 */
	public void run(String filePath, JenaModel jenaModel) throws Exception
	{
		LOG.info("Inside DateResolution");
		//jenaModel.execQuery("SELECT ?s ?o WHERE {?s <http://schema.org/name> ?o}", "/home/kilt/Desktop/res.csv");
		//ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
		//DataSet<Tuple2<String, String>> nameset = env.readCsvFile(filePath).ignoreFirstLine().types(String.class, String.class);
		//nameset.map(mapper)
	}
}
