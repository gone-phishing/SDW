package org.sdw;

import org.apache.commons.configuration2.Configuration;
import org.sdw.ingestion.DatasetLoader;
import org.sdw.ingestion.IngestionConfig;
import org.sdw.mapping.RMLmapper;
import org.sdw.model.JenaModel;
import org.sdw.scheduler.PeriodicScheduler;
import org.sdw.scheduler.QueueProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main
{
	public static final Logger LOG = LoggerFactory.getLogger(Main.class);
	
	/**
	 * Default constructor
	 */
	private Main()
	{
		
	}

	/**
	 * Main method to execute the code sequentially
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception 
	{
		Bootstrap bs = new Bootstrap();
		IngestionConfig ingestionConfig = IngestionConfig.getInstance();
		DatasetLoader datasetLoader = new DatasetLoader(ingestionConfig);
		bs.printStats(datasetLoader.validDatasets.size(), datasetLoader.invalidDatasets.size());
		
		PeriodicScheduler periodicScheduler = new PeriodicScheduler();
		periodicScheduler.pushToQueue(datasetLoader.validDatasets);
		new Thread(new QueueProcessor()).start();
		
		RMLmapper rmlMapper = new RMLmapper(ingestionConfig.commonRdfFormat);
		for (Configuration cfg : PeriodicScheduler.scheduleQueue)
		{
			rmlMapper.execute(cfg);
		}
		LOG.info("Mapping stage complete!");
		
		for (Configuration cfg : PeriodicScheduler.scheduleQueue)
		{
			rmlMapper.execute(cfg);
			JenaModel jenaModel = new JenaModel();
			jenaModel.loadDirectory("/home/kilt/datasets/database/", cfg.getString("outputFile"));
			jenaModel.execQuery("SELECT * WHERE {?s <http://schema.org/name> ?o}");
		}
	}
}
