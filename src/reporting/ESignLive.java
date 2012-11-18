package reporting;

import java.io.BufferedReader;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ESignLive {
	
		public final static String requestURL = "https://stage-api.e-signlive.com/aws/rest/services/codejam";
		
		public static void main(String[] args){
	    	System.out.println(SignDocument("{" +
	        		"\"team\" : \"Flying monkeys\"," +
	        		"\"destination\" : \"mcgillcodejam2012@gmail.com\"," +
	        		"\"transactions\" : [" +
	        		"{" +
	        		"\"time\" : \"8004\"," +
	        		"\"type\" : \"buy\"," +
	        		"\"price\" : 120," +
	        		"\"manager\" : \"Manager1\"," +
	        		"\"strategy\" : \"EMA\"" +
	        		"}]}\""));
		}
		
		public static String SignDocument(String jsonArray) {
			String output = null;
			try {
				URL url = new URL(requestURL);
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	            conn.setDoOutput(true);
	            conn.setDoInput(true);
	            conn.setRequestMethod("POST");
	            conn.setRequestProperty("Authorization", "Basic Y29kZWphbTpBRkxpdGw0TEEyQWQx");
	            conn.setRequestProperty("Content-Type", "application/json");
	            // input to the request connection
				DataOutputStream wr = new DataOutputStream(conn.getOutputStream ());
				wr.writeBytes(jsonArray);
				wr.flush();
				wr.close();
				//read the response
				BufferedReader br = new BufferedReader(new java.io.InputStreamReader(conn.getInputStream()));
			    StringBuffer sb = new StringBuffer();
			    String str = br.readLine();
			    while(str != null){
			    	sb.append(str);
			    	str = br.readLine();
			    }
			    br.close();
			    output = sb.toString();
				conn.disconnect();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return output;
		}
}
