package org.sdw;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bootstrap
{
	public static final Logger LOG = LoggerFactory.getLogger(Bootstrap.class);
	/**
	 * Default constructor to test the project setup
	 */
	public Bootstrap()
	{
		/**
		 * TODO : Check system requirements and memory available
		 * TODO : Check if RML_HOME is set
		 * TODO : Check if FLINK_HOME is set
		 * TODO : Check if CLOUDAMPQ_URL is set
		 */
		
	}
	
	/**
	 * Print stats till the point covered
	 * @param success : Number of successful tasks
	 * @param fail : Number of failed tasks
	 */
	public void printStats(int success, int fail)
	{
		LOG.info("-------------Stats-------------");
		int total = success+fail;
		LOG.info("Total datasets tried loading: "+total);
		LOG.info("Success: "+success+"\tFail: "+fail);
		DecimalFormat df = new DecimalFormat("#.###");
		df.setRoundingMode(RoundingMode.CEILING);
		
		LOG.info("Success rate: "+df.format((success*100.0)/total)+" %");
	}
}