
package gr.iti.mklab.extractfeatures;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import eu.socialsensor.framework.common.domain.Item;
import eu.socialsensor.framework.common.domain.JSONable;
import eu.socialsensor.framework.common.factories.ItemFactory;

public class ItemFeatures implements JSONable {
	
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
    @SerializedName(value = "hasColon")
	protected Boolean hasColon;
	
	@Expose
    @SerializedName(value = "hasPlease")
	protected Boolean hasPlease;
	
	@Expose
    @SerializedName(value = "numNouns")
	protected Integer numNouns;
	
	@Expose
    @SerializedName(value = "hasExternalLink")
	protected Boolean hasExternalLink;
	
	@Expose
    @SerializedName(value = "wotTrust")
	protected Integer wotTrust;
	
	@Expose
    @SerializedName(value = "wotSafe")
	protected Integer wotSafe;
	
	@Expose
    @SerializedName(value = "numSlangs")
	protected Integer numSlangs;
	
	@Expose
    @SerializedName(value = "readability")
	protected Double readability;
		
	@Expose
    @SerializedName(value = "containsWordFake")
	protected Boolean containsWordFake;
	
	@Expose
    @SerializedName(value = "numFakeWords")
	protected Integer numFakeWords;
	
	@Expose
    @SerializedName(value = "numComments")
	protected Integer numComments;
		
	@Expose
    @SerializedName(value = "timeFromStart")
	protected Long timeFromStart;
	
	@Expose
    @SerializedName(value = "reliability")
	protected String reliability;
	
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
	
	public void setNumExclamationMark(Integer numExclamationMark){
		this.numExclamationMark = numExclamationMark;
	}
	
	public Integer getNumExclamationMark(){
		return numExclamationMark;
	}
	
	public void setNumQuestionMark(Integer numQuestionMark){
		this.numQuestionMark = numQuestionMark;
	}
	
	public Integer getNumQuestionMark(){
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
	
	public Boolean getContainsFirstOrderPron(){
		return containsFirstOrderPron;
	}
		
	public void setContainsSecondOrderPron(boolean containsSecondOrderPron){
		this.containsSecondOrderPron = containsSecondOrderPron;
	}
	
	public Boolean getContainsSecondOrderPron(){
		return containsSecondOrderPron;
	}
	
	public void setContainsThirdOrderPron(boolean containsThirdOrderPron){
		this.containsThirdOrderPron = containsThirdOrderPron;
	}
	 
	public Boolean getContainsThirdOrderPron(){
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
	
	public void setReliability(String reliability){
		this.reliability = reliability;
	}
	
	public String getReliability(){
		return reliability;
	}
	
	public void setNumSlangs(Integer numSlangs){
		this.numSlangs = numSlangs;
	}
	
	public Integer getNumSlangs() {
		return numSlangs;
	}
	
	public void setHasColon(boolean hasColon) {
		this.hasColon = hasColon;
	}
	
	public boolean getHasColon() {
		return hasColon;
	}
	
	public void setHasPlease(boolean hasPlease) {
		this.hasPlease = hasPlease;
	}
	
	public boolean getHasPlease() {
		return hasPlease;
	}
	
	public void setHasExternalLink(boolean hasExternalLink) {
		this.hasExternalLink = hasExternalLink;
	}
	
	public boolean getHasExternalLink() {
		return hasExternalLink;
	}
	
	public void setWotTrust(Integer wotTrust){
		this.wotTrust = wotTrust;
	}
	
	public Integer getWotTrust() {
		return wotTrust;
	}
	
	public void setWotSafe(Integer wotSafe){
		this.wotSafe = wotSafe;
	}
	
	public Integer getWotSafe() {
		return wotSafe;
	}
	
	public void setReadability(Double readability){
		this.readability = readability;
	}
	
	public Double getReadability() {
		return readability;
	}
		
	public void setNumNouns(Integer numNouns){
		this.numNouns = numNouns;
	}
	
	public Integer getNumNouns() {
		return numNouns;
	}
	
	@Override
	public String toJSONString() {
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		return gson.toJson(this);
		
	}
	
	 
}
