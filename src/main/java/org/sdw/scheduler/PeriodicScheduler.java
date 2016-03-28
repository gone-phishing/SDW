package org.sdw.scheduler;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import org.apache.commons.configuration2.Configuration;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.repeatSecondlyForever;
import static org.quartz.TriggerBuilder.newTrigger;

public class PeriodicScheduler
{
	public static Logger LOG = LoggerFactory.getLogger(PeriodicScheduler.class);
	private Queue<Configuration> scheduleQueue;
	
	public PeriodicScheduler()
	{
		scheduleQueue = new LinkedList<Configuration>();
		try
		{
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
	        scheduler.start();
	        JobDetail jobDetail = newJob(PeriodicUpdater.class).build();
	        Trigger trigger = newTrigger().startNow().withSchedule(repeatSecondlyForever(2)).build();
	        scheduler.scheduleJob(jobDetail, trigger);
		}
		catch(SchedulerException schedex)
		{
			schedex.printStackTrace();
		}
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