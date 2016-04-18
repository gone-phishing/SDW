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

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.util.Collector;
import org.sdw.Main;
import org.sdw.model.JenaModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ritesh Kumar Singh
 *
 */
public class NameResolution 
{
	public static final Logger LOG = LoggerFactory.getLogger(NameResolution.class);
	public NameResolution()
	{
		
	}
	
	public void run(String filePath, JenaModel jenaModel)
	{
		jenaModel.execQuery("SELECT ?s ?o WHERE {?s <http://schema.org/name> ?o}");
		ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
		//DataStream<Tuple3<String, String, String>> datastream = env.readCsvFile(filePath).ignoreFirstLine();
		LOG.info("Inside NameResolution");
	}
}
