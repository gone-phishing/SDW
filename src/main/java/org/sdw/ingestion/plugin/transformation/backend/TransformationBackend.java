package org.sdw.ingestion.plugin.transformation.backend;

import org.sdw.ingestion.graph.Node;
import org.sdw.ingestion.plugin.GraphDataType;

/**
 * Abstract backend class which can be used to execute backend transformation task
 * 
 * @author kay
 *
 */
public abstract class TransformationBackend {
	
	abstract public GraphDataType addToBackendPipeline(final GraphDataType graph, final VertexTransformer transformer);
	
	/**
	 * Base Transformer class
	 * 
	 * @author kay
	 *
	 */
	public static abstract class VertexTransformer {

		private static final long serialVersionUID = 3932572755707933513L;
		
		/**
		 * This method can be used to perform the actual transformation operation
		 * 
		 * @param input
		 * @return
		 */
		abstract public Node transform(final String key, final Node node);
	}

}
