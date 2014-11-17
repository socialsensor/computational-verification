package gr.iti.mklab.verify;

import gr.iti.mklab.verifyutils.DataHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.unsupervised.attribute.Normalize;
import weka.filters.unsupervised.attribute.Remove;

public class Bagging {
	
	static Normalize normFilter = new Normalize();
	static Normalize normFilterUser = new Normalize();

	static FilteredClassifier model = new FilteredClassifier();
	static FilteredClassifier model2 = new FilteredClassifier();
	static FilteredClassifier model3 = new FilteredClassifier();
	static FilteredClassifier model4 = new FilteredClassifier();
	static FilteredClassifier model5 = new FilteredClassifier();
	static FilteredClassifier model6 = new FilteredClassifier();
	
	/**
	 * Applies linear regression to the missing values of the training set in order to predict their values
	 * and normalization to make the values fall into the range [0,1].
	 * It is the case of the Item type Instances.
	 * @param trainingSet
	 * @return Instances the transformed training set
	 * @throws Exception
	 */
	public static Instances getTransformedTraining(Instances trainingSet) throws Exception {
		
		Remove rm = new Remove();
		rm.setAttributeIndices("1");
			
		ArrayList<Attribute> fvAttributes = ItemClassifier.getFvAttributes();
		
		//REGRESSION
		//wotTrust		
		trainingSet.setClass(fvAttributes.get(22)); 
		LinearRegression lr = new LinearRegression();
		Instances training_regr = null;
		model.setFilter(rm);
		model.setClassifier(lr);
		
		try {
			model.buildClassifier(trainingSet);
			training_regr = DataHandler.getInstance().applyRegressionModel(trainingSet, fvAttributes, model);
		}
		catch(Exception e) {
			training_regr = trainingSet;
			System.out.println("not enought training instances. Linear Regression not performed!");
		}
		
		//readability
		training_regr.setClass(fvAttributes.get(25));
		LinearRegression lr2 = new LinearRegression();
		Instances training_regr2 = null;
		
		model2.setFilter(rm);
		model2.setClassifier(lr2);
		try {
			model2.buildClassifier(training_regr);
			training_regr2 = DataHandler.getInstance().applyRegressionModel(training_regr, fvAttributes, model2);
		}
		catch(Exception e){
			training_regr2 = training_regr;
			System.out.println("not enought training instances. Linear Regression not performed!");
		}
		
		training_regr2.setClass(fvAttributes.get(19));
		LinearRegression lr3 = new LinearRegression();
		Instances training_regr3 = null;
		
		model3.setFilter(rm);
		model3.setClassifier(lr3);
		try {
			model3.buildClassifier(training_regr2);
			training_regr3 = DataHandler.getInstance().applyRegressionModel(training_regr2, fvAttributes, model3);
		}
		catch(Exception e) {
			training_regr3 = training_regr2;
			System.out.println("not enought training instances. Linear Regression not performed!");
		}
		
		training_regr3.setClass(fvAttributes.get(13));
		LinearRegression lr4 = new LinearRegression();
		Instances training_regr4 = null;
		model4.setFilter(rm);
		model4.setClassifier(lr4);
		try {
			model4.buildClassifier(training_regr3);
			training_regr4 = DataHandler.getInstance().applyRegressionModel(training_regr3, fvAttributes, model4);
		}
		catch(Exception e) {
			training_regr4 = training_regr3;
			System.out.println("not enought training instances. Linear Regression not performed!");
		}
		
		training_regr4.setClass(fvAttributes.get(14));
		LinearRegression lr5 = new LinearRegression();
		Instances training_regr5 = null;
		model5.setFilter(rm);
		model5.setClassifier(lr5);
		
		try {
			model5.buildClassifier(training_regr4);
			training_regr5 = DataHandler.getInstance().applyRegressionModel(training_regr4, fvAttributes, model5);
		}
		catch(Exception e) {
			training_regr5 = training_regr4;
			System.out.println("not enought training instances. Linear Regression not performed!");
		}
		
		training_regr5.setClass(fvAttributes.get(6));
		LinearRegression lr6 = new LinearRegression();
		Instances training_regr6 = null;
		model6.setFilter(rm);
		model6.setClassifier(lr6);
		try {
			model6.buildClassifier(training_regr5);
			training_regr6 = DataHandler.getInstance().applyRegressionModel(training_regr5, fvAttributes, model6);
		}
		catch(Exception e) {
			training_regr6 = training_regr5;
		}
		
						
		// normalization part
		String[] options = { "-S", "2.0", "-T", "-1.0" };
		normFilter.setOptions(options);
		normFilter.setInputFormat(training_regr5);
		
		Instances trainingSet_normed = DataHandler.getInstance().normalizeData(training_regr6, fvAttributes.size() - 1, normFilter);
		
				
		return trainingSet_normed;
		
	}
	
	
	static FilteredClassifier usermodel = new FilteredClassifier();
	static FilteredClassifier usermodel2 = new FilteredClassifier();
	static FilteredClassifier usermodel3 = new FilteredClassifier();
	
