package org.sdw.ingestion.backend.flink;

import java.io.IOException;
import java.util.List;

import org.apache.flink.api.java.CollectionEnvironment;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.openrdf.model.Statement;
import org.sdw.ingestion.exception.IngestionException;
import org.sdw.ingestion.graph.Node;
import org.sdw.ingestion.graph.NodeLiteralObject;
import org.sdw.ingestion.graph.NodeObject;
import org.sdw.ingestion.graph.NodeUriObject;
import org.sdw.ingestion.plugin.FlinkGraphDataType;
import org.sdw.ingestion.plugin.GraphDataType;
import org.sdw.util.FlinkConfig;

/**
 * Flink backend class
 * 
 * @author kay
 *
 */
public class FlinkBackend implements IngestionBackend {
	
	 /** environment for flink */
	 final ExecutionEnvironment flinkEnv;
	 
	 /** config */
	 final FlinkConfig config;
	 
	 final FlinkFactory factory;
	 
	 /**
	  * 
	  * @param flinkEnv
	  */
	 public FlinkBackend(final FlinkConfig config) {
		 this.flinkEnv = new CollectionEnvironment();
		 this.config = config;
		 
		 this.factory = new FlinkFactory(this);
		 
		 this.flinkEnv.registerType(Node.class);
		 this.flinkEnv.registerType(NodeUriObject.class);
		 this.flinkEnv.registerType(NodeObject.class);
		 this.flinkEnv.registerType(NodeLiteralObject.class); 
	 }
	 
	 @Override
	 public void registerSerializableType(Class<?> serializableType) {
		 this.flinkEnv.registerType(serializableType);
	 }
	 
	 @Override
	 public void executePipeline() throws IngestionException {
		 try {
			 flinkEnv.execute();
		 } catch (Exception e) {
			 throw new IngestionException("Was not able to execute Flink job", e);
		 }
	 }
	 
	@Override
	public void close() throws IOException {}
	 
	 /**
	  * 
	  * @return Flink Execution Environment
	  */
	 public ExecutionEnvironment getExecutionEnvironment() {
		 return this.flinkEnv;
	 }



//	@Override
//	public InputPluginBackendBase getInputPlugin() {
//		FlinkInputPlugin inputBase = new FlinkInputPlugin(this);
//		return inputBase;
//	}

	@Override
	public FlinkConfig getConfig() {
		return this.config;
	}

	@Override
	public GraphDataType createGraphDataType(final List<Statement[]> statements) {
		// TODO Auto-generated method stub
		try {
			return new FlinkGraphDataType(statements, this);
		} catch (Exception e) {
			throw new RuntimeException("Was not able to convert statements to Flink Graph", e);
		}
	}

	@Override
	public Factory getFactory() {
		return factory;
	}

//	@Override
//	public <T extends DataTypeInterface> InputPluginBase<T> getInputPlugin(Class<T> dataTypeClass) {
//		FlinkInputPluginBase<T> inputBase = new FlinkInputPluginBase<T>();
//		return null;
//	}
}
