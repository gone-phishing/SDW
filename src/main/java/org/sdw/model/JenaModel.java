package org.sdw.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.tdb.TDBFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JenaModel 
{
	public static final Logger LOG = LoggerFactory.getLogger(JenaModel.class);
	private String directoryPath;
	
	public JenaModel()
	{
		
	}
	
	public void loadDirectory(String directoryPath, String outputFile)
	{
		this.directoryPath = directoryPath;
		executeCommandShell("rm -rf "+ directoryPath);
		executeCommandShell("mkdir -P "+ directoryPath);
		String command = "tdbloader2 -l "+directoryPath+" "+outputFile;
		String[] res = executeCommandShell(command);
		if(Integer.parseInt(res[0]) == 0)
		{
			LOG.info(res[1]);
		}
		else
		{
			LOG.error(res[1]);
		}
	}
	
	public void execQuery(String sparqlQuery)
	{
		Dataset dataset = TDBFactory.createDataset(directoryPath);
		dataset.begin(ReadWrite.READ);
		
		try
		(
			QueryExecution qexec = QueryExecutionFactory.create(sparqlQuery, dataset)
		)
		{
			ResultSet results = qexec.execSelect() ;
			ResultSetFormatter.out(results) ;
		}
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
	
	private String[] executeCommandShell(String command) 
	{
		LOG.info("Shell command: $"+command);
		StringBuffer op = new StringBuffer();
		String out[] = new String[2];
		Process process;
		try 
		{
			process = Runtime.getRuntime().exec(command);
			process.waitFor();
			int exitStatus = process.exitValue();
			out[0] = Integer.toString(exitStatus);
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) 
			{
				op.append(line + "\n");
			}
			out[1] = op.toString();
		} 
		catch (Exception e) 
		{
			LOG.error(e.getMessage(), e);
		}

		return out;
	}
}
