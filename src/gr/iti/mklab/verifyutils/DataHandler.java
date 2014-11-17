package gr.iti.mklab.verifyutils;

import gr.iti.mklab.verify.ItemClassifier;
import gr.iti.mklab.verify.UserClassifier;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import weka.classifiers.Classifier;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;
import weka.filters.unsupervised.attribute.Standardize;

/**
 * The DataHandler class provides solutions for handling the data used on the
 * training and testing procedure. It provides functions for normalizing data,
 * stardizing and handling the missing values on the dataset.
 * 
 * @author Boididou Christina
 * @date 11.07.14
 */
public class DataHandler {

	private static DataHandler dhInstance ;

	public static DataHandler getInstance() {
		if (dhInstance == null) {
			dhInstance = new DataHandler();
		}
		return dhInstance;
	}
	
	static Normalize normFilter = new Normalize();
	static Normalize normFilterUser = new Normalize();

	static FilteredClassifier model = new FilteredClassifier();
	static FilteredClassifier model2 = new FilteredClassifier();
	static FilteredClassifier model3 = new FilteredClassifier();
	static FilteredClassifier model4 = new FilteredClassifier();
	static FilteredClassifier model5 = new FilteredClassifier();
	static FilteredClassifier model6 = new FilteredClassifier();
	static FilteredClassifier usermodel = new FilteredClassifier();
	static FilteredClassifier usermodel2 = new FilteredClassifier();
	static FilteredClassifier usermodel3 = new FilteredClassifier();

	public static void initializeModels() {
		
		model = new FilteredClassifier();
		model2 = new FilteredClassifier();
		model3 = new FilteredClassifier();
		model4 = new FilteredClassifier();
		model5 = new FilteredClassifier();
		model6 = new FilteredClassifier();
		usermodel = new FilteredClassifier();
		usermodel2 = new FilteredClassifier();
		usermodel3 = new FilteredClassifier();
	}
	
	/**
	 * Normalizes the given data
	 * 
	 * @param isTrainingSet
	 *            the Instances to be normalized
	 * @param fvAttributes
	 *            List<Attribute> the list of attributes of the current dataset
	 * @return the normalized Instances
	 */
	public Normalize createNormalizationFilter(Instances isTrainingSet) {


		// set the Normalize object
		Normalize norm = new Normalize();
		try {

			// set the parameters for norm object
			norm.setInputFormat(isTrainingSet);
			
			// set and print the normalization options
			System.out.println();
			String[] options = { "-S", "2.0", "-T", "-1.0" };
			norm.setOptions(options);
			//System.out.print("Normalization options:\t");
			/*for (int i = 0; i < norm.getOptions().length; i++) {
				System.out.print(norm.getOptions()[i] + "\t");
			}*/
			
			
			// normalized instances calculated
			/*isTrainingSet_norm = Filter.useFilter(isTrainingSet, norm);
			isTrainingSet_norm.setClassIndex(fvAttributes.size() - 1);*/

		} catch (Exception e) {
			System.out.println("Data Normalization filter cannot be created!");
			e.printStackTrace();
		}

		// System.out.println("-----TRAINING SET-------");
		// System.out.println(isTrainingSet_norm);

		return norm;
	}

	public Instances normalizeData(Instances dataset, int classIndex, Normalize normFilter){
		
		Instances dataset_norm = null;
		try {
			dataset_norm = Filter.useFilter(dataset, normFilter);
			dataset_norm.setClassIndex(classIndex);	
		} catch (Exception e) {
			System.out.println("Data Normalization cannot be performed! Please check your data!");
			e.printStackTrace();
		}
		
		return dataset_norm;
	}
	
	/**
	 * Standardizes the given data
	 * 
	 * @param isTrainingSet
	 *            the Instances to be standardized
	 * @return the standardized Instances
	 */
	public Standardize createStandardizationFilter(Instances isTrainingSet) {

		//Instances isTrainingSet_stand = null;

		Standardize filter = new Standardize();
		try {
			filter.setInputFormat(isTrainingSet); // initializing the filter
													// once with training set
			//isTrainingSet_stand = Filter.useFilter(isTrainingSet, filter);

		} catch (Exception e) {
			
			e.printStackTrace();
		}

		return filter;
	}

	public Instances standardizeData(Instances isTrainingSet, int classIndex,  Standardize filter) {
		Instances isTrainingSet_stand = null;
		
		try {
			isTrainingSet_stand = Filter.useFilter(isTrainingSet, filter);
			isTrainingSet_stand.setClassIndex(classIndex);
		} catch (Exception e) {
			System.out.println("Data Standardization cannot be performed! Please check your data!");
			e.printStackTrace();
		}
		
		return isTrainingSet_stand;
	}
	