	/**
	 * Applies linear regression to the missing values of the training set in order to predict their values
	 * and normalization to make the values fall into the range [0,1].
	 * It is the case of the User type Instances.
	 * @param trainingSet
	 * @return Instances the transformed training set
	 * @throws Exception
	 */
	public static Instances getTransformedTrainingUser(Instances trainingSet) {
		
		ArrayList<Attribute> fvAttributes = UserClassifier.getFvAttributes();
		
		//remove filter in order to remove the id attribute
		Remove rm = new Remove();
		rm.setAttributeIndices("1");
		
		//regression
		trainingSet.setClass(fvAttributes.get(11));
		LinearRegression lr = new LinearRegression();
		Instances training_regr = null;
		usermodel.setFilter(rm);
		usermodel.setClassifier(lr);

		
		try {
			usermodel.buildClassifier(trainingSet);
			training_regr  = DataHandler.getInstance().applyRegressionModel(trainingSet, fvAttributes, usermodel);
		}
		catch(Exception e) {
			training_regr = trainingSet;
			System.out.println("not enought training instances. Linear Regression not performed!");
		}
		
		
		training_regr.setClass(fvAttributes.get(13));
		LinearRegression lr2 = new LinearRegression();
		Instances training_regr2 = null;
		usermodel2.setFilter(rm);
		usermodel2.setClassifier(lr2);
		try {
		usermodel2.buildClassifier(training_regr); 
			training_regr2  = DataHandler.getInstance().applyRegressionModel(training_regr, fvAttributes, usermodel2);
		}
		catch(Exception e) {
			training_regr2 = training_regr;
			System.out.println("not enought training instances. Linear Regression not performed!");
		}
		
		training_regr2.setClass(fvAttributes.get(16));
		LinearRegression lr3 = new LinearRegression();
		Instances training_regr3 = null;
		usermodel3.setFilter(rm);
		usermodel3.setClassifier(lr3);
		try {
			usermodel3.buildClassifier(training_regr2); 
			training_regr3  = DataHandler.getInstance().applyRegressionModel(training_regr2, fvAttributes, usermodel3);
		}
		catch(Exception e) {
			training_regr3 = training_regr2;
			System.out.println("not enought training instances. Linear Regression not performed!");
		}
		
		//normalization
		normFilterUser  =  DataHandler.getInstance().createNormalizationFilter(training_regr3);
		Instances trainingSet_normed = DataHandler.getInstance().normalizeData(training_regr3, fvAttributes.size()-1, normFilterUser);
				
		return trainingSet_normed;
	}
	
