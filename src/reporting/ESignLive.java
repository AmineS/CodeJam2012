package reporting;

import java.io.*;
import java.net.*;

import org.apache.http.HttpResponse;
import org.apache.http.client.*;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.*;
import org.apache.http.entity.*;


public class ESignLive
{
    private String apiKey; 
    
    // default url 
    private String url = "https://stage-api.e-signlive.com/aws/rest/services/codejam";   
    private HttpURLConnection connection;
    
    public ESignLive(String apiKey_)
    {
        if(apiKey_==null)
        {
            // default code jam key 
            apiKey = "Y29kZWphbTpBRkxpdGw0TEEyQWQx";
        }
        else
        {
            apiKey = apiKey_;
        }
    }
    
    public String SignDocument(String document)
    {        
        String ceremonyID = "{}"; 
        HttpClient httpClient = new DefaultHttpClient();
        try
        {
            HttpPost postRequest = new HttpPost(url);
            StringEntity input = new StringEntity(document);
            input.setContentType("application/json");
            
            postRequest.setEntity(input);
            HttpResponse response = httpClient.execute(postRequest);
            
            if (response.getStatusLine().getStatusCode() != 201) {
                throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatusLine().getStatusCode());
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
