package gr.iti.mklab.extractfeatures;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import eu.socialsensor.framework.common.domain.JSONable;


public class UserFeatures implements JSONable{
	
	@Expose
    @SerializedName(value = "id")
	protected String id;
	
	@Expose
    @SerializedName(value = "username")
	protected String username;
	
	@Expose
    @SerializedName(value = "numFriends")
	protected Long numFriends;
	
	@Expose
    @SerializedName(value = "numFollowers")
	protected Long numFollowers;
	
	@Expose
    @SerializedName(value = "FolFrieRatio")
	protected Float FolFrieRatio;
	
	@Expose
    @SerializedName(value = "timesListed")
	protected Long timesListed;
	
	@Expose
    @SerializedName(value = "hasURL")
	protected Boolean hasURL;
	
	@Expose
    @SerializedName(value = "hasBio")
	protected Boolean hasBio;
	
	@Expose
    @SerializedName(value = "isVerified")
	protected Boolean isVerified;
	
	@Expose
    @SerializedName(value = "numTweets")
	protected Long numTweets;
	
	@Expose
    @SerializedName(value = "numMediaContent")
	protected Long numMediaContent;
	
	@Expose
    @SerializedName(value = "numFavorites")
	protected Long numFavorites;
	
	/*@Expose
    @SerializedName(value = "distance")
	protected double distance;*/
	
	@Expose
    @SerializedName(value = "hasLocation")
	protected Boolean hasLocation;
	
	@Expose
    @SerializedName(value = "hasExistingLocation")
	protected Boolean hasExistingLocation;
	
	@Expose
    @SerializedName(value = "accountAge")
	protected Long accountAge;
	
	@Expose
    @SerializedName(value = "wotTrustUser")
	protected Integer wotTrustUser;
	
	@Expose
    @SerializedName(value = "wotSafeUser")
	protected Integer wotSafeUser;
	
	@Expose
    @SerializedName(value = "indegree")
	protected Float indegree;
	
	@Expose
    @SerializedName(value = "harmonic")
	protected Float harmonic;
	
	@Expose
    @SerializedName(value = "hasProfileImg")
	protected Boolean hasProfileImg;
	
	@Expose
    @SerializedName(value = "hasHeaderImg")
	protected Boolean hasHeaderImg;
	
	@Expose
    @SerializedName(value = "tweetRatio")
	protected Float tweetRatio;
	
	public String getId(){
		return id;
	}
	public String getUsername() {
        return username;
    }
	public Long getNumFriends() {
        return numFriends;
    }
	public Long getNumFollowers() {
        return numFollowers;
    }
	public Float getFolFrieRatio() {
        return FolFrieRatio;
    }
	public Long getTimesListed() {
        return timesListed;
    }
	public Boolean gethasURL(){
		return hasURL;
	}
	public Boolean gethasBio(){
		return hasBio;
	}
	public Boolean gethasLocation(){
		return hasLocation;
	}
	public Boolean gethasExistingLocation(){
		return hasExistingLocation;
	}
	public Long getNumTweets(){
		return numTweets;
	}
	public Long getNumFavorites(){
		return numFavorites;
	}
	public Long getNumMediaContent(){
		return numMediaContent;
	}
	public Integer getWotTrustUser(){
		return wotTrustUser;
	}
	public Boolean getisVerified(){
		return isVerified;
	}
	/*public double getDistance(){
		return distance;
	}*/
	public Long getAccountAge() {
		return accountAge;
	}
	public Float getIndegree() {
		return indegree;
	}
	public Float getHarmonic() {
		return harmonic;
	}
	public Boolean getHasProfileImg(){
		return hasProfileImg;
	}
	public Boolean getHasHeaderImg(){
		return hasHeaderImg;
	}
	public Float getTweetRatio() {
		return tweetRatio;
	}
	public static class Builder{
		
		@Expose
	    @SerializedName(value = "id")
		protected String id;
		
		@Expose
	    @SerializedName(value = "username")
		protected String username;
		
		@Expose
	    @SerializedName(value = "numFriends")
		protected Long numFriends;
		
		@Expose
	    @SerializedName(value = "numFollowers")
		protected Long numFollowers;
		
		@Expose
	    @SerializedName(value = "FolFrieRatio")
		protected Float FolFrieRatio;
		
		@Expose
	    @SerializedName(value = "timesListed")
		protected Long timesListed;
		
		@Expose
	    @SerializedName(value = "hasURL")
		protected Boolean hasURL;
		
		@Expose
	    @SerializedName(value = "hasBio")
		protected Boolean hasBio;
		
		@Expose
	    @SerializedName(value = "isVerified")
		protected Boolean isVerified;
		
		@Expose
	    @SerializedName(value = "numTweets")
		protected Long numTweets;
		
		@Expose
	    @SerializedName(value = "numMediaContent")
		protected Long numMediaContent;
		
		@Expose
	    @SerializedName(value = "numFavorites")
		protected Long numFavorites;
		
		/*@Expose
	    @SerializedName(value = "distance")
		protected double distance;*/
		
		@Expose
	    @SerializedName(value = "hasLocation")
		protected Boolean hasLocation;
		
		@Expose
	    @SerializedName(value = "hasExistingLocation")
		protected Boolean hasExistingLocation;
		
