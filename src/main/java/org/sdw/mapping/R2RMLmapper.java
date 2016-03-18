package org.sdw.mapping;

public class R2RMLmapper implements RDFmapper
{
	public String sourceFile;
	public String mappingFile;
	public String outputFile;
	
	public R2RMLmapper(String sourceFile, String mappingFile, String outputFile)
	{
		this.sourceFile = sourceFile;
		this.mappingFile = mappingFile;
		this.outputFile = outputFile;
	}
	
	public void execute(String sourceFile, String mappingFile, String outputFile)
	{
		
	}
}