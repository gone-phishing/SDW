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
package org.sdw.mapping;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import org.apache.commons.configuration2.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ritesh Kumar Singh
 *
 */
public class RMLmapper implements RDFmapper
{
	public static final Logger LOG = LoggerFactory.getLogger(RMLmapper.class);
	private static String commonRdfFormat;
	
	/**
	 * Parametrized constructor with single input
	 * @param cfg : Configuration file for the dataset
	 */
	public RMLmapper(String commonRdfFormat)
	{
		this.commonRdfFormat = commonRdfFormat;
	}
	
	/**
	 * Calls the interface's execute method with params set
	 * @param config : Config of the dataset
	 */
	public void execute(Configuration config)
	{
		execute(config.getString("sourceFile"), config.getString("mappingFile"), config.getString("outputFile"));
	}
	
	/**
	 * Implemented from the interface
	 * @param sourceFile : path to source file
	 * @param mappingFile : rml mapping file
	 * @param outputFile : file to create after conversion
	 */
	public void execute(String sourceFile, String mappingFile, String outputFile)
	{
		deleteOutputIfExists(outputFile);
		String rmlHome = System.getenv("RML_HOME");
		String baseDir = System.getProperty("user.dir");
		rmlHome = baseDir+"/lib/";
		String command = "java -jar "+rmlHome+"RML-Mapper.jar -m "+mappingFile+" -o "+outputFile+" -f "+commonRdfFormat;
		String res[] = executeCommandShell(command);
		if(Integer.parseInt(res[0]) != 0)
		{
			LOG.error("ERROR : Could not convert the file to rdf format");
		}
		else
		{
			LOG.info(res[1]);
		}
	}
	
	/**
	 * Execute command on local shell
	 * @param command : Command to be executed
	 * @return : A string array with exit code and output of execution
	 */
	private String[] executeCommandShell(String command) 
	{
		LOG.info("Shell command: $"+command);
		StringBuffer op = new StringBuffer();
		String out[] = new String[2];
		Process process;
		try 
		{
			process = Runtime.getRuntime().exec(command);
			process.waitFor();
			int exitStatus = process.exitValue();
			out[0] = Integer.toString(exitStatus);
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) 
			{
				op.append(line + "\n");
			}
			out[1] = op.toString();
		} 
		catch (Exception e) 
		{
			LOG.error(e.getMessage(), e);
		}

		return out;
	}
	
	/**
	 * Deletes the output file if it already exists
	 * @param outputFile : delete this file if already exists
	 */
	public void deleteOutputIfExists(String outputFile)
	{
		LOG.info("Deleting old output file to replace with new one");
		File file = new File(outputFile);
		if(file.exists())
		{
			String command = "rm "+outputFile;
			String res[] = executeCommandShell(command);
			if(Integer.parseInt(res[0]) != 0)
			{
				LOG.error("ERROR: Could not delete file: "+outputFile);
			}
		}
	}
}