	/**
	 * Applies Linear Regression and normalization to the testing set according to the models and filters
	 * created before by the training set.
	 * It is the case of the Item type Instances. 
	 * @param testing the Instances to be transformed
	 * @return the transformed testing set
	 * @throws Exception
	 */
	public static Instances getTransformedTesting(Instances testing) throws Exception {
		
		ArrayList<Attribute> fvAttributes = ItemClassifier.getFvAttributes();
		Instances testing_regr=null,testing_regr2=null,testing_regr3=null,testing_regr4=null,testing_regr5=null,testing_regr6=null;
		
		// regression
		if (!model.toString().contains("No model built yet.")) {
			testing.setClass(fvAttributes.get(22));
			testing_regr = DataHandler.getInstance().applyRegressionModel(testing, fvAttributes, model);
		}
		else {
			testing_regr = testing;
		}
		
		if (!model2.toString().contains("No model built yet.")) {
			testing_regr.setClass(fvAttributes.get(25));
			testing_regr2 = DataHandler.getInstance().applyRegressionModel(testing_regr, fvAttributes, model2);
		}
		else {
			testing_regr2 = testing_regr;
		}
		
		if (!model3.toString().contains("No model built yet.")) {
			testing_regr2.setClass(fvAttributes.get(19));
			testing_regr3 = DataHandler.getInstance().applyRegressionModel(testing_regr2, fvAttributes, model3);
		}
		else {
			testing_regr3 = testing_regr2;
		}
		
		if (!model4.toString().contains("No model built yet.")) {
			testing_regr3.setClass(fvAttributes.get(13));
			testing_regr4 = DataHandler.getInstance().applyRegressionModel(testing_regr3, fvAttributes, model4);
		}
		else {
			testing_regr4 = testing_regr3;
		}
		
		if (!model5.toString().contains("No model built yet.")) {
			testing_regr4.setClass(fvAttributes.get(14));
			testing_regr5 = DataHandler.getInstance().applyRegressionModel(testing_regr4, fvAttributes, model5);
		}
		else {
			testing_regr5 = testing_regr4;
		}
		
		if (!model6.toString().contains("No model built yet.")) {
			testing_regr5.setClass(fvAttributes.get(6));
			testing_regr6 = DataHandler.getInstance().applyRegressionModel(testing_regr5, fvAttributes, model6);
		}
		else {
			testing_regr6 = testing_regr5;
		}
			

		// normalization
		Instances testingSet_normed = DataHandler.getInstance().normalizeData(testing_regr6, fvAttributes.size() - 1, normFilter);
		//testingSet_normed = getTrimmedInstances(testingSet_normed);

		return testingSet_normed;
	}
	
	
	/**
	 * Applies Linear Regression and normalization to the testing set according to the models and filters
	 * created before by the training set.
	 * It is the case of the User type Instances. 
	 * @param testing the Instances to be transformed
	 * @return the transformed testing set
	 * @throws Exception
	 */
	public static Instances getTransformedTestingUser(Instances testing) throws Exception {
		
		ArrayList<Attribute> fvAttributes = UserClassifier.getFvAttributes();
		Instances testing_regr=null,testing_regr2=null,testing_regr3=null;
		
		testing_regr3 = testing;
		
		if (!usermodel.toString().contains("No model built yet.")) {
			testing.setClass(fvAttributes.get(11));
			testing_regr  = DataHandler.getInstance().applyRegressionModel(testing, fvAttributes, usermodel);
		}
		else {
			testing_regr = testing;
		}
		if (!usermodel2.toString().contains("No model built yet.")) {
			testing_regr.setClass(fvAttributes.get(13));
			testing_regr2  = DataHandler.getInstance().applyRegressionModel(testing_regr, fvAttributes, usermodel2);
		}
		else {
			testing_regr2= testing_regr;
		}
		if (!usermodel3.toString().contains("No model built yet.")) {
			testing_regr2.setClass(fvAttributes.get(16));
			testing_regr3  = DataHandler.getInstance().applyRegressionModel(testing_regr2, fvAttributes, usermodel3);
		}
		else {
			testing_regr3 = testing_regr2;
		}
		
		if (!usermodel.toString().contains("No model built yet.")) {
			testing.setClass(fvAttributes.get(1));
			testing_regr  = DataHandler.getInstance().applyRegressionModel(testing, fvAttributes, usermodel);
		}
		else {
			testing_regr = testing;
		}
		if (!usermodel2.toString().contains("No model built yet.")) {
			testing_regr.setClass(fvAttributes.get(4));
			testing_regr2  = DataHandler.getInstance().applyRegressionModel(testing_regr, fvAttributes, usermodel2);
		}
		else {
			testing_regr2= testing_regr;
		}
		
		
		//normalization
		Instances testingSet_normed = DataHandler.getInstance().normalizeData(testing_regr3, fvAttributes.size()-1, normFilterUser);
		//testingSet_normed = getTrimmedInstances(testingSet_normed);
		
		return testingSet_normed;
	}
	
	static Instances[] testingSets = new Instances[9];
	
	public static Instances[] getTestingSets() {
		return testingSets;
	}
	
	public static void setTestingSets(Instances[] testingSets) {
		Bagging.testingSets = testingSets;
	}
	
