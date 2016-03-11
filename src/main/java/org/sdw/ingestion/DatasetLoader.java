package org.sdw.ingestion;

public class DatasetLoader 
{
	public DatasetLoader()
	{
		IngestionConfig ic = new IngestionConfig();
		System.out.println("This is lame: "+ ic.get("hashFunction"));
	}
}
