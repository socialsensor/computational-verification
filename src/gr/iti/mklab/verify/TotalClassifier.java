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
 * Class to organize the Total classification using Item and User features 
 * by declaring the attributes,creating the testing set 
 * and perform the classification.
 * @author boididou
 */
public class TotalClassifier {
	
	public static Instances isTrainingSet,isTestingSet;
	static ArrayList<Attribute> fvAttributes  = new ArrayList<Attribute>();
	
	//define the itemfeatures lists for separate datasets
	public static List<TotalFeatures> train = new ArrayList<TotalFeatures>(); 
	public static List<TotalFeatures> test  = new ArrayList<TotalFeatures>();
	
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
			
			List<Attribute> fvAttributes2 = new ArrayList<Attribute>(7);
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
		
		System.out.println("Attributes ");
		for (Attribute att:TotalClassifier.fvAttributes){
			System.out.print(att.name()+",");
		}
		System.out.println();
		
		for (int i=0;i<list.size();i++){
			
			Instance inst  = createInstance(list.get(i));

			for (int j=0;j<itemFeaturesAnnot.size();j++){
				if ((list.get(i).id).equals(itemFeaturesAnnot.get(j).id)){
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
			System.out.println("here 1");
		}
		else{
			fvAttributes = ItemClassifier.getFvAttributes();
			System.out.println("here 2");
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
		
		
		boolean[] flags = new boolean[isTestSet.size()];
		SerializedClassifier classifier = new SerializedClassifier();
		classifier.setModelFile(new File(Vars.MODEL_PATH_TOTAL));
		
		
		for (int i = 0; i < isTestSet.numInstances(); i++) {
			double pred = classifier.classifyInstance(isTestSet.instance(i));
			
			//String actual = isTestSet.classAttribute().value((int)isTestSet.instance(i).classValue());
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
		classifier.setModelFile(new File(Vars.MODEL_PATH_TOTAL));
		
		for (int i = 0; i < isTestSet.numInstances(); i++) {
			double[] probabilityDistribution = classifier.distributionForInstance(isTestSet.instance(i));
			probabilities[i] = probabilityDistribution[1];
		}
		
		return probabilities;
	}
	
}
