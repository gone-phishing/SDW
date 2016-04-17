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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;

/**
 * @author Ritesh Kumar Singh
 *
 */
public class JSONIterator implements EntityResolver
{
	public static final Logger LOG = LoggerFactory.getLogger(JSONIterator.class);

	@Override
	public List<String> extractEntities(String path, String expression)
	{
		try 
		{
			if(expression.contains(" "))
			{
				expression = ".[\'" + expression + "\']";
			}
			File jsonFile = new File(path);
			Object val = JsonPath.read(jsonFile, expression);
			List<String> list = new ArrayList<>();
			if (val instanceof JSONArray) 
			{
				JSONArray arr = (JSONArray) val;
				return Arrays.asList(arr.toArray(new String[0]));
			}
			list.add((String) val.toString());
			return list;
		} 
		catch (com.jayway.jsonpath.InvalidPathException ex) 
		{
			LOG.debug("InvalidPathException " + ex + "for " + expression);
			return new ArrayList<>();
		} 
		catch (Exception ex) 
		{
			LOG.error("Exception: " + ex);
			return null;
		}
	}
}
