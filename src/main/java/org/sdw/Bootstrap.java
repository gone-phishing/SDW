package org.sdw;

import org.sdw.ingestion.DatasetLoader;
import org.sdw.scheduler.PeriodicScheduler;

public class Bootstrap
{
	public Bootstrap()
	{
		/**
		 * TODO : Check system requirements and memory available
		 */
		System.out.println("In Bootstrap class");
		DatasetLoader datasetLoader = new DatasetLoader();
		PeriodicScheduler periodicScheduler = new PeriodicScheduler();
		periodicScheduler.pushToQueue(datasetLoader.validDatasets);
	}
}