/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package org.sdw.scheduler;

import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import org.apache.commons.configuration2.Configuration;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.repeatSecondlyForever;
import static org.quartz.TriggerBuilder.newTrigger;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author Ritesh Kumar Singh
 *
 */
public class PeriodicScheduler
{
	public static final Logger LOG = LoggerFactory.getLogger(PeriodicScheduler.class);
	public static final ConnectionFactory factory = new ConnectionFactory();
	public static Queue<Configuration> scheduleQueue;

	/**
	 * Default constructor to set the triggers
	 */
	public PeriodicScheduler()
	{
		scheduleQueue = new LinkedList<>();
//		try
//		{
//			LOG.info("trying to get url");
//			factory.setUri(System.getenv("CLOUDAMQP_URL"));
//			LOG.info("got url");
//			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
//			scheduler.start();
//			JobDetail jobDetail = newJob(PeriodicUpdater.class).build();
//			Trigger trigger = newTrigger().startNow().withSchedule(repeatSecondlyForever(10)).build();
//			scheduler.scheduleJob(jobDetail, trigger);
//		}
//		catch(SchedulerException | KeyManagementException | NoSuchAlgorithmException | URISyntaxException schedex)
//		{
//			LOG.error(schedex.getMessage(), schedex);
//		}
	}

	/**
	 * Add the valid datasets to the queue
	 * @param validDatasets : A hashmap containing configuration for datasets to be loaded
	 */
	public void pushToQueue(Map<Configuration, String> validDatasets)
	{
		for(Configuration cfg : validDatasets.keySet())
		{
			scheduleQueue.add(cfg);
		}
	}
}