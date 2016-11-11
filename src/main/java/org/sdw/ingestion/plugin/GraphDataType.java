package org.sdw.ingestion.plugin;

import java.util.Iterator;

import org.sdw.ingestion.exception.IngestionException;
import org.sdw.ingestion.graph.Node;

public abstract class GraphDataType implements DataTypeInterface {
	
	/**
	 * 
	 * @return number of entities/nodes/instances stored in the graph
	 * @throws IngestionException
	 */
	abstract public long getEntityCount() throws IngestionException;
	
	/**
	 * 
	 * @return number of edges between entities/nodes/instances
	 * stored in the graph
	 * @throws IngestionException
	 */
	abstract public long getEdgeCount() throws IngestionException;
	
	/**
	 * Iterator of entities/nodes/instances in graph
	 * 
	 * @return iterator to node instances
	 */
	abstract public Iterator<Node> getEntities() throws IngestionException;
	

}
