package gr.iti.mklab.verify;

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
import eu.socialsensor.framework.common.domain.MediaItem;
import gr.iti.mklab.extractfeatures.ItemFeaturesAnnotation;
import gr.iti.mklab.extractfeatures.TotalFeatures;
import gr.iti.mklab.extractfeatures.TotalFeaturesExtractor;
import gr.iti.mklab.extractfeatures.UserFeaturesExtractor;
import gr.iti.mklab.utils.Vars;



/**
 * Class to organize the Total classification using Item and User features 
 * by declaring the attributes,creating the testing set 
 * and perform the classification.
 * @author boididou
 */
public class TotalClassifier {
	
	static ArrayList<Attribute> fvAttributes  = new ArrayList<Attribute>();
		
	public static ArrayList<Attribute> getFvAttributes(){
		return fvAttributes;
	}
	
	/**
	 * @param listItemFeatures the ItemFeatures of the Item
	 * @return the Instance created by the features
	 */
	public static Instance createInstance(TotalFeatures list){
		
		Instance inst = new DenseInstance(fvAttributes.size());
		
		inst.setValue((Attribute)fvAttributes.get(0),  list.getItemLength());  
		inst.setValue((Attribute)fvAttributes.get(1),  list.getNumWords());
		inst.setValue((Attribute)fvAttributes.get(2),  String.valueOf(list.getContainsQuestionMark()));
		inst.setValue((Attribute)fvAttributes.get(3),  String.valueOf(list.getContainsExclamationMark()));
		inst.setValue((Attribute)fvAttributes.get(4),  list.getnumQuestionMark());
		inst.setValue((Attribute)fvAttributes.get(5),  list.getnumExclamationMark());
		inst.setValue((Attribute)fvAttributes.get(6),  String.valueOf(list.getContainsHappyEmo()));
		inst.setValue((Attribute)fvAttributes.get(7),  String.valueOf(list.getContainsSadEmo()));
		inst.setValue((Attribute)fvAttributes.get(8),  String.valueOf(list.getContainsFirstOrderPron()));
		inst.setValue((Attribute)fvAttributes.get(9),  String.valueOf(list.getContainsSecondOrderPron()));
		inst.setValue((Attribute)fvAttributes.get(10), String.valueOf(list.getContainsThirdOrderPron()));
		inst.setValue((Attribute)fvAttributes.get(11), list.getNumUppercaseChars());
		inst.setValue((Attribute)fvAttributes.get(12), list.getNumPosSentiWords());
		inst.setValue((Attribute)fvAttributes.get(13), list.getNumNegSentiWords());
		inst.setValue((Attribute)fvAttributes.get(14), list.getNumMentions());
		inst.setValue((Attribute)fvAttributes.get(15), list.getNumHashtags());
		inst.setValue((Attribute)fvAttributes.get(16), list.getNumURLs());
		inst.setValue((Attribute)fvAttributes.get(17), list.getRetweetCount());
		inst.setValue((Attribute)fvAttributes.get(18), list.getnumFriends());
		inst.setValue((Attribute)fvAttributes.get(19), list.getnumFollowers());
		inst.setValue((Attribute)fvAttributes.get(20), list.getFolFrieRatio());
		inst.setValue((Attribute)fvAttributes.get(21), list.gettimesListed());
		inst.setValue((Attribute)fvAttributes.get(22), String.valueOf(list.gethasURL()));
		inst.setValue((Attribute)fvAttributes.get(23), String.valueOf(list.getisVerified()));
		inst.setValue((Attribute)fvAttributes.get(24), list.getnumTweets());
		
		return inst;
	}
	
