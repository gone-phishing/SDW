package org.sdw.mapping;

public interface RDFmapper
{
	/**
	 * The common method across all mappers
	 * @param sourceFile : Path to the dataset
	 * @param mappingFile : Path to the mapping file
	 * @param outputFile : Path of output converted file
	 */
	public void execute(String sourceFile, String mappingFile, String outputFile);
	
	public void deleteOutputIfExists(String outputFile);
}