package sdw;

import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.util.HashMap;

public class Job 
{

	public static Logger LOG = LoggerFactory.getLogger(Job.class);

	public static void main(String[] args) throws Exception 
	{
		ConfigLoader configLoader = new ConfigLoader("config.properties");
		DataLoader dataLoader = new DataLoader(configLoader);
		HashMap<String, String> dsWithType = dataLoader.getDatasetWithType();

		final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
		//DataSet<String> union_dataset1 = env.readTextFile("/home/gonephishing/aksw/geoquery/custom1.nt").setParallelism(4);
		//DataSet<Tuple2<String, Integer>> counts = text.flatMap(new LineSplitter()).groupBy(0).sum(1).setParallelism(4);
		//union_dataset1.print();

		// execute program
		//env.execute("Flink Java API Skeleton");
		
	}

	public static final class LineSplitter implements FlatMapFunction<String, Tuple2<String, Integer>> 
	{

		@Override
		public void flatMap(String value, Collector<Tuple2<String, Integer>> out) 
		{
			// normalize and split the line
			String[] tokens = value.toLowerCase().split("\\W+");

			// emit the pairs
			for (String token : tokens) 
			{
				if (token.length() > 0) 
				{
					out.collect(new Tuple2<String, Integer>(token, 1));
				}
			}
		}
	}
}
