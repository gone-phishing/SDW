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

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ritesh Kumar Singh
 *
 */
public class QueueProcessor implements Runnable 
{
	public final static Logger LOG = LoggerFactory.getLogger(QueueProcessor.class);
	
	/**
	 * Default constructor for initialization purpose
	 */
	public QueueProcessor()
	{
		
	}

	/**
	 * Implementation of the Runnable interface's run method
	 * Polls for messages in the shared queue and logs the results on arrival of message
	 */
	@Override
	public void run() 
	{
		try
		{
			ConnectionFactory factory = new ConnectionFactory();
			factory.setUri(System.getenv("CLOUDAMQP_URL"));
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();
			String queueName = "work-queue-1";
			Map<String, Object> params = new HashMap<>();
			params.put("x-ha-policy", "all");
			channel.queueDeclare(queueName, true, false, false, params);
			QueueingConsumer consumer = new QueueingConsumer(channel);
			channel.basicConsume(queueName, false, consumer);
	
			while (true) 
			{
				QueueingConsumer.Delivery delivery = consumer.nextDelivery();
				if (delivery != null) 
				{
					String msg = new String(delivery.getBody(), "UTF-8");
					LOG.info("Message Received: " + msg);
					channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
				}
			}
		}
		catch(IOException | KeyManagementException | NoSuchAlgorithmException | URISyntaxException | ShutdownSignalException | ConsumerCancelledException | InterruptedException ex)
		{
			LOG.error(ex.getMessage(), ex);
		}
	}

}