package gr.iti.mklab.extractfeatures;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import me.champeau.ld.UberLanguageDetector;
import weka.core.Instances;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import eu.socialsensor.framework.common.domain.Item;
import eu.socialsensor.framework.common.domain.MediaItem;
import gr.iti.mklab.utils.StringProcessing;
import gr.iti.mklab.utils.TextProcessing;
import gr.iti.mklab.utils.URLProcessing;
import gr.iti.mklab.utils.Vars;
import gr.iti.mklab.verifyutils.WebOfTrustManager;


/**
 * Class that extracts ItemFeatures of an Item
 * @author boididou
 */
public class ItemFeaturesExtractor {
	
	static String[] tokens;
	static String itemTitle;
	public static Instances isTrainingSet;
	static ArrayList<Integer[]> occurrenceArr = new ArrayList<Integer[]>();
	
	/**
	 * Function that extracts features of an Item
	 * @param item Item for which features are extracted
	 * @param lang String that provides the item's language 
	 * @return ItemFeatures object with the features extracted
	 */
	public static ItemFeatures extractFeatures(Item item,String lang) {

		ItemFeatures feat = new ItemFeatures();

		feat.id = item.getId();
		feat.itemLength = itemTitle.length();
		feat.numWords = getNumItemWords();
		feat.containsQuestionMark = containsSymbol("?");
		feat.containsExclamationMark = containsSymbol("!");
		feat.numExclamationMark = getNumSymbol("!");
		feat.numQuestionMark = getNumSymbol("?");
		feat.containsHappyEmo = containsEmo(Vars.HAPPY_EMO_PATH);
		feat.containsSadEmo = containsEmo(Vars.SAD_EMO_PATH);
		feat.numUppercaseChars = getNumUppercaseChars();
		feat.containsFirstOrderPron = containsPronoun(Vars.FIRST_PRON_PATH);
		feat.containsSecondOrderPron = containsPronoun(Vars.SECOND_PRON_PATH);
		feat.containsThirdOrderPron = containsPronoun(Vars.THIRD_PRON_PATH);
		feat.numMentions = getNumMentions();
		feat.numHashtags = getNumHashtags();
		feat.numURLs = getNumURLs();
		
		if (lang.equals("en")) {
			feat.numPosSentiWords = getNumSentiWords(Vars.POS_WORDS_ENG_PATH);
			feat.numNegSentiWords = getNumSentiWords(Vars.NEG_WORDS_ENG_PATH);
		}
		else if (lang.equals("es")) {
			feat.numPosSentiWords = getNumSentiWords(Vars.POS_WORDS_ES_PATH);
			feat.numNegSentiWords = getNumSentiWords(Vars.NEG_WORDS_ES_PATH);
		}
		else if (lang.equals("de")) {
			feat.numPosSentiWords = getNumSentiWords(Vars.POS_WORDS_DE_PATH); 
			feat.numNegSentiWords = getNumSentiWords(Vars.NEG_WORDS_DE_PATH);
		}
		feat.retweetCount = getRetweetsCount(item);

		return feat;
	}
	/**
	 * Function that extracts features of a MediaItem
	 * @param item MediaItem for which features are extracted
	 * @param lang String that provides the item's language 
	 * @return ItemFeatures object with the features extracted
	 * @throws IOException 
	 * @throws MalformedURLException 
	 */
	public static ItemFeatures extractFeatures(MediaItem item,String lang) throws MalformedURLException, IOException {

		ItemFeatures feat = new ItemFeatures();

		feat.setId(item.getId());
		feat.setItemLength(itemTitle.length());
		feat.setNumWords(getNumItemWords());
		feat.setContainsQuestionMark(containsSymbol("?"));
		feat.setContainsExclamationMark(containsSymbol("!"));
		feat.setNumExclamationMark(getNumSymbol("!"));
		feat.setNumQuestionMark(getNumSymbol("?"));
		feat.setContainsHappyEmo(containsEmo(Vars.HAPPY_EMO_PATH));
		feat.setContainsSadEmo(containsEmo(Vars.SAD_EMO_PATH));
		feat.setNumUppercaseChars(getNumUppercaseChars());
		feat.setContainsFirstOrderPron(containsPronoun(Vars.FIRST_PRON_PATH));
		feat.setContainsSecondOrderPron(containsPronoun(Vars.SECOND_PRON_PATH));
		feat.setContainsThirdOrderPron(containsPronoun(Vars.THIRD_PRON_PATH));
		feat.setNumMentions(getNumMentions());
		feat.setNumHashtags(getNumHashtags());
		feat.setNumURLs(getNumURLs());
		feat.setRetweetCount(getRetweetsCount(item));
		feat.setHasColon(containsSymbol(":"));
		feat.setHasPlease(containsSymbol("please"));
		
		//external links (except for the image link)
		HashSet<String> extLinks = checkForExternalLinks(item);
		feat.setHasExternalLink( !(extLinks.isEmpty()) );
				
		if (lang.equals("en")) {
			feat.setNumPosSentiWords(getNumSentiWords(Vars.POS_WORDS_ENG_PATH));
			feat.setNumNegSentiWords(getNumSentiWords(Vars.NEG_WORDS_ENG_PATH));
		}
		else if (lang.equals("es")) {
			feat.setNumPosSentiWords(getNumSentiWords(Vars.POS_WORDS_ES_PATH));
			feat.setNumNegSentiWords(getNumSentiWords(Vars.NEG_WORDS_ES_PATH));
		}
		else if (lang.equals("de")) {
			feat.setNumPosSentiWords(getNumSentiWords(Vars.POS_WORDS_DE_PATH)); 
			feat.setNumNegSentiWords(getNumSentiWords(Vars.NEG_WORDS_DE_PATH));
		}
		

		return feat;
	}

