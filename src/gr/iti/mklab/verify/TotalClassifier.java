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
	static ArrayList<Attribute> fvAttributes  = new ArrayList<Attribute>(19);
	
	//define the itemfeatures lists for separate datasets
	public static List<TotalFeatures> train = new ArrayList<TotalFeatures>(); 
	public static List<TotalFeatures> test  = new ArrayList<TotalFeatures>();
	
	/**
	 * @param list of TotalFeatures computed
	 * @param itemFeaturesAnnot list of the items' annotation details
	 * @return Instances that form the testing set
	 * @throws Exception
	 */
	public static Instances createTestingSet(List<TotalFeatures> list, List<ItemFeaturesAnnotation> itemFeaturesAnnot) throws Exception{
		
		Integer index=0;
		
		List<Attribute> fvAttributes2 = new ArrayList<Attribute>(7);
		fvAttributes  = (ArrayList<Attribute>) ItemClassifier.declareAttributes();
		fvAttributes2 = UserClassifier.declareAttributes();
		fvAttributes.addAll(fvAttributes2);
		fvAttributes.remove(18);
		
		Instances isTestingSet = new Instances("Rel", fvAttributes, list.size());  
		isTestingSet.setClassIndex(fvAttributes.size()-1);
		
		System.out.println("Attributes ");
		for (Attribute att:TotalClassifier.fvAttributes){
			System.out.print(att.name()+",");
		}
		System.out.println();
		
		for (int i=0;i<list.size();i++){
			Instance inst  = new DenseInstance(fvAttributes.size());
			inst.setValue((Attribute)fvAttributes.get(0), list.get(i).itemLength);  
			inst.setValue((Attribute)fvAttributes.get(1), list.get(i).numWords);
			inst.setValue((Attribute)fvAttributes.get(2), list.get(i).containsQuestionMark.toString());
			inst.setValue((Attribute)fvAttributes.get(3), list.get(i).containsExclamationMark.toString());
			inst.setValue((Attribute)fvAttributes.get(4), list.get(i).numQuestionMark);
			inst.setValue((Attribute)fvAttributes.get(5), list.get(i).numExclamationMark);
			inst.setValue((Attribute)fvAttributes.get(6), list.get(i).containsHappyEmo.toString());
			inst.setValue((Attribute)fvAttributes.get(7), list.get(i).containsSadEmo.toString());
			inst.setValue((Attribute)fvAttributes.get(8), list.get(i).containsFirstOrderPron.toString());
			inst.setValue((Attribute)fvAttributes.get(9), list.get(i).containsSecondOrderPron.toString());
			inst.setValue((Attribute)fvAttributes.get(10), list.get(i).containsThirdOrderPron.toString());
			inst.setValue((Attribute)fvAttributes.get(11), list.get(i).numUppercaseChars);
			inst.setValue((Attribute)fvAttributes.get(12), list.get(i).numPosSentiWords);
			inst.setValue((Attribute)fvAttributes.get(13), list.get(i).numNegSentiWords);
			inst.setValue((Attribute)fvAttributes.get(14), list.get(i).numMentions);
			inst.setValue((Attribute)fvAttributes.get(15), list.get(i).numHashtags);
			inst.setValue((Attribute)fvAttributes.get(16), list.get(i).numURLs);
			inst.setValue((Attribute)fvAttributes.get(17), list.get(i).retweetCount);
			inst.setValue((Attribute)fvAttributes.get(18), list.get(i).numFriends);
			inst.setValue((Attribute)fvAttributes.get(19), list.get(i).numFollowers);
			inst.setValue((Attribute)fvAttributes.get(20), list.get(i).FolFrieRatio);
			inst.setValue((Attribute)fvAttributes.get(21), list.get(i).timesListed);
			inst.setValue((Attribute)fvAttributes.get(22), list.get(i).hasURL.toString());
			inst.setValue((Attribute)fvAttributes.get(23), list.get(i).isVerified.toString());
			inst.setValue((Attribute)fvAttributes.get(24), list.get(i).numTweets);
			
			for (int j=0;j<itemFeaturesAnnot.size();j++){
				if ((list.get(i).id).equals(itemFeaturesAnnot.get(j).id)){
					index = j;
					//System.out.println(list.get(i).id+" "+list.get(i).itemLength);
				}
			}
			
			inst.setValue((Attribute)fvAttributes.get(fvAttributes.size()-1), itemFeaturesAnnot.get(index).getReliability());
			isTestingSet.add(inst);
			

		}
		
		/*System.out.println("-----TESTING SET-------");
		System.out.println(isTestingSet);*/
		
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
	
}
