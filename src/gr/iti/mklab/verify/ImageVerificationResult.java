package gr.iti.mklab.verify;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import eu.socialsensor.framework.common.domain.JSONable;

/**
 * Class used to save the result of the tweet image verification 
 * @author boididou
 */
public class ImageVerificationResult implements JSONable {

	@Expose
    @SerializedName(value = "id")
	protected String id;
	
	@Expose
    @SerializedName(value = "flag")
	protected boolean flag;
	
	@Expose
    @SerializedName(value = "fakePercentage")
	protected double fakePercentage;
	
	@Expose
    @SerializedName(value = "explanation")
	protected String explanation;
	
	@Override
	public String toJSONString() {
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		return gson.toJson(this);
	}
	
	public String getId(){
		return id;
	}
	
	public boolean getFlag(){
		return flag;
	}
	
	public double getFakePercentage(){
		return fakePercentage;
	}
	
	public String getExplanation(){
		return explanation;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public void setFlag(boolean flag){
		this.flag = flag;
	}
	
	public void setFakePercentage(double fakePercentage){
		this.fakePercentage = fakePercentage;
	}
	
	public void setExplanation(String explanation){
		this.explanation = explanation;
	}
	
	
	
	
	
}
