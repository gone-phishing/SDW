package org.sdw;

import org.apache.commons.configuration2.Configuration;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;
import org.sdw.ingestion.DatasetLoader;
import org.sdw.ingestion.IngestionConfig;
import org.sdw.mapping.RMLmapper;
import org.sdw.scheduler.PeriodicScheduler;
import org.sdw.scheduler.QueueProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main
{
	public static final Logger LOG = LoggerFactory.getLogger(Main.class);
	
	private Main()
	{
		
	}

	public static void main(String[] args) throws Exception 
	{
		Bootstrap bs = new Bootstrap();
		IngestionConfig ingestionConfig = IngestionConfig.getInstance();
		DatasetLoader datasetLoader = new DatasetLoader(ingestionConfig);
		bs.printStats(datasetLoader.validDatasets.size(), datasetLoader.invalidDatasets.size());
		
		PeriodicScheduler periodicScheduler = new PeriodicScheduler();
		periodicScheduler.pushToQueue(datasetLoader.validDatasets);
		new Thread(new QueueProcessor()).start();;
		for (Configuration cfg : periodicScheduler.scheduleQueue)
		{
			new RMLmapper(cfg, ingestionConfig.commonRdfFormat);
		}
		LOG.info("Success!");
	}
}
