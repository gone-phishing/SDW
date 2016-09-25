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

import java.io.BufferedWriter;
import java.io.File;
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
	
	/**
	 * Constructor to make and populate part files
	 * @param maxSize : Allowed maximum size of a dataset
	 * @param cfg : Configuration for the supplied dataset
	 */
	public FileProcessor(long maxSize, Configuration cfg) 
	{
		super(cfg);
		
		// Check if number of partitions exceed 1 for the given dataset
		if(getNumberOfPartitions(maxSize) > 1)
		{
			// Check if part files were created successfully
			if( makePartFiles( getPartFileNames()) )
			{
				// Populate part files with entities
				populatePartFiles( getFileExtension() );
			}
			else
			{
				LOG.error("Part files couldn't be created");
			}
		}
	}
	
	/**
	 * Populate entities in the part files
	 * @param format : File format of the part files
	 */
	public void populatePartFiles(String format) 
	{
		// Iterator Factory design pattern
		IteratorFactory iteratorFactory = new IteratorFactory();
		EntityResolver entityResolver = iteratorFactory.getEntityResolver(format);
		List<String> extractedEntities = entityResolver.extractEntities(getFileNameWithExtension(), cfg.getString("iterator"));
		
		ArrayList<PrintWriter> writers = new ArrayList<PrintWriter>();
		
		// Round Robin implementation to distribute entities amongst part files
		try
		{
			// Create an iterator over all Print Writers for the part files
			for(String str : partFileAbsolutePaths)
			{
				writers.add(new PrintWriter(new BufferedWriter( new FileWriter(str, true))));
			}
			writersIterator = writers.iterator();
			
			for (String entity : extractedEntities)
			{
				// If reached the end of iterator, start again from the first one
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
			// Close all the Print Writers
			for(PrintWriter pw : writers)
			{
				pw.close();
			}
		}
	}
	
	/**
	 * Makes the part files based on given names
	 * @param partFileNames : File name with extensions to be created
	 * @return boolean based on successful file creation
	 */
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
				LOG.debug("File \""+ str +"\" already exists!!");
				return false;
			}
		}
		validPartedSets.put(cfg, partFileAbsolutePaths);
		return true;
	}
}