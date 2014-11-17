package gr.iti.mklab.extractfeatures;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import eu.socialsensor.framework.client.dao.impl.StreamUserDAOImpl;
import eu.socialsensor.framework.common.domain.MediaItem;
import eu.socialsensor.framework.common.domain.StreamUser;
import eu.socialsensor.geo.Countrycoder;
import gr.iti.mklab.utils.TextProcessing;


/**
 * Class to extract UserFeatures of a user associated with an Item
 * @author boididou
 */
public class UserFeaturesExtractor {
	
	static String db = "";
	static String collection = "";
	
	static String rootGeonamesDir="";
	static String citiesFile="" ;
	static String countryInfoFile="";
	static String adminNamesFile="";
	
	public static void setDb(String db) {
		UserFeaturesExtractor.db = db;
	}
	
	public static void setCollection(String collection) {
		UserFeaturesExtractor.collection = collection;
	}
	
	public static String getDb() {
		return db;
	}
	
	public static String getCollection() {
		return collection;
	}	
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
	 * Function that finds whether the user provides url in his/her Twitter
	 * account It uses the getUserUrl(Document doc) function
	 * 
	 * @param doc
	 *            the Document of the user's profile
	 * @return Boolean whether the user provides url
	 */
	public static Boolean hasUrl(Document doc) {

		Boolean hasUrl = false;

		String url = getUserUrl(doc);
		if (url.length()>2 ) {
			hasUrl = true;
		}
		return hasUrl;
	}
	
	/**
	 * Function that finds the user's url that provides in his/her Twitter
	 * account The function supports both the old and the new version of the Twitter
	 * profile.
	 * 
	 * @param doc the Document of the user's profile
	 * @return String the url that the user provides or null if he/she does not
	 *         provide one.
	 */
	public static String getUserUrl(Document doc) {

		Elements newsHeadlines = null;
		String val = null;

		newsHeadlines = doc.select(".url .profile-field a");
		System.out.println("news "+newsHeadlines);
		if (newsHeadlines != null && !newsHeadlines.equals("")
				&& !newsHeadlines.isEmpty()) {
			val = newsHeadlines.attr("href");
			//System.out.println("if "+val);
		} else {
			newsHeadlines = doc.select(".ProfileHeaderCard-urlText a");
			val = newsHeadlines.attr("href");
			//System.out.println("else "+val+"-");
		}
		//System.out.println("val "+val);
		return val;
	}
	
	/**
	 * Function that finds if the user provides description in his/her Twitter
	 * profile The function supports both the old and the new version Twitter
	 * profile.
	 * 
	 * @param doc
	 *            the Document of the user's profile
	 * @return Boolean variable about whether the user provides description
	 */
	public static Boolean hasBio(Document doc) {
		Boolean hasBio = false;
		Elements newsHeadlines = null;
		String val = null;

		newsHeadlines = doc.select(".bio-container .bio");
		if (newsHeadlines != null && !newsHeadlines.equals("")
				&& !newsHeadlines.isEmpty()) {
			val = newsHeadlines.text();
		} else {
			newsHeadlines = doc.select(".ProfileHeaderCard-bio");
			val = newsHeadlines.text();
		}

		if (!val.isEmpty()) {
			hasBio = true;
		}
		//System.out.println("has bio " + hasBio);

		return hasBio;
	}
	
	/**
	 * Function that finds if the user account is verified or not The function
	 * supports both the old and new version of the Twitter profile.
	 * 
	 * @param doc
	 *            the Document of the user's profile
	 * @return Boolean whether the account is verified
	 */
	public static Boolean isVerifiedUser(Document doc) {
		Boolean isVerified = false;
		String text = null;
		Elements newsHeadlines = null;

		newsHeadlines = doc.select(".verified-large-border .visuallyhidden");
		if (newsHeadlines != null && !newsHeadlines.equals("")
				&& !newsHeadlines.isEmpty()) {
			text = newsHeadlines.text();
		} else {
			newsHeadlines = doc
					.select(".ProfileHeaderCard-badges .u-isHiddenVisually");
			text = newsHeadlines.text();
		}

		if (text.isEmpty()) {
			isVerified = false;
		} else if (text.equals("Verified account")) {
			isVerified = true;
		}

		//System.out.println("verified account " + isVerified);
		return isVerified;
	}
	
