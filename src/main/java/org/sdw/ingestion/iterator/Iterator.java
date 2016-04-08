package org.sdw.ingestion.iterator;

import java.util.List;

public abstract class Iterator 
{
	public abstract List<String> extractEntities(String path, String expression);
}
