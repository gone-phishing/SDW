package org.sdw.ingestion.iterator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSONIterator 
{
	public static final Logger LOG = LoggerFactory.getLogger(JSONIterator.class);
	
	public List<String> extractEntities(String path, String expression)
	{
		List<String> entity = new ArrayList<>();
		try
		(
			BufferedReader br = new BufferedReader(new FileReader(path));
		)
		{
			String line = null;
			while( (line = br.readLine()) != null)
			{
				entity.add(line);
			}
		}
		catch(Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
		}
		return entity;
	}
}
