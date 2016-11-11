package org.sdw.ingestion.graph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class which can be used to store information about a node
 * 
 * @author kay
 *
 */
public class Node implements Serializable {
	
	private static final long serialVersionUID = -7826053348993841758L;
	
	/** This is the node URI */
	protected String uri;
	
	/** list of child nodes */
	protected List<Node> childNodes = null;
	
	protected Map<String, List<NodeObject>> predicatesObjectMap = null;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		if (null == this.predicatesObjectMap) {
			builder.append("<").append(this.uri).append(">");
		} else {
			for (String predicate : this.predicatesObjectMap.keySet()) {
				List<NodeObject> objectValues = this.predicatesObjectMap.get(predicate);					
				if (null == objectValues) {
					continue;
				}
				
				for (NodeObject objectValue : objectValues) {
					builder.append("<").append(this.uri).append(">");
					builder.append(" <").append(predicate).append("> ");
					builder.append(objectValue);
					builder.append(" .\n");
				}					
			}
		}			
		
		if (null != this.childNodes) {
			for (Node child : this.childNodes) {
				builder.append(child);
			}
		}
		
		return builder.toString();
	}
	
	/**
	 * Method which can be used to set URI of entity.
	 * 
	 * @param uri
	 */
	public void setUri(final String uri) {
		this.uri = uri;
	}
	
	public String getUri() {
		return this.uri;
	}
	
	/**
	 * This method can be used to add predicates and objects to this node/entity
	 * 
	 * @param predicateString
	 * @param object
	 */
	public void addPredicateObject(final String predicateString, final NodeObject object) {
		if (null == predicateString || null == object) {
			return;
		}
		
		if (null == this.predicatesObjectMap) {
			this.predicatesObjectMap = new HashMap<>();
		}
		
		List<NodeObject> objectList = this.predicatesObjectMap.get(predicateString);
		if (null == objectList) {
			objectList = new ArrayList<>();
			this.predicatesObjectMap.put(predicateString, objectList);
		}
		
		objectList.add(object);
	}
	
	/**
	 * This method can be used to obtain all predicate values
	 * 
	 * @param predicateString
	 * @return
	 */
	public List<NodeObject> getPredicateValue(final String predicateString) {
		if (null == predicateString || null == this.predicatesObjectMap) {
			return null;
		}
		
		List<NodeObject> objects = this.predicatesObjectMap.get(predicateString);
		return objects;
	}
	
	/**
	 * This method can be used to add a child node to this node
	 * (e.g. location entity of an the main entity)
	 * 
	 * @param childNode - child node/entity of main entity
	 */
	public void addChildNode(final Node childNode) {
		if (null == childNode) {
			return;
		}
		
		if (null == this.childNodes) {
			this.childNodes = new ArrayList<>();
		}
		
		this.childNodes.add(childNode);
	}
	
}
