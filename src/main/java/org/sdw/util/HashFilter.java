package org.sdw.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

public class HashFilter
{
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
			ex.printStackTrace();
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
	 * @throws Exception generated because of i/o
	 */
	public byte[] createChecksum(String filename, String hashFunction) throws Exception
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