	/**
	 * Initializes the random values
	 * @return
	 */
	static int[] randomVals = new int[10];
	public static ArrayList<int[]> initializeRandomVals() {
		
		ArrayList<int[]> random = new ArrayList<int[]>();
		
		random.add(new int[] {6,7,8,90,32,334,777,188,149});
		random.add(new int[] {56,38,58,42,59,65,71,18,19});
		random.add(new int[] {33,46,11,88,99,27,35,29,20});
		random.add(new int[] {10,55,13,81,199,127,235,329,420});
		random.add(new int[] {28,29,30,31,39,327,335,429,520});
		random.add(new int[] {1,2,3,4,55,55,6,77,66});
		random.add(new int[] {51,62,333,334,955,255,36,877,626});
		random.add(new int[] {27,227,123,234,135,147,336,890,891});
		random.add(new int[] {9,10,11,22,783,472,90,91,99});
		random.add(new int[] {31,34,67,88,89,908,869,245,12});
		
		return random;
	}
	
	
	/**
	 * Creates the classifiers of the bagging process trained with Instances of Item type. The number of classifiers is equal to the size of each randomVals[i]
	 * @param training2 the specified training set
	 * @param testing2 the specified testing set
	 * @param trainingSize size of the training set of each iteration of the bagging
	 * @return the Classifier[] array of classifiers of each iteration
	 * @throws Exception
	 */
	public static Classifier[] createClassifiers(Instances training2, Instances testing2, int trainingSize) throws Exception {
		
		
		Instances training = new Instances(training2);
		Instances testing  = new Instances(testing2);
		
		int countFake=0, countReal=0;
		Classifier[] classifiers = new Classifier[randomVals.length];
		
		for (int j=0; j<randomVals.length; j++) {
			
			Instances currentTrain = new Instances("rel", ItemClassifier.getFvAttributes(), trainingSize);
			
			Collections.shuffle(training, new Random(randomVals[j]));
			countFake = 0; 
			countReal = 0;
				
			for (int i=0; i<training.size(); i++) {
				if (countFake<(trainingSize/2)) {
					if (training.classAttribute().value((int) training.get(i).classValue()).equals("fake")) {
						countFake++;
						currentTrain.add(training.get(i));
					}
				}
							
			}
			
			
			for (int i=0; i<training.size(); i++) {
				if ( countReal < (trainingSize/2) ) {
					if (training.classAttribute().value((int) training.get(i).classValue()).equals("real")) {
						currentTrain.add(training.get(i));
						countReal++;
					}
				}
			}
			
						
			currentTrain = DataHandler.getInstance().getTransformedTraining(currentTrain);
					
			testingSets[j] = DataHandler.getInstance().getTransformedTesting(testing);
			
			FilteredClassifier fc = new FilteredClassifier();
			RandomForest tree = new RandomForest();
			//remove the first attribute, the id
			Remove rm2 = new Remove();
			rm2.setAttributeIndices("1");
			try {
				fc.setFilter(rm2);
				fc.setClassifier(tree);	
				fc.buildClassifier(currentTrain);
				classifiers[j] = fc;
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return classifiers;
	}
	
	static Instances[] testingSetsUser = new Instances[9];
	
	public static Instances[] getTestingSetsUser() {
		return testingSetsUser;
	}
	
	public static void setTestingSetsUser(Instances[] testingSetsUser) {
		Bagging.testingSetsUser = testingSetsUser;
	}
	
	/**
	 * Creates the classifiers of the bagging process trained with Instances of User type. The number of classifiers is equal to the size of each randomVals[i]
	 * @param training2 the specified training set
	 * @param testing2 the specified testing set
	 * @param trainingSize size of the training set of each iteration of the bagging
	 * @return the Classifier[] array of classifiers of each iteration
	 * @throws Exception
	 */
	public static Classifier[] createClassifiersUser(Instances training2, Instances testing2, int trainingSize) throws Exception {
		
		Instances training = new Instances(training2);
		Instances testing  = new Instances(testing2);
		
		int countFake=0, countReal=0;
		Classifier[] classifiers = new Classifier[randomVals.length];
		
		for (int j=0; j<randomVals.length; j++) {
			Instances currentTrain = new Instances("rel", UserClassifier.getFvAttributes(), trainingSize);
			Collections.shuffle(training, new Random(randomVals[j]));
			countFake = 0; 
			countReal = 0;
			
			for (int i=0; i<training.size(); i++) {
				if (countFake<(trainingSize/2)) {
					if (training.classAttribute().value((int) training.get(i).classValue()).equals("fake")) {
						countFake++;
						currentTrain.add(training.get(i));
					}
				}
							
			}
			
			for (int i=0; i<training.size(); i++) {
				if ( countReal < (trainingSize/2)/*(trainingSize-countFake)*/ ) {
					if (training.classAttribute().value((int) training.get(i).classValue()).equals("real")) {
						currentTrain.add(training.get(i));
						countReal++;
					}
				}
			}
			
			
			currentTrain = DataHandler.getInstance().getTransformedTrainingUser(currentTrain);
			
			/*if (testingSetsUser[j]!=null) {
				System.out.println(testingSetsUser[j].size());
			}*/
			testingSetsUser[j] = DataHandler.getInstance().getTransformedTestingUser(testing);
			
			FilteredClassifier fc = new FilteredClassifier();
			//J48 tree = new J48();
			RandomForest tree = new RandomForest();
			//Remove the first attribute, the id
			Remove rm2 = new Remove();
			rm2.setAttributeIndices("1");
			try {
				fc.setClassifier(tree);
				fc.setFilter(rm2);				
				fc.buildClassifier(currentTrain);
				classifiers[j] = fc;
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return classifiers;
	}
	
	
	/**
	 * Classifies the specified Instance
	 * @param cls Classifier given
	 * @param inst the Instance given
	 * @return String the verification result
	 */
	public static String classifyItem(Classifier cls,Instance inst) {
		
		String predicted = null;
		
		double pred;
		try {
			pred = cls.classifyInstance(inst);
			predicted = inst.classAttribute().value((int) pred);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return predicted;
	}
	
	/**
	 * According to the verification results array that came from the classifiers verification, 
	 * computes the majority of the results and returns it.
	 * @param String[] predictions the array of the verification results
	 * @return String predicted the majority of the results ("fake" or "real" value)
	 */
	public static String getMajorityPred(String[] predictions) {
		
		Map<String,Integer> map = new HashMap<String,Integer>();
		for (String pred:predictions) {
			if (map.get(pred)==null) map.put(pred, 1);
			else map.put(pred, map.get(pred)+1);
		}
		
		//System.out.println(map);
		if (map.get("fake") == null) return "real";
		if (map.get("real") == null) return "fake";
		
		if (map.get("fake") > map.get("real")) {
			return "fake";
		}
		else {
			return "real";
		}
		
	}
	
	static int internal = 0;
	/**
	 * Classifies the specified list of testing sets
	 * @param classifiers Classifier[] the array of classifiers
	 * @param testingSets
	 * @return
	 */
	public static String[] classifyItems(Classifier[] classifiers,Instances[] testingSets) {
		
		int sizeTest = testingSets[0].size();
		String[] finalPredictions = new String[sizeTest];
		String[] predicted = new String[classifiers.length];
		int count = 0,countFake=0,countReal=0, fake=0, real=0;
		String actual = "";
		
	
		for (int j=0; j<sizeTest; j++) {
			
			for (int i=0; i<testingSets.length; i++) {
				//System.out.println("instance "+testingSets[i].get(j));
				predicted[i] = classifyItem(classifiers[i], testingSets[i].get(j));
				actual = testingSets[i].classAttribute().value((int) testingSets[i].get(j).classValue());
			}
			
			String predict = getMajorityPred(predicted);
			finalPredictions[j] = predict;
			//System.out.println("actual "+actual);
			//System.out.println("prediction "+predict);
			if (predict.equals(actual)) {
				count++;
			}
			if (actual == "fake" && actual.equals(predict)) {
				countFake++;
			} else if (actual == "real" && actual.equals(predict)) {
				countReal++;
			}
			if (actual == "fake") fake++;
			else real++;
		}
			
		System.out.println("Total items " + sizeTest);
		System.out.println("Items classified correctly:" + count);
		System.out.println("Fake items classified correctly: " + countFake	+ ". Percentage: " + ((double) countFake / fake)* 100);
		System.out.println("Real items classified correctly: " + countReal	+ ". Percentage: " + ((double) countReal / real)* 100);
		System.out.println("Percentage " + ((double) count / sizeTest) * 100);
		
		
		return finalPredictions;
	}
	
	
	
	public static void main(String[] args) throws Exception {
		
		
	}

}
