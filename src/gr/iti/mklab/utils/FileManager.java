package gr.iti.mklab.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.tartarus.snowball.SnowballStemmer;

import me.champeau.ld.UberLanguageDetector;
import weka.core.Instances;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.process.Morphology;
import eu.socialsensor.framework.client.dao.impl.ItemDAOImpl;
import eu.socialsensor.framework.client.dao.impl.MediaItemDAOImpl;
import eu.socialsensor.framework.client.mongo.MongoHandler;
import eu.socialsensor.framework.common.domain.Item;
import eu.socialsensor.framework.common.domain.MediaItem;
import gr.iti.mklab.extractfeatures.ItemFeatures;
import gr.iti.mklab.extractfeatures.TotalFeatures;
import gr.iti.mklab.extractfeatures.TotalFeaturesExtractor;

public class FileManager {

	private static FileManager sInstance = new FileManager();

	public static FileManager getInstance() {
		
		if (sInstance == null) {	
			sInstance = new FileManager();
		}
		return sInstance;

	}
	
	/**
	 * Downloads the file from a given url.
	 * @param url URL source from where the file is downloaded.
	 * @param name String the name of the file to be stored.
	 * @param directory String the name of the directory.
	 * @return the File that has been downloaded.
	 */
	public File downloadFile(URL url, String name, String directory) {
		File CurrentDirectory = new File("C:\\Users\\boididou\\workspace");
		File f2 = new File(CurrentDirectory	+directory + name + ".jpg");
		try {
			BufferedInputStream fr = new BufferedInputStream(url.openStream());
			FileOutputStream fw = new FileOutputStream(f2);

			int c;

			while ((c = fr.read()) != -1) {
				fw.write(c);
			}

			fr.close();
			fw.close();

		} catch (IOException e) {
			System.out.println(name);
			System.out.println(e);
			
		}
		return f2;
	}

	/**
	 * Writes the content of a HashMap<String,String> to a file.
	 * @param hashmap the HashMap<String,String> to be written in the file.
	 */
	public void writeHashmapToFile(HashMap<String, String> hashmap){
		
		File file = new File("C:\\Users\\boididou\\workspace\\TweetFeatureExtraction\\resources\\files\\random_subsets\\training_last_ids1.txt");//your file
		try
		{
		   BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		   
		   for(String p:hashmap.keySet())
		   {
		      bw.write(p + "\t" + hashmap.get(p));
		      bw.newLine();
		   }
		   bw.flush();
		   bw.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void writeSetToFile(List<String> list) {
		File file = new File("C:\\Users\\boididou\\workspace\\TweetFeatureExtraction\\resources\\files\\random_subsets\\training_last_ids.txt");//your file
		try
		{
		   BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		   
		  for (String l:list) {
			  bw.append(l);
			  bw.newLine();
		  }
		   
		   bw.flush();
		   bw.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void writeListToFile(List<String> list) {
		File file = new File("C:\\Users\\boididou\\workspace\\TweetFeatureExtraction\\resources\\files\\random_subsets\\ferry_ids.txt");//your file
		try
		{
		   BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		   
		   for (int i=0;i<list.size();i++) {
			   bw.append(list.get(i));
			   bw.newLine();
		   }
		   bw.flush();
		   bw.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets the file's content to a HashMap<String,String>.
	 * @param file String the name of the file.
	 * @return the HashMap<String,String> which contains the file's data in a comma-separated format.
	 * @throws IOException
	 */
	public HashMap<String, String> loadHashmapfromFile(String file) throws IOException {
		// load hashmap from file
		HashMap<String, String> hashmap = new HashMap<String, String>();
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader(file));
			String line = "";
			while ((line = in.readLine()) != null) {
				String parts[] = line.split("\\s");
				hashmap.put(parts[0], parts[1]);
			}
			in.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		return hashmap;
	}
	
	/**
	 * Sets the file's content to a HashSet<String>.
	 * @param file String the name of the file.
	 * @return the HashSet<String> which contains the file's data.
	 * @throws IOException
	 */
	public HashSet<String> loadSetfromFile(String file) throws IOException {
		// load hashmap from file
		HashSet<String> set = new HashSet<String>();
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader(file));
			String line = "";
			while ((line = in.readLine()) != null) {
				set.add(line);
			}
			in.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		return set;
	}
	
	public void writeDataToFile(String filePath,Instances dataset) throws IOException {
		
		BufferedWriter bw = null;
		
		for (int i = 0; i < dataset.size(); i++) {
			try {

				bw = new BufferedWriter(new FileWriter(filePath, true));
				// if file doesn't exist, then create it
				System.out.println(dataset.get(i).toString());
				bw.append(dataset.get(i).toString());
				bw.newLine();
				bw.flush();
				bw.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void writeSelectedToFile(String filePath, String filePath2, String fileOutput) {
		
		BufferedWriter bw = null;
		BufferedReader in_ids, in_texts;
		
		try {
			bw = new BufferedWriter(new FileWriter(fileOutput, true));
			
			in_ids = new BufferedReader(new FileReader(filePath));
			
			
			String line = "", line2="";
			
			//read in_ids file
			while ((line = in_ids.readLine()) != null) {
				
				in_texts = new BufferedReader(new FileReader(filePath2));
				//read in_texts file
				while ((line2 = in_texts.readLine()) != null) {
					//define each id of the in_texts file
					String id = line2.split(",")[0];
					//check if equals the line of the in_ids file
					if (id.equals(line)) {
						String text = line2.split(",")[1];
						bw.append(id+","+text+",real");
						bw.newLine();
					}
				}
			}
			bw.close();
			
		}
		
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void manageRequestedItems(List<ItemFeatures> itemFeats, String db, String collection, String collection2) throws Exception {
		
		List<String> ids = new ArrayList<String>();
		
		for (ItemFeatures feat:itemFeats) {
			ids.add(feat.getId());
		}
		
		MongoHandler mh = null;
		try {
			mh = new MongoHandler(Vars.LOCALHOST_IP, "Experiments");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		ItemDAOImpl dao = new ItemDAOImpl(Vars.LOCALHOST_IP, db, collection);
		
		for (String id:ids) {
			//id = id.replaceAll("Twitter::", "");
			Item item = dao.getItem(id);
			mh.insert(item, collection2);
		}
		
		
	}
	
}
