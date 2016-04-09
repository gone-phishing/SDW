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
package org.sdw.mapping;

/**
 * @author Ritesh Kumar Singh
 *
 */
public interface RDFmapper
{
	/**
	 * The execute method to carry out conversions
	 * @param sourceFile : Path to the dataset
	 * @param mappingFile : Path to the mapping file
	 * @param outputFile : Path of output converted file
	 */
	public void execute(String sourceFile, String mappingFile, String outputFile);
	
	/**
	 * If source file got modified and so is ingested again, delete the previous output file
	 * @param outputFile : Path of the output file
	 */
	public void deleteOutputIfExists(String outputFile);
}