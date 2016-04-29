/**
 * 
 */
package org.sdw.plugins;

import org.sdw.model.JenaModel;

/**
 * @author Ritesh Kumar Singh
 *
 */
public interface XEnhancer 
{
	/**
	 * Run method called at runtime using java reflection api
	 * @param directoryPath : Directory location to store the query dump files
	 * @param jenaModel : Jena model to perform SPARQL queries upon
	 * @throws Exception : Exception generated while execution
	 */
	public void run(String directoryPath, JenaModel jenaModel) throws Exception;
}
