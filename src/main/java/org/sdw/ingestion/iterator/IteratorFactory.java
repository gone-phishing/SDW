package org.sdw.ingestion.iterator;

public class IteratorFactory 
{
	public EntityResolver getEntityResolver(String format)
	{
		if ( format == null)
		{
			return null;
		}
		
		if ( format.equalsIgnoreCase("JSON"))
		{
			return new JSONIterator();
		}
		else if ( format.equalsIgnoreCase("XML"))
		{
			return new XPathIterator();
		}
		else if ( format.equalsIgnoreCase("CSV"))
		{
			return new CSVIterator();
		}
		
		return null;
	}
}
