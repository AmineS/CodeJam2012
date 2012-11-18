package reporting;

import java.io.*;

import org.apache.http.HttpResponse;
import org.apache.http.client.*;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.*;
import org.apache.http.entity.*;


public class ESignLive
{
    private static String apiKey; 
    
    // default url 
    private static final String url = "https://stage-api.e-signlive.com/aws/rest/services/codejam";   
    
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
    
    public ESignLive(String apiKey_)
    {
        if(apiKey_ == null)
        {
            // default code jam key 
            apiKey = "Y29kZWphbTpBRkxpdGw0TEEyQWQx";
        }
        else
        {
            apiKey = apiKey_;
        }
    }
    
    public static String SignDocument(String document)
    {
    	String ceremonyID = "{}"; 
        HttpClient httpClient = new DefaultHttpClient();
        try
        {
            HttpPost postRequest = new HttpPost(url);
            postRequest.addHeader("Authorization", "Basic " + apiKey);
            StringEntity input = new StringEntity(document);
            input.setContentType("application/json");
            
            postRequest.setEntity(input);
            HttpResponse response = httpClient.execute(postRequest);
            
            if (response.getStatusLine().getStatusCode() != 201) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
            }
     
            BufferedReader reader = new BufferedReader(
                            new InputStreamReader((response.getEntity().getContent())));
     
            /** Debugging*/
            String output;
            ceremonyID = "";
            while ((output = reader.readLine()) != null) 
            {
                System.out.println(output);
                ceremonyID += output;
            }                       
            
        } catch (UnsupportedEncodingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClientProtocolException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }        
        return ceremonyID;
    }
}
