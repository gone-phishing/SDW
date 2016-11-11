package org.sdw.util;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base config class
 * 
 * @author kay
 *
 */
public interface Config<ConfigClass> {
	
	/** logger */
	public static final Logger LOG = LoggerFactory.getLogger(Config.class);	

	/**
	 * @return Configuration object
	 */
	public ConfigClass getSourceConfig();
	
	/**
	 * Add property with key and value
	 * 
	 * @param key
	 * @param property
	 */
	public void addProperty(final String key, final Object property);
	
	/**
	 * Get property for key
	 * 
	 * @param parameterName
	 * @param propertyClass
	 * @return
	 */
	public <T> T getProperty(final String key, final Class<T> propertyClass);
	
	/**
	 * Get property for key
	 * 
	 * @param key
	 * @return
	 */
	public Object getProperty(final String key);
	
	/**
	 * 
	 * @return Set of property keys
	 */
	public Iterator<String> getKeys();

}
