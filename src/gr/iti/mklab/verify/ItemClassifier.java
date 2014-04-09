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
 * Class to organize the Item classification using Item features 
 * by declaring the attributes,creating the testing set 
 * and perform the classification.
 * @author boididou
 * boididou@iti.gr
 */
public class ItemClassifier {
	
	//define the itemfeatures lists for separate datasets
	public static List<ItemFeatures> train = new ArrayList<ItemFeatures>(); 
	public static List<ItemFeatures> test = new ArrayList<ItemFeatures>();
	static ArrayList<Attribute> fvAttributes = new ArrayList<Attribute>();
	
	public static ArrayList<Attribute> getFvAttributes(){
		return fvAttributes;
	}
	
	/**
	 * @return List of attributes needed for the classification
	 */
	public static List<Attribute> declareAttributes(){
		//declare numeric attributes
		Attribute ItemLength 			= new Attribute("ItemLength");
		Attribute numWords 				= new Attribute("numWords");
		Attribute numQuestionMark 		= new Attribute("numQuestionMark");
		Attribute numExclamationMark 	= new Attribute("numExclamationMark");
		Attribute numUppercaseChars 	= new Attribute("numUppercaseChars");
		Attribute numPosSentiWords 		= new Attribute("numPosSentiWords");
		Attribute numNegSentiWords 		= new Attribute("numNegSentiWords");
		Attribute numMentions 			= new Attribute("numMentions");
		Attribute numHashtags 			= new Attribute("numHashtags");
		Attribute numURLs 				= new Attribute("numURLs");
		Attribute retweetCount 			= new Attribute("retweetCount");
		
		//declare nominal attributes
		List<String> fvnominal1 = new ArrayList<String>(2);
		fvnominal1.add("true");
		fvnominal1.add("false");
		Attribute containsQuestionMark = new Attribute("containsQuestionMark",fvnominal1);
		
		List<String> fvnominal2 = new ArrayList<String>(2);
		fvnominal2.add("true");
		fvnominal2.add("false");
		Attribute containsExclamationMark = new Attribute("containsExclamationMark",fvnominal2);
		
		List<String> fvnominal3 = new ArrayList<String>(2);
		fvnominal3.add("true");
		fvnominal3.add("false");
		Attribute containsHappyEmo = new Attribute("containsHappyEmo",fvnominal3);
		
		List<String> fvnominal4 = new ArrayList<String>(2);
		fvnominal4.add("true");
		fvnominal4.add("false");
		Attribute containsSadEmo = new Attribute("containsSadEmo",fvnominal4);
		
		List<String> fvnominal5 = new ArrayList<String>(2);
		fvnominal5.add("true");
		fvnominal5.add("false");
		Attribute containsFirstOrderPron = new Attribute("containsFirstOrderPron",fvnominal5);
		
		List<String> fvnominal6 = new ArrayList<String>(2);
		fvnominal6.add("true");
		fvnominal6.add("false");
		Attribute containsSecondOrderPron = new Attribute("containsSecondOrderPron",fvnominal6);
		
		List<String> fvnominal7 = new ArrayList<String>(2);
		fvnominal7.add("true");
		fvnominal7.add("false");
		Attribute containsThirdOrderPron = new Attribute("containsThirdOrderPron",fvnominal7);
		
		List<String> fvClass = new ArrayList<String>(2);
		fvClass.add("real");
		fvClass.add("fake");
		Attribute ClassAttribute = new Attribute("theClass",fvClass);
		
		
		//declare the feature vector
		fvAttributes.add(ItemLength);
		fvAttributes.add(numWords);
		fvAttributes.add(containsQuestionMark);
		fvAttributes.add(containsExclamationMark);
		fvAttributes.add(numQuestionMark);
		fvAttributes.add(numExclamationMark);
		fvAttributes.add(containsHappyEmo);
		fvAttributes.add(containsSadEmo);
		fvAttributes.add(containsFirstOrderPron);
		fvAttributes.add(containsSecondOrderPron);
		fvAttributes.add(containsThirdOrderPron);
		fvAttributes.add(numUppercaseChars);
		fvAttributes.add(numPosSentiWords);
		fvAttributes.add(numNegSentiWords);
		fvAttributes.add(numMentions);
		fvAttributes.add(numHashtags);
		fvAttributes.add(numURLs);
		fvAttributes.add(retweetCount);
		fvAttributes.add(ClassAttribute);
		
		return fvAttributes;
	}
	
