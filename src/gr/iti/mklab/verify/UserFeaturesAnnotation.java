package gr.iti.mklab.verify;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Class that keeps the annotation details of a user associated with an Item
 * @author boididou
 */
public class UserFeaturesAnnotation {
	@Expose
    @SerializedName(value = "username")
	protected String username;
	@Expose
    @SerializedName(value = "reliability")
	protected String reliability;
	
	public void setReliability(String reliability){
		this.reliability = reliability;
	}
	
	public void setUsername(String username){
		this.username = username;
	}
	
	public String getReliability() {
		return reliability;
	}
	
	public String getUsername(){
		return username;
	}
}
