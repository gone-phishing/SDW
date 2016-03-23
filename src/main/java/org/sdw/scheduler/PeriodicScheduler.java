package org.sdw.scheduler;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import org.apache.commons.configuration2.Configuration;


public class PeriodicScheduler
{
	private Queue<Configuration> scheduleQueue;
	
	public PeriodicScheduler()
	{
		scheduleQueue = new LinkedList<Configuration>();
	}
	
	/**
	 * Add the valid datasets to the queue
	 * @param validDatasets : A hashmap containing configuration for datasets to be loaded
	 */
	public void pushToQueue(HashMap<Configuration, String> validDatasets)
	{
		for(Configuration cfg : validDatasets.keySet())
		{
			scheduleQueue.add(cfg);
		}
	}
}