	/**
	 * @param str String that clears Item's text and tokenizes it 
	 * @return String[] that keeps the tokens computed
	 */
	public static String[] tokenizeText(String str) {

		// replace all the useless chars inside the text
		// str = str.replaceAll("RT ",""); //clear retweet
		str = str.replaceAll(",", " "); // Clear commas
		// str = str.replaceAll("@", ""); // Clear @'s (optional)
		str = str.replaceAll("$", " "); // Clear $'s (optional)
		// str = str.replaceAll("&#\\d+;", " "); //change
		// &#[digits]; to space
		str = str.replaceAll("&quot;", " "); // change &quot; to space
		str = str.replaceAll("http://[^ ]+ ", " "); // drop urls
		str = str.replaceAll("-", " ");
		// characters
		str = str.replaceAll("/", " ");
		str = str.replaceAll("=", " ");
		str = str.replaceAll("\\!", " "); // drop exclamation mark
		// str = str.replaceAll("\\?" , "\\\\?");
		str = str.replaceAll("\\.+", " "); // Clear dots
		str = str.replaceAll("[^a-zA-Z&#@_\\d+; ]", " "); // drop
															// non-alphanumeric
		str = str.trim();
		str = str.replaceAll("\\s+", " ");

	

		// split the result string by space
		String tokens[] = str.split(" ");

		return tokens;
	}

	/**
	 * @return Integer the number of words of the text
	 */
	public static Integer getNumItemWords() {

		// call the tokenizer to get the words of the string
		tokens = tokenizeText(itemTitle);

		// find the number of words
		Integer numWords = tokens.length;

		// print info
		/*System.out.println("Words found:" + numWords);
		for (int i = 0; i < tokens.length; i++) {
			System.out.print(i + 1 + " " + tokens[i] + " ");
		}
		System.out.println();*/

		return numWords;
	}
	
	/**
	 * @param symbol String contains the symbol to check
	 * @return Boolean variable whether the text contains the symbol
	 */
	public static Boolean containsSymbol(String symbol) {

		// check if the text contains the given symbol
		
		// print info
		//System.out.println("Symbol: " + symbol + " " + itemTitle.contains(symbol));

		return (itemTitle.contains(symbol));
	}
	
	/**
	 * @param symbol String contains the symbol to check
	 * @return Integer number of timed the symbol be contained in the text
	 */
	public static Integer getNumSymbol(String symbol) {
		
		Integer numSymbols = 0;

		// check every single character of text for the given symbol
		for (int i = 0; i < itemTitle.length(); i++) {
			Character ch = itemTitle.charAt(i);
			if (ch.toString().equals(symbol)) {
				numSymbols++;
			}
		}
		// print info
		//System.out.println("num of " + symbol + ": " + numSymbols);
		return numSymbols;
	}
	
