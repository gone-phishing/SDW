package org.sdw.ingestion.plugin.transformation;

import java.io.Serializable;
import java.util.List;

import org.apache.flink.graph.Vertex;
import org.sdw.ingestion.backend.flink.IngestionBackend;
import org.sdw.ingestion.graph.Node;
import org.sdw.ingestion.graph.NodeObject;
import org.sdw.ingestion.plugin.GraphDataType;
import org.sdw.ingestion.plugin.transformation.backend.TransformationBackend.VertexTransformer;

public class TransformLowerCaseName extends VertexTransformerBase {
	
	public TransformLowerCaseName(final IngestionBackend backend, final GraphDataType graph) {
		super(backend, graph, new LowerCaser());
	}	
	
	protected static class LowerCaser extends VertexTransformer implements Serializable {

		private static final long serialVersionUID = -4051528154600759673L;

		@Override
		public Node transform(final String key, final Node node) {		
			lowerCaseObjects(node, "http://www.w3.org/2004/02/skos/core#altLabel");
			lowerCaseObjects(node, "http://www.w3.org/2004/02/skos/core#prefLabel");
			return node;
		}
		
		/**
		 * This method lowercases the input data
		 * @param node
		 * @param predicateString
		 */
		private void lowerCaseObjects(final Node node, final String predicateString) {
			List<NodeObject> objects = node.getPredicateValue(predicateString);
			if (null == objects) {
				return;
			}
			
			for (NodeObject object : objects) {
				object.setStringValue(object.getStringValue().toLowerCase());
			}
		}		
	}
}
