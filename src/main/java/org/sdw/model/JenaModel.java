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
package org.sdw.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.util.FileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ritesh Kumar Singh
 *
 */
public class JenaModel 
{
	public static final Logger LOG = LoggerFactory.getLogger(JenaModel.class);
	private String directoryPath;
	
	/**
	 * Default constructor
	 */
	public JenaModel(String directoryPath)
	{
		this.directoryPath = directoryPath;
		executeCommandShell("rm -rf "+ directoryPath);
		executeCommandShell("mkdir -p "+ directoryPath);
	}
	
	/**
	 * Load rdf dataset as a graph in specified empty directory path
	 * @param outputFile : Path to rdf dataset
	 */
	public void loadDirectory(String outputFile)
	{
		try
		{
			Dataset dataset = TDBFactory.createDataset(directoryPath);
			Model tdb = dataset.getDefaultModel();
			FileManager.get().readModel(tdb, outputFile);
			tdb.close();
			dataset.close();
		}
		catch(Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
		}
		LOG.info("RDF dataset loaded to memory");
	}
	
	/**
	 * Execute a SPARQL query
	 * @param sparqlQuery : SPARQL query string
	 * @param outputPath : Location to write the result of the query
	 */
	public void execQuery(String sparqlQuery, String outputPath) throws FileNotFoundException
	{
		Dataset dataset = TDBFactory.createDataset(directoryPath);
		Model tdb = dataset.getDefaultModel();
		Query query = QueryFactory.create(sparqlQuery);
		QueryExecution qexec = QueryExecutionFactory.create(sparqlQuery, tdb);
		ResultSet results = qexec.execSelect();
		ResultSetFormatter.outputAsCSV(new FileOutputStream(outputPath), results);
		qexec.close();
		tdb.close();
		
	}
	
	/**
	 * Count number of rows in the output of SPARQL query
	 * @param sparqlQueryString : SPARQL query string
	 * @param dataset : Dataset on which the query is to be executed
	 */
	public void countRows(String sparqlQueryString, Dataset dataset)
	{
		Query query = QueryFactory.create(sparqlQueryString) ;
		QueryExecution qexec = QueryExecutionFactory.create(query, dataset) ;
		try 
		{
			ResultSet results = qexec.execSelect();
			for ( ; results.hasNext() ; )
			{
				QuerySolution soln = results.nextSolution() ;
				int count = soln.getLiteral("count").getInt() ;
				System.out.println("count = "+count) ;
			}
		} 
		finally { qexec.close() ; }
	}
	
	/**
	 * Execute a command on local shell
	 * @param command : The command to be executed
	 * @return : String array with index 0 showing exit code and index 1 showing the output of the command
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
}
