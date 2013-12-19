package gr.iti.mklab.verify;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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


/**
 * Class that extracts ItemFeatures of an Item
 * @author boididou
 */
public class ItemFeaturesExtractor {
	
	static String[] tokens;
	static String itemTitle;
	public static Instances isTrainingSet;
	
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
	 */
	public static ItemFeatures extractFeatures(MediaItem item,String lang) {

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

		// if (str.equals("\\s+")) str="";

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

		Boolean checkSymbol = false;

		// check if the text contains the given symbol
		if (itemTitle.contains(symbol)) {
			checkSymbol = true;
		}

		// print info
		//System.out.println("Symbol: " + symbol + " " + checkSymbol);

		return checkSymbol;
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
		
		return numRetweets;
	}

	/**
	 * Checks Item text for english language
	 * @return Boolean variable of whether the item language is english
	 */
	public static Boolean isEnglishLang() {
		
		Boolean isEnglish = false;
		
		// drop all URLs, hashtags and mentions - no need to detect the language
		// on them
		String str = itemTitle.replaceAll("http+s*+://[^ ]+", "")
				.replaceAll("@[^ ]+", "").replaceAll("#[^ ]+ ", "");

		// language detection only if the str is not empty in order to avoid exceptions
		if (!str.trim().isEmpty()){
			UberLanguageDetector detector = UberLanguageDetector.getInstance();
			//System.out.println("string "+str);
			String language = detector.detectLang(str);
			//System.out.println("language "+detector.scoreLanguages(str));
			if (language!=null && language.equals("en")) {
				isEnglish = true;
			}
			
		}
		
		return isEnglish;
	}
	
	/**
	 * Checks Item text for spanish language
	 * @return Boolean variable of whether the item language is spanish
	 */
	public static Boolean isSpanishLang() {
		
		Boolean isSpanish = false;
		String str = itemTitle.replaceAll("http+s*+://[^ ]+", "")
				.replaceAll("@[^ ]+", "").replaceAll("#[^ ]+ ", "");
		
		if (!str.trim().isEmpty()){
			UberLanguageDetector detector = UberLanguageDetector.getInstance();
			//System.out.println("string "+str);
			String language = detector.detectLang(str);
			//System.out.println("language "+detector.scoreLanguages(str));
			
			if (language!=null && language.equals("es")) {
				isSpanish = true;
			}
			
		}
		return isSpanish;
	}
	
	/**
	 * Checks Item text for german language
	 * @return Boolean variable of whether the item language is german
	 */
	public static Boolean isGermanLang() {
		
		Boolean isGerman = false;
		
		String str = itemTitle.replaceAll("http+s*+://[^ ]+", "")
				.replaceAll("@[^ ]+", "").replaceAll("#[^ ]+ ", "");
		
		if (!str.trim().isEmpty()){
			UberLanguageDetector detector = UberLanguageDetector.getInstance();
			//System.out.println("string "+str);
			String language = detector.detectLang(str);
			//System.out.println("language "+detector.scoreLanguages(str));
			
			if (language!=null && language.equals("de")) {
				isGerman = true;
			}
			
		}
		return isGerman;
	}
	
	
	/**
	 * Function that organizes the feature extraction of the given list of MediaItems
	 * @param listMediaItems the list of MediaItems for which features are extracted
	 * @return ItemFeatures list with the item features computed
	 */
	public static List<ItemFeatures> featureExtractionMedia(List<MediaItem> listMediaItems) {
		
		Long time1 = null;
		Long time2 = null, sum = (long) 0;
		Boolean isEnglish,isSpanish,isGerman;
		Integer count = 0, count2 = 0, count3 = 0, count4=0, count5=0;
		List<ItemFeatures> itemFeaturesList = new ArrayList<ItemFeatures>();
		
		// create ItemFeatures object
		ItemFeatures feat2 = new ItemFeatures();
		
		// extract features for each item 
		for (int i = 0; i < listMediaItems.size(); i++) {
			count++;
			if (listMediaItems.get(i).getId() != null) {
				itemTitle = "";
				itemTitle = listMediaItems.get(i).getTitle().toString();
				count2++;
				isEnglish = isEnglishLang();
				isSpanish = isSpanishLang();
				isGerman = isGermanLang();
				
				if (isEnglish) {
					count3++;
					time1 = System.currentTimeMillis();
					feat2 = extractFeatures(listMediaItems.get(i),"en");
					time2 = System.currentTimeMillis();

					// save all the ItemFeatures items in a list
					itemFeaturesList.add(feat2);

					sum = sum + (time2 - time1);
				}
				else if (isSpanish){
					count4++;
					feat2 = extractFeatures(listMediaItems.get(i),"es");
					itemFeaturesList.add(feat2);
					
				}
				else if (isGerman){
					count5++;
					feat2 = extractFeatures(listMediaItems.get(i),"de");
					itemFeaturesList.add(feat2);
					
				}
				
			}
		}
		// print info
		//System.out.println();
		System.out.println("Total items: " + count);
		System.out.println("Items checked for language " + count2);
		System.out.println("Items in English " + count3);
		System.out.println("Items in Spanish " + count4);
		System.out.println("Items in German " + count5);
		
		System.out.println("Total time:" + (double) sum / 1000 + " secs");
		System.out.println("Time per tweet:" + (double) sum / count3 + " ms");

		return itemFeaturesList;
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
