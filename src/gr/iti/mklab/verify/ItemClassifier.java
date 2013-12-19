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
	static ArrayList<Attribute> fvAttributes = new ArrayList<Attribute>(19);
	
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
	 * @param listTest list of the ItemFeatures computed
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
			
			Instance inst  = new DenseInstance(fvAttributes.size());
			
			inst.setValue((Attribute)fvAttributes.get(0), listItemFeatures.get(i).itemLength);  
			inst.setValue((Attribute)fvAttributes.get(1), listItemFeatures.get(i).numWords);
			inst.setValue((Attribute)fvAttributes.get(2), listItemFeatures.get(i).containsQuestionMark.toString());
			inst.setValue((Attribute)fvAttributes.get(3), listItemFeatures.get(i).containsExclamationMark.toString());
			inst.setValue((Attribute)fvAttributes.get(4), listItemFeatures.get(i).numQuestionMark);
			inst.setValue((Attribute)fvAttributes.get(5), listItemFeatures.get(i).numExclamationMark);
			inst.setValue((Attribute)fvAttributes.get(6), listItemFeatures.get(i).containsHappyEmo.toString());
			inst.setValue((Attribute)fvAttributes.get(7), listItemFeatures.get(i).containsSadEmo.toString());
			inst.setValue((Attribute)fvAttributes.get(8), listItemFeatures.get(i).containsFirstOrderPron.toString());
			inst.setValue((Attribute)fvAttributes.get(9), listItemFeatures.get(i).containsSecondOrderPron.toString());
			inst.setValue((Attribute)fvAttributes.get(10), listItemFeatures.get(i).containsThirdOrderPron.toString());
			inst.setValue((Attribute)fvAttributes.get(11), listItemFeatures.get(i).numUppercaseChars);
			inst.setValue((Attribute)fvAttributes.get(12), listItemFeatures.get(i).numPosSentiWords);
			inst.setValue((Attribute)fvAttributes.get(13), listItemFeatures.get(i).numNegSentiWords);
			inst.setValue((Attribute)fvAttributes.get(14), listItemFeatures.get(i).numMentions);
			inst.setValue((Attribute)fvAttributes.get(15), listItemFeatures.get(i).numHashtags);
			inst.setValue((Attribute)fvAttributes.get(16), listItemFeatures.get(i).numURLs);
			inst.setValue((Attribute)fvAttributes.get(17), listItemFeatures.get(i).retweetCount);
			
			for (int j=0;j<listFeaturesAnnot.size();j++){
				if (listItemFeatures.get(i).id.equals(listFeaturesAnnot.get(j).id)){
					index = j;
				}
			}
			inst.setValue((Attribute)fvAttributes.get(fvAttributes.size()-1), listFeaturesAnnot.get(index).getReliability());
			
			isTestSet.add(inst);
		}
		return isTestSet;	
		
	}
	
	/**
	 * @param isTestSet Instances of the test set
	 * @return Boolean table of reliability values of the test set instances 
	 * @throws Exception
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
	
	
	

}
