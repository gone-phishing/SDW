package org.sdw.ingestion.graph;

/**
 * This class stores a URI
 * @author kay
 *
 */
public class NodeUriObject extends NodeObject {

	private static final long serialVersionUID = 8147680114885229225L;

	public NodeUriObject(String objectString) {
		super(objectString);
	}
	
	@Override
	public String toString() {
		return "<" + this.getStringValue() + ">";
	}
}
