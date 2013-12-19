package gr.iti.mklab.verify;

/**
 * Auxiliary class that keeps the values of the variables
 * @author boididou
 */
public class Vars {
	
	//general variables
	public static final String LOCALHOST_IP = "160.40.50.242";
	
	//Class ItemFeaturesExtractor paths
	public static final String HAPPY_EMO_PATH = "resources/files/Happyemoticons.txt";
	public static final String SAD_EMO_PATH="resources/files/Sademoticons.txt";
	public static final String FIRST_PRON_PATH = "resources/files/FirstOrderProns.txt";
	public static final String SECOND_PRON_PATH = "resources/files/SecondOrderProns.txt";
	public static final String THIRD_PRON_PATH = "resources/files/ThirdOrderProns.txt";
	
	public static final String POS_WORDS_ENG_PATH = "resources/files/positive-words.txt";
	public static final String POS_WORDS_ES_PATH = "resources/files/positive-words-spanish.txt";
	public static final String POS_WORDS_DE_PATH = "resources/files/positive-words-german.txt";
	public static final String NEG_WORDS_ENG_PATH = "resources/files/negative-words.txt";
	public static final String NEG_WORDS_ES_PATH = "resources/files/negative-words-spanish.txt";
	public static final String NEG_WORDS_DE_PATH = "resources/files/negative-words-german.txt";

	//Classification
	public static final String MODEL_PATH_ITEM = "resources/model/j48.model";
	public static final String MODEL_PATH_TOTAL= "resources/model/j48total.model";
	public static final String MODEL_PATH_USER= "resources/model/j48user.model";
	
}
