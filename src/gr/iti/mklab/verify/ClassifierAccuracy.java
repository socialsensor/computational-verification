package gr.iti.mklab.verify;

import java.io.File;

import weka.classifiers.Classifier;

public class ClassifierAccuracy {

	Classifier cls;
	double accuracy;
	String model;
	
	public void setClassifier(Classifier cls) {
		this.cls = cls;
	}
	public void setAccuracy(double accuracy) {
		this.accuracy = accuracy;
	}
	public void setModel(String model) {
		this.model = model;
	}
	
	public Classifier getClassifier() {
		return cls;
	}
	public double getAccuracy() {
		return accuracy;
	}
	public String getModel() {
		return model;
	}
}