	/**
	 * Function that finds the number of tweets that the user posted The
	 * function supports both the old and new version of the Twitter profile.
	 * 
	 * @param doc
	 *            the Document of the user's profile
	 * @return Integer the number of tweets the user posted
	 */
	public static Long getNumTweets(Document doc) {

		String str = null;
		Long numTweets;

		Elements newsHeadlines = null;

		newsHeadlines = doc.select("[data-element-term=tweet_stats] strong");
		// System.out.println(newsHeadlines);
		if (newsHeadlines != null && !newsHeadlines.equals("")
				&& !newsHeadlines.isEmpty()) {
			str = newsHeadlines.text().replace(",", "");
		} else {
			newsHeadlines = doc
					.select(".ProfileNav-item--tweets .ProfileNav-value");
			str = newsHeadlines.text().replace(",", "");
		}

		if (str.contains("K")) {
			str = str.replace(".", "").replace("K", "").concat("00");
		} else if (str.contains("M")) {
			str = str.replace(".", "").replace("M", "").concat("000");
		}
		if (!str.equals("")) {
			numTweets = Long.parseLong(str);
		} else {
			numTweets = 0L;
		}
		//System.out.println("num tweets  " + numTweets);

		return numTweets;
	}
	
	/**
	 * Function that finds the number of Media content that the user shared via
	 * his twitter account This information is provided only in the new type of
	 * Twitter profile.
	 * 
	 * @param doc
	 *            the Document of the user's profile page
	 * @return Integer the number of the media content found
	 */
	public static Long getNumMediaContent(Document doc) {
		
		Long numMediaContent = 0L;
		String media = null;
		Elements newsHeadlines = null;

		newsHeadlines = doc.select(".ProfileNav-item--media .ProfileNav-value");

		media = newsHeadlines.text().replace(",", "");

		if (media.contains("K")) {
			media = media.replace(".", "").replace("K", "").concat("00");
		} else if (media.contains("M")) {
			media = media.replace(".", "").replace("M", "").concat("000");
		}
		if (!media.equals("")) {
			numMediaContent = Long.parseLong(media);
		} else {
			numMediaContent = 0L;
		}

		//System.out.println("num media content " + numMediaContent);
		return numMediaContent;
	}
	
	/**
	 * Function that finds the location that the user provides in his/her
	 * profile
	 * 
	 * @param doc
	 *            the Document of the user's profile page
	 * @return String that holds the location found
	 */
	public static String getLocation(Document doc) {

		Elements newsHeadlines = null;

		// find location as string
		newsHeadlines = doc.select(".trend-location");
		String location = newsHeadlines.text();
		// System.out.println(location);

		return location;

	}
	
	/**
	 * Function that finds whether the user provides information about location
	 * or not
	 * 
	 * @param doc
	 *            the Document of the User
	 * @return Boolean variable whether the user provides information about
	 *         location
	 * @throws MalformedURLException
	 * @throws JSONException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static Boolean hasLocation(String location)
			throws MalformedURLException, JSONException, IOException,
			InterruptedException {

		Boolean hasLoc = false;
		
		//System.out.println("Location " + location);

		// location in case of using location as String only
		if (!location.equals("Worldwide trends") && (location.length()!=0)) {
			hasLoc = true;
		}

		//System.out.println("has location " + hasLoc);

		return hasLoc;
	}
	
	public static Long getAccountAge(String username) {
		
		Document doc = null;
		String content;
		Long accountAge = null;
		String permalink = "http://twbirthday.com/" + username + "/";
		try {
			doc = Jsoup.connect(permalink).get();
		} catch (IOException e) {
			// System.out.println(e);
			doc = null;
		}
		
		if (doc != null) {
			Elements newsHeadlines = doc.select(".twstats");
			content = newsHeadlines.text();
			// System.out.println(content);
			if (!content.equals("")) {
				long hours = Long.parseLong(content.split("\\ ")[0].replace(
						",", "").trim());
				long secondstamp = hours * 3600;
				// System.out.println(secondstamp);
				long epoch = System.currentTimeMillis() / 1000;
				accountAge = epoch - secondstamp;

				//System.out.println("Account age " + accountAge);
			}
		}
		return accountAge;
	}

	private static String getStringFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}
	
	/**
	 * Expands the given shortened url 
	 * @param shortenedUrl String that holds the shortened url
	 * @return String long transformed url
	 */
	public static String expandUrl(String shortenedUrl) {
		
		String expandedURL = null;
		try{
	        URL url = new URL(shortenedUrl);  
			
	        // open connection
	        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection(Proxy.NO_PROXY); 
	        
	        // stop following browser redirect
	        httpURLConnection.setInstanceFollowRedirects(false);
	         
	        // extract location header containing the actual destination URL
	        expandedURL = httpURLConnection.getHeaderField("Location");
	        httpURLConnection.disconnect();
		}
		catch (IOException e) {
			expandedURL = null;
		}

        return expandedURL;
    }
	
