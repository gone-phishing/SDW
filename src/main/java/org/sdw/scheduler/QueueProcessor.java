package org.sdw.scheduler;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class QueueProcessor 
{
	public final static Logger LOG = LoggerFactory.getLogger(QueueProcessor.class);

	public void processQueue() throws Exception 
	{
		ConnectionFactory factory = new ConnectionFactory();
		factory.setUri(System.getenv("CLOUDAMQP_URL"));
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		String queueName = "work-queue-1";
		Map<String, Object> params = new HashMap<String, Object>();
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

}