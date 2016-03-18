package org.sdw.mapping;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.commons.configuration2.Configuration;

public class RMLmapper implements RDFmapper
{
	public String sourceFile;
	public String mappingFile;
	public String outputFile;
	
	public RMLmapper(Configuration cfg)
	{
	
	}
	
	public RMLmapper(String sourceFile, String mappingFile, String outputFile)
	{
		this.sourceFile = sourceFile;
		this.mappingFile = mappingFile;
		this.outputFile = outputFile;
	}
	
	public void execute(String sourceFile, String mappingFile, String outputFile)
	{
		
	}
	
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