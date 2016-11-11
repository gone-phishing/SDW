package org.sdw.ingestion.plugin;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.io.OutputFormat;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.graph.Edge;
import org.apache.flink.graph.Graph;
import org.apache.flink.graph.Vertex;
import org.apache.flink.util.Collector;
import org.openrdf.model.Literal;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.sdw.ingestion.backend.flink.FlinkBackend;
import org.sdw.ingestion.backend.flink.IngestionBackend;
import org.sdw.ingestion.exception.IngestionException;
import org.sdw.ingestion.graph.Node;
import org.sdw.ingestion.graph.NodeLiteralObject;
import org.sdw.ingestion.graph.NodeObject;
import org.sdw.ingestion.graph.NodeUriObject;

/**
 * Data type for a Flink Graph
 * 
 * @author kay
 *
 */
public class FlinkGraphDataType extends GraphDataType {
	
	/** graph instance */
	final Graph<String, Node, String> graph;
	
	public FlinkGraphDataType(final Graph<String, Node, String> graph) {
		this.graph = graph;		
	}
	
	public FlinkGraphDataType(final List<Statement[]> statementLists, final FlinkBackend flinkBackend) throws IngestionException {
		Graph<String, Node, String> graph = this.convertStatementsToGraph(flinkBackend, statementLists);
		this.graph = graph;
	}
	
	public Graph<String, Node, String> getFlinkGraph() {
		return this.graph;
	}
	
	/**
	 * This method can be used to create a graph dataset from a list of Sesame/RDF4J statements
	 * 
	 * @param backend
	 * @param statementLists
	 * @return
	 * @throws IngestionException
	 */
	protected Graph<String, Node, String> convertStatementsToGraph(final IngestionBackend backend, final List<Statement[]> statementLists) throws IngestionException {
		if (null == backend || null == statementLists) {
			return null;
		}
		
	    if (false == backend instanceof FlinkBackend) {
	    	throw new IngestionException("Only supports Apache Flink backend!");
	    }
	    
	    FlinkBackend flinkBackend = (FlinkBackend) backend;
	    
	    Configuration config = flinkBackend.getConfig().getSourceConfig();	    
	    ExecutionEnvironment env = flinkBackend.getExecutionEnvironment();
	    
	    DataSet<Statement[]> datasetGraphs = env.fromCollection(statementLists);
	    
		// set up the execution environment
		DataSet<Edge<String, String>> edges = datasetGraphs.flatMap(new EdgeExtractor(config));
		DataSet<Vertex<String, Node>> initialVertices = datasetGraphs.map(new VertexExtractor(config));
		Graph<String, Node, String> rdfGraph = Graph.fromDataSet(initialVertices, edges, env);
		
		return rdfGraph;
	}
	
	/**
	 * Class which can be used to obtain vertex/instance/node properties from input statements
	 * 
	 * @author kay
	 *
	 */
	private static final class VertexExtractor implements MapFunction<Statement[], Vertex<String, Node>> {
		
		private static final long serialVersionUID = -8875465958660563458L;

		/** configuration which could be used here */
		final Configuration config;
		
		private Vertex<String, Node> vertex = new Vertex<>();
		
		public VertexExtractor(final Configuration config) {
			this.config = config;
		}

		@Override
		public Vertex<String, Node> map(Statement[] statements) throws Exception {
			
			Node node = new Node();
			String subjectString = null;
			
			Node currentNode = null;
			
			for (Statement statement : statements) {
				Resource subject = statement.getSubject();
				
				// set URI --> requires that the first URI in array is main URI of entity!
				if (null == subjectString) {
					String uri = subject.stringValue();
					node.setUri(uri);
					
					// do some other book keeping tasks
					subjectString = subject.stringValue();					
					currentNode = node;
				}
				
				if (false == subjectString.equals(subject.stringValue())) {
					subjectString = subject.stringValue();
					
					if (node.getUri().equals(subjectString)) {
						currentNode = node;
					} else {
						String uri = subjectString;
						
						currentNode = new Node();
						currentNode.setUri(uri);					
					}
				}
				
				String predicateString = statement.getPredicate().stringValue();
				NodeObject nodeObject = null;
				
				Value object = statement.getObject();
				if (object instanceof URI) {
					nodeObject = new NodeUriObject(object.stringValue());
				} else if (object instanceof Literal){
					Literal literalObject = (Literal) object;
					
					String language = null;
					URI dataType = literalObject.getDatatype();
					if (null == dataType) {
						language = literalObject.getLanguage();
					}
					
					nodeObject = new NodeLiteralObject(literalObject.stringValue(),
									null != dataType ? dataType.stringValue() : language,
									null != dataType);
				}

				node.addPredicateObject(predicateString, nodeObject);
			}
			
			vertex.f0 = node.getUri();
			vertex.f1 = node;
			
			return vertex;
		}
	}

	/**
	 * Class wgucg cab be used to get edges for new graph
	 * 
	 * @author kay
	 * 
	 *
	 */
	private static final class EdgeExtractor implements FlatMapFunction<Statement[], Edge<String, String>> {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private final Edge<String, String> edge = new Edge<>();
		
		final Configuration config;
		
		public EdgeExtractor(final Configuration config) {
			this.config = config;
		}

		/// TODO Add filters for URIs! --> only get external entities and not internal properties (e.g. rdf:type
		@Override
		public void flatMap(Statement[] statements, Collector<Edge<String, String>> out) throws Exception {
			
			for (Statement statement : statements) {
				Value object = statement.getObject();
				if (false == (object instanceof URI)) {
					continue;
				}
				
				String subjectUri = statement.getSubject().stringValue();
				String objectUri = statement.getPredicate().stringValue();
				
				// only pick different entities
				if (objectUri.startsWith(subjectUri)) {
					continue;
				}
				
				/// TODO get base URI
				if (false == objectUri.startsWith("http://corp.dbpedia.org/resource/")) {
					continue;
				}
				
				edge.f0 = statement.getSubject().stringValue();

				edge.f1 = object.stringValue();
				edge.f2 = statement.getPredicate().stringValue();
				out.collect(edge);
			}
		}
	}

	
	public long getEntityCount() throws IngestionException {
		try {
			return this.graph.numberOfVertices();
		} catch (Exception e) {
			throw new IngestionException("Was not able to count entities", e);
		}
	}
	
	public long getEdgeCount() throws IngestionException {
		try {
			return this.graph.numberOfVertices();
		} catch (Exception e) {
			throw new IngestionException("Was not able to count edges", e);
		}
	}

	@Override
	public Iterator<Node> getEntities() throws IngestionException {
//		DataSet<Vertex<String, Node>> vertices = this.graph.getVertices();
//
//		
//		long entityCount = this.getEntityCount();
//		
//		IterativeDataSet<Vertex<String, Node>> iterator = vertices.iterate((int) entityCount);
		
		return null;
	}
	
	/**
	 * Internal class which can be used to output the data to the output file
	 * 
	 * @author kay
	 *
	 */
	static class FlinkVerticesOutputFormat implements OutputFormat<Vertex<String,Node>> {

		private static final long serialVersionUID = 4223077947638189019L;

		@Override
		public void configure(Configuration parameters) {
			System.out.println("Got parameters: " + parameters.keySet().size());
			
		}

		@Override
		public void open(int taskNumber, int numTasks) throws IOException {
			System.out.println("Task number: " + taskNumber + " numTasks: " + numTasks);					
		}

		@Override
		public void writeRecord(Vertex<String,Node> record) throws IOException {
			System.out.println(record.f1);
		}

		@Override
		public void close() throws IOException {
			System.out.println("close");
			
		}
	};

}
