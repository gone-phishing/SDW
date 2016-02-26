package sdw;

import java.io.*;
import java.util.*;
import java.security.MessageDigest;

class DataLoader
{
	private String dsLinkFilePath = null;
	private String hashFunction = null;
	private String hashedFile = null;
	private Set<String> loadedHash = new HashSet<>();
	private ArrayList<String> newHashedDatasets = new ArrayList<>();
	private Map<String, String> path_config = new HashMap<String, String>();

	public DataLoader(ConfigLoader configLoader)
	{
		dsLinkFilePath = configLoader.getDatasetLocation();
		hashFunction = configLoader.getHashFunction();
		hashedFile = configLoader.getHashedFile();
		String validTypes[] = configLoader.getValidInputTypes().split(",");

		// Print supported data types
		System.out.print("[INFO] Supported file formats: \n---------->");
		for(String str : validTypes)
		{
			System.out.print(str+" ");
		}
		System.out.println("");

		readOldHashes(hashedFile);

		// Filter datasets on content based hash values
		try
		(
			BufferedReader br = new BufferedReader(new FileReader(dsLinkFilePath));
		)
		{
			String str= null;
			while((str = br.readLine()) != null)
			{
				String params[] = str.split(":");
				if(params[0].startsWith("dataset_path"))
				{
					String fileContentHash = getContentHash(params[1], hashFunction);
					//Check if generated hash value already present in the HashSet
					if(loadedHash.add(fileContentHash))
					{
						String dsConfLocation[] = br.readLine().split(":");
						// Check for the config file for the dataset
						if(dsConfLocation[0].startsWith("dataset_config"))
						{
							newHashedDatasets.add( fileContentHash );
							path_config.put(params[1], dsConfLocation[1]);
						}
						else
						{
							System.out.println("[ERROR] Config file not specified");
							// @TODO --> System.exit(0);
						}
					}
					else
					{
						System.out.println("[INFO] Dataset "+params[1]+" already present in the store...");
						/**
						 * @TODO : Instead of ignoring the conf file, check if it got updated
						 */
						String ignore_conf = br.readLine();
					}
				}
			}
		}
		catch(FileNotFoundException ex1)
		{
			System.out.println("dataset_loc parameter in config file is wrong...");
			ex1.printStackTrace();
		}
		catch(IOException ex2)
		{
			ex2.printStackTrace();
		}

		writeNewHashes(hashedFile);
	}

	public HashMap<String, String> getDatasetWithType() throws Exception
	{
		HashMap<String, String> dsWithType = new HashMap<>();
		for(String str : path_config.keySet())
		{
			ConfigLoader configLoader = new ConfigLoader(path_config.get(str));
			dsWithType.put(str, configLoader.getPropertyValue("type"));
		}
		return dsWithType;
	}

	public void readOldHashes(String path)
	{
		try
		(
			BufferedReader br = new BufferedReader(new FileReader(path));
		)
		{
			String str = null;
			while((str = br.readLine()) != null)
			{
				loadedHash.add(str);
			}
		}
		catch(Exception ex3)
		{
			ex3.printStackTrace();
		}
	}

	public void writeNewHashes(String path)
	{
		try
		(
			BufferedWriter bw = new BufferedWriter(new FileWriter(path, true));
		)
		{
			for(String str : newHashedDatasets)
			{
				bw.write(str);
				bw.newLine();
			}
		}
		catch(Exception ex4)
		{
			ex4.printStackTrace();
		}
	}

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
}