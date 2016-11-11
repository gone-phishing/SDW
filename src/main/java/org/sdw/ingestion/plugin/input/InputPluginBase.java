package org.sdw.ingestion.plugin.input;

import org.sdw.ingestion.backend.flink.IngestionBackend;
import org.sdw.ingestion.plugin.DataTypeInterface;
import org.sdw.ingestion.plugin.PluginBase;

/**
 * This interface has to be implemented by each
 * input plugin
 * 
 * @author kay
 *
 */
public abstract class InputPluginBase<T extends DataTypeInterface> extends PluginBase {

	public InputPluginBase(IngestionBackend backend) {
		super(backend);
	}
}
