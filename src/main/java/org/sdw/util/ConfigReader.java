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
package org.sdw.util;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ritesh Kumar Singh
 *
 */
public class ConfigReader
{
	public static final Logger LOG = LoggerFactory.getLogger(ConfigReader.class);
	public FileBasedConfigurationBuilder<FileBasedConfiguration> builder;
	private Configuration config;
	
	/**
	 * Single parameter constructor
	 * @param propertyFile : Absolute/ Relative path of property file to be read
	 */
	public ConfigReader(String propertyFile)
	{
		Parameters params = new Parameters();
		builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class).configure(params.properties().setFileName(propertyFile).setListDelimiterHandler(new DefaultListDelimiterHandler(',')));
	}
	
	/**
	 * @return Configuration object
	 */
	public Configuration getConfig()
	{
		try
		{
			config = builder.getConfiguration();
		}
		catch(ConfigurationException cex)
		{
			LOG.error(cex.getMessage(), cex);
		}
		return config;
	}
}