	/**
	 * @param list of TotalFeatures computed
	 * @param itemFeaturesAnnot list of the items' annotation details
	 * @return Instances that form the testing set
	 * @throws Exception
	 */
	public static Instances createTestingSet(List<TotalFeatures> list, List<ItemFeaturesAnnotation> itemFeaturesAnnot) throws Exception{
		
		Integer index=0;
		if (TotalClassifier.getFvAttributes().size()==0){
			if (ItemClassifier.getFvAttributes().size()==0){
				fvAttributes  = (ArrayList<Attribute>) ItemClassifier.declareAttributes();
			}
			else{
				fvAttributes = ItemClassifier.getFvAttributes();
			}
			
			List<Attribute> fvAttributes2 = new ArrayList<Attribute>();
			if (UserClassifier.getFvAttributes().size()==0){
				fvAttributes2 = UserClassifier.declareAttributes();
			}
			else{
				fvAttributes2 = UserClassifier.getFvAttributes();
			}
			
			fvAttributes.addAll(fvAttributes2);
			fvAttributes.remove(18);
		}
		
		Instances isTestingSet = new Instances("TotalClassific", fvAttributes, list.size());  
		isTestingSet.setClassIndex(fvAttributes.size()-1);
		
		/*System.out.println("Attributes ");
		for (Attribute att:TotalClassifier.fvAttributes){
			System.out.print(att.name()+",");
		}
		System.out.println();*/
		
		for (int i=0;i<list.size();i++){
			
			Instance inst  = createInstance(list.get(i));

			for (int j=0;j<itemFeaturesAnnot.size();j++){
				if ((list.get(i).getId()).equals(itemFeaturesAnnot.get(j).getId())){
					index = j;				
				}
			}
			
			inst.setValue((Attribute)fvAttributes.get(fvAttributes.size()-1), itemFeaturesAnnot.get(index).getReliability());
			isTestingSet.add(inst);
			

		}
				
		return isTestingSet;
	}
	
