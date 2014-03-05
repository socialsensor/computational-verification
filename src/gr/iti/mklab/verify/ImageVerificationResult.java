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

}
