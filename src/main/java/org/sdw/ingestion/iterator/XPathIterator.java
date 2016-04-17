/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package org.sdw.ingestion.iterator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nu.xom.Attribute;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;
import nu.xom.ParsingException;
import nu.xom.XPathContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ritesh Kumar Singh
 *
 */
public class XPathIterator implements EntityResolver 
{
	public static final Logger LOG = LoggerFactory.getLogger(XPathIterator.class);
	public XPathContext nsContext = new XPathContext();

	/* (non-Javadoc)
	 * @see org.sdw.ingestion.iterator.EntityResolver#extractEntities(java.lang.String, java.lang.String)
	 */
	@Override
	public List<String> extractEntities(String path, String expression) 
	{
		File file = new File(path);
		Document document = null;
		try 
		{
			document = new Builder().build(file);
		} 
		catch (ParsingException | IOException e) 
		{
			LOG.error(e.getMessage(), e);
		}
		Node node  = document.getDocument();
		return extractEntities(node, expression);
	}

	/**
	 * Process a XPath expression against an XML node
	 * @param node
	 * @param expression
	 * @return value that matches expression
	 */
	private List<String> extractEntities(Node node, String expression) 
	{
		List<String> list = new ArrayList<>();
		Nodes nodes = node.query(expression, nsContext);

		for (int i = 0; i < nodes.size(); i++) 
		{
			Node n = nodes.get(i);
			if (!n.getValue().isEmpty() || (n.getChildCount() != 0)) 
			{
				if (!(n instanceof Attribute) && n.getChildCount() > 1) 
				{
					list.add(n.getValue().trim().replaceAll("[\\t\\n\\r]", " ").replaceAll(" +", " ").replaceAll("\\( ", "\\(").replaceAll(" \\)", "\\)").replaceAll(" :", ":").replaceAll(" ,", ","));
				} 
				else 
				{
					list.add(n.getValue().toString());
				}
			}
		}

		return list;
	}
}
