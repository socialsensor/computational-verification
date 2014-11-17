package gr.iti.mklab.extractfeatures;

import weka.core.Instance;

public class ElementAnnotation {

	
	Instance inst;
	String id;
	String actual;
	String predicted;
	boolean agreed;
	
	public void setInstance(Instance inst) {
		this.inst = inst;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setActual(String actual) {
		this.actual = actual;
	}
	public void setPredicted(String predicted) {
		this.predicted = predicted;
	}
	public void setAgreed(boolean agreed) {
		this.agreed = agreed;
	}
	
	public Instance getInstance() {
		return inst;
	}
	public String getId() {
		return id;
	}
	public String getActual() {
		return actual;
	}
	public String getPredicted() {
		return predicted;
	}
	public boolean getAgreed() {
		return agreed;
	}
}
