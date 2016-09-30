package org.sdw.ingestion;

import java.io.File;
import java.util.ArrayList;

import org.apache.commons.configuration2.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileParams 
{
	public static final Logger LOG = LoggerFactory.getLogger(FileParams.class);
	
	private File file;
	private long numPartitions = 1;    // Default number of file partitions
	protected Configuration cfg;    // Configuration properties of the parent file
	
	/**
	 * Parametrized constructor
	 * @param cfg : Configuration file for the dataset
	 */
	public FileParams(Configuration cfg)
	{
		this.cfg = cfg;
		this.file = new File(cfg.getString("sourceFile"));
	}
	
	/**
	 * Get the file name from the configuration
	 * @return : File name without its absolute path and extension
	 */
	public String getFileName()
	{
		String fileNameWithExtension[] = file.getName().split("\\.");
		return fileNameWithExtension[0];
	}
	
	/**
	 * Get the source file name with absolute path and the file extension
	 * @return source file name as defined in the cofigurations
	 */
	public String getFileNameWithExtension()
	{
		return cfg.getString("sourceFile");
	}
	
	/**
	 * Get the file format from the configuration
	 * @return Returns the file extension in lower case
	 */
	public String getFileExtension()
	{
		return cfg.getString("sourceFormat").toLowerCase();
	}
	
	/**
	 * Get the file size in Bytes
	 * @return Returns the file length 
	 */
	public long getFileSize()
	{
		return file.length();
	}
	
	/**
	 * Get the parent directory for the file
	 * @return Returns the absolute path of parent directory
	 */
	public String getParentDirectory()
	{
		return file.getParent();
	}
	
	/**
	 * Get the number of file partitions required to fit in the memory based on the maximum file size specified
	 * @param maxSize : Max file size allowed in Ingestion configuration properties
	 * @return : Number of file partitions required (defualt == 1)
	 */
	public long getNumberOfPartitions(long maxSize)
	{
		long fileSize = getFileSize();
		if(fileSize > maxSize)
		{
			numPartitions = (fileSize/maxSize) + 1; 
		}
		return numPartitions;
	}
	
	/**
	 * Dynamically generate the part file names from the given source file 
	 * @return : An array list containing file names with their extensions
	 */
	public ArrayList<String> getPartFileNames()
	{
		ArrayList<String> partFiles = new ArrayList<String>();
		String parentName = getFileName();
		for(int i=1; i<=numPartitions; i++)
		{
			partFiles.add(parentName + "_part" + i + "." + getFileExtension());
		}
		return partFiles;
	}
}
