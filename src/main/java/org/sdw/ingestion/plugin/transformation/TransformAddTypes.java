package org.sdw.ingestion.plugin.transformation;

import java.io.Serializable;

import org.apache.flink.graph.Vertex;
import org.openrdf.model.vocabulary.RDF;
import org.sdw.ingestion.backend.flink.IngestionBackend;
import org.sdw.ingestion.graph.Node;
import org.sdw.ingestion.graph.NodeLiteralObject;
import org.sdw.ingestion.graph.NodeUriObject;
import org.sdw.ingestion.plugin.GraphDataType;
import org.sdw.ingestion.plugin.transformation.backend.TransformationBackend.VertexTransformer;

public class TransformAddTypes extends VertexTransformerBase {

	public TransformAddTypes(IngestionBackend backend, GraphDataType graph) {
		super(backend, graph, new AddTypesTransformer());
		
	}
	
	static class AddTypesTransformer extends VertexTransformer implements Serializable {

		private static final long serialVersionUID = -3735643770211797015L;

		@Override
		public Node transform(final String key, final Node node) {
			node.addPredicateObject(RDF.TYPE.stringValue(), new NodeUriObject("http://dbpedia.org/ontology/Organisation"));
			node.addPredicateObject(RDF.TYPE.stringValue(), new NodeUriObject("http://dbpedia.org/ontology/Company"));
			return node;
		}
		
	}

}
