package org.sdw.model;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb.TDBFactory;

public class JenaModel 
{
	public JenaModel()
	{
		
	}
	
	public void loadDirectory(String directoryPath)
	{
		Dataset dataset = TDBFactory.createDataset(directoryPath);
		dataset.begin(ReadWrite.READ);
		Model model = dataset.getDefaultModel();
		dataset.end();
	}
	
	
}
