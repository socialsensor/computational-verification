package gr.iti.mklab.verify;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.Instance;
import weka.gui.treevisualizer.Edge;
import weka.gui.treevisualizer.Node;
import weka.gui.treevisualizer.TreeBuild;

/**
 * Utility class to parse the decision tree model from WEKA's J48 class
 * and compute the tree path of a given instance, followed during classification.
 * @author boididou
 */
public class J48Parser {

	
	static List<Attribute> atts = new ArrayList<Attribute>();
	static Boolean check=false;
	
	/**
	 * Parse the tree model to find the tree path
	 * @param instance Instance whose tree path need to be computed
	 * @param attsList the list of Attributes
	 * @param cModel the J48 current model
	 * @return String that keeps the tree path found 
	 * @throws Exception
	 */
	public static String findTreePath(Instance instance,List<Attribute> attsList,J48 cModel) throws Exception{
		
		//re-initialize check variable for the new instance
		check=false;
		//define the pathToResult var that keeps the path computed
		List<String> pathToResult = new ArrayList<String>();
		//attributes var
		atts=attsList;
		
		//define the tree details
		Reader treeDot = new StringReader(((J48) cModel).graph());
		TreeBuild treeBuild = new TreeBuild();
		Node treeRoot = treeBuild.create(treeDot);
		
		//recursively parse the tree model
		findTreePath(treeRoot,instance,pathToResult);
		
		//integrate the separate strings into one String
		StringBuilder sb = new StringBuilder();
		for (int j=0;j<pathToResult.size();j++){
			sb.append((j+1)+" "+pathToResult.get(j)+"\n");
		}
		String result = sb.toString();
		
		return result;
	}
	
	/**
	 * Recursively parse the tree Node to find the whole path for the current instance 
	 * @param node Node that has to be parsed whose tree path need to be computed
	 * @param instance Instance whose path is computed
	 * @param pathToResult list of String that keeps the String values of the path
	 */
	public static void findTreePath(Node node,Instance instance,List<String> pathToResult){
		
		Edge edgeToChild;
		int i=0;
		
		//check if node is leaf
		if (node.getChild(0) == null){
			pathToResult.add(node.getLabel());
			check=true;
		}
		
		if (check==false){
		while ((edgeToChild = node.getChild(i)) != null) {
			
			Node childNode = edgeToChild.getTarget();
			
			//finds for each node the corresponding attribute 
			for (Attribute att:atts){
				if (att.name().equals(node.getLabel())){
	
					//for numeric attributes
					if (att.isNumeric()){
						String[] parts = edgeToChild.getLine(0).split(" ");
						
						
						if (parts[0].equals("<=")){
							if (instance.value(att) <= Float.parseFloat(parts[1])){
								pathToResult.add(node.getLabel().concat(" "+edgeToChild.getLine(0)));
								findTreePath(childNode,instance,pathToResult);		
							}
						}
						else if (parts[0].equals(">")){					
							if (instance.value(att) > Float.parseFloat(parts[1])){
								pathToResult.add(node.getLabel().concat(" "+edgeToChild.getLine(0)));
								findTreePath(childNode,instance,pathToResult);	
							}
						}
					}//end of if attr is numeric
					
					//else part for non-numeric attributes
					else {
						Integer temp;
						String part = edgeToChild.getLine(0).replace("="," ").trim();
						
						if (part.equals("false")) temp=1;
						else temp=0;
												
						if (instance.value(att) == temp){	
							pathToResult.add(node.getLabel().concat(" "+edgeToChild.getLine(0)));
							findTreePath(childNode,instance,pathToResult);	
							
						}
						
					}
					
				}
				
			}
			
			i++;
		}
		}
	
	}
	
}