	public Instances replaceMissingValues(Instances isTrainingSet,ReplaceMissingValues replace) {

		
		Instances training_data_filter = null;

		try {
			//replace.setInputFormat(isTrainingSet);
			training_data_filter = Filter.useFilter(isTrainingSet, replace);
		} catch (Exception e) {
			System.out
					.println("Cannot replace the missing values! Please check your data!");
			e.printStackTrace();
		}

		// write training set to file
		BufferedWriter bw = null;
		String filePath = "trainingset_replaced.txt";
		for (int i = 0; i < training_data_filter.size(); i++) {
			try {

				bw = new BufferedWriter(new FileWriter(filePath, true));
				// if file doesn't exist, then create it
				bw.append(training_data_filter.get(i).toString());
				bw.newLine();
				bw.flush();
				bw.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return training_data_filter;

	}
	
	public Instances applyRegressionModel(Instances data, ArrayList<Attribute> fvAttributes, Classifier model) throws Exception{
				
		//System.out.println("======Apply regression model======");
		
		//declare the new training set
		int size = data.size();
		
		Instances newData = new Instances("Rel", fvAttributes, size);
		
		//change the class index
		//isTrainingSet.setClassIndex(index);
		int index = data.classIndex();
		//System.out.println(model);
		
		int counter = 0;
		//classify the instances
		for (int i = 0; i < data.numInstances(); i++) {
			
			Instance inst = data.get(i);
			
			if (inst.isMissing(index)){
				//System.out.println(inst.stringValue(0));
				double value = model.classifyInstance(inst);
				//System.out.println("Instance ("+inst+"): "+wotTrustValue);
				Instance newinst =  new DenseInstance(fvAttributes.size());
				for (int j=0; j<inst.numAttributes(); j++){
					newinst.setValue(j, inst.value(j));
				}
				newinst.setValue(index, value);
				
				newData.add(newinst);
				counter++;
			}
			else{
				newData.add(inst);
			}
		}
		
		//System.out.println(newData.size());
		//System.out.println("counter "+counter);
		newData.setClassIndex(fvAttributes.size()-1);
		data.setClassIndex(fvAttributes.size()-1);
		//System.out.println(isTrainingSet);
		
		return newData;
	}

	
	/**
	 * Applies linear regression to the missing values of the training set in
	 * order to predict their values and normalization to make the values fall
	 * into the range [0,1]. It is the case of the Item type Instances.
	 * 
	 * @param trainingSet
	 * @return Instances the transformed training set
	 * @throws Exception
	 */
	public Instances getTransformedTraining(Instances trainingSet)
			throws Exception {

		initializeModels();
		
		Remove rm = new Remove();
		rm.setAttributeIndices("1");

		ArrayList<Attribute> fvAttributes = ItemClassifier.getFvAttributes();

		// REGRESSION
		// wotTrust
		trainingSet.setClass(fvAttributes.get(22));
		LinearRegression lr = new LinearRegression();
		Instances training_regr = null;
		model.setFilter(rm);
		model.setClassifier(lr);

		try {
			model.buildClassifier(trainingSet);
			training_regr = DataHandler.getInstance().applyRegressionModel(
					trainingSet, fvAttributes, model);
		} catch (Exception e) {
			training_regr = trainingSet;
			//System.out.println("not enough training instances. Linear Regression not performed!");
		}

		// readability
		training_regr.setClass(fvAttributes.get(25));
		LinearRegression lr2 = new LinearRegression();
		Instances training_regr2 = null;

		model2.setFilter(rm);
		model2.setClassifier(lr2);
		try {
			model2.buildClassifier(training_regr);
			training_regr2 = DataHandler.getInstance().applyRegressionModel(
					training_regr, fvAttributes, model2);
		} catch (Exception e) {
			training_regr2 = training_regr;
			//System.out.println("not enough training instances. Linear Regression not performed!");
		}

		training_regr2.setClass(fvAttributes.get(19));
		LinearRegression lr3 = new LinearRegression();
		Instances training_regr3 = null;

		model3.setFilter(rm);
		model3.setClassifier(lr3);
		try {
			model3.buildClassifier(training_regr2);
			training_regr3 = DataHandler.getInstance().applyRegressionModel(
					training_regr2, fvAttributes, model3);
		} catch (Exception e) {
			training_regr3 = training_regr2;
			//System.out.println("not enough training instances. Linear Regression not performed!");
		}

		training_regr3.setClass(fvAttributes.get(13));
		LinearRegression lr4 = new LinearRegression();
		Instances training_regr4 = null;
		model4.setFilter(rm);
		model4.setClassifier(lr4);
		try {
			model4.buildClassifier(training_regr3);
			training_regr4 = DataHandler.getInstance().applyRegressionModel(
					training_regr3, fvAttributes, model4);
		} catch (Exception e) {
			training_regr4 = training_regr3;
			//System.out.println("not enough training instances. Linear Regression not performed!");
		}

		training_regr4.setClass(fvAttributes.get(14));
		LinearRegression lr5 = new LinearRegression();
		Instances training_regr5 = null;
		model5.setFilter(rm);
		model5.setClassifier(lr5);

		try {
			model5.buildClassifier(training_regr4);
			training_regr5 = DataHandler.getInstance().applyRegressionModel(
					training_regr4, fvAttributes, model5);
		} catch (Exception e) {
			training_regr5 = training_regr4;
			//System.out.println("not enough training instances. Linear Regression not performed!");
		}

		training_regr5.setClass(fvAttributes.get(6));
		LinearRegression lr6 = new LinearRegression();
		Instances training_regr6 = null;
		model6.setFilter(rm);
		model6.setClassifier(lr6);
		try {
			model6.buildClassifier(training_regr5);
			training_regr6 = DataHandler.getInstance().applyRegressionModel(
					training_regr5, fvAttributes, model6);
		} catch (Exception e) {
			training_regr6 = training_regr5;
		}

		// normalization part
		String[] options = { "-S", "2.0", "-T", "-1.0" };
		normFilter.setOptions(options);
		normFilter.setInputFormat(training_regr5);

		Instances trainingSet_normed = DataHandler.getInstance().normalizeData(
				training_regr6, fvAttributes.size() - 1, normFilter);

		return trainingSet_normed;

	}


	/**
	 * Applies linear regression to the missing values of the training set in
	 * order to predict their values and normalization to make the values fall
	 * into the range [0,1]. It is the case of the User type Instances.
	 * 
	 * @param trainingSet
	 * @return Instances the transformed training set
	 * @throws Exception
	 */
	public Instances getTransformedTrainingUser(Instances trainingSet) {

		initializeModels();
		
		ArrayList<Attribute> fvAttributes = UserClassifier.getFvAttributes();

		// remove filter in order to remove the id attribute
		Remove rm = new Remove();
		rm.setAttributeIndices("1");

		// regression
		trainingSet.setClass(fvAttributes.get(11));
		LinearRegression lr = new LinearRegression();
		Instances training_regr = null;
		usermodel.setFilter(rm);
		usermodel.setClassifier(lr);

		try {
			usermodel.buildClassifier(trainingSet);
			training_regr = DataHandler.getInstance().applyRegressionModel(
					trainingSet, fvAttributes, usermodel);
		} catch (Exception e) {
			training_regr = trainingSet;
			//System.out.println("not enough training instances. Linear Regression not performed!");
		}

		training_regr.setClass(fvAttributes.get(13));
		LinearRegression lr2 = new LinearRegression();
		Instances training_regr2 = null;
		usermodel2.setFilter(rm);
		usermodel2.setClassifier(lr2);
		try {
			usermodel2.buildClassifier(training_regr);
			training_regr2 = DataHandler.getInstance().applyRegressionModel(
					training_regr, fvAttributes, usermodel2);
		} catch (Exception e) {
			training_regr2 = training_regr;
			//System.out.println("not enough training instances. Linear Regression not performed!");
		}

		training_regr2.setClass(fvAttributes.get(16));
		LinearRegression lr3 = new LinearRegression();
		Instances training_regr3 = null;
		usermodel3.setFilter(rm);
		usermodel3.setClassifier(lr3);
		try {
			usermodel3.buildClassifier(training_regr2);
			training_regr3 = DataHandler.getInstance().applyRegressionModel(
					training_regr2, fvAttributes, usermodel3);
		} catch (Exception e) {
			training_regr3 = training_regr2;
			//System.out.println("not enough training instances. Linear Regression not performed!");
		}

		// normalization
		normFilterUser = DataHandler.getInstance().createNormalizationFilter(
				training_regr3);
		Instances trainingSet_normed = DataHandler.getInstance().normalizeData(
				training_regr3, fvAttributes.size() - 1, normFilterUser);

		return trainingSet_normed;
	}

	/**
	 * Applies Linear Regression and normalization to the testing set according
	 * to the models and filters created before by the training set. It is the
	 * case of the Item type Instances.
	 * 
	 * @param testing
	 *            the Instances to be transformed
	 * @return the transformed testing set
	 * @throws Exception
	 */
	public Instances getTransformedTesting(Instances testing)
			throws Exception {

		ArrayList<Attribute> fvAttributes = ItemClassifier.getFvAttributes();
		Instances testing_regr = null, testing_regr2 = null, testing_regr3 = null, testing_regr4 = null, testing_regr5 = null, testing_regr6 = null;

		// regression
		if (!model.toString().contains("No model built yet.")) {
			testing.setClass(fvAttributes.get(22));
			testing_regr = DataHandler.getInstance().applyRegressionModel(
					testing, fvAttributes, model);
		} else {
			testing_regr = testing;
		}

		if (!model2.toString().contains("No model built yet.")) {
			testing_regr.setClass(fvAttributes.get(25));
			testing_regr2 = DataHandler.getInstance().applyRegressionModel(
					testing_regr, fvAttributes, model2);
		} else {
			testing_regr2 = testing_regr;
		}

		if (!model3.toString().contains("No model built yet.")) {
			testing_regr2.setClass(fvAttributes.get(19));
			testing_regr3 = DataHandler.getInstance().applyRegressionModel(
					testing_regr2, fvAttributes, model3);
		} else {
			testing_regr3 = testing_regr2;
		}

		if (!model4.toString().contains("No model built yet.")) {
			testing_regr3.setClass(fvAttributes.get(13));
			testing_regr4 = DataHandler.getInstance().applyRegressionModel(
					testing_regr3, fvAttributes, model4);
		} else {
			testing_regr4 = testing_regr3;
		}

		if (!model5.toString().contains("No model built yet.")) {
			testing_regr4.setClass(fvAttributes.get(14));
			testing_regr5 = DataHandler.getInstance().applyRegressionModel(
					testing_regr4, fvAttributes, model5);
		} else {
			testing_regr5 = testing_regr4;
		}

		if (!model6.toString().contains("No model built yet.")) {
			testing_regr5.setClass(fvAttributes.get(6));
			testing_regr6 = DataHandler.getInstance().applyRegressionModel(
					testing_regr5, fvAttributes, model6);
		} else {
			testing_regr6 = testing_regr5;
		}

		// normalization
		Instances testingSet_normed = DataHandler.getInstance().normalizeData(
				testing_regr6, fvAttributes.size() - 1, normFilter);
		// testingSet_normed = getTrimmedInstances(testingSet_normed);

		return testingSet_normed;
	}

	/**
	 * Applies Linear Regression and normalization to the testing set according
	 * to the models and filters created before by the training set. It is the
	 * case of the User type Instances.
	 * 
	 * @param testing
	 *            the Instances to be transformed
	 * @return the transformed testing set
	 * @throws Exception
	 */
	public Instances getTransformedTestingUser(Instances testing)
			throws Exception {

		ArrayList<Attribute> fvAttributes = UserClassifier.getFvAttributes();
		Instances testing_regr = null, testing_regr2 = null, testing_regr3 = null;

		testing_regr3 = testing;

		if (!usermodel.toString().contains("No model built yet.")) {
			testing.setClass(fvAttributes.get(11));
			testing_regr = DataHandler.getInstance().applyRegressionModel(
					testing, fvAttributes, usermodel);
		} else {
			testing_regr = testing;
		}
		if (!usermodel2.toString().contains("No model built yet.")) {
			testing_regr.setClass(fvAttributes.get(13));
			testing_regr2 = DataHandler.getInstance().applyRegressionModel(
					testing_regr, fvAttributes, usermodel2);
		} else {
			testing_regr2 = testing_regr;
		}
		if (!usermodel3.toString().contains("No model built yet.")) {
			testing_regr2.setClass(fvAttributes.get(16));
			testing_regr3 = DataHandler.getInstance().applyRegressionModel(
					testing_regr2, fvAttributes, usermodel3);
		} else {
			testing_regr3 = testing_regr2;
		}

		if (!usermodel.toString().contains("No model built yet.")) {
			testing.setClass(fvAttributes.get(1));
			testing_regr = DataHandler.getInstance().applyRegressionModel(
					testing, fvAttributes, usermodel);
		} else {
			testing_regr = testing;
		}
		if (!usermodel2.toString().contains("No model built yet.")) {
			testing_regr.setClass(fvAttributes.get(4));
			testing_regr2 = DataHandler.getInstance().applyRegressionModel(
					testing_regr, fvAttributes, usermodel2);
		} else {
			testing_regr2 = testing_regr;
		}

		// normalization
		Instances testingSet_normed = DataHandler.getInstance().normalizeData(
				testing_regr3, fvAttributes.size() - 1, normFilterUser);
		// testingSet_normed = getTrimmedInstances(testingSet_normed);

		return testingSet_normed;
	}
}
