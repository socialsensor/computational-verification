package gr.iti.mklab.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Vars {
	//general variables
	public static final String LOCALHOST_IP = "160.40.50.242";
	
	//path of images' urls extracted 
	public static final String URL_IMAGES_PATH = "../TweetFeatureExtraction/resources/url_files/BringBack/fake_prot.txt";
		
	//Class ItemFeaturesExtractor paths
	public static final String HAPPY_EMO_PATH = "C:/Users/boididou/workspace/TweetFeatureExtraction/resources/files/happy-emoticons.txt";
	public static final String SAD_EMO_PATH="/Users/boididou/workspace/TweetFeatureExtraction/resources/files/sad-emoticons.txt";
	
	public static final String FIRST_PRON_PATH = "/Users/boididou/workspace/TweetFeatureExtraction/resources/files/first-order-prons.txt";
	public static final String SECOND_PRON_PATH = "/Users/boididou/workspace/TweetFeatureExtraction/resources/files/second-order-prons.txt";
	public static final String THIRD_PRON_PATH = "/Users/boididou/workspace/TweetFeatureExtraction/resources/files/third-order-prons.txt";
	
	public static final String FIRST_PRON_ES_PATH = "/Users/boididou/workspace/TweetFeatureExtraction/resources/files/first-order-prons-spanish.txt";
	public static final String SECOND_PRON_ES_PATH = "/Users/boididou/workspace/TweetFeatureExtraction/resources/files/second-order-prons-spanish.txt";
	public static final String THIRD_PRON_ES_PATH = "/Users/boididou/workspace/TweetFeatureExtraction/resources/files/third-order-prons-spanish.txt";
	
	public static final String FIRST_PRON_DE_PATH = "/Users/boididou/workspace/TweetFeatureExtraction/resources/files/first-order-prons-german.txt";
	public static final String SECOND_PRON_DE_PATH = "/Users/boididou/workspace/TweetFeatureExtraction/resources/files/second-order-prons-german.txt";
	public static final String THIRD_PRON_DE_PATH = "/Users/boididou/workspace/TweetFeatureExtraction/resources/files/third-order-prons-german.txt";
	
	public static final String SLANG_ENG_PATH = "/Users/boididou/workspace/TweetFeatureExtraction/resources/files/slangwords-english.txt";
	public static final String SLANG_ES_PATH = "/Users/boididou/workspace/TweetFeatureExtraction/resources/files/slangwords-spanish.txt";
	
	public static final String POS_WORDS_ENG_PATH = "/Users/boididou/workspace/TweetFeatureExtraction/resources/files/positive-words.txt";
	public static final String POS_WORDS_ES_PATH = "/Users/boididou/workspace/TweetFeatureExtraction/resources/files/positive-words-spanish.txt";
	public static final String POS_WORDS_DE_PATH = "/Users/boididou/workspace/TweetFeatureExtraction/resources/files/positive-words-german.txt";
	public static final String NEG_WORDS_ENG_PATH = "/Users/boididou/workspace/TweetFeatureExtraction/resources/files/negative-words.txt";
	public static final String NEG_WORDS_ES_PATH = "/Users/boididou/workspace/TweetFeatureExtraction/resources/files/negative-words-spanish.txt";
	public static final String NEG_WORDS_DE_PATH = "/Users/boididou/workspace/TweetFeatureExtraction/resources/files/negative-words-german.txt";
	
	//Where to save 
	public static final String DB_NAME_ITEM_EXTRACTION = "FeaturesObjects2";
	public static final String COLL_NAME_ITEM_EXTRACTION = "ItemFeatReals_Bostonunique2";
	
	public static final String DB_NAME_USER_EXTRACTION = "JetBlue";
	public static final String COLL_NAME_USER_EXTRACTION = "UserFeatFakes2";
	
	
	
	
		
	//Classification
	//model files that we used for our training and testing experiments
	public static final String MODEL_PATH_ITEM = "resources/model/j48updated.model";
	public static final String MODEL_PATH_TOTAL= "resources/model/j48total.model";
	public static final String MODEL_PATH_USER= "resources/model/j48user.model";
	
	//model files for additional experiment
	public static final String MODEL_PATH_ITEM_sample = "resources/model/j48-item.model";
	public static final String MODEL_PATH_USER_sample = "resources/model/j48-user.model";
	public static final String MODEL_PATH_TOTAL_sample = "";
	//supported langs
	public static final HashSet<String> SUPPORTED_LANGS = new HashSet<String>(Arrays.asList("en","es","nolang"));	
	
	//MODELS
	//Classification models
	public static final String MODEL_PATH = "/Users/boididou/workspace/TweetFeatureExtraction/resources/models/sandy/pure_dataset/random-25-original.model";
	
	public static final String MODEL_PATH2 = "/Users/boididou/workspace/TweetFeatureExtraction/resources/models/sandy/pure_dataset/j48-item.model";
	public static final String MODEL_PATH3 = "/Users/boididou/workspace/TweetFeatureExtraction/resources/models/sandy/pure_dataset/j48-user.model";
	public static final String MODEL_PATH4 = "/Users/boididou/workspace/TweetFeatureExtraction/resources/models/sandy/pure_dataset/bayes.model";
	
	public static final String MODEL_PATH_total = "/Users/boididou/workspace/TweetFeatureExtraction/resources/models/sandy/pure_dataset/J48/Atts no.34 - greed/j48-test.model";
	public static final String MODEL_PATH_total2 = "/Users/boididou/workspace/TweetFeatureExtraction/resources/models/sandy/pure_dataset/RandomForest/Atts no.35 - greed/random-35-greedy-9.model";
	public static final String MODEL_PATH_total3 = "/Users/boididou/workspace/TweetFeatureExtraction/resources/models/sandy/pure_dataset/kstar-35-greedy-.model";
	
	//lang models
	public static final String MODEL_PARSER = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";
	
	//values
	//fake
	//public static final String[] SET_VALUES_TESTING_FAKE = new String[] { "0","1","2","3","4","5","6","12","13","15","18","20","22","23","24","25","26","30","31","32","34" };
	//public static final String[] SET_VALUES_TRAINING_FAKE = new String[] {"7","8","9","10","11","14","16","17","19","21","27","28","29","33","35","36","37","38","39","40","41","42","43","44","45","46","47"};
	public static final String[] SET_VALUES_TESTING_FAKE = new String[] { "27","28","0","1","4","5","6","12","7","8","9","10","11","14","16","17","19","21","40","41","45","46" };
	public static final String[] SET_VALUES_TRAINING_FAKE = new String[] {"29","33","35","36","37","38","39","40","41","42","43","44","47","20","13","15","18","22","23","24","25","2","3","26","30","31","32","34"};
	public static final Set<String> TRAINING_SET_FAKE = new HashSet<String>(Arrays.asList(SET_VALUES_TRAINING_FAKE));
	public static final Set<String> TESTING_SET_FAKE = new HashSet<String>(Arrays.asList(SET_VALUES_TESTING_FAKE));
	//real
	//public static final String[] SET_VALUES_TRAINING_REAL = new String[] {"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50"};
	//public static final String[] SET_VALUES_TESTING_REAL = new String[] {"51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78"};
	public static final String[] SET_VALUES_TRAINING_REAL = new String[] {"21","22","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","60","61","62","67","68","69","70","71","72","73","74","75","76","77","78"};
	public static final String[] SET_VALUES_TESTING_REAL = new String[] {"51","52","53","54","55","56","57","58","59","63","64","65","66","0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","23","24","25"};
	public static final Set<String> TRAINING_SET_REAL = new HashSet<String>(Arrays.asList(SET_VALUES_TRAINING_REAL));
	public static final Set<String> TESTING_SET_REAL = new HashSet<String>(Arrays.asList(SET_VALUES_TESTING_REAL));
	
	//fake
	public static final String[] SET_VALUES_TESTING_FAKEb = new String[] { "0","1","2","3","4","5","6","12","13","15","18","20","22","23","24","25","26","30","31","32","34" };
	public static final String[] SET_VALUES_TRAINING_FAKEb = new String[] {"7","8","9","10","11","14","16","17","19","21","27","28","29","33","35","36","37","38","39","40","41","42","43","44","45","46","47"};
	public static final Set<String> TRAINING_SET_FAKEb = new HashSet<String>(Arrays.asList(SET_VALUES_TRAINING_FAKEb));
	public static final Set<String> TESTING_SET_FAKEb = new HashSet<String>(Arrays.asList(SET_VALUES_TESTING_FAKEb));
	//real
	public static final String[] SET_VALUES_TRAINING_REALb = new String[] {"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50"};
	public static final String[] SET_VALUES_TESTING_REALb = new String[] {"51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73","74","75","76","77","78"};
	public static final Set<String> TRAINING_SET_REALb = new HashSet<String>(Arrays.asList(SET_VALUES_TRAINING_REALb));
	public static final Set<String> TESTING_SET_REALb = new HashSet<String>(Arrays.asList(SET_VALUES_TESTING_REALb));
	//hashset
	public static final String URL_HASHMAP_FAKE = "hashmap.txt";
	public static final String URL_HASHMAP_REAL = "hashmapreal.txt";
	public static final String FAKE_WORDS_PATH = "/Users/boididou/workspace/TweetFeatureExtraction/frequencymanager/fakes/mapfake.txt";
	
	//hashset stanford parser labels 
	public static final String[] set_labels = {"NN","NNS","NNP","NNPS"};
	public static final Set<String> LABELS = new HashSet<String>(Arrays.asList(set_labels));
	public static final String[] set_stopwords = {"RT"};
	public static final Set<String> STOP_WORDS = new HashSet<String>(Arrays.asList(set_stopwords));


}
