package org.sdw;

import org.sdw.ingestion.DatasetLoader;

public class Bootstrap
{
	public Bootstrap()
	{
		/**
		 * TODO : Check system requirements and memory available
		 */
		System.out.println("In Bootstrap class");
		DatasetLoader datasetLoader = new DatasetLoader();
	}
}