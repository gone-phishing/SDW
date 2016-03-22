package org.sdw;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;
import org.sdw.ingestion.DatasetLoader;
import org.sdw.scheduler.PeriodicScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main
{
	public static Logger LOG = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) throws Exception 
	{
		Bootstrap bs = new Bootstrap();
		DatasetLoader datasetLoader = new DatasetLoader();
		bs.printStats(datasetLoader.validDatasets.size(), datasetLoader.invalidDatasets.size());
		
		PeriodicScheduler periodicScheduler = new PeriodicScheduler();
		periodicScheduler.pushToQueue(datasetLoader.validDatasets);
			
	}
}
