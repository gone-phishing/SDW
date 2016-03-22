package org.sdw;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import org.sdw.ingestion.DatasetLoader;
import org.sdw.scheduler.PeriodicScheduler;

public class Bootstrap
{
	public Bootstrap()
	{
		/**
		 * TODO : Check system requirements and memory available
		 * TODO : Check if RML_HOME is set
		 * TODO : Check if FLINK_HOME is set
		 */
		
	}
	
	/**
	 * Print stats till the point covered
	 * @param success : Number of successful tasks
	 * @param fail : Number of failed tasks
	 */
	public void printStats(int success, int fail)
	{
		System.out.println("-------------Stats-------------");
		int total = success+fail;
		System.out.println("Total datasets tried loading: "+total);
		System.out.println("Success: "+success+"\tFail: "+fail);
		DecimalFormat df = new DecimalFormat("#.###");
		df.setRoundingMode(RoundingMode.CEILING);
		
		System.out.println("Success rate: "+df.format(((success*100.0)/total))+" %");
	}
}