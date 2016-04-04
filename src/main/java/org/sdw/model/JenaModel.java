package org.sdw.model;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
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
		execQuery("SELECT * WHERE {?s ?p ?o}", dataset);
		dataset.end();
	}
	
	public void execQuery(String sparqlQuery, Dataset dataset)
	{
		QueryExecution qexec = QueryExecutionFactory.create(sparqlQuery, dataset) ;
		ResultSet results = qexec.execSelect() ;
		results.toString();
		ResultSetFormatter.out(results) ;
		qexec.close();
	}

	public void countRows(String sparqlQueryString, Dataset dataset)
    {
        Query query = QueryFactory.create(sparqlQueryString) ;
        QueryExecution qexec = QueryExecutionFactory.create(query, dataset) ;
        try {
            ResultSet results = qexec.execSelect() ;
            for ( ; results.hasNext() ; )
            {
                QuerySolution soln = results.nextSolution() ;
                int count = soln.getLiteral("count").getInt() ;
                System.out.println("count = "+count) ;
            }
          } finally { qexec.close() ; }
    }

}
