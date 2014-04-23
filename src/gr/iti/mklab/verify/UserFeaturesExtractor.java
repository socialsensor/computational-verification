package gr.iti.mklab.verify;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import eu.socialsensor.framework.client.dao.impl.StreamUserDAOImpl;
import eu.socialsensor.framework.common.domain.MediaItem;
import eu.socialsensor.framework.common.domain.StreamUser;


/**
 * Class to extract UserFeatures of a user associated with an Item
 * @author boididou
 */
public class UserFeaturesExtractor {
	
	static String db;
	static String collection;
	
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
		uf.setId(id);
		uf.setUsername(suser.getUsername());
		uf.setnumFriends(suser.getFriends());
		uf.setnumFollowers(suser.getFollowers());
		numFr = uf.getnumFriends();
		numFol = uf.getnumFollowers();
		uf.setFolFriendRatio(getFollowerFriendRatio(numFr,numFol));
		uf.setiimesListed(suser.getListedCount());
		uf.sethasUrl(hasUrl(suser));
		uf.setisVerified(suser.isVerified());
		uf.setnumTweets(suser.getItems());
		
		/*System.out.println("username " + uf.username);
		System.out.println("num friends " + uf.numFriends);
		System.out.println("num followers " + uf.numFollowers);
		System.out.println("ratio " + uf.FolFrieRatio );
		System.out.println("times listed "+ uf.timesListed);
		System.out.println("has url " + uf.hasURL);
		System.out.println("is verified "+ uf.isVerified);
		System.out.println("num tweets "+ uf.numTweets);*/
		
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
     * Function that returns the StreamUser associated to the MediaItem
     * @param id the StreamUser id 
     * @return the StreamUser that has the specified id
     * @throws Exception 
     */
    public static StreamUser getStreamUser(String id) throws Exception{
    	
    	StreamUserDAOImpl dao = new StreamUserDAOImpl("ip", db, collection);
		StreamUser su = dao.getStreamUser(id);
		return su;
    }
    
    /**
     * Function that organizes the UserFeature extraction of a MediaItem
     * @param item from which the features need to be extracted
     * @return UserFeatures userFeat the features extracted
     * @throws Exception 
     */
    public static UserFeatures userFeatureExtractionMedia(MediaItem item) throws Exception{
    	
    	UserFeatures userFeat = new UserFeatures();
    	
    	String id = item.getUserId().replace("Twitter#", "");
    	
    	StreamUser su = getStreamUser(id);
    	userFeat = extractUserFeaturesMedia(su, item.getId());
    	
    	return userFeat;
    }
    
    /**
     * Function that organizes the UserFeature extraction of a specified list of MediaItems
     * @param listMediaItems list of MediaItems need to be extracted
     * @return List of UserFeatures of the MediaItems
     * @throws Exception 
     */
    public static List<UserFeatures> userFeatureExtractionMedia(List<MediaItem> listMediaItems) throws Exception{
    	
    	List<UserFeatures> listUserFeat = new ArrayList<UserFeatures>();
    	
    	for (int i=0;i<listMediaItems.size();i++){
    		
    		UserFeatures userFeatures = new UserFeatures();
    		
    		String id = listMediaItems.get(i).getUserId().replace("Twitter#", "");
    		
    		StreamUser su = getStreamUser(id);
    		userFeatures = extractUserFeaturesMedia(su,listMediaItems.get(i).getId());
    		
    		listUserFeat.add(userFeatures);
    		
    	}
    	
    	return listUserFeat;
    }
    

}
