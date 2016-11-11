package org.sdw.ingestion.plugin.input;

import java.util.ArrayList;
import java.util.List;

import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.rio.RDFFormat;
import org.sdw.ingestion.backend.flink.IngestionBackend;
import org.sdw.ingestion.exception.IngestionException;
import org.sdw.ingestion.plugin.GraphDataType;

import be.ugent.mmlab.rml.core.RMLEngine;
import be.ugent.mmlab.rml.core.StdRMLEngine;
import be.ugent.mmlab.rml.mapdochandler.extraction.std.StdRMLMappingFactory;
import be.ugent.mmlab.rml.mapdochandler.retrieval.RMLDocRetrieval;
import be.ugent.mmlab.rml.model.RMLMapping;
import be.ugent.mmlab.rml.model.dataset.RMLDataset;

public class RmlInput extends InputPluginBase<GraphDataType> {
	
	public RmlInput(final IngestionBackend backend) {
		super(backend);
	}

	@Override
	public GraphDataType addToPipeline() throws IngestionException {
		try {

			// get rml mapping dataset
			RMLDataset dataset = this.createRmlDataset();
			
		    // list which will hold mapping statements
		    List<Statement[]> statementLists = new ArrayList<>();
		    
		    String subjectString = null;
		    RepositoryConnection connection = dataset.getRepository().getConnection();
		    try {
		    	// create buffer instance which stores NT string representation of statement
		    	List<Statement> statementList = null;
		    	
		    	/// TODO km: check whether this has to be done in blocks
		    	/// TODO km: Find more generic way of doing this --> Try to find way for grouping!
		    	RepositoryResult<Statement> statements = connection.getStatements(null, null, null, true);
		    	while (statements.hasNext()) {
		    		// get statement
		    		Statement statement = statements.next();	
		    		
		    		// create string representation of these statements
		    		
		    		// add subject
		    		Resource subject = statement.getSubject();
		    		if (null == subjectString || false == subjectString.startsWith(subject.toString())) {
		    			if (null != subjectString) {
		    				int listSize = statementList.size();
		    				Statement[] statementArray = new Statement[listSize];
		    				statementLists.add(statementList.toArray(statementArray));
		    			}

		    			// add new list for next node
	    				statementList = null;
	    				statementList = new ArrayList<>();		    			

		    			subjectString = subject.toString();
		    		}
		    		
		    		statementList.add(statement);
		    	}		    	
		    	
		    	statements.close();
		    	
		    } finally {
		    	connection.close();
		    }
		    
		    // now create actual graph data
		    IngestionBackend backend = this.getBackend();
		    return backend.createGraphDataType(statementLists);
		} catch (Exception e) {
			throw new IngestionException("Was not able to load graph data", e);
		}
	}
	
	/**
	 * Method which loads the mapping from the input and creates RDF dataset from the source data
	 * 
	 * @return
	 */
	protected RMLDataset createRmlDataset() {
		String mappingFilePath = this.getBackend().getConfig().getProperty("MappingFilePath", String.class);
		StdRMLMappingFactory mappingFactory = new StdRMLMappingFactory();
		
		// read in mapping document data
        RMLDocRetrieval mapDocRetrieval = new RMLDocRetrieval();
        
        // create RDF repository from mapping file
        Repository repository = 
                mapDocRetrieval.getMappingDoc(mappingFilePath, RDFFormat.TURTLE);
		
        // use config RDF repo to create mapping class
		RMLMapping mapping = mappingFactory.extractRMLMapping(repository);		
		
		RMLEngine engine = new StdRMLEngine();
		 
        // create respository dataset which can store mapping 
		RMLDataset dataset = engine.chooseSesameDataSet(
	                "dataset", null, "nquads");			 
 
		// run actual mapping
	    engine.runRMLMapping(dataset, mapping, "http://test.graph.de", null, null);
	    
	    return dataset;
	}
}