	/**
	 * Calculates the WOT values(trust and safe) of a link. Returns 0 for
	 * unavailable values.
	 * 
	 * @param host
	 *            the link to calculate for
	 * @return Integer[] WOT values
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws JSONException
	 */
	public static Integer[] getWotValues(String host) throws JSONException, MalformedURLException, IOException{

		Integer[] values = new Integer[2];

		// String host = item.getMediaLinks().get(0).getMediaLink();
		System.out.println("the host " +host);
		
		String host0 = expandUrl(host);
		if (host0 == null){
			host0 = host;
		}
		System.out.println("the host transformed "+host0);
		
		
		
		InputStream response;
		String res = null;
		
		try {
			response = new URL(
					"http://api.mywot.com/0.4/public_link_json2?hosts="
							+ host0
							+ "/&key=75ff0cddd33a6e731c2d862c570de6c19f78423f")
					.openStream();
			res = getStringFromInputStream(response);

		} catch (Exception e1) {
			
			
				host0 = host0.split("/")[0] + "/" + host0.split("/")[1]
				+ "/" + host0.split("/")[2];
				
				response = new URL(
						"http://api.mywot.com/0.4/public_link_json2?hosts="
								+ host0
								+ "/&key=75ff0cddd33a6e731c2d862c570de6c19f78423f")
						.openStream();
				
				res = getStringFromInputStream(response);
			
			
		}
		//System.out.println("RESPONSE: "+res);
		JSONObject jO = new JSONObject(res);
		if (jO.length()>0){
			System.out.println(jO);
			String name = jO.names().get(0).toString();
			
			System.out.println("the name " + name);
	
			try {
				JSONArray trust = jO.getJSONObject(name).getJSONArray("0");
				Integer valueTrust = Integer.parseInt(trust.get(0).toString());
				Integer confTrust = Integer.parseInt(trust.get(1).toString());
				values[0] = valueTrust * confTrust / 100;
	
				JSONArray safe = jO.getJSONObject(name).getJSONArray("4");
				Integer valueSafe = Integer.parseInt(safe.get(0).toString());
				Integer confSafe = Integer.parseInt(safe.get(1).toString());
				values[1] = valueSafe * confSafe / 100;
				
				//System.out.println(valueTrust+" "+confTrust+" "+values[0]);
				//System.out.println(valueSafe+" "+confSafe+" "+values[1]);
			} catch (Exception e) {
				values[0] = 0;
				values[1] = 0;
				System.out.println("Not available WOT values for this link!");
			}

		}
		//System.out.println(values[0]+" "+values[1]);
		if (values[0]==null || values[1]==null){
			values[0] = 0;
			values[1] = 0;
		}
		return values;
	}
	
	public static Boolean hasProfileImg(Document doc) {
		
		Elements docElements = null;

		docElements = doc.select(".ProfileAvatar-image");
		String att = docElements.attr("src");
		
		if (att.contains("sticky/default_profile_images")) {
			//System.out.println("Default profile image found");
			return false;
		}
		else {
			//System.out.println("Profile image found");
			return true;
		}
	}
	
	public static Boolean hasHeaderImg(Document doc) {
		
		Elements docElements = null;

		docElements = doc.select(".ProfileCanopy-headerBg img");
		String att = docElements.attr("src");
		
		if (att == null) {
			//System.out.println("Default header image found");
			return false;
		}
		else {
			//System.out.println("Header image found");
			return true;
		}
	}
	
	
	public static void initializeFiles() {
		
		rootGeonamesDir = "C:/Users/boididou/workspace/twitter-image-verification/resources/files/";
		citiesFile = rootGeonamesDir + "cities1000_mod.txt";
		countryInfoFile = rootGeonamesDir + "countryInfo.txt";
		adminNamesFile = rootGeonamesDir + "admin1CodesASCII_mod.txt";
		
	}
	
