package org.sdw.ingestion.exception;

/**
 * General Ingestion Exception
 * 
 * @author kay
 *
 */
public class IngestionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5889244123317334649L;
	
	public IngestionException(final String message) {
		super(message);
	}
	
	public IngestionException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
