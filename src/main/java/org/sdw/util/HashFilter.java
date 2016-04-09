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
package org.sdw.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ritesh Kumar Singh
 *
 */
public class HashFilter
{
	public static final Logger LOG = LoggerFactory.getLogger(HashFilter.class);
	private final String hashFunction;

	/**
	 * Parametrized constructor with hash function as input
	 * @param hashFunction : Initialize the hashFunction string
	 */
	public HashFilter(String hashFunction)
	{
		this.hashFunction = hashFunction;
	}

	/**
	 * @param path : Path of the file for which the hash value is to be found out
	 * @param hashFunction : Can be MD5, SHA1 or any like these
	 * @return Hash value in string format
	 */
	public String getContentHash(String path, String hashFunction)
	{
		String result = "";
		try
		{
			byte[] b = createChecksum(path, hashFunction);
			for (int i=0; i < b.length; i++) 
			{
				result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
			}
		}
		catch(Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
		}
		return result;
	}

	/**
	 * @param path : Path of the file for which the hash value is to be found out 
	 * @return Hash value in string format
	 * This method uses the hash function defined in the constructor
	 */
	public String getContentHash(String path)
	{
		return getContentHash(path, hashFunction);
	}

	/**
	 * @param filename : Path of the file for which checksum value is to be calculated
	 * @param hashFunction : hashing function to be used for the purpose
	 * @return A hashed byte array
	 * @throws NoSuchAlgorithmException 
	 * @throws IOException 
	 */
	public byte[] createChecksum(String filename, String hashFunction) throws NoSuchAlgorithmException, IOException
	{
		InputStream fis =  new FileInputStream(filename);

		byte[] buffer = new byte[1024];
		MessageDigest complete = MessageDigest.getInstance(hashFunction);
		int numRead;
		do 
		{
			numRead = fis.read(buffer);
			if (numRead > 0) 
			{
				complete.update(buffer, 0, numRead);
			}
		} while (numRead != -1);
		fis.close();
		return complete.digest();
	}
}