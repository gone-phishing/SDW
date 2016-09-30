package org.sdw.ingestion;

import java.util.ArrayList;

import org.apache.commons.configuration2.Configuration;

public class DatasetConfig 
{
	private Configuration cfg;
	private ArrayList<String> partFileAbsolutePaths;
	
	public DatasetConfig(Configuration cfg)
	{
		this.cfg = cfg;
	}
	
	public void setPartFileAbsolutePaths(ArrayList<String> al)
	{
		this.partFileAbsolutePaths = al;
	}
	
	public Configuration getConfiguration()
	{
		return cfg;
	}
	
	public ArrayList<String> getPartFileAbsolutePaths()
	{
		return partFileAbsolutePaths;
	}
}