	/**
	 * @param list of TotalFeatures computed
	 * @param itemFeaturesAnnot list of the item's annotation details
	 * @return Instances that form the testing set
	 * @throws Exception
	 */
	public static Instances createTestingSet(TotalFeatures list, ItemFeaturesAnnotation itemFeaturesAnnot) throws Exception{
		
		System.out.println("size "+ItemClassifier.getFvAttributes().size());
		
		if (ItemClassifier.getFvAttributes().size()==0){
			fvAttributes  = (ArrayList<Attribute>) ItemClassifier.declareAttributes();
		}
		else{
			fvAttributes = ItemClassifier.getFvAttributes();
		}
		

		List<Attribute> fvAttributes2 = new ArrayList<Attribute>();
		
		if (UserClassifier.getFvAttributes().size()==0){
			fvAttributes2 = UserClassifier.declareAttributes();
		}
		else{
			fvAttributes2 = UserClassifier.getFvAttributes();
		}
		
		
		fvAttributes.addAll(fvAttributes2);
		fvAttributes.remove(18);
		
		Instances isTestingSet = new Instances("TotalClassification", fvAttributes, 1);  
		isTestingSet.setClassIndex(fvAttributes.size()-1);
		
		System.out.println("Attributes ");
		for (Attribute att:TotalClassifier.getFvAttributes()){
			System.out.print(att.name()+",");
		}
		System.out.println();
		

		Instance inst  = createInstance(list);

		inst.setValue((Attribute)fvAttributes.get(fvAttributes.size()-1), itemFeaturesAnnot.getReliability());
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
		classifier.setModelFile(new File(Vars.MODEL_PATH_TOTAL_sample));
		
		
		for (int i = 0; i < isTestSet.numInstances(); i++) {
			double pred = classifier.classifyInstance(isTestSet.instance(i));
			
			String actual = isTestSet.classAttribute().value((int)isTestSet.instance(i).classValue());
			String predicted = isTestSet.classAttribute().value((int) pred);
			
			if (predicted=="fake"){
				flags[i]=true;
			}
			else{
				flags[i]=false;
			}
			if (actual.equals(predicted)){
				count++;
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
	 * @param list the list of Total features
	 * @param itemFeaturesAnnot the Items' annotation details
	 * @return the Instances formed 
	 * @throws Exception
	 */
	public static Instances createTrainingSet(List<TotalFeatures> list, List<ItemFeaturesAnnotation> itemFeaturesAnnot) throws Exception{
		
		Integer index=0;
		
		if (ItemClassifier.getFvAttributes().size()==0){
			fvAttributes  = (ArrayList<Attribute>) ItemClassifier.declareAttributes();
		}
		else{
			fvAttributes = ItemClassifier.getFvAttributes();
		}
		

		List<Attribute> fvAttributes2 = new ArrayList<Attribute>();
		
		if (UserClassifier.getFvAttributes().size()==0){
			fvAttributes2 = UserClassifier.declareAttributes();
		}
		else{
			fvAttributes2 = UserClassifier.getFvAttributes();
		}
		
		
		fvAttributes.addAll(fvAttributes2);
		fvAttributes.remove(18);
		
		Instances isTrainingSet = new Instances("Rel", fvAttributes, list.size());  
		isTrainingSet.setClassIndex(fvAttributes.size()-1);
		
		for (int i=0;i<list.size();i++){
			Instance inst  = createInstance(list.get(i));
			
			
			for (int j=0;j<itemFeaturesAnnot.size();j++){
				if ((list.get(i).getId()).equals(itemFeaturesAnnot.get(j).getId())){
					index = j;
				}
			}
			
			inst.setValue((Attribute)fvAttributes.get(fvAttributes.size()-1), itemFeaturesAnnot.get(index).getReliability());
			isTrainingSet.add(inst);

		}
		//System.out.println("-----TRAINING SET-------");
		//System.out.println(isTrainingSet);
		
		return isTrainingSet;
	}
	
	/**
	 * Method that creates the classifier given the training Instances
	 * @param isTrainingSet the Instances
	 * @throws Exception
	 */
	public static void createClassifier(Instances isTrainingSet) throws Exception{

		//create the classifier
		J48 j48 = new J48();
		Classifier fc = (Classifier) j48;
		fc.buildClassifier(isTrainingSet);
		Debug.saveToFile(Vars.MODEL_PATH_TOTAL_sample, fc);
		System.out.println("Model file saved to "+Vars.MODEL_PATH_TOTAL_sample);
	}
	
	/**
	 * Function that organizes the classification process given the training and testing sets
	 * @param itemsFakeTrain Fake Items for the training set
	 * @param itemsFakeTest Fake Items for the testing set
	 * @param itemsRealTrain Real Items for the training set
	 * @param itemsRealTest Real Items for the testing set
	 * @throws Exception
	 */
	public static void doClassification(List<MediaItem> itemsFakeTrain, List<MediaItem> itemsFakeTest, List<MediaItem> itemsRealTrain, List<MediaItem> itemsRealTest) throws Exception{
		
		System.out.println("=== Classification using Total features ===");
		//define the list of itemFeatures that are used for training and testing
		List<TotalFeatures> totFeaturesTraining = new ArrayList<TotalFeatures>();
		List<TotalFeatures> totFeaturesTesting  = new ArrayList<TotalFeatures>();
		
		//define the list of annotations of the items trained
		List<ItemFeaturesAnnotation> itemFeaturesAnnot  = new ArrayList<ItemFeaturesAnnotation>();
		List<ItemFeaturesAnnotation> itemFeaturesAnnot2 = new ArrayList<ItemFeaturesAnnotation>();
		
		//features
		UserFeaturesExtractor ufe = new UserFeaturesExtractor();
		
		UserFeaturesExtractor.setDb("Sochi");
		UserFeaturesExtractor.setCollection("UsersFake");
		System.out.println("Extracting features for training fake Items...");
		
		List<TotalFeatures> totFeaturesFakeTrain = TotalFeaturesExtractor.featureExtractionMedia(itemsFakeTrain);
		System.out.println("Extracting features for testing fake Items...");
		
		List<TotalFeatures> totFeaturesFakeTest = TotalFeaturesExtractor.featureExtractionMedia(itemsFakeTest);
		System.out.println("Extracting features for training real Items...");
		
		UserFeaturesExtractor.setCollection("UsersReal");
		
		List<TotalFeatures> totFeaturesRealTrain = TotalFeaturesExtractor.featureExtractionMedia(itemsRealTrain);
		System.out.println("Extracting features for testing real Items...");
		
		List<TotalFeatures> totFeaturesRealTest = TotalFeaturesExtractor.featureExtractionMedia(itemsRealTest);
		
		
		/*--------FAKE ITEMS--------------*/
		//annotate and add to the itemFeaturesAnnot list
		//add item to the itemFeaturesTraining list
		for (int i = 0; i < totFeaturesFakeTrain.size(); i++) {
			ItemFeaturesAnnotation itemAnnot = new ItemFeaturesAnnotation();
			itemAnnot.setId(totFeaturesFakeTrain.get(i).getId());
			itemAnnot.setReliability("fake");	
			itemFeaturesAnnot.add(itemAnnot);
			totFeaturesTraining.add(totFeaturesFakeTrain.get(i));
		}
		int trainfakesize = totFeaturesTraining.size();
		System.out.println("Training size of fake items : "+ trainfakesize);

		for (int i = 0; i < totFeaturesFakeTest.size(); i++) {
			ItemFeaturesAnnotation itemAnnot = new ItemFeaturesAnnotation();
			itemAnnot.setId(totFeaturesFakeTest.get(i).getId());
			itemAnnot.setReliability("fake");	
			itemFeaturesAnnot2.add(itemAnnot);
			totFeaturesTesting.add(totFeaturesFakeTest.get(i));
		}
		int trainrealsize = totFeaturesTesting.size();
		System.out.println("Testing size of fake items : "+ trainrealsize);
		
		/*--------REAL ITEMS--------------*/
		//annotate and add to the itemFeaturesAnnot list
		//add item to the itemFeaturesTraining list
		
		for (int i = 0; i < totFeaturesRealTrain.size(); i++) {
			ItemFeaturesAnnotation itemAnnot = new ItemFeaturesAnnotation();
			itemAnnot.setId(totFeaturesRealTrain.get(i).getId());
			itemAnnot.setReliability("real");	
			itemFeaturesAnnot.add(itemAnnot);
			totFeaturesTraining.add(totFeaturesRealTrain.get(i));
		}
		
		System.out.println("Training size of real items : "+ (totFeaturesTraining.size() - trainfakesize));
		
		for (int i = 0 ; i < totFeaturesRealTest.size(); i++) {
			ItemFeaturesAnnotation itemAnnot = new ItemFeaturesAnnotation();
			itemAnnot.setId(totFeaturesRealTest.get(i).getId());
			itemAnnot.setReliability("real");	
			itemFeaturesAnnot2.add(itemAnnot);
			totFeaturesTesting.add(totFeaturesRealTest.get(i));
		}
		System.out.println("Testing size of real items : "+ (totFeaturesTesting.size() - trainrealsize));
		
		Instances isTrainingSet = TotalClassifier.createTrainingSet(totFeaturesTraining,itemFeaturesAnnot);
		Instances isTestingSet  = TotalClassifier.createTestingSet(totFeaturesTesting,itemFeaturesAnnot2);
		
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
		
		//define the list of itemFeatures that are used for training
		List<TotalFeatures> totFeaturesTraining = new ArrayList<TotalFeatures>();
		//define the list of annotations of the items trained
		List<ItemFeaturesAnnotation> itemFeaturesAnnot = new ArrayList<ItemFeaturesAnnotation>();
		
		UserFeaturesExtractor.setDb("Sochi");
		UserFeaturesExtractor.setCollection("UsersFake");
		List<TotalFeatures> totFeaturesFake = TotalFeaturesExtractor.featureExtractionMedia(itemsFake);
		
		UserFeaturesExtractor.setCollection("UsersReal");
		List<TotalFeatures> totFeaturesReal = TotalFeaturesExtractor.featureExtractionMedia(itemsReal);
		
		/*--------FAKE ITEMS--------------*/
				
		//annotate and add to the itemFeaturesAnnot list
		//add item to the totFeaturesTraining list
		Collections.shuffle(totFeaturesFake);
		for (int i = 0; i < totFeaturesFake.size(); i++) {
			ItemFeaturesAnnotation itemAnnot = new ItemFeaturesAnnotation();
			itemAnnot.setId(totFeaturesFake.get(i).getId());
			itemAnnot.setReliability("fake");	
			itemFeaturesAnnot.add(itemAnnot);
			totFeaturesTraining.add(totFeaturesFake.get(i));
		}
		int trainfake = totFeaturesTraining.size();
		System.out.println("Training size of fake items : "+ trainfake);
		
		/*--------REAL ITEMS--------------*/

		//decide to shuffle or not to transform the fakeItems training set
		Collections.shuffle(totFeaturesReal);
		//annotate and add to the itemFeaturesAnnot list
		//add item to the totFeaturesTraining list
		for (int i = 0 ; i < totFeaturesReal.size(); i++) {
			ItemFeaturesAnnotation itemAnnot = new ItemFeaturesAnnotation();
			itemAnnot.setId(totFeaturesReal.get(i).getId());
			itemAnnot.setReliability("real");	
			itemFeaturesAnnot.add(itemAnnot);
			totFeaturesTraining.add(totFeaturesReal.get(i));
		}
		System.out.println("Training size of real items : " +(totFeaturesTraining.size() - trainfake));
		System.out.println("Total size of training set :  " + totFeaturesTraining.size());
		
		Instances isTrainingSet = TotalClassifier.createTrainingSet(totFeaturesTraining,itemFeaturesAnnot);
		
		/*---------J48 tree--------------*/
		Evaluation eval = new Evaluation(isTrainingSet);
		J48 tree = new J48(); 

		StringBuffer forPredictionsPrinting = new StringBuffer();
		PlainText classifierOutput = new PlainText();
		
		classifierOutput.setBuffer(forPredictionsPrinting);
		weka.core.Range attsToOutput = null;
		Boolean outputDistribution = new Boolean(true);
		classifierOutput.setOutputDistribution(true);

		eval.crossValidateModel(tree, isTrainingSet, 10, new Random(1), classifierOutput, attsToOutput, outputDistribution);
			
		System.out.println("J48 classifier");
		System.out.println("Number of correct classified "+eval.correct());
		System.out.println("Percentage of correct classified "+eval.pctCorrect());
		System.out.println(eval.toClassDetailsString());
		System.out.println(eval.toMatrixString());
	
		System.out.println(eval.toSummaryString());
		
		/*---------Random Forest tree--------------*/
		Evaluation evall = new Evaluation(isTrainingSet);
		RandomForest tr = new RandomForest();
		StringBuffer forPredictionsPrintingl = new StringBuffer();
		PlainText classifierOutputl = new PlainText();
		
		classifierOutputl.setBuffer(forPredictionsPrintingl);
		weka.core.Range attsToOutputl = null;
		Boolean outputDistributionl = new Boolean(true);
		classifierOutputl.setOutputDistribution(true);

		evall.crossValidateModel(tr, isTrainingSet, 10, new Random(1), classifierOutputl, attsToOutputl, outputDistributionl);
		
		System.out.println("Random Forest");
		System.out.println("Number of correct classified "+evall.correct());
		System.out.println("Percentage of correct classified "+evall.pctCorrect());
		System.out.println(evall.toClassDetailsString());
		System.out.println(evall.toMatrixString());
		System.out.println(evall.toSummaryString());
				
		Evaluation eval4 = new Evaluation(isTrainingSet);	
		KStar tree4 = new KStar();
		eval4.crossValidateModel(tree4, isTrainingSet, 10, new Random(1));
		System.out.println("KStar classifier");
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
		//classifier.setModelFile(new File(Vars.MODEL_PATH_TOTAL));
		
		for (int i = 0; i < isTestSet.numInstances(); i++) {
			double[] probabilityDistribution = classifier.distributionForInstance(isTestSet.instance(i));
			probabilities[i] = probabilityDistribution[1];
		}
		
		return probabilities;
	}
	
}
