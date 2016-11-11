package org.sdw.ingestion.graph;

import java.io.Serializable;

/**
 * This class represents an object/value of a node predicate
 * 
 * @author kay
 *
 */
public abstract class NodeObject implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5417149082057679824L;

	/** string of triple object */
	String objectString;
	
	public NodeObject(final String objectString) {
		this.objectString = objectString;
	}
	
	/**
	 * @return Returns the string representation of this object
	 */
	public String getStringValue() {
		return this.objectString;
	}
	
	public void setStringValue(final String objectString) {
		this.objectString = objectString;
	}
	
}