	/**
	 * @param listItemFeatures the ItemFeatures of the Item
	 * @return the Instance created by the features
	 */
	public static Instance createInstance(ItemFeatures listItemFeatures){
		
		Instance inst = new DenseInstance(fvAttributes.size());
		
		inst.setValue((Attribute)fvAttributes.get(0), listItemFeatures.getItemLength());  
		inst.setValue((Attribute)fvAttributes.get(1), listItemFeatures.getNumWords());
		inst.setValue((Attribute)fvAttributes.get(2), String.valueOf(listItemFeatures.getContainsQuestionMark()));
		inst.setValue((Attribute)fvAttributes.get(3), String.valueOf(listItemFeatures.getContainsExclamationMark()));
		inst.setValue((Attribute)fvAttributes.get(4), listItemFeatures.getnumQuestionMark());
		inst.setValue((Attribute)fvAttributes.get(5), listItemFeatures.getnumExclamationMark());
		inst.setValue((Attribute)fvAttributes.get(6), String.valueOf(listItemFeatures.getContainsHappyEmo()));
		inst.setValue((Attribute)fvAttributes.get(7), String.valueOf(listItemFeatures.getContainsSadEmo()));
		inst.setValue((Attribute)fvAttributes.get(8), String.valueOf(listItemFeatures.getContainsFirstOrderPron()));
		inst.setValue((Attribute)fvAttributes.get(9), String.valueOf(listItemFeatures.getContainsSecondOrderPron()));
		inst.setValue((Attribute)fvAttributes.get(10), String.valueOf(listItemFeatures.getContainsThirdOrderPron()));
		inst.setValue((Attribute)fvAttributes.get(11), listItemFeatures.getNumUppercaseChars());
		inst.setValue((Attribute)fvAttributes.get(12), listItemFeatures.getNumPosSentiWords());
		inst.setValue((Attribute)fvAttributes.get(13), listItemFeatures.getNumNegSentiWords());
		inst.setValue((Attribute)fvAttributes.get(14), listItemFeatures.getNumMentions());
		inst.setValue((Attribute)fvAttributes.get(15), listItemFeatures.getNumHashtags());
		inst.setValue((Attribute)fvAttributes.get(16), listItemFeatures.getNumURLs());
		inst.setValue((Attribute)fvAttributes.get(17), listItemFeatures.getRetweetCount());
		
		return inst;
	}
	
	/**
	 * @param listItemFeatures list of the ItemFeatures computed
	 * @param itemFeaturesAnnot list of the items' annotation details
	 * @return Instances that form the testing set
	 */
	public static Instances createTestingSet(List<ItemFeatures> listItemFeatures,List<ItemFeaturesAnnotation> listFeaturesAnnot){
		
		Integer index=0;
		// Create an empty training set
		Instances isTestSet = new Instances("Rel", fvAttributes, listItemFeatures.size());           
		// Set class index
		isTestSet.setClassIndex(fvAttributes.size()-1);
		
		for (int i=0;i<listItemFeatures.size();i++){
			Instance inst = createInstance(listItemFeatures.get(i));
						
			for (int j=0;j<listFeaturesAnnot.size();j++){
				if (listItemFeatures.get(i).getId().equals(listFeaturesAnnot.get(j).getId())){
					index = j;
				}
			}
			inst.setValue((Attribute)fvAttributes.get(fvAttributes.size()-1), listFeaturesAnnot.get(index).getReliability());
			
			isTestSet.add(inst);
		}
		return isTestSet;	
		
	}
	
	/**
	 * @param listItemFeatures the ItemFeatures computed for the MediaItem
	 * @param listFeaturesAnnot the MediaItem's annotation details
	 * @return Instances that form the testing set
	 */
	public static Instances createTestingSet(ItemFeatures listItemFeatures,ItemFeaturesAnnotation listFeaturesAnnot){
		
		// Create an empty training set
		Instances isTestSet = new Instances("UserFeatureClassification", fvAttributes, 1);           
		// Set class index
		isTestSet.setClassIndex(fvAttributes.size()-1);
	
		Instance inst = createInstance(listItemFeatures);
			
		inst.setValue((Attribute)fvAttributes.get(fvAttributes.size()-1), listFeaturesAnnot.getReliability());
		
		isTestSet.add(inst);
		
		return isTestSet;	
		
	}
	
	/**
	 * @param isTestSet Instances of the test set
	 * @return Boolean table of reliability values of the test set instances 
	 * @throws Exception file
	 */
	public static boolean[] classifyItems(Instances isTestSet) throws Exception{
		
		//flags variable for the values of the verification
		boolean[] flags = new boolean[isTestSet.size()];
		//define the classifier and set the appropriate model file 
		SerializedClassifier classifier = new SerializedClassifier();
		classifier.setModelFile(new File(Vars.MODEL_PATH_ITEM));

		for (int i = 0; i < isTestSet.numInstances(); i++) {
			
			double pred = classifier.classifyInstance(isTestSet.instance(i));
			
			//String actual = isTestSet.classAttribute().value((int)isTestSet.instance(i).classValue());
			String predicted = isTestSet.classAttribute().value((int) pred);
			
			//assign appropriate value to the flag
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
		classifier.setModelFile(new File(Vars.MODEL_PATH_ITEM));
		
		for (int i = 0; i < isTestSet.numInstances(); i++) {
			double[] probabilityDistribution = classifier.distributionForInstance(isTestSet.instance(i));
			probabilities[i] = probabilityDistribution[1];
		}
		return probabilities;
	}
	

}
