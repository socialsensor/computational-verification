package gr.iti.mklab.extractfeatures;

import java.util.ArrayList;
import java.util.List;

import eu.socialsensor.framework.common.domain.Item;
//import eu.socialsensor.framework.common.domain.Selector;
import eu.socialsensor.framework.common.domain.MediaItem;
import gr.iti.mklab.extractfeatures.ItemFeatures;
import gr.iti.mklab.extractfeatures.ItemFeaturesExtractor;
import gr.iti.mklab.extractfeatures.TotalFeatures;
import gr.iti.mklab.extractfeatures.UserFeatures;
import gr.iti.mklab.extractfeatures.UserFeaturesExtractor;

/**
 * Class that extracts TotalFeatures of an Item (ItemFeatures & UserFeatures)
 * @author boididou
 */
public class TotalFeaturesExtractor {

 
	public static TotalFeatures extractTotalFeatures(ItemFeatures itemFeatures, UserFeatures itemUserFeatures){
		
		TotalFeatures totFeat = new TotalFeatures();
		
		totFeat.setId(itemFeatures.getId());
		totFeat.setItemLength(itemFeatures.getItemLength());
		totFeat.setNumWords(itemFeatures.getNumWords());
		totFeat.setContainsQuestionMark(itemFeatures.getContainsQuestionMark());
		totFeat.setContainsExclamationMark(itemFeatures.getContainsExclamationMark());
		totFeat.setnumQuestionMark(itemFeatures.getNumQuestionMark());
		totFeat.setnumExclamationMark(itemFeatures.getNumExclamationMark());
		totFeat.setContainsHappyEmo(itemFeatures.getContainsHappyEmo());
		totFeat.setContainsSadEmo(itemFeatures.getContainsSadEmo());
		totFeat.setContainsFirstOrderPron(itemFeatures.getContainsFirstOrderPron());
		totFeat.setContainsSecondOrderPron(itemFeatures.getContainsSecondOrderPron());
		totFeat.setContainsThirdOrderPron(itemFeatures.getContainsThirdOrderPron());
		totFeat.setNumUppercaseChars(itemFeatures.getNumUppercaseChars());
		totFeat.setNumPosSentiWords(itemFeatures.getNumPosSentiWords());
		totFeat.setNumNegSentiWords(itemFeatures.getNumNegSentiWords());
		totFeat.setNumMentions(itemFeatures.getNumMentions());
		totFeat.setNumHashtags(itemFeatures.getNumHashtags());
		totFeat.setNumURLs(itemFeatures.getNumURLs());
		totFeat.setRetweetCount(itemFeatures.getRetweetCount());
		totFeat.setUsername(itemUserFeatures.getUsername());
		totFeat.setnumFriends(itemUserFeatures.getNumFriends());
		totFeat.setnumFollowers(itemUserFeatures.getNumFollowers());
		totFeat.setFolFriendRatio(itemUserFeatures.getFolFrieRatio());
		totFeat.settimesListed(itemUserFeatures.getTimesListed());
		totFeat.sethasUrl(itemUserFeatures.gethasURL());
		totFeat.setisVerified(itemUserFeatures.getisVerified());
		totFeat.setnumTweets(itemUserFeatures.getNumTweets());
		
		return totFeat;
	}
	
    /**
     * Function that performs Item and User feature extraction of a MediaItem
     * @param listMediaItems the list of MediaItems need to be extracted
     * @return TotalFeatures list of the features extracted
     * @throws Exception 
     */
    public static List<TotalFeatures> featureExtractionMedia(List<MediaItem> listMediaItems) throws Exception {

		//extract features of the items
		List<ItemFeatures> itemFeatures = ItemFeaturesExtractor.featureExtractionMedia(listMediaItems);
		//extract user features of the items
		List<UserFeatures> itemUserFeatures = UserFeaturesExtractor.userFeatureExtractionMedia(listMediaItems);

		List<TotalFeatures> totalFeatures = new ArrayList<TotalFeatures>();
		
		for (int i=0;i<itemFeatures.size();i++){
			
			TotalFeatures totFeat = extractTotalFeatures(itemFeatures.get(i),itemUserFeatures.get(i));
			totalFeatures.add(totFeat);
		}
		
    	return totalFeatures;			
    }
    
    /**
     * Function that performs Item and User feature extraction of a MediaItem
     * @param listMediaItems the list of MediaItems need to be extracted
     * @return TotalFeatures list of the features extracted
     * @throws Exception 
     */
    public static TotalFeatures featureExtractionMedia(MediaItem item) throws Exception {
    	
    	
		//extract item features of the MediaItem
		ItemFeatures itemFeatures = ItemFeaturesExtractor.featureExtractionMedia(item);

		//extract user features of the MediaItem
		UserFeatures itemUserFeatures = UserFeaturesExtractor.userFeatureExtractionMedia(item);

		TotalFeatures totalFeatures = extractTotalFeatures(itemFeatures, itemUserFeatures);
						
    	return totalFeatures;			
    }
    

    
   
}
