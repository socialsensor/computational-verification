package gr.iti.mklab.extractfeatures;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import eu.socialsensor.framework.common.domain.JSONable;

/**
 * Class that keeps the TotalFeatures of an Item
 * @author boididou
 *
 */

public class TotalFeatures implements JSONable {
	@Expose
    @SerializedName(value = "id")
	protected String id;
	
	@Expose
    @SerializedName(value = "itemLength")
	protected Integer itemLength;
	
	@Expose
    @SerializedName(value = "numWords")
	protected Integer numWords;
	
	@Expose
    @SerializedName(value = "containsQuestionMark")
	protected Boolean containsQuestionMark;
	
	@Expose
    @SerializedName(value = "containsExclamationMark")
	protected Boolean containsExclamationMark;
	
	@Expose
    @SerializedName(value = "numQuestionMark")
	protected Integer numQuestionMark;
	
	@Expose
    @SerializedName(value = "numExclamationMark")
	protected Integer numExclamationMark;
	
	@Expose
    @SerializedName(value = "containsHappyEmo")
	protected Boolean containsHappyEmo;
	
	@Expose
    @SerializedName(value = "containsSadEmo")
	protected Boolean containsSadEmo;
	
	@Expose
    @SerializedName(value = "containsFirstOrderPron")
	protected Boolean containsFirstOrderPron;
	
	@Expose
    @SerializedName(value = "containsSecondOrderPron")
	protected Boolean containsSecondOrderPron;
	
	@Expose
    @SerializedName(value = "containsThirdOrderPron")
	protected Boolean containsThirdOrderPron;
	
	@Expose
    @SerializedName(value = "numUppercaseChars")
	protected Integer numUppercaseChars;
	
	@Expose
    @SerializedName(value = "numNegSentiWords")
	protected Integer numNegSentiWords;
	
	@Expose
    @SerializedName(value = "numPosSentiWords")
	protected Integer numPosSentiWords;
	
	@Expose
    @SerializedName(value = "numMentions")
	protected Integer numMentions;
	
	@Expose
    @SerializedName(value = "numHashtags")
	protected Integer numHashtags;
	
	@Expose
    @SerializedName(value = "numURLs")
	protected Integer numURLs;
	
	@Expose
    @SerializedName(value = "retweetCount")
	protected Long retweetCount;
	
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
    @SerializedName(value = "isVerified")
	protected Boolean isVerified;

	@Expose
    @SerializedName(value = "numTweets")
	protected Long numTweets;

	public void setId(String id){
		this.id = id;
	}
	
	public String getId(){
		return id;
	}
	
	public void setItemLength(Integer itemLength){
		this.itemLength = itemLength;
	}
	
	public Integer getItemLength(){
		return itemLength;
	}
	
	public void setNumWords(Integer numWords){
		this.numWords = numWords;
	}
	
	public Integer getNumWords(){
		return numWords;
	}
	
	public void setContainsExclamationMark(Boolean containsExclamationMark){
		this.containsExclamationMark = containsExclamationMark;
	}
	
	public boolean getContainsExclamationMark(){
		return containsExclamationMark;
	}
	
	public void setContainsQuestionMark(Boolean containsQuestionMark){
		this.containsQuestionMark = containsQuestionMark;
	}

	public boolean getContainsQuestionMark(){
		return containsQuestionMark;
	}
	
	public void setnumExclamationMark(Integer numExclamationMark){
		this.numExclamationMark = numExclamationMark;
	}
	
	public Integer getnumExclamationMark(){
		return numExclamationMark;
	}
	
	public void setnumQuestionMark(Integer numQuestionMark){
		this.numQuestionMark = numQuestionMark;
	}
	
	public Integer getnumQuestionMark(){
		return numQuestionMark;
	}
	
	public void setContainsHappyEmo(boolean containsHappyEmo){
		this.containsHappyEmo = containsHappyEmo;
	}
	
	public boolean getContainsHappyEmo(){
		return containsHappyEmo;
	}
	
	public void setContainsSadEmo(boolean containsSadEmo){
		this.containsSadEmo = containsSadEmo;
	}
	
	public boolean getContainsSadEmo(){
		return containsSadEmo;
	}
	
	public void setContainsFirstOrderPron(boolean containsFirstOrderPron){
		this.containsFirstOrderPron = containsFirstOrderPron;
	}
	
	public boolean getContainsFirstOrderPron(){
		return containsFirstOrderPron;
	}
		
	public void setContainsSecondOrderPron(boolean containsSecondOrderPron){
		this.containsSecondOrderPron = containsSecondOrderPron;
	}
	
	public boolean getContainsSecondOrderPron(){
		return containsSecondOrderPron;
	}
	
	public void setContainsThirdOrderPron(boolean containsThirdOrderPron){
		this.containsThirdOrderPron = containsThirdOrderPron;
	}
	 
	public boolean getContainsThirdOrderPron(){
		return containsThirdOrderPron;
	}
	
	public void setNumUppercaseChars(Integer numUppercaseChars){
		this.numUppercaseChars = numUppercaseChars;
	}
	
	public Integer getNumUppercaseChars(){
		return numUppercaseChars;
	}
	
	public void setNumNegSentiWords(Integer numNegSentiWords){
		this.numNegSentiWords = numNegSentiWords;
	}
	
	public Integer getNumNegSentiWords(){
		return numNegSentiWords;
	}
	
	public void setNumPosSentiWords(Integer numPosSentiWords){
		this.numPosSentiWords = numPosSentiWords;
	}
	
	public Integer getNumPosSentiWords(){
		return numPosSentiWords;
	}
	
	public void setNumMentions(Integer numMentions){
		this.numMentions = numMentions;
	}
	
	public Integer getNumMentions(){
		return numMentions;
	}
	
	public void setNumHashtags(Integer numHashtags){
		this.numHashtags = numHashtags;
	}
	
	public Integer getNumHashtags(){
		return numHashtags;
	}
	
	public void setNumURLs(Integer numURLs){
		this.numURLs = numURLs;
	}
	
	public Integer getNumURLs(){
		return numURLs;
	}
	
	public void setRetweetCount(Long retweetCount){
		this.retweetCount = retweetCount;
	}
	
	public Long getRetweetCount(){
		return retweetCount;
	}
	
	
	public void setUsername(String username){
		this.username = username;
	}
	public void setnumFriends(Long numFriends){
		this.numFriends = numFriends;
	}
	public void setnumFollowers(Long numFollowers){
		this.numFollowers = numFollowers;
	}
	public void setFolFriendRatio(Float FolFriendRatio){
		this.FolFrieRatio = FolFriendRatio;
	}
	public void settimesListed(Long timesListed){
		this.timesListed = timesListed;
	}
	public void sethasUrl(Boolean hasUrl){
		this.hasURL = hasUrl;
	}
	public void setisVerified(Boolean isVerified){
		this.isVerified = isVerified;
	}
	public void setnumTweets(Long numTweets){
		this.numTweets = numTweets;
	}
	
	
	public String getUsername() {
        return username;
    }
	public Long getnumFriends() {
        return numFriends;
    }
	public Long getnumFollowers() {
        return numFollowers;
    }
	public Float getFolFrieRatio() {
        return FolFrieRatio;
    }
	public Long gettimesListed() {
        return timesListed;
    }
	public Boolean gethasURL(){
		return hasURL;
	}
	public Boolean getisVerified(){
		return isVerified;
	}
	public Long getnumTweets(){
		return numTweets;
	}
	
	@Override
	public String toJSONString() {
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		return gson.toJson(this);
	}
}
