package org.sdw.ingestion.backend.flink;

import java.io.Closeable;
import java.util.List;

import org.openrdf.model.Statement;
import org.sdw.ingestion.exception.IngestionException;
import org.sdw.ingestion.plugin.DataTypeInterface;
import org.sdw.ingestion.plugin.GraphDataType;
import org.sdw.util.Config;

public interface IngestionBackend extends Closeable {	
	
	 /**
	  * This method can be used to register flink type
	  * 
	  * @param serializableType
	  */
	 public void registerSerializableType(Class<?> serializableType);
	 
	 /**
	  * Execute pipeline
	  * 
	  * @throws IngestionException
	  */
	 public void executePipeline() throws IngestionException;
	 
//	 public InputPluginBackendBase getInputPlugin();
	 
	 public GraphDataType createGraphDataType(final List<Statement[]> statements);
	 
	 public Config<?> getConfig();
	 
	 public Factory getFactory();
}
