package org.sdw.mapping;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import org.apache.commons.configuration2.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RMLmapper implements RDFmapper
{
	public static final Logger LOG = LoggerFactory.getLogger(RMLmapper.class);
	public String sourceFile;
	public String mappingFile;
	public String outputFile;
	public String sourceFormat;
	
	/**
	 * Parametrized constructor with single input
	 * @param cfg : Configuration file for the dataset
	 */
//	public RMLmapper(Configuration cfg)
//	{
//		new RMLmapper(cfg.getString("sourceFile"), cfg.getString("mappingFile"), cfg.getString("outputFile"), cfg.getString("sourceFormat"));
//	}
	
	/**
	 * Parametrized constructor for setting the fields
	 * @param sourceFile : path to source file
	 * @param mappingFile : rml mapping file
	 * @param outputFile : file to create after conversion
	 * @param sourceFormat : file format of source dataset
	 */
	public RMLmapper(String sourceFile, String mappingFile, String outputFile, String sourceFormat)
	{
		this.sourceFile = sourceFile;
		this.mappingFile = mappingFile;
		this.outputFile = outputFile;
		this.sourceFormat = sourceFormat;
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
		
		String command = "$RML_HOME/bin/RML-Mapper -m "+mappingFile+" -o "+outputFile;
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
	 * @param outputFile : delete this file if already exists
	 */
	public void deleteOutputIfExists(String outputFile)
	{
		File file = new File(outputFile);
		if(file.exists())
		{
			String command = "rm "+outputFile;
			String res[] = executeCommandShell(command);
			if(Integer.parseInt(res[0]) != 0)
			{
				System.out.println("ERROR: Could not delete file: "+outputFile);
			}
		}
	}
}