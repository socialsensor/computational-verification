package gr.iti.mklab.utils;



public class URLProcessing {
	
	private static URLProcessing mInstance = new URLProcessing();

	public static URLProcessing getInstance() {
		
		
		if (mInstance == null) {			
			mInstance = new URLProcessing();
		}
		return mInstance;

	}
	
	/**
	 * Checks if a url is a page of Twitter.
	 * @param url to be checked
	 * @return boolean true if the url is twitter page or false if it is not.
	 */
	public boolean isTwitterPage(String url){
		
		if (url.contains("twitter")){
			return true;
		}
		return false;
	}
	
	public boolean isUncompleted(String url){
		
		if (url.endsWith("…")){
			return true;
		}
		
		return false;
	}
	
	public boolean isTumblrPage(String url) {
		
		if (url.matches(".+.tumblr.com")) {
			return true;
		}
		return false;
	}
}
