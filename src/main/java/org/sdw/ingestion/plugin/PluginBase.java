package org.sdw.ingestion.plugin;

import org.sdw.ingestion.backend.flink.IngestionBackend;
import org.sdw.ingestion.exception.IngestionException;
import org.sdw.util.Config;

/**
 * This is an interface which is implement by each plugin
 * 
 * @author kay
 *
 */
public abstract class PluginBase {
	
	/** instance which points to actual backend of this system (e.g. Flink, Spark, Beam) */
	final private IngestionBackend backend;
	
	public PluginBase(final IngestionBackend backend) {
		this.backend = backend;
	}
	
	abstract public DataTypeInterface addToPipeline() throws IngestionException;
	
	public Config<?> getConfig() {
		return this.backend.getConfig();
	}
	
	public IngestionBackend getBackend() {
		return this.backend;
	}

}
