//package org.sdw.ingestion.plugin.transformation;
//
//import org.apache.flink.api.common.functions.MapFunction;
//import org.apache.flink.graph.Graph;
//import org.apache.flink.graph.Vertex;
//import org.sdw.ingestion.backend.flink.FlinkFactory;
//import org.sdw.ingestion.backend.flink.IngestionBackend;
//import org.sdw.ingestion.exception.IngestionException;
//import org.sdw.ingestion.graph.Node;
//import org.sdw.ingestion.plugin.FlinkGraphDataType;
//import org.sdw.ingestion.plugin.GraphDataType;
//
//public abstract class FlinkVertexTransformerBase extends VertexTransformerBase {
//	
//	final FlinkGraphDataType flinkGraph;
//	
//	final FlinkVertexTransformer transformer;
//
//	public FlinkVertexTransformerBase(final IngestionBackend backend, final GraphDataType graph, final FlinkVertexTransformer transformer) {
//		super(backend);
//		
//		if (false == graph instanceof FlinkGraphDataType) {
//			throw new RuntimeException("Only Flink graph is supported!");
//		}
//		
//		this.flinkGraph = (FlinkGraphDataType) graph;
//		this.transformer = transformer;
//	}
//
//
//	
//	/**
//	 * Base Transformer class
//	 * 
//	 * @author kay
//	 *
//	 */
//	protected static abstract class FlinkVertexTransformer extends VertexTransformer implements MapFunction<Vertex<String, Node>, Node> {
//
//		private static final long serialVersionUID = 3932572755707933513L;
//			
//		@Override
//		public Node map(Vertex<String, Node> value) throws Exception {
//			return this.transform(value.f0, value.f1);
//		}
//	}
//}
