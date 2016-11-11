package org.sdw.ingestion.plugin.output;

import java.io.IOException;

import org.apache.flink.api.common.io.OutputFormat;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.graph.Edge;
import org.apache.flink.graph.Vertex;
import org.sdw.ingestion.backend.flink.IngestionBackend;
import org.sdw.ingestion.exception.IngestionException;
import org.sdw.ingestion.graph.Node;
import org.sdw.ingestion.plugin.FlinkGraphDataType;
import org.sdw.ingestion.plugin.GraphDataType;

public class FlinkGraphOutputFile extends OutputPluginBase {
	
	final FlinkGraphDataType flinkGraph;

	public FlinkGraphOutputFile(final GraphDataType graph, final IngestionBackend backend) {
		super(backend);
		
		if (false == graph instanceof FlinkGraphDataType) {
			throw new RuntimeException("Did not get flink graph!");
		}
		
		this.flinkGraph = (FlinkGraphDataType) graph;
	}

	@Override
	public GraphDataType addToPipeline() throws IngestionException {
		
		Configuration params = (Configuration) this.getBackend().getConfig().getSourceConfig();
		params.setString("Test", "blabla");
		
		DataSet<Vertex<String, Node>> verticesResult = this.flinkGraph.getFlinkGraph().getVertices();
		verticesResult.output(new FlinkVerticesOutputFormat());//.withParameters(params);
		
		DataSet<Edge<String, String>> edgesResult = this.flinkGraph.getFlinkGraph().getEdges();
		edgesResult.output(new FlinkEdgesOutputFormat());
		
		return this.flinkGraph;
	}
	
	/**
	 * Internal class which can be used to output the data to the output file
	 * 
	 * @author kay
	 *
	 */
	static class FlinkVerticesOutputFormat implements OutputFormat<Vertex<String,Node>> {

		private static final long serialVersionUID = 4223077947638189019L;

		@Override
		public void configure(Configuration parameters) {
			System.out.println("Got parameters: " + parameters.keySet().size());
			
		}

		@Override
		public void open(int taskNumber, int numTasks) throws IOException {
			System.out.println("Task number: " + taskNumber + " numTasks: " + numTasks);					
		}

		@Override
		public void writeRecord(Vertex<String,Node> record) throws IOException {
			System.out.println(record.f1);
		}

		@Override
		public void close() throws IOException {
			System.out.println("close");
			
		}
	};
	
	/**
	 * Internal class which can be used to output the data to the output file
	 * 
	 * @author kay
	 *
	 */
	static class FlinkEdgesOutputFormat implements OutputFormat<Edge<String,String>> {

		private static final long serialVersionUID = 4223077947638189019L;

		@Override
		public void configure(Configuration parameters) {
			System.out.println("Got parameters: " + parameters.keySet().size());
			
		}

		@Override
		public void open(int taskNumber, int numTasks) throws IOException {
			System.out.println("Task number: " + taskNumber + " numTasks: " + numTasks);					
		}

		@Override
		public void writeRecord(Edge<String,String> record) throws IOException {
			System.out.println(record.f0 + " <--> " + record.f1);
		}

		@Override
		public void close() throws IOException {
			System.out.println("close");
			
		}
	}
}
