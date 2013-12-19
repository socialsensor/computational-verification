package gr.iti.mklab.verify;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import eu.socialsensor.framework.client.dao.impl.ItemDAOImpl;
import eu.socialsensor.framework.client.dao.impl.StreamUserDAOImpl;
import eu.socialsensor.framework.client.mongo.MongoHandler;
import eu.socialsensor.framework.common.domain.Item;
import eu.socialsensor.framework.common.domain.MediaItem;
import eu.socialsensor.framework.common.domain.StreamUser;


/**
 * Class to extract UserFeatures of a user associated with an Item
 * @author boididou
 */
public class UserFeaturesExtractor {
	
	/**
	 * @param numFr Integer number of friends of the user
	 * @param numFol Integer number of followers of the user
	 * @return Float ratio numFr/numFol
	 */
	public static Float getFollowerFriendRatio(Long numFr, Long numFol){
		
		Float ratio = (float)0;
		if (numFr!=0) ratio = (float)numFol/numFr;
		//System.out.println("Ratio "+ratio);
		
		return ratio;
	}

	/**
	 * @param su the StreamUser 
	 * @return Boolean variable of whether the user has url
	 */
	public static Boolean hasUrl(StreamUser su){
		Boolean hasUrl = false;
		
		if (su.getUrl()!=null){
			hasUrl=true;
		}
		return hasUrl;
	}
	

	/**
	 * Function that extracts UserFeatures for a specified user 
	 * @param suser the StreamUser for whom the features are extracted
	 * @param id String that keeps the tweet id of the post made by the user
	 * @return UserFeatures object with the features extracted
	 */
	public static UserFeatures extractUserFeaturesMedia(StreamUser suser,String id){

		UserFeatures uf = new UserFeatures();
		
		Long numFr,numFol;
		uf.id = id;
		uf.username = suser.getUsername();
		uf.numFriends = suser.getFriends();
		uf.numFollowers = suser.getFollowers();
		numFr = uf.numFriends;
		numFol = uf.numFollowers;
		uf.FolFrieRatio =  getFollowerFriendRatio(numFr,numFol);
		uf.timesListed = suser.getListedCount();
		uf.hasURL = hasUrl(suser);
		uf.isVerified = suser.isVerified();
		uf.numTweets = suser.getItems();
		
		return uf;
	}

    
    static Gson gson = new GsonBuilder()
    .excludeFieldsWithoutExposeAnnotation()
    .create();
    
    /**
     * @param json String that keeps the object need to be deserialized 
     * @return UserFeatures object, after deserialization into an object of the specified class
     */
    public static synchronized UserFeatures create(String json) {
        synchronized (gson) {
        	UserFeatures item = gson.fromJson(json, UserFeatures.class);
            return item;
        }
    }
    
    /**
     * Function that organizes the UserFeature extraction of a specified list of MediaItems
     * @param listMediaItems list of MediaItems need to be extracted
     * @return List of UserFeatures of the MediaItems
     */
    public static List<UserFeatures> userFeatureExtractionMedia(List<MediaItem> listMediaItems){
    	
    	//find associated Items with the MediaItems
		ItemDAOImpl dao3 = new ItemDAOImpl("160.40.50.207", "Streams", "Items");
		List<Item> listItems = new ArrayList<Item>();
		for (MediaItem mItem:listMediaItems){	
			Item item = dao3.getItem(mItem.getRef());
			listItems.add(item);
		}
		
    	List<UserFeatures> listUserFeat = new ArrayList<UserFeatures>();
    	
    	for (int i=0;i<listItems.size();i++){
    		
    		UserFeatures userFeatures = new UserFeatures();
    		
    		StreamUserDAOImpl dao = new StreamUserDAOImpl("160.40.50.207", "Streams", "StreamUsers");
    		String id = listItems.get(i).getUserId().replace("Twitter#", "");
    		
    		StreamUser su = dao.getStreamUser(id);
    		
    		userFeatures = extractUserFeaturesMedia(su,listItems.get(i).getId());
    		
    		listUserFeat.add(userFeatures);
    		
    	}
    	
    	return listUserFeat;
    }
    

}
