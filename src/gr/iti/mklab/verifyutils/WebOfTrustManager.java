package gr.iti.mklab.verifyutils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The WebOfTrustManager class organizes the extraction of the WOT values and
 * includes all the necessary values for this purpose.
 * 
 * @author boididou
 * 
 */
public class WebOfTrustManager {

	/**
	 * Expands the given shortened url
	 * 
	 * @param shortenedUrl
	 *            String that holds the shortened url
	 * @return String long transformed url
	 */
	public static String expandUrl(String shortenedUrl) {

		String expandedURL = null;
		try {
			URL url = new URL(shortenedUrl);

			// open connection
			HttpURLConnection httpURLConnection = (HttpURLConnection) url
					.openConnection(Proxy.NO_PROXY);

			// stop following browser redirect
			httpURLConnection.setInstanceFollowRedirects(false);

			// extract location header containing the actual destination URL
			expandedURL = httpURLConnection.getHeaderField("Location");
			httpURLConnection.disconnect();
		} catch (IOException e) {
			expandedURL = null;
		}

		return expandedURL;
	}

	/**
	 * Calculates the WOT values(trust and safe) of a link. Returns 0 for
	 * unavailable values.
	 * 
	 * @param host
	 *            the link to calculate for
	 * @return int[] WOT values
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws JSONException
	 */
	public Integer[] getWotValues(String host) throws MalformedURLException,
			IOException, JSONException {

		Integer[] values = { 0, 0 };
		//System.out.println("the host " + host);

		String host0 = expandUrl(host);
		if (host0 == null) {
			host0 = host;
		}

		String hostToCheck = null;
		try {
			hostToCheck = host0.split("/")[0] + "/" + host0.split("/")[1] + "/"
					+ host0.split("/")[2];
		} catch (Exception e) {
			hostToCheck = host0;
		}
		//System.out.println("for the request " + hostToCheck);

		if (hostToCheck.contains("?")){
			hostToCheck = hostToCheck.split(Pattern.quote("?"))[0];
		}
		
		InputStream response = new URL(
				"http://api.mywot.com/0.4/public_link_json2?hosts="
						+ hostToCheck
						+ "/&key=75ff0cddd33a6e731c2d862c570de6c19f78423f")
				.openStream();

		String res = getStringFromInputStream(response);

		JSONObject jO = new JSONObject(res);
		if (jO.length() != 0) {
			
			String name = jO.names().get(0).toString();
			

			try {
				JSONArray trust = jO.getJSONObject(name).getJSONArray("0");
				int valueTrust = Integer.parseInt(trust.get(0).toString());
				int confTrust = Integer.parseInt(trust.get(1).toString());
				values[0] = valueTrust * confTrust / 100;

				JSONArray safe = jO.getJSONObject(name).getJSONArray("4");
				int valueSafe = Integer.parseInt(safe.get(0).toString());
				int confSafe = Integer.parseInt(safe.get(1).toString());
				values[1] = valueSafe * confSafe / 100;
			} catch (Exception e) {
				values[0] = 0;
				values[1] = 0;
				//System.out.println("Not available WOT values for this link!");
			}
		}
		return values;
	}

	/**
	 * Given an InputStream, returns the corresponding String
	 * 
	 * @param is
	 *            InputStream given
	 * @return String from the InputStream
	 */
	private static String getStringFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}

	/**
	 * Transforms the url of instagram in the appropriate form.
	 * 
	 * @param url
	 *            String of the url needs to be transformed
	 * @return String the url transformed
	 */
	public static String handleInstagram(String url) {

		int indexInstagram = url.indexOf("instagr");
		
		String charInstagram = String.valueOf(url.charAt(indexInstagram + 7));
		
		if (charInstagram.equals(".")) {
			url = url.replaceAll("instagr.am", "instagram.com");
		}
		return url;
	}

}
