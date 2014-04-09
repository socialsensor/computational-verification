package gr.iti.mklab.verify;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Class that holds the annotation details of an Item
 * @author boididou
 */
public class ItemFeaturesAnnotation {
	
	@Expose
    @SerializedName(value = "id")
	protected String id;
	@Expose
    @SerializedName(value = "reliability")
	protected String reliability;
	
	public void setId(String id) {
        this.id = id;
    }
	
	public void setReliability(String reliability){
		this.reliability = reliability;
	}
	
	public String getId() {
        return id;
    }
	
	public String getReliability() {
		return reliability;
	}
}
