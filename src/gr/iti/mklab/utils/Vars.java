package gr.iti.mklab.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Vars {
	
	
	//Class ItemFeaturesExtractor paths
	public static final String HAPPY_EMO_PATH = "resources/files/happy-emoticons.txt";
	public static final String SAD_EMO_PATH="resources/files/sad-emoticons.txt";
	
	public static final String FIRST_PRON_PATH = "resources/files/first-order-prons.txt";
	public static final String SECOND_PRON_PATH = "resources/files/second-order-prons.txt";
	public static final String THIRD_PRON_PATH = "resources/files/third-order-prons.txt";
	
	public static final String FIRST_PRON_ES_PATH = "resources/files/first-order-prons-spanish.txt";
	public static final String SECOND_PRON_ES_PATH = "resources/files/second-order-prons-spanish.txt";
	public static final String THIRD_PRON_ES_PATH = "resources/files/third-order-prons-spanish.txt";
	
	public static final String FIRST_PRON_DE_PATH = "resources/files/first-order-prons-german.txt";
	public static final String SECOND_PRON_DE_PATH = "resources/files/second-order-prons-german.txt";
	public static final String THIRD_PRON_DE_PATH = "resources/files/third-order-prons-german.txt";
	
	public static final String SLANG_ENG_PATH = "resources/files/slangwords-english.txt";
	public static final String SLANG_ES_PATH = "resources/files/slangwords-spanish.txt";
	
	public static final String POS_WORDS_ENG_PATH = "resources/files/positive-words.txt";
	public static final String POS_WORDS_ES_PATH = "resources/files/positive-words-spanish.txt";
	public static final String POS_WORDS_DE_PATH = "resources/files/positive-words-german.txt";
	public static final String NEG_WORDS_ENG_PATH = "resources/files/negative-words.txt";
	public static final String NEG_WORDS_ES_PATH = "resources/files/negative-words-spanish.txt";
	public static final String NEG_WORDS_DE_PATH = "resources/files/negative-words-german.txt";
		
		
	//Classification
	//Previous model files that we used for our training and testing experiments with initial item and user features included
	public static final String MODEL_PATH_ITEM = "resources/model/j48updated.model";
	public static final String MODEL_PATH_USER= "resources/model/j48user.model";
	public static final String MODEL_PATH_TOTAL= "resources/model/j48total.model";
	
	//Updated files that we used for our experiments with new item and user features included
	public static final String MODEL_PATH_ITEM_sample = "resources/model/j48-item.model";
	public static final String MODEL_PATH_USER_sample = "resources/model/j48-user.model";
	public static final String MODEL_PATH_TOTAL_sample = "";
	
	//supported langs
	public static final HashSet<String> SUPPORTED_LANGS = new HashSet<String>(Arrays.asList("en","es","nolang"));	
		
	//lang models
	public static final String MODEL_PARSER = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";

	//hashset stanford parser labels 
	public static final String[] set_labels = {"NN","NNS","NNP","NNPS"};
	public static final Set<String> LABELS = new HashSet<String>(Arrays.asList(set_labels));
	public static final String[] set_stopwords = {"RT"};
	public static final Set<String> STOP_WORDS = new HashSet<String>(Arrays.asList(set_stopwords));


}
