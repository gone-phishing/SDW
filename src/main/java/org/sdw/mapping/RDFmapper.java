package org.sdw.mapping;

public interface RDFmapper
{
	/**
	 * The execute method to carry out conversions
	 * @param sourceFile : Path to the dataset
	 * @param mappingFile : Path to the mapping file
	 * @param outputFile : Path of output converted file
	 */
	public void execute(String sourceFile, String mappingFile, String outputFile);
	
	/**
	 * If source file got modified and so is ingested again, delete the previous output file
	 * @param outputFile : Path of the output file
	 */
	public void deleteOutputIfExists(String outputFile);
}