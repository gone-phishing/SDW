package org.sdw;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.sdw.ingestion.backend.flink.FlinkBackend;
import org.sdw.ingestion.plugin.FlinkGraphDataType;
import org.sdw.ingestion.plugin.GraphDataType;
import org.sdw.ingestion.plugin.input.RmlInput;
import org.sdw.ingestion.plugin.output.FlinkGraphOutputFile;
import org.sdw.ingestion.plugin.transformation.TransformAddTypes;
import org.sdw.ingestion.plugin.transformation.TransformLowerCaseName;
import org.sdw.util.FlinkConfig;

/**
 * This class can be used as a starting point
 * 
 * @author kay
 *
 */
public class Starter {

	private static final Log logger = LogFactory.getLog(Starter.class);

	static public void main(String[] args) {
		System.out.println("Flink Tester");

		try {
			// URL gridInputUrl =
			// Starter.class.getClassLoader().getResource("grid20.rml.ttl");
			File gridMappingUrl = new File(
					"/home/kay/Uni/Projects/SmartDataWeb/Code/IngestionRitesh/SDW/examples/grid.rml.ttl");
			String mapDoc = gridMappingUrl.getPath();

			List<String> rmlArgs = new ArrayList<>();
			String outputFile = "/home/kay/grid.output.test";

			FlinkConfig config = new FlinkConfig();
			config.addProperty("MappingFilePath", mapDoc);

			// get backend instance
			FlinkBackend flinkBackend = new FlinkBackend(config);

			/************************************
			 * Input Layer
			 ************************************/
			RmlInput rmlInput = new RmlInput(flinkBackend);
			GraphDataType graph = rmlInput.addToPipeline();
			if (false == graph instanceof FlinkGraphDataType) {
				throw new RuntimeException("Did not get flink graph");
			}

			/************************************
			 * Transformation / Computation Layer
			 ************************************/
			FlinkGraphDataType flinkGraph = (FlinkGraphDataType) graph;

			System.out.println("Count: " + flinkGraph.getFlinkGraph().numberOfVertices());

			TransformLowerCaseName lowerCaser = new TransformLowerCaseName(flinkBackend, flinkGraph);
			GraphDataType lowerCaseGraph = lowerCaser.addToPipeline();

			TransformAddTypes addTypes = new TransformAddTypes(flinkBackend, lowerCaseGraph);
			GraphDataType addedTypesGraph = addTypes.addToPipeline();

			/************************************
			 * Output Layer
			 ************************************/
			FlinkGraphOutputFile graphOutput = new FlinkGraphOutputFile(addedTypesGraph, flinkBackend);
			graphOutput.addToPipeline();

			/************************************
			 * Execution
			 ************************************/
			flinkBackend.executePipeline();
			flinkBackend.close();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
