package org.sdw.scheduler;

import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
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
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class PeriodicScheduler
{
	public final static Logger LOG = LoggerFactory.getLogger(PeriodicScheduler.class);
	public final static ConnectionFactory factory = new ConnectionFactory();
	public Queue<Configuration> scheduleQueue;
	
	public PeriodicScheduler()
	{
		scheduleQueue = new LinkedList<Configuration>();
		
		try
		{
			factory.setUri(System.getenv("CLOUDAMQP_URL"));
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
	        scheduler.start();
	        JobDetail jobDetail = newJob(PeriodicUpdater.class).build();
	        Trigger trigger = newTrigger().startNow().withSchedule(repeatSecondlyForever(5)).build();
	        scheduler.scheduleJob(jobDetail, trigger);
		}
		catch(SchedulerException | KeyManagementException | NoSuchAlgorithmException | URISyntaxException schedex)
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