	public static boolean hasExistingLocation(String locationName) {
		
		Countrycoder countrycodingService = new Countrycoder(citiesFile, countryInfoFile, adminNamesFile);
		String[] locParts = null;
		boolean hasExistingLocation = false;
		
		//System.out.println("Location name "+locationName);
		//System.out.println(StringUtils.isAlphanumeric(locationName)+" "+!locationName.contains("."));
		
		
		locParts = locationName.split(",");
		
		int i=0;
		String[] countries = new String[locParts.length];
		for (String locPart:locParts) {
			String newlocPart = TextProcessing.getInstance().eraseAllCharacters(locPart);
			
			countries[i] = countrycodingService.getCountryByLocationName(newlocPart);
			//System.out.println("For "+locPart+" found: "+ countries[i]);
			i++;
		}
		
		for (String country:countries) {
			if (country != "unknown") {
				//System.out.println("Country for location "+locationName+": "+country);
				hasExistingLocation = true;
			}
		}
		
		return hasExistingLocation;

	}
	
	/**
	 * Function that extracts UserFeatures for a specified user 
	 * @param suser the StreamUser for whom the features are extracted
	 * @param id String that keeps the tweet id of the post made by the user
	 * @return UserFeatures object with the features extracted
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @throws JSONException 
	 * @throws MalformedURLException 
	 */
	public static UserFeatures extractUserFeaturesMedia(String username,String id) throws MalformedURLException, JSONException, IOException, InterruptedException{

		UserFeatures uf = null;
		
	
		/**Get information for features by scraping their twitter profile webpage**/
		
		Document doc = null;
		try {
			String permalink1 = "http://twitter.com/" + username;
			
			try {
				doc = Jsoup.connect(permalink1).get();
			} catch (IOException e) {
				doc = null;
			}
		
		
		//Declare the values of the features
		
		//UserFeatures uf = null;
		
			if (doc != null) {
				Long numFriends 		= getNumFriends(doc);
				Long numFollowers 		= getNumFollowers(doc);
				Float FolFrieRatio 		= getFollowerFriendRatio(numFriends,numFollowers);
				Long timesListed		= getTimesListed(doc);
				Boolean hasURL 			= hasUrl(doc);
				//System.out.println("has url "+hasURL);
				
				Boolean hasBio 			= hasBio(doc);
				Boolean isVerified  	= isVerifiedUser(doc);
				Long numTweets 			= getNumTweets(doc);
				Long numMediaContent 	= getNumMediaContent(doc);
				
				
				String location 		= getLocation(doc);
				Boolean hasLocation 	= hasLocation(location);
				Long accountAge			= getAccountAge(username);
				
				Float tweetRatio;
				long epoch = System.currentTimeMillis()/1000;
				if (accountAge != null) {
					tweetRatio			=  (( (float)numTweets / (float)(epoch - accountAge) ) *86400L);
				}
				else tweetRatio 		= (float) 0;
				
							
				Boolean hasExistingLocation;
				if (!hasLocation) {
					hasExistingLocation = false;
				}
				else {
					hasExistingLocation = hasExistingLocation(location);
				}
				//System.out.println("existing location "+hasExistingLocation);
				
				
				Integer wotTrustUser = null;
				Integer wotSafeUser	 = null;
				Integer[] values = {0,0};
				
				if (hasURL)	values = getWotValues(getUserUrl(doc));
				
				if (values[0] != 0 && values[1] != 0) {
					wotTrustUser = values[0];
					wotSafeUser = values[1];
				}
				
				Boolean hasProfileImg 	= hasProfileImg(doc);
				Boolean hasHeaderImg	= hasHeaderImg(doc);
	
				
				/**common block for the two cases **/
				uf = new UserFeatures.Builder(id, username)
						.numFriends(numFriends).numFollowers(numFollowers)
						.FolFrieRatio(FolFrieRatio).timesListed(timesListed)
						.hasURL(hasURL).hasBio(hasBio).isVerified(isVerified).numTweets(numTweets)
						.numMediaContent(numMediaContent).hasLocation(hasLocation)
						.hasExistingLocation(hasExistingLocation).wotTrustUser(wotTrustUser).accountAge(accountAge)
						.hasProfileImg(hasProfileImg)
						.hasHeaderImg(hasHeaderImg).wotSafeUser(wotSafeUser).tweetRatio(tweetRatio).build();
				
				/*MongoHandler mh = null;
				try {
					mh = new MongoHandler(Vars.LOCALHOST_IP, Vars.DB_NAME_USER_EXTRACTION);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
			mh.insert(uf,Vars.COLL_NAME_USER_EXTRACTION );*/
			}
		}catch(Exception e) {
			System.out.println("User not found for this item.");
			return null;
		}
		return uf;
	}

