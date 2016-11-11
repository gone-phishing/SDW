package org.sdw.ingestion.plugin.transformation.backend;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.graph.Graph;
import org.apache.flink.graph.Vertex;
import org.sdw.ingestion.backend.flink.FlinkBackend;
import org.sdw.ingestion.backend.flink.IngestionBackend;
import org.sdw.ingestion.graph.Node;
import org.sdw.ingestion.plugin.FlinkGraphDataType;
import org.sdw.ingestion.plugin.GraphDataType;

/**
 * Flink transformation class which can be used for Vertex Transformer operations.
 * 
 * @author kay
 *
 */
public class FlinkVertexTransformationBackend extends TransformationBackend {
	IngestionBackend backend;
	
	public FlinkVertexTransformationBackend(final IngestionBackend backend) {
		this.backend = (FlinkBackend) backend;		
	}
	
	@Override
	public GraphDataType addToBackendPipeline(final GraphDataType graph, final VertexTransformer transformer) {
		FlinkGraphDataType flinkGraph = (FlinkGraphDataType) graph;
		
		// flink transformer which executes generic transfomer in flink environment
		FlinkVertexTransformer flinkTransformer = new FlinkVertexTransformer(transformer);		
		Graph<String, Node, String> updatedGraph = flinkGraph.getFlinkGraph().mapVertices(flinkTransformer);		
		return new FlinkGraphDataType(updatedGraph);
	}
	
	/**
	 * Base Vertex Transformer class
	 * 
	 * @author kay
	 *
	 */
	public static class FlinkVertexTransformer extends VertexTransformer implements MapFunction<Vertex<String, Node>, Node> {

		private static final long serialVersionUID = 3932572755707933513L;
		
		final VertexTransformer transformer;
		
		public FlinkVertexTransformer(final VertexTransformer transformer) {
			this.transformer = transformer;
		}
			
		@Override
		public Node map(Vertex<String, Node> value) throws Exception {
			return this.transform(value.f0, value.f1);
		}
		
		public Node transform(final String key, final Node node) {
			return this.transformer.transform(key, node);
		}
	}
}
