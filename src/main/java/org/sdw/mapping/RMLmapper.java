package org.sdw.mapping;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.commons.configuration2.Configuration;

public class RMLmapper implements RDFmapper
{
	public String sourceFile;
	public String mappingFile;
	public String outputFile;
	public String sourceFormat;
	
	public RMLmapper(Configuration cfg)
	{
		new RMLmapper(cfg.getString("sourceFile"), cfg.getString("mappingFile"), cfg.getString("outputFile"), cfg.getString("sourceFormat"));
	}
	
	public RMLmapper(String sourceFile, String mappingFile, String outputFile, String sourceFormat)
	{
		this.sourceFile = sourceFile;
		this.mappingFile = mappingFile;
		this.outputFile = outputFile;
		this.sourceFormat = sourceFormat;
	}
	
	public void execute(String sourceFile, String mappingFile, String outputFile)
	{
		String command = "$RML_HOME/bin/RML-Mapper -m "+mappingFile+" -o "+outputFile;
		String res[] = executeCommandShell(command);
		if(Integer.parseInt(res[0]) != 0)
		{
			System.out.println("ERROR : Could not convert the file to rdf format");
		}
		else
		{
			System.out.println(res[1]);
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
			e.printStackTrace();
		}

		return out;
	}
}