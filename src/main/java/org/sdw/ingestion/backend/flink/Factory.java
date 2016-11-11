package org.sdw.ingestion.backend.flink;

import org.sdw.ingestion.plugin.transformation.backend.TransformationBackend;

public interface Factory {

	public TransformationBackend getVertexTransformerBackend();

}
