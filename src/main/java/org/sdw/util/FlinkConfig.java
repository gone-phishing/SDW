package org.sdw.util;

import java.util.Iterator;

import org.apache.flink.configuration.Configuration;

public class FlinkConfig implements Config<Configuration> {
	
	/** configuration instance */
	private final Configuration config  = new Configuration();
	
	public FlinkConfig() {}
	
	public FlinkConfig(Config<?> config) {
		if (null == config) {
			return;
		}
		
		Iterator<String> configIterator = config.getKeys();
		while (configIterator.hasNext()) {
			String key = configIterator.next();
			Object property = config.getProperty(key);
			this.addProperty(key, property);
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
				
		if (property instanceof String) {
			this.config.setString(key, (String) property); 
		} else if (property instanceof Double) {
			this.config.setDouble(key, (Double) property); 
		} else if (property instanceof Float) {
			this.config.setFloat(key, (Float) property); 
		} else if (property instanceof Integer) {
			this.config.setInteger(key, (Integer) property); 
		} else if (property instanceof Long) {
			this.config.setLong(key, (Long) property); 
		} else if (property instanceof Boolean) {
			this.config.setBoolean(key, (Boolean) property); 
		} else if (property instanceof byte[]) {
			this.config.setBytes(key, (byte[]) property); 
		} else {
			LOG.warn("Do not know data type of property: " + property);
		}	
	}

	@Override
	public <T> T getProperty(String key, Class<T> propertyClass) {
		String className = propertyClass.getName();
		
		T property = null;
		if (String.class.getName().equals(className)) {
			property = propertyClass.cast(this.config.getString(key, null));
		} else if (Double.class.getName().equals(className)) {
			property = propertyClass.cast(this.config.getDouble(key, -1));
		} else if (Float.class.getName().equals(className)) {
			property = propertyClass.cast(this.config.getFloat(key, -1));
		} else if (Integer.class.getName().equals(className)) {
			property = propertyClass.cast(this.config.getInteger(key, -1));
		} else if (Long.class.getName().equals(className)) {
			property = propertyClass.cast(this.config.getLong(key, -1));
		} else if (Boolean.class.getName().equals(className)) {
			property = propertyClass.cast(this.config.getBoolean(key, false));
		} else if (Byte[].class.getName().equals(className)) {
			property = propertyClass.cast(this.config.getBytes(key, null));
		} else {
			LOG.warn("Could not find matching class type:: " + propertyClass.getName());
		}
		
		return property;
	}

	@Override
	public Object getProperty(String key) {
		
		try {
			Class<?> classType = this.config.getClass(key, Object.class, FlinkConfig.class.getClassLoader());
			return this.getProperty(key, classType);
		} catch (Exception e) {
			throw new RuntimeException("Was not able to get key!");
		}
	}

	@Override
	public Iterator<String> getKeys() {
		return this.config.keySet().iterator();
	}


}
