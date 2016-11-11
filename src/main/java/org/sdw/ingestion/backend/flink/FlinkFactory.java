package org.sdw.ingestion.backend.flink;

import org.sdw.ingestion.plugin.transformation.backend.FlinkTransformationBackend;

public class FlinkFactory implements Factory {
	
	final FlinkBackend backend;
	
	public FlinkFactory(final FlinkBackend backend) {
		this.backend = backend;
	}

	@Override
	public FlinkTransformationBackend getVertexTransformerBackend() {
		return new FlinkTransformationBackend(this.backend);
	}
}
