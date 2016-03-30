package org.sdw.mapping;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.configuration2.Configuration;

public class R2RMLmapper implements RDFmapper
{
	public static final Logger LOG = LoggerFactory.getLogger(R2RMLmapper.class);
	private final String sourceFile;
	private final String mappingFile;
	private final String outputFile;
	private final String sourceFormat;
	
	/**
	 * Parametrized constructor with single input
	 * @param cfg : Configuration file for the dataset
	 */
//	public R2RMLmapper(Configuration cfg)
//	{
//		new R2RMLmapper(cfg.getString("sourceFile"), cfg.getString("mappingFile"), cfg.getString("outputFile"), cfg.getString("sourceFormat"));
//	}
	
	/**
	 * Parametrized constructor for setting the fields
	 * @param sourceFile : path to source file
	 * @param mappingFile : rml mapping file
	 * @param outputFile : file to create after conversion
	 * @param sourceFormat : file format of source dataset
	 */
	public R2RMLmapper(String sourceFile, String mappingFile, String outputFile, String sourceFormat)
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
		/**
		 * TODO : Add implementation for this
		 */
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
			out[0] = "" + exitStatus;
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = "";
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