package gr.iti.mklab.verify;

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
@SuppressWarnings("serial")
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
	protected Integer numTweets;

	@Override
	public String toJSONString() {
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		return gson.toJson(this);
	}
}
