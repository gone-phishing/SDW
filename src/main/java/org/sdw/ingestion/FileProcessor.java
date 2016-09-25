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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration2.Configuration;
import org.sdw.ingestion.iterator.EntityResolver;
import org.sdw.ingestion.iterator.IteratorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ritesh Kumar Singh
 *
 */
public class FileProcessor extends FileParams
{
	public static final Logger LOG = LoggerFactory.getLogger(FileProcessor.class);
	
	private Map<Configuration, ArrayList<String>> validPartedSets = new HashMap<>();
	private ArrayList<String> partFileAbsolutePaths = new ArrayList<String>();
	private Iterator<PrintWriter> writersIterator;
	
	public FileProcessor(long maxSize, Configuration cfg) 
	{
		super(cfg);
		if(getNumberOfPartitions(maxSize) > 1)
		{
			if( makePartFiles( getPartFileNames()) )
			{	
				populatePartFiles( getFileExtension() );
			}
			else
			{
				LOG.error("Part files couldn't be created");
			}
		}
	}
	
	public void populatePartFiles(String format) 
	{
		IteratorFactory iteratorFactory = new IteratorFactory();
		EntityResolver entityResolver = iteratorFactory.getEntityResolver(format);
		List<String> extractedEntities = entityResolver.extractEntities(getFileNameWithExtension(), cfg.getString("iterator"));
		
		ArrayList<PrintWriter> writers = new ArrayList<PrintWriter>();
		
		try
		{
			for(String str : partFileAbsolutePaths)
			{
				writers.add(new PrintWriter(new BufferedWriter( new FileWriter(str, true))));
			}
			writersIterator = writers.iterator();
			
			for (String entity : extractedEntities)
			{
				if(!writersIterator.hasNext())
				{
					writersIterator = writers.iterator();
				}
				else
				{
					PrintWriter pw = writersIterator.next();
					pw.println(entity);
				}
			}
		}
		catch(IOException iex)
		{
			iex.printStackTrace();
		}
		finally
		{
			for(PrintWriter pw : writers)
			{
				pw.close();
			}
		}
	}
	
	public boolean makePartFiles(ArrayList<String> partFileNames)
	{
		String parentDir = getParentDirectory();
		for(String str : partFileNames)
		{
			File partFile = new File(parentDir, str);
			if(!partFile.exists())
			{
				try 
				{
					partFile.createNewFile();
					partFileAbsolutePaths.add(parentDir + "/" + str);
					LOG.info("File created: "+parentDir + "/" + str);
				} 
				catch (IOException e) 
				{
					LOG.error(e.getMessage(), e);
					return false;
				}
			}
			else
			{
				LOG.info("File \""+ str +"\" already exists!!");
			}
		}
		validPartedSets.put(cfg, partFileAbsolutePaths);
		return true;
	}
}