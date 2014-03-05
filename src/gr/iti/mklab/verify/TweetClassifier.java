package gr.iti.mklab.verify;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.Instances;
import context.arch.discoverer.query.ClassifierWrapper;
import eu.socialsensor.framework.client.dao.impl.MediaItemDAOImpl;
import eu.socialsensor.framework.client.mongo.MongoHandler;
import eu.socialsensor.framework.common.domain.MediaItem;

/**
 * General main class used to perform the Item classification based on specified features and present the result 
 * @author boididou
 */
public class TweetClassifier {
	
	/**
	 * Function that extracts MediaItem features for a list of MediaItems, classifies them
	 * and computes the tree condition path of the classification.
	 * @param listItem the list of MediaItems need to be classified
	 * @return ImageVerification list of the verification results(flag and description) of the classification
	 */
	public static List<ImageVerificationResult> tweetClassificationbyMediaItem(List<MediaItem> listItem){
		
		Instances testSet;   
			
		boolean[] flagsReliability;
		double[] probabilities;
		List<ImageVerificationResult> verifyResults = new ArrayList<ImageVerificationResult>();
		
		//create ItemFeatures list to keep the features extracted
		List<ItemFeatures> listFeatures = new ArrayList<ItemFeatures>();
		listFeatures = ItemFeaturesExtractor.featureExtractionMedia(listItem);
		
		//annotate items
		List<ItemFeaturesAnnotation> itemFeaturesAnnot = new ArrayList<ItemFeaturesAnnotation>();
		for (int i=0;i<listFeatures.size();i++){
			ItemFeaturesAnnotation itemAnnot = new ItemFeaturesAnnotation();
			itemAnnot.id = listFeatures.get(i).id;
			itemAnnot.reliability = "real";
			itemFeaturesAnnot.add(itemAnnot);
		}

		//call ItemClassifier method to declare the attributes used in classification and print them
		ItemClassifier.declareAttributes();
		System.out.println("Attributes ");
		for (Attribute att:ItemClassifier.fvAttributes){
			System.out.print(att.name()+",");
		}
		
		//create testing set 
		testSet = ItemClassifier.createTestingSet(listFeatures,itemFeaturesAnnot);
		
		for (int i=0;i<listItem.size();i++){
			ImageVerificationResult verifyResult = new ImageVerificationResult();
			verifyResult.id = listItem.get(i).getId();
			verifyResults.add(verifyResult);
		}
		System.out.println();
		//call methods to classify items and compute the J48 tree path
		try {
			flagsReliability = ItemClassifier.classifyItems(testSet);	
			probabilities	 = ItemClassifier.findProbDistribution(testSet);
			
			J48 cModel = (J48) ClassifierWrapper.loadClassifier(Vars.MODEL_PATH_ITEM);
			
			//check if features computed for this item;if yes find the tree path, otherwise do nothing.
			//this way we count also items for which features could not be computed 
			for (int i=0;i<listItem.size();i++){
				for (int j=0;j<listFeatures.size();j++){
					if (listItem.get(i).getId().equals(listFeatures.get(j).id)){
						
						verifyResults.get(i).flag = flagsReliability[j];
						verifyResults.get(i).fakePercentage = probabilities[j];
						String result = J48Parser.findTreePath(testSet.get(j),ItemClassifier.fvAttributes,cModel);
						verifyResults.get(i).explanation = result;
						
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return verifyResults;
	}
	
	/**
	 * Function that extracts MediaItem features for a  MediaItem, classifies it
	 * and computes the tree condition path of the classification.
	 * @param item the MediaItem needs to be classified
	 * @return ImageVerificationResult the result (flag and description) of the classification
	 */
	public static ImageVerificationResult tweetClassificationbyMediaItem(MediaItem item){
		
		Instances testSet;   
			
		boolean[] flagsReliability;
		double[] probabilities;
		
		//create ItemFeatures list to keep the features extracted
		ItemFeatures listFeatures;
		listFeatures = ItemFeaturesExtractor.featureExtractionMedia(item);
		
		//annotate items
		ItemFeaturesAnnotation itemAnnot = new ItemFeaturesAnnotation();
		itemAnnot.id = listFeatures.id;
		itemAnnot.reliability = "real";
		

		//call ItemClassifier method to declare the attributes used in classification and print them
		ItemClassifier.declareAttributes();
		System.out.println("Attributes ");
		for (Attribute att:ItemClassifier.fvAttributes){
			System.out.print(att.name()+",");
		}
		
		//create testing set 
		testSet = ItemClassifier.createTestingSet(listFeatures,itemAnnot);
		
		
		ImageVerificationResult verifyResult = new ImageVerificationResult();
		verifyResult.id = item.getId();
		//verifyResults.add(verifyResult);
		
		System.out.println();
		//call methods to classify items and compute the J48 tree path
		try {
			flagsReliability = ItemClassifier.classifyItems(testSet);	
			probabilities	 = ItemClassifier.findProbDistribution(testSet);
			
			J48 cModel = (J48) ClassifierWrapper.loadClassifier(Vars.MODEL_PATH_ITEM);
			
			//check if features computed for this item;if yes find the tree path, otherwise do nothing.
			//this way we count also items for which features could not be computed 
			
			if (item.getId().equals(listFeatures.id)){
				
				verifyResult.flag = flagsReliability[0];
				verifyResult.fakePercentage = probabilities[0];
				String result = J48Parser.findTreePath(testSet.get(0),ItemClassifier.fvAttributes,cModel);
				verifyResult.explanation = result;
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return verifyResult;
	}
	
	
	/**
	 * Function that calls methods to extract User features, classifies the MediaItems
	 * and computes the tree condition path of the classification.
	 * @param listMediaItems the list of MediaItems need to be classified by user features
	 * @return ImageVerification list of the verification results(flag and description) of the classification
	 * @throws Exception
	 */
	public static List<ImageVerificationResult> tweetClassificationbyUserMedia(List<MediaItem> listMediaItems) throws Exception{
		
		Instances testSet;   
		
		boolean[] flagsReliability;
		List<ImageVerificationResult> verifyResults = new ArrayList<ImageVerificationResult>();
		
		//create UserFeatures list to keep the features extracted
		List<UserFeatures> listFeatures = new ArrayList<UserFeatures>();
		
		listFeatures = UserFeaturesExtractor.userFeatureExtractionMedia(listMediaItems);
		
		//annotate items
		List<UserFeaturesAnnotation> userFeaturesAnnot = new ArrayList<UserFeaturesAnnotation>();
		for (int i=0;i<listFeatures.size();i++){
			UserFeaturesAnnotation itemAnnot = new UserFeaturesAnnotation();
			itemAnnot.username = listFeatures.get(i).id;
			itemAnnot.reliability = "real";
			userFeaturesAnnot.add(itemAnnot);
		}
		
		//call UserClassifier method to declare the attributes used in classification and print them
		UserClassifier.declareAttributes();
		System.out.println("Attributes ");
		for (Attribute att:UserClassifier.fvAttributes){
			System.out.print(att.name()+",");
		}
		System.out.println();
		//create testing set 
		testSet = UserClassifier.createTestingSet(listFeatures,userFeaturesAnnot);
		for (int i=0;i<listMediaItems.size();i++){
			ImageVerificationResult verifyResult = new ImageVerificationResult();
			verifyResult.id = listMediaItems.get(i).getId();
			verifyResults.add(verifyResult);
		}
		
		//call methods to classify items and compute the J48 tree path
		try {
			flagsReliability = UserClassifier.classifyItems(testSet);
			J48 cModel = (J48) ClassifierWrapper.loadClassifier(Vars.MODEL_PATH_USER);
			
			//check if features computed for this item;if yes find the tree path, otherwise do nothing.
			//this way we count also items for which features could not be computed 
			for (int i=0;i<listMediaItems.size();i++){
				for (int j=0;j<listFeatures.size();j++){
					
					if (listMediaItems.get(i).getRef().equals(listFeatures.get(j).id)){
						
						verifyResults.get(i).flag = flagsReliability[j];
						String result = J48Parser.findTreePath(testSet.get(j),UserClassifier.fvAttributes,cModel);
						verifyResults.get(i).explanation = result;
						
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return verifyResults;
	}	
	
	/**
	 * Function that calls methods to extract User features, classifies the MediaItems
	 * and computes the tree condition path of the classification.
	 * @param listItem the list of MediaItems need to be classified by user features
	 * @return ImageVerification list of the verification results(flag and description) of the classification
	 * @throws Exception
	 */
	public static List<ImageVerificationResult> tweetClassificationTotalMedia(List<MediaItem> listItem) throws Exception{
		
		Instances testSet;   	
		boolean[] flagsReliability;
		List<ImageVerificationResult> verifyResults = new ArrayList<ImageVerificationResult>();
		
		//create TotalFeatures list to keep the features extracted
		List<TotalFeatures> listFeatures = new ArrayList<TotalFeatures>();
		listFeatures = TotalFeaturesExtractor.featureExtractionMedia(listItem);
		
		
		//annotate items (use ItemFeaturesAnnotation for total classification)
		List<ItemFeaturesAnnotation> itemFeaturesAnnot = new ArrayList<ItemFeaturesAnnotation>();
		for (int i=0;i<listFeatures.size();i++){
			ItemFeaturesAnnotation itemAnnot = new ItemFeaturesAnnotation();
			itemAnnot.id = listFeatures.get(i).id;
			itemAnnot.reliability = "fake";
			itemFeaturesAnnot.add(itemAnnot);
		}
		
		//create testing set 
		testSet = TotalClassifier.createTestingSet(listFeatures,itemFeaturesAnnot);
		for (int i=0;i<listItem.size();i++){
			ImageVerificationResult verifyResult = new ImageVerificationResult();
			verifyResult.id = listItem.get(i).getId();
			verifyResults.add(verifyResult);
		}
		
		//call methods to classify items and compute the J48 tree path
		try {
			
			flagsReliability = TotalClassifier.classifyItems(testSet);
			J48 cModel = (J48) ClassifierWrapper.loadClassifier(Vars.MODEL_PATH_TOTAL);	
			
			//check if features computed for this item;if yes find the tree path, otherwise do nothing.
			//this way we count also items for which features could not be computed 
			for (int i=0;i<listItem.size();i++){
				for (int j=0;j<listFeatures.size();j++){
					if (listItem.get(i).getId().equals(listFeatures.get(j).id)){
						
						verifyResults.get(i).flag = flagsReliability[j];
						String result = J48Parser.findTreePath(testSet.get(j),TotalClassifier.fvAttributes,cModel);
						verifyResults.get(i).explanation = result;
						
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return verifyResults;
	}	
	
	public static void main(String[] args) throws Exception {
		

		ImageVerificationResult verif = new ImageVerificationResult();
		String flag;
		
		MediaItemDAOImpl dao3 = new MediaItemDAOImpl("160.40.50.242", "Sochi", "ItemsFake");
		MediaItem item = dao3.getMediaItem("Twitter#432025097746915329");

		verif = tweetClassificationbyMediaItem(item);
		
		/*MediaItemDAOImpl dao4 = new MediaItemDAOImpl("160.40.50.207", "Streams", "MediaItems");
		List<MediaItem> listMedia2 = dao4.getLastMediaItems(10);
		verif = tweetClassificationbyUserMedia(listMedia2);*/
		
		/*MediaItemDAOImpl dao5 = new MediaItemDAOImpl("160.40.50.207", "Streams", "MediaItems");
		List<MediaItem> listMedia3 = dao5.getLastMediaItems(7);
		verif = tweetClassificationTotalMedia(listMedia3);*/
		
		//print info about tweet image verification result
		
		System.out.println("MediaItem id: "+verif.id);
		if (verif.explanation!=null){
			if (verif.flag) flag="fake";
			else flag="real";
			System.out.println("Classified as "+flag);
			System.out.println(verif.explanation);
			
		}
		else{
			System.out.println("No available information found for this item.");
			System.out.println();
		}

	}

}
