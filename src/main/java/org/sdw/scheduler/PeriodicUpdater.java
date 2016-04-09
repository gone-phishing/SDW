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

import java.util.HashMap;
import java.util.Map;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
import org.sdw.scheduler.PeriodicScheduler;

/**
 * @author Ritesh Kumar Singh
 *
 */
public class PeriodicUpdater implements Job
{
	public static final Logger LOG = LoggerFactory.getLogger(PeriodicUpdater.class);

	/**
	 * Pushes messages to the shared queue on execution of triggers
	 * @param JobExecutionContext Job execution context
	 * @throws JobExecutionException 
	 */
	@Override
	public void execute(JobExecutionContext jobExecutionContext) 
	{
		try
		{
			Connection connection = PeriodicScheduler.factory.newConnection();
			Channel channel = connection.createChannel();
			String queueName = "work-queue-1";
			Map<String, Object> params = new HashMap<>();
			params.put("x-ha-policy", "all");
			channel.queueDeclare(queueName, true, false, false, params);
			String mesg = "Sent at: "+ System.currentTimeMillis();
			byte[] body = mesg.getBytes("UTF-8");
			channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, body);
			LOG.info("Message sent: "+mesg);
			connection.close();
		}
		catch(Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
		}
	}
}