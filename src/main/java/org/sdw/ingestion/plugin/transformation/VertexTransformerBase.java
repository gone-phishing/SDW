package org.sdw.ingestion.plugin.transformation;

import org.sdw.ingestion.backend.flink.IngestionBackend;
import org.sdw.ingestion.exception.IngestionException;
import org.sdw.ingestion.plugin.GraphDataType;
import org.sdw.ingestion.plugin.PluginBase;
import org.sdw.ingestion.plugin.transformation.backend.TransformationBackend.VertexTransformer;

public abstract class VertexTransformerBase extends PluginBase {
	
	final GraphDataType graphDataType;
	final VertexTransformer transformer;

	public VertexTransformerBase(IngestionBackend backend, final GraphDataType graphDataType, final VertexTransformer transformer) {
		super(backend);
		
		this.graphDataType = graphDataType;
		this.transformer = transformer;
	}
	
	@Override
	public GraphDataType addToPipeline() throws IngestionException {
		return this.getBackend().getFactory().
				getVertexTransformerBackend().
				addToBackendPipeline(this.graphDataType, this.transformer);
	}
}
