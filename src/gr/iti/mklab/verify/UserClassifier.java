package gr.iti.mklab.verify;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import weka.classifiers.misc.SerializedClassifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Class to organize the Item classification using User features by defining the attributes,
 * creating the testing set and perform the classification.
 * @author boididou
 */
public class UserClassifier {
	
	//define the itemfeatures lists for separate datasets
	public static List<UserFeatures> train = new ArrayList<UserFeatures>(); 
	public static List<UserFeatures> test = new ArrayList<UserFeatures>();
	
	static ArrayList<Attribute> fvAttributes = new ArrayList<Attribute>();
	public static Instances isTrainingSet,isTestingSet;
	
	public static ArrayList<Attribute> getFvAttributes(){
		return fvAttributes;
	}
	
	/**
	 * @return List of attributes needed for the classification
	 */
	public static List<Attribute> declareAttributes(){
		
		//numeric attributes
		Attribute numFriends		= new Attribute("numFriends");
		Attribute numFollowers		= new Attribute("numFollowers");
		Attribute FolFrieRatio		= new Attribute("FolFrieRatio");
		Attribute timesListed		= new Attribute("timesListed");
		Attribute numTweets			= new Attribute("numTweets");
		//declare nominal attributes
		List<String> fvnominal1 = new ArrayList<String>(2);
		fvnominal1.add("true");
		fvnominal1.add("false");
		Attribute hasUrl = new Attribute("hasUrl",fvnominal1);
		
		//declare nominal attributes
		List<String> fvnominal2 = new ArrayList<String>(2);
		fvnominal2.add("true");
		fvnominal2.add("false");
		Attribute isVerified = new Attribute("isVerified",fvnominal2);
		
		List<String> fvClass = new ArrayList<String>(2);
		fvClass.add("real");
		fvClass.add("fake");
		Attribute ClassAttribute = new Attribute("theClass",fvClass);
		
		fvAttributes.add(numFriends);
		fvAttributes.add(numFollowers);
		fvAttributes.add(FolFrieRatio);
		fvAttributes.add(timesListed);
		fvAttributes.add(hasUrl);
		fvAttributes.add(isVerified);
		fvAttributes.add(numTweets);
		fvAttributes.add(ClassAttribute);
		
		return fvAttributes;
	}
	
	/**
	 * @param listUserFeatures the features of the current User
	 * @return Instance created
	 */
	public static Instance createInstance(UserFeatures listUserFeatures){
		
		Instance inst = new DenseInstance(fvAttributes.size());
		
		inst.setValue((Attribute)fvAttributes.get(0), listUserFeatures.getnumFriends());  
		inst.setValue((Attribute)fvAttributes.get(1), listUserFeatures.getnumFollowers());  
		inst.setValue((Attribute)fvAttributes.get(2), listUserFeatures.getFolFrieRatio());  
		inst.setValue((Attribute)fvAttributes.get(3), listUserFeatures.gettimesListed());  
		inst.setValue((Attribute)fvAttributes.get(4), String.valueOf(listUserFeatures.gethasURL()));  
		inst.setValue((Attribute)fvAttributes.get(5), String.valueOf(listUserFeatures.getisVerified())); 
		inst.setValue((Attribute)fvAttributes.get(6), listUserFeatures.getnumTweets());

		return inst;
	}
		
	/**
	 * @param list of the UserFeatures computed
	 * @param itemFeaturesAnnot list of the users' annotation details
	 * @return Instances that form the testing set
	 * @throws Exception
	 */
	public static Instances createTestingSet(List<UserFeatures> listUserFeatures, List<UserFeaturesAnnotation> listFeaturesAnnot) throws Exception{
		
		int index=0;
		//create an empty training set and then keep the instances 
		Instances isTestingSet = new Instances("Rel", fvAttributes, listUserFeatures.size());        
		// Set class index
		isTestingSet.setClassIndex(fvAttributes.size()-1);
		
		for (int i=0;i<listUserFeatures.size();i++){
			
			//declare instance and define its values
			Instance inst  = createInstance(listUserFeatures.get(i));
			
			for (int j=0;j<listFeaturesAnnot.size();j++){
				if (listUserFeatures.get(i).getUsername().equals(listFeaturesAnnot.get(j).getUsername())){
					index = j;
				}
			}	
			
			inst.setValue((Attribute)fvAttributes.get(fvAttributes.size()-1), listFeaturesAnnot.get(index).getReliability());
			//add the instance to the testing set
			isTestingSet.add(inst);
		}

		return isTestingSet;
	}
	
	/**
	 * @param listUserFeatures the features for the MediaItem
	 * @param listFeaturesAnnot the User's annotation details
	 * @return Instances that form the testing set
	 * @throws Exception
	 */
	public static Instances createTestingSet(UserFeatures listUserFeatures, UserFeaturesAnnotation listFeaturesAnnot) throws Exception{
		
		//create an empty training set and then keep the instances 
		Instances isTestingSet = new Instances("Rel", fvAttributes, 1);        
		// Set class index
		isTestingSet.setClassIndex(fvAttributes.size()-1);

		//declare instance and define its values
		Instance inst  = createInstance(listUserFeatures);
		inst.setValue((Attribute)fvAttributes.get(fvAttributes.size()-1), listFeaturesAnnot.getReliability());
		
		//add the instance to the testing set
		isTestingSet.add(inst);
		
		return isTestingSet;
	}
	
	/**
	 * @param isTestSet Instances of the test set
	 * @return Boolean table of reliability values of the test set instances
	 * @throws Exception
	 */
	public static boolean[] classifyItems(Instances isTestSet) throws Exception{
		
		
		boolean[] flags = new boolean[isTestSet.size()];
		SerializedClassifier classifier = new SerializedClassifier();
		classifier.setModelFile(new File(Vars.MODEL_PATH_USER));

		for (int i = 0; i < isTestSet.numInstances(); i++) {
			
			double pred = classifier.classifyInstance(isTestSet.instance(i));
			
			String predicted = isTestSet.classAttribute().value((int) pred);

			if (predicted=="fake"){
				flags[i]=true;
			}
			else{
				flags[i]=false;
			}

		}

		return flags;
	}
	
	/**
	 * @param isTestSet the current Instances of the dataset
	 * @return double[] distribution probabilities
	 * @throws Exception file
	 */
	public static double[] findProbDistribution(Instances isTestSet) throws Exception{
		
		//probabilities variable
		double[] probabilities = new double[isTestSet.size()];
		SerializedClassifier classifier = new SerializedClassifier();
		classifier.setModelFile(new File(Vars.MODEL_PATH_USER));
		
		for (int i = 0; i < isTestSet.numInstances(); i++) {
			double[] probabilityDistribution = classifier.distributionForInstance(isTestSet.instance(i));
			probabilities[i] = probabilityDistribution[1];
		}
		
		return probabilities;
	}
}
