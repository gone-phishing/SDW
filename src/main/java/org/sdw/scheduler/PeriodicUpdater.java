package org.sdw.scheduler;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PeriodicUpdater implements Job
{
	public static Logger LOG = LoggerFactory.getLogger(PeriodicUpdater.class);
	
	@Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException 
	{
		LOG.info("HelloJob executed");
    }
}