	/**
	 * Function that finds the number of friends of a Twitter account
	 * 
	 * @param doc
	 *            the Document of the user's profile
	 * @return Long number of the friends
	 */
	public static Long getNumFriends(Document doc) {
		Long numFriends = 0L;

		Elements newsHeadlines = null;
		String friends;

		newsHeadlines = doc
				.select("[data-element-term=following_stats] strong");

		if (newsHeadlines != null && !newsHeadlines.equals("")
				&& !newsHeadlines.isEmpty()) {
			friends = newsHeadlines.text().replace(",", "");
		} else {
			newsHeadlines = doc
					.select(".ProfileNav-item--following .ProfileNav-value");
			friends = newsHeadlines.text().replace(",", "");
		}

		if (!friends.isEmpty()) {
			if (friends.contains("K")) {
				friends = friends.replace("K", "").replace(".", "")
						.concat("00");
			} else if (friends.contains("M")) {
				friends = friends.replace("M", "").replace(".", "")
						.concat("000");
			}
			numFriends = Long.parseLong(friends);
		} else {
			numFriends = 0L;
		}
		//System.out.println("Number of friends " + numFriends);
		return numFriends;
	}

	/**
	 * Function that finds the number of followers of a Twitter account
	 * 
	 * @param doc
	 *            the Document of the user's profile
	 * @return Long number of the followers
	 */
	public static Long getNumFollowers(Document doc) {
		Long numFollowers = 0L;
		String followers = null;

		Elements newsHeadlines = null;
		newsHeadlines = doc.select("[data-element-term=follower_stats] strong");

		if (newsHeadlines != null && !newsHeadlines.equals("")
				&& !newsHeadlines.isEmpty()) {
			followers = newsHeadlines.text().replace(",", "");
		} else {
			newsHeadlines = doc
					.select(".ProfileNav-item--followers .ProfileNav-value");
			followers = newsHeadlines.text().replace(",", "");
		}

		if (!followers.isEmpty()) {
			if (followers.contains("K")) {
				followers = followers.replace("K", "").replace(".", "")
						.concat("00");
			} else if (followers.contains("M")) {
				followers = followers.replace("M", "").replace(".", "")
						.concat("000");
			}
			numFollowers = Long.parseLong(followers);
		} else
			numFollowers = 0L;
		//System.out.println("Number of followers " + numFollowers);

		return numFollowers;
	}
	
	/**
	 * Function that finds the times that the user was listed in Twitter
	 * 
	 * @param doc
	 *            the Document of the user's profile
	 * @return Long number, the times the user was listed
	 */
	public static Long getTimesListed(Document doc) {
		Long times = 0L;
		String val, val2, value;
		Element newsHeadlines = doc.getElementById("init-data");

		val = newsHeadlines.attr("value");
		if (val.contains("listed_count\":")) {
			val2 = val.split("listed_count\":")[1];
			value = val2.split(",")[0];
			times = Long.parseLong(value);
		}

		else
			times = 0L;

		//System.out.println("Times listed " + times);

		return times;
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
    	
    	StreamUserDAOImpl dao = new StreamUserDAOImpl("160.40.50.242", db, collection);
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
    	
    	initializeFiles();
    	
    	UserFeatures userFeat = null;
    	
    	//String id = item.getUserId();//.replace("Twitter#", "");
    	//StreamUser su = getStreamUser(id);
    	String username = item.getPageUrl().replaceAll("http://", "").split("//")[1];
    	userFeat = extractUserFeaturesMedia(username, item.getId());
    	
    	return userFeat;
    }
    
    /**
     * Function that organizes the UserFeature extraction of a specified list of MediaItems
     * @param listMediaItems list of MediaItems need to be extracted
     * @return List of UserFeatures of the MediaItems
     * @throws Exception 
     */
    public static List<UserFeatures> userFeatureExtractionMedia(List<MediaItem> listMediaItems) throws Exception{
    	
    	initializeFiles();
    	
    	List<UserFeatures> listUserFeat = new ArrayList<UserFeatures>();
    	
    	for (int i=0;i<listMediaItems.size();i++){
    		
    		UserFeatures userFeatures = null;
    		//String id = listMediaItems.get(i).getUserId().replace("Twitter#", "");
    		
    		
    		//StreamUser su = getStreamUser(id);
    		
    		
    		String username = listMediaItems.get(i).getPageUrl().replaceAll("http://", "").split("/")[1];
    		
    		userFeatures = extractUserFeaturesMedia(username,listMediaItems.get(i).getId());
    		if (userFeatures!=null) {
    			listUserFeat.add(userFeatures);
    		}
    		
    	}
    	
    	
    	
    	return listUserFeat;
    }


    

}
