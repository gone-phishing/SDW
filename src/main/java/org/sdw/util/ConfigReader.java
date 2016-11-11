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

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ritesh Kumar Singh
 *
 */
public class ConfigReader implements Config<Configuration>
{
	public static final Logger LOG = LoggerFactory.getLogger(ConfigReader.class);
	
	/** actual config instance */
	final private Configuration config;
	
	FileBasedConfigurationBuilder<FileBasedConfiguration> builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class);
	
	/**
	 * Single parameter constructor
	 * @param propertyFile : Absolute/ Relative path of property file to be read
	 */
	public ConfigReader(final String propertyFile) 
	{
		Parameters params = new Parameters();
		params.properties().setFileName(propertyFile);
		params.properties().setListDelimiterHandler(new DefaultListDelimiterHandler(','));
		
		this.builder.configure(params.properties());
		
		try {
			this.config = builder.getConfiguration();
		} catch (Exception e) {
			throw new RuntimeException("Was not able to get config!");
		}
	}

	@Override
	public Configuration getSourceConfig() {
		return this.config;
	}

	@Override
	public void addProperty(String key, Object property) {
		if (null == key || null == property) {
			return;
		}
		
		this.config.addProperty(key, property);		
	}

	@Override
	public <T> T getProperty(String key, Class<T> propertyClass) {
		if (null == key || null == propertyClass) {
			return null;
		}
		
		T property = this.config.get(propertyClass, key);		
		return property;
	}
	
	@Override
	public Object getProperty(String key) {
		if (null == key) {
			return null;
		}
		
		Object property = this.config.getProperty(key);
		return property;
	}	

	@Override
	public Iterator<String> getKeys() {
		Iterator<String> iterator = this.config.getKeys();
		return iterator;
	}

	
}