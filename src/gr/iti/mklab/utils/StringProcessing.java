package gr.iti.mklab.utils;

import java.io.IOException;
import java.util.HashSet;

public class StringProcessing {

	private static StringProcessing sInstance = new StringProcessing();

	public static StringProcessing getInstance() {
		
		if (sInstance == null) {	
			sInstance = new StringProcessing();
		}
		return sInstance;

	}
	
	public boolean isAppropriate(String str){
		
		if (str == null || str.isEmpty() || str.equals("null")){
			return false;
		}
		
		return true;
	}
	
	
}
