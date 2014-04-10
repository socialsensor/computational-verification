package gr.iti.mklab.verify;
import eu.socialsensor.framework.common.domain.MediaItem;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.output.prediction.PlainText;
import weka.classifiers.lazy.KStar;
import weka.classifiers.misc.SerializedClassifier;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.Debug;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Class to organize the Item classification using User features by defining the attributes,
 * creating the testing set and perform the classification.
 * @author boididou
 */
public class UserClassifier {
	
	static ArrayList<Attribute> fvAttributes = new ArrayList<Attribute>();
	
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
		
		int count = 0;
		boolean[] flags = new boolean[isTestSet.size()];
		SerializedClassifier classifier = new SerializedClassifier();
		classifier.setModelFile(new File(Vars.MODEL_PATH_USER_sample));

		for (int i = 0; i < isTestSet.numInstances(); i++) {
			
			double pred = classifier.classifyInstance(isTestSet.instance(i));
			
			String actual = isTestSet.classAttribute().value((int)isTestSet.instance(i).classValue());
			String predicted = isTestSet.classAttribute().value((int) pred);

			if (actual.equals(predicted)){
				count++;
			}
			
			if (predicted=="fake"){
				flags[i]=true;
			}
			else{
				flags[i]=false;
			}

		}
		
		/*System.out.println();
		System.out.println("=== Results ===");
		System.out.println("Total items "+isTestSet.numInstances());
		System.out.println("Items classified correctly:"+count);
		System.out.println("Percentage "+((double)count/isTestSet.numInstances())*100);
		System.out.println();*/
		return flags;
	}
	
	/**
	 * Function that creates the training set given the features calculated before
	 * @param listUserFeatures the list of User features 
	 * @param userFeaturesAnnot the Items' annotation details
	 * @return the Instances formed 
	 */
	public static Instances createTrainingSet(List<UserFeatures> listUserFeatures, List<UserFeaturesAnnotation> userFeaturesAnnot){
		
		//auxiliary variable
		Integer index=0;
		
		if (UserClassifier.getFvAttributes().size()==0){
			fvAttributes = (ArrayList<Attribute>) declareAttributes();
		}
		
		// Create an empty training set
		Instances isTrainingSet = new Instances("TrainingUserFeatures",  UserClassifier.getFvAttributes(), listUserFeatures.size());           

		// Set class index
		isTrainingSet.setClassIndex(UserClassifier.getFvAttributes().size()-1);
		
		for (int i=0;i<listUserFeatures.size();i++){
			
			Instance inst  = createInstance(listUserFeatures.get(i));
			
			for (int j=0;j<userFeaturesAnnot.size();j++){
				if ((listUserFeatures.get(i).getUsername()).equals(userFeaturesAnnot.get(j).getUsername())){
					index = j;
				}
			}
			
			inst.setValue((Attribute)fvAttributes.get(UserClassifier.getFvAttributes().size()-1), userFeaturesAnnot.get(index).getReliability());
			isTrainingSet.add(inst);
		}
		
		//System.out.println("-----TRAINING SET-------");
		//System.out.println(isTrainingSet);
		
		return isTrainingSet;
	}
	
	/**
	 * Method that creates the classifier
	 * @param isTrainingSet the Instances from which the classifier is created
	 * @throws Exception
	 */
	public static void createClassifier(Instances isTrainingSet) throws Exception{
		
		//create the classifier
		J48 j48 = new J48();
		Classifier fc = (Classifier) j48 ;
		fc.buildClassifier(isTrainingSet);
		Debug.saveToFile(Vars.MODEL_PATH_USER_sample, fc);
		System.out.println("Model file saved to "+Vars.MODEL_PATH_USER_sample);
		
	}
	
	/**
	 * Function that organizes the classification process given the training and testing set
	 * @param itemsFakeTrain Fake Items for the training set
	 * @param itemsFakeTest Fake Items for the testing set
	 * @param itemsRealTrain Real Items for the training set
	 * @param itemsRealTest Real Items for the testing set
	 * @throws Exception
	 */
	public static void doClassification(List<MediaItem> itemsFakeTrain, List<MediaItem> itemsFakeTest, List<MediaItem> itemsRealTrain, List<MediaItem> itemsRealTest) throws Exception{ 
		
		System.out.println("=== Classification using User features ===");
		
		//define the list of itemFeatures that are used for training and testing
		List<UserFeatures> userFeaturesTraining = new ArrayList<UserFeatures>();
		List<UserFeatures> userFeaturesTesting  = new ArrayList<UserFeatures>();
		//define the list of annotations of the items trained
		List<UserFeaturesAnnotation> itemFeaturesAnnot  = new ArrayList<UserFeaturesAnnotation>();
		List<UserFeaturesAnnotation> itemFeaturesAnnot2 = new ArrayList<UserFeaturesAnnotation>();
		
		//features
		UserFeaturesExtractor.db = "Sochi";
		UserFeaturesExtractor.collection = "UsersFake";
		System.out.println("Extracting features for training fake Items...");
		List<UserFeatures> userFeatsFakeTrain = UserFeaturesExtractor.userFeatureExtractionMedia(itemsFakeTrain);
		System.out.println("Extracting features for testing fake Items...");
		List<UserFeatures> userFeatsFakeTest  = UserFeaturesExtractor.userFeatureExtractionMedia(itemsFakeTest);
		
		UserFeaturesExtractor.collection = "UsersReal";
		System.out.println("Extracting features for training real Items...");
		List<UserFeatures> userFeatsRealTrain = UserFeaturesExtractor.userFeatureExtractionMedia(itemsRealTrain);
		System.out.println("Extracting features for testing real Items...");
		List<UserFeatures> userFeatsRealTest  = UserFeaturesExtractor.userFeatureExtractionMedia(itemsRealTest);

		
		/*--------FAKE ITEMS--------------*/
		//annotate and add to the itemFeaturesAnnot list
		//add item to the itemFeaturesTraining list
		for (int i = 0; i < userFeatsFakeTrain.size(); i++) {
			UserFeaturesAnnotation userAnnot = new UserFeaturesAnnotation();
			userAnnot.setUsername(userFeatsFakeTrain.get(i).getUsername());
			userAnnot.setReliability("fake");	
			itemFeaturesAnnot.add(userAnnot);
			userFeaturesTraining.add(userFeatsFakeTrain.get(i));
		}
		int trainfakesize = userFeaturesTraining.size();
		System.out.println("Training size of fake items : "+ trainfakesize);
		
		
		for (int i = 0; i < userFeatsFakeTest.size(); i++) {
			UserFeaturesAnnotation userAnnot = new UserFeaturesAnnotation();
			userAnnot.setUsername(userFeatsFakeTest.get(i).getUsername());
			userAnnot.setReliability("fake");	
			itemFeaturesAnnot2.add(userAnnot);
			userFeaturesTesting.add(userFeatsFakeTest.get(i));
		}
		int testfakesize = userFeaturesTesting.size();
		System.out.println("Testing size of fake items : "+ testfakesize);
		
		
		/*--------REAL ITEMS--------------*/
		//annotate and add to the itemFeaturesAnnot list
		//add item to the itemFeaturesTraining list

		for (int i = 0; i < userFeatsRealTrain.size(); i++) {
			UserFeaturesAnnotation userAnnot = new UserFeaturesAnnotation();
			userAnnot.setUsername(userFeatsRealTrain.get(i).getUsername());
			userAnnot.setReliability("real");	
			itemFeaturesAnnot.add(userAnnot);
			userFeaturesTraining.add(userFeatsRealTrain.get(i));
		}
		System.out.println("Training size of real items : "+ (userFeaturesTraining.size() - trainfakesize));
		
		for (int i = 0 ; i < userFeatsRealTest.size(); i++) {
			UserFeaturesAnnotation userAnnot = new UserFeaturesAnnotation();
			userAnnot.setUsername(userFeatsRealTest.get(i).getUsername());
			userAnnot.setReliability("real");	
			itemFeaturesAnnot2.add(userAnnot);
			userFeaturesTesting.add(userFeatsRealTest.get(i));
		}
		System.out.println("Testing size of real items : "+ (userFeaturesTesting.size() - testfakesize));
		
		
		Instances isTrainingSet = UserClassifier.createTrainingSet(userFeaturesTraining,itemFeaturesAnnot);
		Instances isTestingSet  = UserClassifier.createTestingSet(userFeaturesTesting,itemFeaturesAnnot2);
		
		System.out.println("Total size of training set : " +  isTrainingSet.size());
		System.out.println("Total size of testing set : "  +  isTestingSet.size());

		createClassifier(isTrainingSet);
		classifyItems(isTestingSet);
	}

	/**
	 * Method that implements cross validation to the items specified
	 * @param itemsFake the list of Items annotated as fake
	 * @param itemsReal the list of Items annotated as real
	 * @throws Exception
	 */
	public static void crossValidate(List<MediaItem> itemsFake, List<MediaItem> itemsReal) throws Exception{
		
		UserFeaturesExtractor.db = "Sochi";
		UserFeaturesExtractor.collection = "UsersFake";
		List<UserFeatures> userFeatsFake = UserFeaturesExtractor.userFeatureExtractionMedia(itemsFake);
		
		UserFeaturesExtractor.collection = "UsersReal";
		List<UserFeatures> userFeatsReal = UserFeaturesExtractor.userFeatureExtractionMedia(itemsReal);
		
		
		//define the list of itemFeatures that are used for training
		List<UserFeatures> userFeaturesTraining = new ArrayList<UserFeatures>();
		//define the list of annotations of the items trained
		List<UserFeaturesAnnotation> userFeaturesAnnot = new ArrayList<UserFeaturesAnnotation>();
		
		
		/*--------REAL ITEMS--------------*/
		// a)annotate and add to the itemFeaturesAnnot list and 
		// b)add item to the itemFeaturesTraining list
		
		Collections.shuffle(userFeatsReal);
		for (int i = 0; i < userFeatsReal.size(); i++) {
			UserFeaturesAnnotation userAnnot = new UserFeaturesAnnotation();
			userAnnot.setUsername(userFeatsReal.get(i).getUsername());
			userAnnot.setReliability("real");	
			userFeaturesAnnot.add(userAnnot);
			userFeaturesTraining.add(userFeatsReal.get(i));
		}
		int trainreal =  userFeaturesTraining.size();
		System.out.println("Training size of real items : "+ trainreal);
		
		
		/*--------FAKE ITEMS--------------*/
		// a)annotate and add to the itemFeaturesAnnot list and 
		// b)add item to the itemFeaturesTraining list
		
		Collections.shuffle(userFeatsFake);
		for (int i = 0 ; i < userFeatsFake.size(); i++) {
			UserFeaturesAnnotation userAnnot = new UserFeaturesAnnotation();
			userAnnot.setUsername(userFeatsFake.get(i).getUsername());
			userAnnot.setReliability("fake");	
			userFeaturesAnnot.add(userAnnot);
			userFeaturesTraining.add(userFeatsFake.get(i));
		}
		
		System.out.println("Training size of fake items : "+(userFeaturesTraining.size() - trainreal));
		System.out.println("Total size of training set : "+ userFeaturesTraining.size());
		
		Instances isTrainingSet = UserClassifier.createTrainingSet(userFeaturesTraining, userFeaturesAnnot);
		
		/*--------- J48 tree --------------*/
		Evaluation eval = new Evaluation(isTrainingSet);
		J48 tree = new J48(); 

		StringBuffer forPredictionsPrinting = new StringBuffer();
		PlainText classifierOutput = new PlainText();
		
		classifierOutput.setBuffer(forPredictionsPrinting);
		weka.core.Range attsToOutput = null;
		Boolean outputDistribution = new Boolean(true);
		classifierOutput.setOutputDistribution(true);

		eval.crossValidateModel(tree, isTrainingSet, 10, new Random(1), classifierOutput, attsToOutput, outputDistribution);
			
		System.out.println("===== J48 classifier =====");
		System.out.println("Number of correct classified "+eval.correct());
		System.out.println("Percentage of correct classified "+eval.pctCorrect());
		System.out.println(eval.toClassDetailsString());
		System.out.println(eval.toMatrixString());
	
		System.out.println(eval.toSummaryString());
		
		/*--------- Random Forest tree --------------*/
		Evaluation evall = new Evaluation(isTrainingSet);
		RandomForest tr = new RandomForest();
		StringBuffer forPredictionsPrintingl = new StringBuffer();
		PlainText classifierOutputl = new PlainText();
		
		classifierOutputl.setBuffer(forPredictionsPrintingl);
		weka.core.Range attsToOutputl = null;
		Boolean outputDistributionl = new Boolean(true);
		classifierOutputl.setOutputDistribution(true);

		evall.crossValidateModel(tr, isTrainingSet, 10, new Random(1), classifierOutputl, attsToOutputl, outputDistributionl);
		
		System.out.println("===== Random Forest =====");
		System.out.println("Number of correct classified "+evall.correct());
		System.out.println("Percentage of correct classified "+evall.pctCorrect());
		System.out.println(evall.toClassDetailsString());
		System.out.println(evall.toMatrixString());
		System.out.println(evall.toSummaryString());
		
		/*--------- KStar --------------*/
		Evaluation eval4 = new Evaluation(isTrainingSet);	
		KStar tree4 = new KStar();
		eval4.crossValidateModel(tree4, isTrainingSet, 10, new Random(1));
		
		System.out.println("===== KStar classifier =====");
		System.out.println("Number of correct classified "+eval4.correct());
		System.out.println("Percentage of correct classified "+eval4.pctCorrect());
		System.out.println(eval4.toClassDetailsString());
		System.out.println(eval4.toMatrixString());
		
		
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
