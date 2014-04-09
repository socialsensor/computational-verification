package gr.iti.mklab.verify;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import eu.socialsensor.framework.common.domain.JSONable;

/**
 * Class that keeps the UserFeatures of the user related to an Item object
 * @author boididou
 */
@SuppressWarnings("serial")
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
    @SerializedName(value = "isVerified")
	protected Boolean isVerified;
	
	@Expose
    @SerializedName(value = "numTweets")
	protected Integer numTweets;
	
	public void setId(String id){
		this.id = id;
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
	public void setiimesListed(Long timesListed){
		this.timesListed = timesListed;
	}
	public void sethasUrl(Boolean hasUrl){
		this.hasURL = hasUrl;
	}
	public void setisVerified(Boolean isVerified){
		this.isVerified = isVerified;
	}
	public void setnumTweets(Integer numTweets){
		this.numTweets = numTweets;
	}
	
	public String getId(){
		return id;
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
	public Integer getnumTweets(){
		return numTweets;
	}
	
	@Override
	public String toJSONString() {
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		return gson.toJson(this);
	}
	
}
