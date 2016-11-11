package org.sdw.ingestion.backend.flink;

import org.sdw.ingestion.plugin.transformation.backend.FlinkVertexTransformationBackend;

public class FlinkFactory implements Factory {
	
	final FlinkBackend backend;
	
	public FlinkFactory(final FlinkBackend backend) {
		this.backend = backend;
	}

	@Override
	public FlinkVertexTransformationBackend getVertexTransformerBackend() {
		return new FlinkVertexTransformationBackend(this.backend);
	}
}
