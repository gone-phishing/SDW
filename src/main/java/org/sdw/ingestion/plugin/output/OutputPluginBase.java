package org.sdw.ingestion.plugin.output;

import org.sdw.ingestion.backend.flink.IngestionBackend;
import org.sdw.ingestion.plugin.PluginBase;

public abstract class OutputPluginBase extends PluginBase {

	public OutputPluginBase(IngestionBackend backend) {
		super(backend);
	}	
}
