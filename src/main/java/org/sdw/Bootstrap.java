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
package org.sdw;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisConnection;
import com.lambdaworks.redis.RedisStringsConnection;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.api.StatefulRedisConnection;

/**
 * @author Ritesh Kumar Singh
 *
 */
public class Bootstrap
{
	public static final Logger LOG = LoggerFactory.getLogger(Bootstrap.class);
	RedisClient redisClient = new RedisClient( RedisURI.create("redis://localhost"));
	/**
	 * Default constructor to test the project setup
	 */
	public Bootstrap()
	{
		/**
		 * TODO : Check system requirements and memory available
		 * TODO : Check if RML_HOME is set
		 * TODO : Check if FLINK_HOME is set
		 * TODO : Check if CLOUDAMPQ_URL is set
		 * TODO : Check if JENA_HOME is set
		 */
		Recovery rc = new Recovery();
		if(System.getenv("RML_HOME") == null)
		{
			rc.checkAlternates("$RML_HOME is not set. Expect mapping failure");
		}
		if(System.getenv("FLINK_HOME") == null)
		{
			rc.checkAlternates("$FLINK_HOME is not set. Flow operatos can't work");
		}
		if(System.getenv("CLOUDAMQP_URL") == null)
		{
			rc.checkAlternates("$CLOUDAMQP_URL is not set. RabbitMQ shared queue can't be created");
		}
		if(System.getenv("JENA_HOME") == null)
		{
			rc.checkAlternates("$JENA_HOME is not set. Memory model can't be created");
		}
	}
	
	/**
	 * Print stats till the point covered
	 * @param success : Number of successful tasks
	 * @param fail : Number of failed tasks
	 */
	public void printStats(int success, int fail)
	{
		LOG.info("-------------Stats-------------");
		int total = success+fail;
		LOG.info("Total datasets tried loading: "+total);
		LOG.info("Success: "+success+"\tFail: "+fail);
		DecimalFormat df = new DecimalFormat("#.###");
		df.setRoundingMode(RoundingMode.CEILING);
		
		LOG.info("Success rate: "+df.format((success*100.0)/total)+" %");
	}
	
	/**
	 * Create a Redis connection using Lettuce client API
	 * @return StatefulRedisConnection<String, String> : Connection to a Redis instance
	 */
	public StatefulRedisConnection<String, String> redisConnection()
	{
		StatefulRedisConnection<String, String> connection = redisClient.connect();
		LOG.info("Connected to Redis");
		return connection;
	}
	
	/**
	 * Close connection to the Redis instance
	 * @param connection : The redis instance connection to be closed
	 * @param clientConnection : If set, the client connection will also be shut down
	 */
	public void closeRedisConnection(StatefulRedisConnection<String, String> connection, Boolean clientConnection)
	{
		connection.close();
		if(clientConnection)
		{
			redisClient.shutdown();
		}
	}
}