package gr.iti.mklab.verify;

import java.net.UnknownHostException;
import java.text.DecimalFormat;
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
		ItemFeatures features;
		ItemFeaturesExtractor ife = new ItemFeaturesExtractor();
		List<ItemFeatures> listFeatures = new ArrayList<ItemFeatures>();
		
		for (int i=0; i<listItem.size(); i++){
			MediaItem item = listItem.get(i);
			features = ife.featureExtractionMedia(item);
			if (features!=null){
				listFeatures.add(features);
			}
		}
		
		//annotate items
		List<ItemFeaturesAnnotation> itemFeaturesAnnot = new ArrayList<ItemFeaturesAnnotation>();
		for (int i=0;i<listFeatures.size();i++){
			ItemFeaturesAnnotation itemAnnot = new ItemFeaturesAnnotation();
			itemAnnot.setId(listFeatures.get(i).getId());
			itemAnnot.setReliability("real");
			itemFeaturesAnnot.add(itemAnnot);
		}

		//call ItemClassifier method to declare the attributes used in classification and print them
		if (ItemClassifier.getFvAttributes().size()==0){
			ItemClassifier.declareAttributes();
		}

		System.out.println("Attributes ");
		for (Attribute att:ItemClassifier.fvAttributes){
			System.out.print(att.name()+",");
		}
		
		//create testing set 
		testSet = ItemClassifier.createTestingSet(listFeatures,itemFeaturesAnnot);
		
		for (int i=0;i<listItem.size();i++){
			ImageVerificationResult verifyResult = new ImageVerificationResult();
			verifyResult.setId(listItem.get(i).getId());
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
					if ( listItem.get(i).getId().equals(listFeatures.get(j).getId()) ){
						
						verifyResults.get(i).setFlag(flagsReliability[j]);
						verifyResults.get(i).setFakePercentage(probabilities[j]);
						String result = J48Parser.findTreePath(testSet.get(j),ItemClassifier.getFvAttributes(),cModel);
						verifyResults.get(i).setExplanation(result);
						
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
		ItemFeatures features;
		ItemFeaturesExtractor ife = new ItemFeaturesExtractor();
		features = ife.featureExtractionMedia(item);
		
		//annotate items
		ItemFeaturesAnnotation itemAnnot = new ItemFeaturesAnnotation();
		itemAnnot.setId(features.getId());
		itemAnnot.setReliability("real");
		

		//call ItemClassifier method to declare the attributes used in classification and print them
		ItemClassifier.declareAttributes();
		System.out.println("Attributes ");
		for (Attribute att:ItemClassifier.getFvAttributes()){
			System.out.print(att.name()+",");
		}
		
		//create testing set 
		testSet = ItemClassifier.createTestingSet(features,itemAnnot);

		ImageVerificationResult verifyResult = new ImageVerificationResult();
		verifyResult.setId(item.getId());
		
		System.out.println();
		
		//call methods to classify items and compute the J48 tree path
		try {
			flagsReliability = ItemClassifier.classifyItems(testSet);	
			probabilities	 = ItemClassifier.findProbDistribution(testSet);
			
			J48 cModel = (J48) ClassifierWrapper.loadClassifier(Vars.MODEL_PATH_ITEM);
			
			//check if features computed for this item;if yes find the tree path, otherwise do nothing.
			//this way we count also items for which features could not be computed 
			
			if (item.getId().equals(features.getId())){
				
				verifyResult.setFlag(flagsReliability[0]);
				verifyResult.setFakePercentage(probabilities[0]);
				String result = J48Parser.findTreePath(testSet.get(0),ItemClassifier.getFvAttributes(),cModel);
				verifyResult.setExplanation(result);
				
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
	 * @return ImageVerification list of the verification results(flag, fake percentage and description) of the classification
	 * @throws Exception
	 */
	public static List<ImageVerificationResult> tweetClassificationbyUserMedia(List<MediaItem> listMediaItems) throws Exception{
		
		Instances testSet;   
		
		boolean[] flagsReliability;
		double[] probabilities;
		List<ImageVerificationResult> verifyResults = new ArrayList<ImageVerificationResult>();
		
		//create UserFeatures list to keep the features extracted
		List<UserFeatures> listFeatures = new ArrayList<UserFeatures>();
		
		listFeatures = UserFeaturesExtractor.userFeatureExtractionMedia(listMediaItems);
		
		//annotate items
		List<UserFeaturesAnnotation> userFeaturesAnnot = new ArrayList<UserFeaturesAnnotation>();
		for (int i=0;i<listFeatures.size();i++){
			UserFeaturesAnnotation itemAnnot = new UserFeaturesAnnotation();
			itemAnnot.setUsername(listFeatures.get(i).getUsername());
			itemAnnot.reliability = "real";
			userFeaturesAnnot.add(itemAnnot);
		}
		
		//call UserClassifier method to declare the attributes used in classification and print them
		if (UserClassifier.getFvAttributes().size()==0){
			UserClassifier.declareAttributes();
		}
		System.out.println("Attributes ");
		for (Attribute att:UserClassifier.getFvAttributes()){
			System.out.print(att.name()+",");
		}
		System.out.println();
		
		//create testing set 
		testSet = UserClassifier.createTestingSet(listFeatures,userFeaturesAnnot);
		for (int i=0;i<listMediaItems.size();i++){
			ImageVerificationResult verifyResult = new ImageVerificationResult();
			verifyResult.setId(listMediaItems.get(i).getId());
			verifyResults.add(verifyResult);
		}
		
		//call methods to classify items and compute the J48 tree path
		try {
			flagsReliability = UserClassifier.classifyItems(testSet);
			probabilities	 = UserClassifier.findProbDistribution(testSet);
			
			J48 cModel = (J48) ClassifierWrapper.loadClassifier(Vars.MODEL_PATH_USER);
			
			//check if features computed for this item;if yes find the tree path, otherwise do nothing.
			//this way we count also items for which features could not be computed 
			for (int i=0;i<listMediaItems.size();i++){
				for (int j=0;j<listFeatures.size();j++){
					
					if (listMediaItems.get(i).getId().equals(listFeatures.get(j).getId())){
						
						verifyResults.get(i).setFlag(flagsReliability[j]);
						String result = J48Parser.findTreePath(testSet.get(j),UserClassifier.getFvAttributes(),cModel);
						verifyResults.get(i).setExplanation(result);
						verifyResults.get(i).setFakePercentage(probabilities[j]);
						
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return verifyResults;
	}	
	
	/**
	 * Function that calls methods to extract User features, classifies the MediaItem
	 * and computes the tree condition path of the classification.
	 * @param item the MediaItem need to be classified by user features
	 * @return ImageVerification the verification results(flag, fake percentage and description) of the classification
	 * @throws Exception
	 */
	public static ImageVerificationResult tweetClassificationbyUserMedia(MediaItem item) throws Exception{
		
		Instances testSet;   
		
		boolean[] flagsReliability;
		double[] probabilities;
		
		
		//create UserFeatures list to keep the features extracted
		UserFeatures listFeatures = new UserFeatures();
		
		listFeatures = UserFeaturesExtractor.userFeatureExtractionMedia(item);
		
		//annotate items
		UserFeaturesAnnotation itemAnnot = new UserFeaturesAnnotation();
		itemAnnot.setUsername(listFeatures.getUsername());
		itemAnnot.reliability = "real";

		//call UserClassifier method to declare the attributes used in classification and print them
		if (UserClassifier.getFvAttributes().size()==0){
			UserClassifier.declareAttributes();
		}
		System.out.println("Attributes ");
		for (Attribute att:UserClassifier.getFvAttributes()){
			System.out.print(att.name()+",");
		}
		System.out.println();
		
		//create testing set 
		testSet = UserClassifier.createTestingSet(listFeatures,itemAnnot);
		
		ImageVerificationResult verifyResult = new ImageVerificationResult();
		verifyResult.setId(item.getId());
		
		//call methods to classify items and compute the J48 tree path
		try {
			flagsReliability = UserClassifier.classifyItems(testSet);
			probabilities	 = UserClassifier.findProbDistribution(testSet);
			
			J48 cModel = (J48) ClassifierWrapper.loadClassifier(Vars.MODEL_PATH_USER);
			
			//check if features computed for this item;if yes find the tree path, otherwise do nothing.
			//this way we count also items for which features could not be computed 
			
			if (item.getId().equals(listFeatures.getId())){
						
				verifyResult.setFlag(flagsReliability[0]);
				String result = J48Parser.findTreePath(testSet.get(0),UserClassifier.getFvAttributes(),cModel);
				verifyResult.setExplanation(result);
				verifyResult.setFakePercentage(probabilities[0]);
				
			}
	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return verifyResult;
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
		double[] probabilities;
		
		List<ImageVerificationResult> verifyResults = new ArrayList<ImageVerificationResult>();
		
		//create TotalFeatures list to keep the features extracted
		List<TotalFeatures> listFeatures = new ArrayList<TotalFeatures>();
		listFeatures = TotalFeaturesExtractor.featureExtractionMedia(listItem);
		
		
		//annotate items (use ItemFeaturesAnnotation for total classification)
		List<ItemFeaturesAnnotation> itemFeaturesAnnot = new ArrayList<ItemFeaturesAnnotation>();
		for (int i=0;i<listFeatures.size();i++){
			ItemFeaturesAnnotation itemAnnot = new ItemFeaturesAnnotation();
			itemAnnot.setId(listFeatures.get(i).getId());
			itemAnnot.setReliability("fake");
			itemFeaturesAnnot.add(itemAnnot);
		}
		
		//create testing set 
		testSet = TotalClassifier.createTestingSet(listFeatures,itemFeaturesAnnot);
		for (int i=0;i<listItem.size();i++){
			ImageVerificationResult verifyResult = new ImageVerificationResult();
			verifyResult.setId(listItem.get(i).getId());
			verifyResults.add(verifyResult);
		}
		
		//call methods to classify items and compute the J48 tree path
		try {
			
			flagsReliability = TotalClassifier.classifyItems(testSet);
			probabilities	 = TotalClassifier.findProbDistribution(testSet);
			
			J48 cModel = (J48) ClassifierWrapper.loadClassifier(Vars.MODEL_PATH_TOTAL);	
			
			//check if features computed for this item;if yes find the tree path, otherwise do nothing.
			//this way we count also items for which features could not be computed 
			for (int i=0;i<listItem.size();i++){
				for (int j=0;j<listFeatures.size();j++){
					if (listItem.get(i).getId().equals(listFeatures.get(j).getId())){
						
						verifyResults.get(i).setFlag(flagsReliability[j]);
						String result = J48Parser.findTreePath(testSet.get(j),TotalClassifier.getFvAttributes(),cModel);
						verifyResults.get(i).setExplanation(result);
						verifyResults.get(i).setFakePercentage(probabilities[j]);
						
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
	public static ImageVerificationResult tweetClassificationTotalMedia(MediaItem item) throws Exception{
		
		Instances testSet;   	
		boolean[] flagsReliability;
		double[] probabilities;

		//create TotalFeatures list to keep the features extracted
		TotalFeatures listFeatures = new TotalFeatures();
		listFeatures = TotalFeaturesExtractor.featureExtractionMedia(item);
		
		//annotate items (use ItemFeaturesAnnotation for total classification)
		ItemFeaturesAnnotation itemAnnot = new ItemFeaturesAnnotation();
		itemAnnot.setId(listFeatures.getId());
		itemAnnot.setReliability("fake");
		
		//create testing set 
		testSet = TotalClassifier.createTestingSet(listFeatures,itemAnnot);
		
		ImageVerificationResult verifyResult = new ImageVerificationResult();
		verifyResult.setId(item.getId());		
		
		//call methods to classify items and compute the J48 tree path
		try {
			
			flagsReliability = TotalClassifier.classifyItems(testSet);
			probabilities	 = TotalClassifier.findProbDistribution(testSet);
			
			J48 cModel = (J48) ClassifierWrapper.loadClassifier(Vars.MODEL_PATH_TOTAL);	
			
			//check if features computed for this item;if yes find the tree path, otherwise do nothing.
			//this way we count also items for which features could not be computed 
			
			if (item.getId().equals(listFeatures.getId())){
				
				verifyResult.setFlag(flagsReliability[0]);
				String result = J48Parser.findTreePath(testSet.get(0),TotalClassifier.getFvAttributes(),cModel);
				verifyResult.setExplanation(result);
				verifyResult.setFakePercentage(probabilities[0]);
				
			}
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return verifyResult;
	}
	
	
	public static void printVerificationResults(ImageVerificationResult verif){
		
		String flag;
		
		System.out.println("MediaItem id: "+verif.id);
		
		if (verif.getExplanation()!=null){
			if (verif.getFlag()) flag="fake";
			else flag="real";
			System.out.println("Classified as "+flag);
			String fakepercent = new DecimalFormat("#.###").format(verif.getFakePercentage()*100);
			System.out.println("Fake percentage: "+fakepercent +"%");
			System.out.println(verif.getExplanation());
			
		}
		else{
			System.out.println("No available information found for this item.");
			System.out.println();
		}
	}
	
	public static void main(String[] args) throws Exception {
		
		/* === Classification for one MediaItem === */
		
		//define the verification object
		ImageVerificationResult verif = new ImageVerificationResult();
		
		
		//get the MediaItem from mongodb 
		MediaItemDAOImpl dao3 = new MediaItemDAOImpl("ip", "dbname", "collectionname");
		MediaItem item = dao3.getMediaItem("Twitter#445854762949677056");
		
		//Classification by using Content(Tweet) features
		System.out.println(" === Classification using Content Features === ");
		verif = tweetClassificationbyMediaItem(item);
		printVerificationResults(verif);
		
		//Classification by using User features
		System.out.println(" === Classification using User Features === ");
		verif = tweetClassificationbyUserMedia(item);
		printVerificationResults(verif);
		
		//Classification by using Total features
		System.out.println(" === Classification using Total Features === ");
		verif = tweetClassificationTotalMedia(item);
		printVerificationResults(verif);
		
		
		
		/* === Classification for a list of MediaItem === */
		
		//define the list of verification objects
		List<ImageVerificationResult> verifs = new ArrayList<ImageVerificationResult>();
		
		//get the list of MediaItem from mongodb
		MediaItemDAOImpl dao = new MediaItemDAOImpl("ip", "dbname", "collectionname");
		List<MediaItem> listMedia2 = dao.getLastMediaItems(5);
		
		//Classification by using Content(Tweet) features
		System.out.println(" === Classification using Content Features === ");
		verifs = tweetClassificationbyMediaItem(listMedia2);
		
		for (int i=0;i<listMedia2.size();i++){
			printVerificationResults(verifs.get(i));
		}
		
		//Classification by using User features
		System.out.println(" === Classification using User Features === ");
		verifs = tweetClassificationbyUserMedia(listMedia2);
		
		for (int i=0;i<listMedia2.size();i++){
			printVerificationResults(verifs.get(i));
		}
		
		//Classification by using Total(Content & Tweet) features
		System.out.println(" === Classification using Total Features === ");
		verifs = tweetClassificationTotalMedia(listMedia2);
		
		for (int i=0;i<listMedia2.size();i++){
			printVerificationResults(verifs.get(i));
		}


	}

}