	/**
	 * @param filePath String path of the text file contains the emoticons 
	 * @return Boolean variable of whether text contains emoticons
	 */
	public static Boolean containsEmo(String filePath) {
		
		Boolean containsEmo = false;
		BufferedReader br = null;

		// hashset that contains the emoticons from the txt file
		HashSet<String> emoticons = new HashSet<String>();

		try {
			File fileEmoticons = new File(filePath);
			if (!fileEmoticons.exists()) {
				fileEmoticons.createNewFile();
			}
			String currentLine;
			// create the file reader
			br = new BufferedReader(new FileReader(fileEmoticons));
			// read the txt file and add each line to the hash set
			while ((currentLine = br.readLine()) != null) {
				emoticons.add(currentLine);
			}

			// use the iterator to get elements from the hashset
			// check if text contains each of the elements
			Iterator<String> iterator = emoticons.iterator();
			while (iterator.hasNext()) {
				String emo = iterator.next().toString();
				if (itemTitle.contains(emo)) {
					containsEmo = true;
				}
			}

			br.close();

			// print info
			//System.out.println("Contains emoticon: " + containsEmo);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return containsEmo;

	}

	/**
	 * @return Integer number of uppercase chars contained in text
	 */
	public static Integer getNumUppercaseChars() {
		Integer numUppercaseChars = 0;
		Character ch = null;

		// drop all URLs, hashtags and mentions ("http://", "#anyhashtag", "@anymention", "@anymentionwithspace")- no need to count the uppercase
		// chars on them
		
		String str = itemTitle.replaceAll("http://[^ ]+", "").replaceAll("@ [^ ]+ ", "")
				.replaceAll("@[^ ]+", "").replaceAll("#[^ ]+", "");
		
		// count the uppercase chars
		for (int i = 0; i < str.length(); i++) {
			ch = str.charAt(i);
			if (Character.isUpperCase(ch)) {
				numUppercaseChars++;
			}
		}
		if (itemTitle.contains("RT ") && numUppercaseChars>1){
			numUppercaseChars=numUppercaseChars-2;
		}
		// print info
		//System.out.println("Num of uppercase chars: " + numUppercaseChars);

		return numUppercaseChars;
	}

	/**
	 * @param filePath String path of the text file contains the pronouns 
	 * @return Boolean variable  of whether text contains pronoun
	 */
	public static Boolean containsPronoun(String filePath) {
		
		Boolean containsPron = false;
		BufferedReader br = null;

		// hash set that contains the words from the txt file
		HashSet<String> pronounWords = new HashSet<String>();

		try {
			File Prons = new File(filePath);
			if (!Prons.exists()) {
				Prons.createNewFile();
			}
			String currentLine;
			br = new BufferedReader(new FileReader(Prons));

			// save to hashset every line of the txt file
			while ((currentLine = br.readLine()) != null) {
				pronounWords.add(currentLine);
			}

			for (int j = 0; j < tokens.length; j++) {
				if (pronounWords.contains(tokens[j].toLowerCase())) {
					containsPron = true;
				}
			}

			// print info
			//System.out.println("Contains pronoun: " + containsPron);
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return containsPron;
	}

	/**
	 * @return Integer number of mentions contained in text
	 */
	public static Integer getNumMentions() {
		
		Integer numMentions = 0;

		// check if any token is a mention, so if it starts with @
		for (int i = 0; i < tokens.length; i++) {
			if (tokens[i].startsWith("@")) {
				numMentions++;
			}
		}
		// print info
		//System.out.println("Num of mentions: " + numMentions);
		return numMentions;
	}

	/**
	 * @return Integer number of hashtags contained in text
	 */
	public static Integer getNumHashtags() {
		
		Integer numHashtags = 0;
		
		// check if any token is a hashtag, so if it starts with #
		for (int i = 0; i < tokens.length; i++) {
			if (tokens[i].startsWith("#")) {
				numHashtags++;
			}
		}
		// print info
		//System.out.println("Num of hashtags: " + numHashtags);
		return numHashtags;
	}

	/**
	 * @return Integer number of URLs contained in text
	 */
	public static Integer getNumURLs() {
		
		Integer numURLs = 0;

		// count the urls by checking if text contains "http" string
		if (itemTitle.contains("http://")) {
			numURLs++;
		}
		// print info
		//System.out.println("Num of URLs:" + numURLs);
		return numURLs;
	}
	
	/**
	 * @param filePath String path of the text file contains the sentiment words
	 * @return Integer number of sentiment words contained in text 
	 */
	public static Integer getNumSentiWords(String filePath) {
		
		Integer numSentiWords = 0;
		BufferedReader br = null;

		// use hashset to save the words from the txt file
		HashSet<String> sentiwords = new HashSet<String>();
		try {
			File sentiWords = new File(filePath);
			if (!sentiWords.exists()) {
				sentiWords.createNewFile();
			}
			String currentLine;
			br = new BufferedReader(new FileReader(sentiWords));

			while ((currentLine = br.readLine()) != null) {
				sentiwords.add(currentLine);
			}

			for (int i = 0; i < tokens.length; i++) {
				if (sentiwords.contains(tokens[i].toLowerCase())) {
					numSentiWords++;
					// print info
					//System.out.println("Senti word found:" + tokens[i]);
				}
			}

			// print info
			//System.out.println("Number of senti words: " + numSentiWords);
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return numSentiWords;
	}
	
	/**
	 * @param item current Item  
	 * @return Long number of retweets of this item
	 */
	public static Long getRetweetsCount(Item item) {
		
		Long numRetweets;
		numRetweets = item.getShares();
		
		return numRetweets;
	}
	
	/**
	 * @param item current MediaItem
	 * @return Long number of retweets of this MediaItem
	 */
	public static Long getRetweetsCount(MediaItem item) {
		
		Long numRetweets;
		numRetweets = item.getShares();
		
		//print info
		//System.out.println("Number of retweets: "+numRetweets);
		return numRetweets;
	}
	
	/**
	 * Function that given a string, a pattern and an initial index, finds the indexes where the pattern is observed.
	 * The method is recursive, given as initial index for searching, the last index of the previous iteration.
	 * @param str String that holds the adapted text of the original tweet text
	 * @param pattern String that holds the pattern whose existence is checked
	 * @param index the start index from where the pattern is searched
	 * @return ArrayList<Integer[]> list of the pairs [startIndex, endIndex] of the patern occurrences found
	 */
	public static ArrayList<Integer[]> findPatternOccurrence(String str,
			String pattern, int index) {

		Integer occurStart = str.indexOf("http", index);
		//System.out.println("start " + occurStart);
		//System.out.println("string length "+str.length());
		Integer[] occurs = new Integer[2];
		Integer occurEnd = -1;
		if (occurStart != -1) {
			occurs[0] = occurStart;
			occurEnd = str.indexOf(" ", occurStart);
			
			if (str.substring(occurStart, occurEnd).contains("\n")){
				occurEnd = str.indexOf("\n", occurStart);
				
			}
			//System.out.println("end " + occurEnd);	
		}
		
		if (occurEnd.equals(-1)) {
			occurs[1] = str.length();
			//System.out.println("end again "+occurs[1]);
		} else {
			occurs[1] = occurEnd;
		}
		//System.out.println("string "+itemTitle.substring(occurStart,occurs[1]));
		
		if ( occurs[0] != null && occurs[1] != null && ( (occurs[1]-occurs[0]) >7) ){
			//System.out.println("occurs before added "+occurs[0]+" "+occurs[1]);			
			occurrenceArr.add(occurs);
		}
		//System.out.println(occurEnd+" "+occurStart);
		if (occurEnd < str.length()  && !occurStart.equals(-1) && !occurEnd.equals(-1)) {
			//System.out.println("hello");
			findPatternOccurrence(str, pattern, occurEnd + 1);
			
		}

		return occurrenceArr;
	}
	
	
	/**
	 * Function that finds in the tweet text the external links provided (if any) except for the links of the media content provided.
	 * The steps followed:
	 * 1. Erase all the useless characters from the text.
	 * 2. Call the findPatternOccurrence(text, "http://", 0) to spot the pairs of indexes [startIndex, endIndex] 
	 * 	  of the links that exist in the text (see findPatternOccurrence method for more).
	 * 3. For each link found, check if it is the same with the media link or not.
	 * 4. Keep as external links, all the links except for the media links provided. 
	 * @param item
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static HashSet<String> checkForExternalLinks(MediaItem item) throws MalformedURLException, IOException {

		String text = TextProcessing.getInstance().eraseCharacters(itemTitle);
		occurrenceArr = findPatternOccurrence(text, "http://", 0);
		
		//System.out.println("size before clear "+occurrenceArr.size());
		String mediaLink = item.getUrl();
		
		HashSet<String> extLinks = new HashSet<String>();
		
		for (Integer[] array : occurrenceArr){
			//System.out.println(text);
			//System.out.println(array[0]+" "+array[1]);
			String substring = text.substring(array[0],array[1]);
			//System.out.println("substring: "+substring+" mediaLink: "+mediaLink);
			
			if (StringProcessing.getInstance().isAppropriate(mediaLink)){ 
				mediaLink = WebOfTrustManager.handleInstagram(mediaLink);
			}
			
			if (substring.endsWith(".") || substring.endsWith(":") || substring.endsWith("//")){
				substring = substring.substring(0, substring.length()-1);
			}
			
			String longUrl = WebOfTrustManager.expandUrl(substring);
			String longUrl2 = WebOfTrustManager.expandUrl(longUrl);
			
			if (StringProcessing.getInstance().isAppropriate(longUrl2)){
				longUrl = longUrl2;
			}
			if (!StringProcessing.getInstance().isAppropriate(longUrl)){
				longUrl = substring;
			}
	
			if (substring.equals(longUrl)){
				//System.out.println("Substring equals long url!");
				//longUrl = expandUrl(longUrl);
			}
			
			longUrl = WebOfTrustManager.handleInstagram(longUrl);
			//boolean longequalspage = longUrl.replaceAll("https", "http").equals(mediaLink.replaceAll("https", "http"));
			if ( StringProcessing.getInstance().isAppropriate(longUrl) && !longUrl.equals(mediaLink) && !URLProcessing.getInstance().isTwitterPage(longUrl) &&
					!URLProcessing.getInstance().isUncompleted(longUrl)){								
				extLinks.add(longUrl);
			}
			
		}
		
		occurrenceArr.clear();
		//info
		//System.out.println("External links found: "+extLinks);
		
		return extLinks;
	}
	
	/**
	 * Returns the language of the given string
	 * @param str
	 * @return
	 */
	public static String getItemLang(String str){
		String language = null;
		if (!str.trim().isEmpty()){
			UberLanguageDetector detector = UberLanguageDetector.getInstance();
			language = detector.detectLang(str);
		}
		if (language==null){
			language = "nolang";
		}
		return language;
	}
	
	/**
	 * Function that organizes the feature extraction of the given list of MediaItems
	 * @param listMediaItems the list of MediaItems for which features are extracted
	 * @return ItemFeatures list with the item features computed
	 * @throws IOException 
	 * @throws MalformedURLException 
	 */
	public static List<ItemFeatures> featureExtractionMedia(List<MediaItem> listMediaItems) throws MalformedURLException, IOException {
		
		Long time1 = null;
		Long time2 = null, sum = (long) 0;
		
		Integer count = 0, count2 = 0, count3 = 0, count4=0, count5=0, count6=0;
		List<ItemFeatures> itemFeaturesList = new ArrayList<ItemFeatures>();
		
		// create ItemFeatures object
		ItemFeatures feat2 = new ItemFeatures();
		
		// extract features for each item 
		for (int i = 0; i < listMediaItems.size(); i++) {
			
		
			count++;
			MediaItem item = listMediaItems.get(i);
			String id_temp = item.getId();
			//System.out.println(id_temp);
			if (id_temp != null ) {
				itemTitle = "";
				itemTitle = item.getTitle().toString();
				//System.out.println("before " + itemTitle);

				String str = itemTitle.replaceAll("http+s*+://[^ ]+", "")
						.replaceAll("@[^ ]+", "").replaceAll("#[^ ]+ ", "")
						.replaceAll("RT", "").toLowerCase().trim();

				//System.out.println("after http removal " + str);

				String lang = TextProcessing.getInstance().getLanguage(str);
				
				HashSet<String> supportedLangs = Vars.SUPPORTED_LANGS; 	
				count2++;
				
				//check if item's lang is one of the supported ones
				//if (supportedLangs.contains(lang)) {
					try {
						//if no language was detected 
						if (lang.equals("nolang") && !supportedLangs.contains(lang)) {
							lang = "en";
							feat2 = extractFeatures(item, lang);
							count6++;
						}
						
						else {
							feat2 = extractFeatures(item, lang);
							//info 
							if (lang.equals("en")) count3++;
							else if (lang.equals("es")) count4++;
							else if (lang.equals("de")) count5++;
						}
						
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
			itemFeaturesList.add(feat2);
		}
		// print info
		//System.out.println();
		/*System.out.println("Total items: " + count);
		//System.out.println("Items checked for language " + count2);
		System.out.println("Items in English " + count3);
		System.out.println("Items in Spanish " + count4);
		System.out.println("Items in German " + count5);
		System.out.println("Items with no language "+ count6);
		
		System.out.println("Total time:" + (double) sum / 1000 + " secs");
		System.out.println("Time per tweet:" + (double) sum / count3 + " ms");*/

		return itemFeaturesList;
	}
	
	/**
	 * Function that organizes the feature extraction of the given MediaItem
	 * @param item the MediaItem for which features are extracted
	 * @return ItemFeatures computed for the MediaItem
	 * @throws IOException 
	 * @throws MalformedURLException 
	 */
	public static ItemFeatures featureExtractionMedia(MediaItem item) throws MalformedURLException, IOException {
		
		Long time1 = null;
		Long time2 = null, sum = (long) 0;
		
		Integer count = 0, count2 = 0, count3 = 0, count4=0, count5=0, count6=0;
		//List<ItemFeatures> itemFeaturesList = new ArrayList<ItemFeatures>();
		
		// create ItemFeatures object
		ItemFeatures feat2 = new ItemFeatures();
		
		// extract features for each item 
		
		String id_temp = item.getId();
		System.out.println(id_temp);
		if (id_temp != null ) {
			itemTitle = "";
			itemTitle = item.getTitle().toString();
			//System.out.println("before " + itemTitle);

			String str = itemTitle.replaceAll("http+s*+://[^ ]+", "")
					.replaceAll("@[^ ]+", "").replaceAll("#[^ ]+ ", "")
					.replaceAll("RT", "").toLowerCase().trim();

			//System.out.println("after http removal " + str);

			String lang = TextProcessing.getInstance().getLanguage(str);
			
			HashSet<String> supportedLangs = Vars.SUPPORTED_LANGS;
			count2++;
			
			//check if item's lang is one of the supported ones
			//if (supportedLangs.contains(lang)) {
				try {
					//if no language was detected 
					if (lang.equals("nolang") && !supportedLangs.contains(lang)) {
						lang = "en";
						feat2 = extractFeatures(item, lang);
						count6++;
					}
					
					else {
						feat2 = extractFeatures(item, lang);
						//info 
						if (lang.equals("en")) count3++;
						else if (lang.equals("es")) count4++;
						else if (lang.equals("de")) count5++;
					}
					
				}catch(Exception e) {
					e.printStackTrace();
				}			
		}
		// print info
		//System.out.println();
		/*System.out.println("Total items: " + count);
		//System.out.println("Items checked for language " + count2);
		System.out.println("Items in English " + count3);
		System.out.println("Items in Spanish " + count4);
		System.out.println("Items in German " + count5);
		System.out.println("Items with no language "+ count6);
		
		System.out.println("Total time:" + (double) sum / 1000 + " secs");
		System.out.println("Time per tweet:" + (double) sum / count3 + " ms");*/
			
		return feat2;
	}

    static Gson gson = new GsonBuilder()
    .excludeFieldsWithoutExposeAnnotation()
    .create();
    
    /**
     * @param json String that keeps the object need to be deserialized 
     * @return ItemFeatures object, after deserialization into an object of the specified class
     */
    public static synchronized ItemFeatures create(String json) {
        synchronized (gson) {
        	ItemFeatures item = gson.fromJson(json, ItemFeatures.class);
            return item;
        }
    }
	
}
 