		@Expose
	    @SerializedName(value = "accountAge")
		protected Long accountAge;
		
		@Expose
	    @SerializedName(value = "wotTrustUser")
		protected Integer wotTrustUser;
		
		@Expose
	    @SerializedName(value = "wotSafeUser")
		protected Integer wotSafeUser;
		
		@Expose
	    @SerializedName(value = "indegree")
		protected Float indegree;
		
		@Expose
	    @SerializedName(value = "harmonic")
		protected Float harmonic;
		
		@Expose
	    @SerializedName(value = "hasProfileImg")
		protected Boolean hasProfileImg;
		
		@Expose
	    @SerializedName(value = "hasHeaderImg")
		protected Boolean hasHeaderImg;
		
		@Expose
	    @SerializedName(value = "tweetRatio")
		protected Float tweetRatio;
		
		public Builder(String id, String username){
			this.id = id;
			this.username = username;
		}
		
		public Builder numFriends(Long val){
			numFriends = val; return this;
		}
		public Builder numFollowers(Long val){
			numFollowers = val; return this;
		}
		public Builder FolFrieRatio(Float val){
			FolFrieRatio = val; return this;
		}
		public Builder timesListed(Long val){
			timesListed = val; return this;
		}
		public Builder hasURL(Boolean val){
			hasURL = val; return this;
		}
		public Builder hasBio(Boolean val){
			hasBio = val; return this;
		}
		public Builder isVerified(Boolean val){
			isVerified = val; return this;
		}
		public Builder numTweets(Long val){
			numTweets = val; return this;
		}
		public Builder numMediaContent(Long val){
			numMediaContent = val; return this;
		}
		public Builder numFavorites(Long val){
			numFavorites = val; return this;
		}
		public Builder hasLocation(Boolean val){
			hasLocation = val; return this;
		}
		public Builder hasExistingLocation(Boolean val){
			hasExistingLocation = val; return this;
		}
		public Builder accountAge(Long val){
			accountAge = val; return this;
		}
		public Builder wotTrustUser(Integer val){
			wotTrustUser = val; return this;
		}
		public Builder wotSafeUser(Integer val){
			wotSafeUser = val; return this;
		}
		public Builder indegree(Float val){
			indegree = val; return this;
		}
		public Builder harmonic(Float val){
			harmonic = val; return this;
		}
		public Builder hasProfileImg(Boolean val){
			hasProfileImg = val; return this;
		}
		public Builder hasHeaderImg(Boolean val){
			hasHeaderImg = val; return this;
		}
		public Builder tweetRatio(Float val){
			tweetRatio = val; return this;
		}
		public UserFeatures build(){
			return new UserFeatures(this);
		}	
	}
	public void setId(String id){
		this.id = id;
	}
	public void setUsername(String username){
		this.username = username;
	}
	public void setNumFriends(Long numFriends){
		this.numFriends = numFriends;
	}
	public void setNumFollowers(Long numFollowers){
		this.numFollowers = numFollowers;
	}
	public void setFolFriendRatio(Float FolFriendRatio){
		this.FolFrieRatio = FolFriendRatio;
	}
	public void setTimesListed(Long timesListed){
		this.timesListed = timesListed;
	}
	public void setHasUrl(Boolean hasUrl){
		this.hasURL = hasUrl;
	}
	public void setIsVerified(Boolean isVerified){
		this.isVerified = isVerified;
	}
	public void setNumTweets(Long numTweets){
		this.numTweets = numTweets;
	}
	public void setNumMediaContent(Long numMediaContent){
		this.numMediaContent = numMediaContent;
	}
	public void setNumFavorites(Long numFavorites){
		this.numFavorites = numFavorites;
	}
	public void setIndegree(Float indegree){
		this.indegree = indegree;
	}
	public void setHarmonic(Float harmonic){
		this.harmonic = harmonic;
	}
	public void setHasProfileImg(Boolean hasProfileImg){
		this.hasProfileImg = hasProfileImg;
	}
	public void setHasHeaderImg(Boolean hasHeaderImg){
		this.hasHeaderImg = hasHeaderImg;
	}
	public void setTweetRatio(Float tweetRatio){
		this.tweetRatio = tweetRatio;
	}
	
	private UserFeatures(Builder builder){
		id					= builder.id;
		username			= builder.username;
		numFriends	 		= builder.numFriends;
		numFollowers		= builder.numFollowers;
		FolFrieRatio		= builder.FolFrieRatio;
		timesListed 		= builder.timesListed;
		hasURL 				= builder.hasURL;
		hasBio 				= builder.hasBio;
		isVerified 			= builder.isVerified;
		numTweets 			= builder.numTweets;
		numMediaContent		= builder.numMediaContent;
		numFavorites		= builder.numFavorites;
		hasLocation 		= builder.hasLocation;
		hasExistingLocation = builder.hasExistingLocation;
		accountAge 			= builder.accountAge;
		wotTrustUser 		= builder.wotTrustUser;
		wotSafeUser 		= builder.wotSafeUser;	
		indegree			= builder.indegree;
		harmonic			= builder.harmonic;
		hasProfileImg		= builder.hasProfileImg;
		hasHeaderImg		= builder.hasHeaderImg;
		tweetRatio			= builder.tweetRatio;
	}
	
	@Override
	public String toJSONString() {
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		return gson.toJson(this);
	}
	
}

