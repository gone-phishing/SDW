package org.sdw.ingestion.graph;

public class NodeLiteralObject extends NodeObject {
	
	private static final long serialVersionUID = -6933727985444280648L;

	/** literal's datatype */
	final String dataType;
	
	/** language of literal string */
	final String language;
	
	/**
	 * Constructor for just a literal string value
	 * @param literalValue
	 */
	public NodeLiteralObject(final String literalValue) {
		this(literalValue, null, false);
	}
	
	/**
	 * Literal with its value. The dataTypeLanguage variable can be either a datatype or a language ID,
	 * which is specified by the isDataType boolean variable.
	 * 
	 * @param literalValue		- actual value
	 * @param dataTypeLanguage	- additional information
	 * @param isDataType		- specifies whether dataTypeLanguage is a datatype or a language ID
	 */
	public NodeLiteralObject(final String literalValue, final String dataTypeLanguage, final boolean isDataType) {
		super(literalValue);
		if (isDataType) {
			this.dataType = dataTypeLanguage;
			this.language = null;
		} else {
			this.dataType = null;
			this.language = dataTypeLanguage;
		}
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("\"").append(this.getStringValue()).append("\"");
		
		if (null != this.dataType) {
			builder.append("^^").append("<").append(this.dataType).append(">");
		} else {
			builder.append("@").append(this.language);
		}
		
		return builder.toString();
	}
}
