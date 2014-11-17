package gr.iti.mklab.verifyutils;

import weka.attributeSelection.*;
import weka.core.*;
import weka.core.converters.ConverterUtils.*;
import weka.classifiers.*;
import weka.classifiers.meta.*;
import weka.classifiers.trees.*;
import weka.filters.*;
import gr.iti.mklab.verify.ItemClassifier;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import weka.filters.supervised.attribute.AttributeSelection;
/**
 * performs attribute selection using CfsSubsetEval and GreedyStepwise
 * (backwards) and trains J48 with that. Needs 3.5.5 or higher to compile.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class AttributeSelectionHandler {

  /**
   * uses the meta-classifier
   */
  protected static void useClassifier(Instances data) throws Exception {
	  
    System.out.println("\n1. Meta-classfier");
    AttributeSelectedClassifier classifier = new AttributeSelectedClassifier();
    CfsSubsetEval eval = new CfsSubsetEval();
    GreedyStepwise search = new GreedyStepwise();
    search.setSearchBackwards(true);
    J48 base = new J48();
    classifier.setClassifier(base);
    classifier.setEvaluator(eval);
    classifier.setSearch(search);
    Evaluation evaluation = new Evaluation(data);
    evaluation.crossValidateModel(classifier, data, 10, new Random(1));
    System.out.println(evaluation.toSummaryString());
  }

  
  public static AttributeSelection createFilter(int numAtts) {
	  
	AttributeSelection filter = new AttributeSelection();
	try {
		 
		Ranker search = new Ranker();
		//search.setSearchBackwards(true); 
		search.setNumToSelect(numAtts);
		
		GainRatioAttributeEval eval = new GainRatioAttributeEval();
		//InfoGainAttributeEval eval = new InfoGainAttributeEval();
		//ReliefFAttributeEval eval = new ReliefFAttributeEval();
		filter.setEvaluator(eval);
		filter.setSearch(search);
		//filter.setInputFormat(data);
				
	} catch (Exception e) {
		e.printStackTrace();
	}
	  
	  return filter;
  }
  
  /**
   * uses the filter
   */
  public Instances useFilter(Instances data, AttributeSelection filter) throws Exception {
	  
    System.out.println("\n2. Filter");
    System.out.println(data.get(0));
    Instances newData = Filter.useFilter(data, filter);
    for (int i=0;i<newData.numAttributes();i++){
    	System.out.print(newData.attribute(i).name()+" ");
    }
    
    // write training set to file
	/*BufferedWriter bw = null;
	String filePath = "training_newdata.txt";
	for (int i = 0; i < newData.size(); i++) {
		try {

			bw = new BufferedWriter(new FileWriter(filePath, true));
			// if file doesn't exist, then create it
			bw.append(newData.get(i).toString());
			bw.newLine();
			bw.flush();
			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
	//System.out.println(newData);
    return newData;
  }

  
  public static void useFilter(Instances data) throws Exception {
	    System.out.println("\n2. Filter");
	    weka.filters.supervised.attribute.AttributeSelection filter = new weka.filters.supervised.attribute.AttributeSelection();
	    CfsSubsetEval eval = new CfsSubsetEval();
	    GreedyStepwise search = new GreedyStepwise();
	    search.setSearchBackwards(true);
	    filter.setEvaluator(eval);
	    filter.setSearch(search);
	    filter.setInputFormat(data);
	    Instances newData = Filter.useFilter(data, filter);
	    System.out.println(newData);
	  }
  
  public static AttributeSelection createFilter2(Instances data) throws Exception {
	 
	  weka.filters.supervised.attribute.AttributeSelection filter = new weka.filters.supervised.attribute.AttributeSelection();
	  CfsSubsetEval eval = new CfsSubsetEval();
	  GreedyStepwise search = new GreedyStepwise();
	  search.setSearchBackwards(true);
	  search.setGenerateRanking(true);
	  search.setNumToSelect(15);
	  //System.out.println("threshold "+search.getThreshold());
	  filter.setEvaluator(eval);
	  filter.setSearch(search);
	  //filter.setInputFormat(data);
	  
	  return filter;
  }
  
  public Instances useFilter2(Instances data,AttributeSelection filter) throws Exception {
	    System.out.println("\n2. Filter");
	    
	    Instances newData = Filter.useFilter(data, filter);
	    
	    //System.out.println(newData);
	    
	    for (int i=0;i<newData.numAttributes();i++){
	    	System.out.print(newData.attribute(i).name()+" ");
	    }
	    
	    return newData;
	  }
  
  /**
   * uses the low level approach
   */
  public void useLowLevel(Instances data) throws Exception {
	  
	  System.out.println("\n3. Low-level");
	    weka.attributeSelection.AttributeSelection attsel = new  weka.attributeSelection.AttributeSelection ();
	    CfsSubsetEval eval = new CfsSubsetEval();
	    GreedyStepwise search = new GreedyStepwise();
	    search.setSearchBackwards(true);
	    attsel.setEvaluator(eval);
	    attsel.setSearch(search);
	    attsel.SelectAttributes(data);
	    int[] indices = attsel.selectedAttributes();
	    System.out.println("selected attribute indices (starting with 0):\n" + Utils.arrayToString(indices));
    
  }
  
  public void wrapperEvaluation(Instances data){
	  
	  WrapperSubsetEval eval = new WrapperSubsetEval();
	  GreedyStepwise search = new GreedyStepwise();
	  eval.setFolds(10);
	 
  }

  /**
   * takes a dataset as first argument
   *
   * @param args        the commandline arguments
   * @throws Exception  if something goes wrong
   */
  public static void main(String[] args) throws Exception {
   
  }
}
