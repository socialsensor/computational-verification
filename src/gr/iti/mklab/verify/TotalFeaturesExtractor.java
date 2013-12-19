package gr.iti.mklab.verify;

import java.util.ArrayList;
import java.util.List;

import eu.socialsensor.framework.common.domain.Item;
//import eu.socialsensor.framework.common.domain.Selector;
import eu.socialsensor.framework.common.domain.MediaItem;

/**
 * Class that extracts TotalFeatures of an Item (ItemFeatures & UserFeatures)
 * @author boididou
 */
public class TotalFeaturesExtractor {

 
    /**
     * Function that performs Item and User feature extraction of a MediaItem
     * @param listMediaItems the list of MediaItems need to be extracted
     * @return TotalFeatures list of the features extracted
     */
    public static List<TotalFeatures> featureExtractionMedia(List<MediaItem> listMediaItems) {

		//extract features of fake items
		List<ItemFeatures> itemFeatures = ItemFeaturesExtractor.featureExtractionMedia(listMediaItems);
		//extract user features of fake items
		List<UserFeatures> itemUserFeatures = UserFeaturesExtractor.userFeatureExtractionMedia(listMediaItems);

		List<TotalFeatures> totalFeatures = new ArrayList<TotalFeatures>();
		
		for (int i=0;i<itemFeatures.size();i++){
			TotalFeatures totFeat = new TotalFeatures();
			totFeat.id = itemFeatures.get(i).id;
			totFeat.itemLength = itemFeatures.get(i).itemLength;
			totFeat.numWords = itemFeatures.get(i).numWords;
			totFeat.containsQuestionMark = itemFeatures.get(i).containsQuestionMark;
			totFeat.containsExclamationMark=itemFeatures.get(i).containsExclamationMark;
			totFeat.numQuestionMark = itemFeatures.get(i).numQuestionMark;
			totFeat.numExclamationMark = itemFeatures.get(i).numExclamationMark;
			totFeat.containsHappyEmo = itemFeatures.get(i).containsHappyEmo;
			totFeat.containsSadEmo = itemFeatures.get(i).containsSadEmo;
			totFeat.containsFirstOrderPron = itemFeatures.get(i).containsFirstOrderPron;
			totFeat.containsSecondOrderPron = itemFeatures.get(i).containsSecondOrderPron;
			totFeat.containsThirdOrderPron = itemFeatures.get(i).containsThirdOrderPron;
			totFeat.numUppercaseChars = itemFeatures.get(i).numUppercaseChars;
			totFeat.numPosSentiWords = itemFeatures.get(i).numPosSentiWords;
			totFeat.numNegSentiWords = itemFeatures.get(i).numNegSentiWords;
			totFeat.numMentions = itemFeatures.get(i).numMentions;
			totFeat.numHashtags = itemFeatures.get(i).numHashtags;
			totFeat.numURLs = itemFeatures.get(i).numURLs;
			totFeat.retweetCount = itemFeatures.get(i).retweetCount;
			totFeat.username = itemUserFeatures.get(i).username;
			totFeat.numFriends = itemUserFeatures.get(i).numFriends;
			totFeat.numFollowers = itemUserFeatures.get(i).numFollowers;
			totFeat.FolFrieRatio = itemUserFeatures.get(i).FolFrieRatio;
			totFeat.timesListed = itemUserFeatures.get(i).timesListed;
			totFeat.hasURL = itemUserFeatures.get(i).hasURL;
			totFeat.isVerified = itemUserFeatures.get(i).isVerified;
			totFeat.numTweets = itemUserFeatures.get(i).numTweets;
			
			totalFeatures.add(totFeat);
		}
		
    	return totalFeatures;			
    }
    

    
   
}
