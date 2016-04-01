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