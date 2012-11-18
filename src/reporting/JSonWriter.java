package reporting;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The JSonWriter
 * @author dbhage
 */
public class JSonWriter
{
    private ArrayList<Transaction> transactionList = null;
    private JSONObject jsonObj;
    private String[] labels = {"time", "type", "price", "manager", "strategy"};
    
    /**
     * Constructor
     * @param tl - a transaction list and the destination email
     */
    public JSonWriter(ArrayList<Transaction> tl, String email) throws JSONException
    {
        transactionList = tl;
        jsonObj = new JSONObject();
        jsonObj.put("team","Team007");
        jsonObj.put("destination", email);
    }

    private String transactionsJSONString()
    {
        StringBuilder transactionsString = new StringBuilder();
        
        transactionsString.append("[");
        
        for (Transaction t: transactionList)
        {   
            transactionsString.append(t.toJSON());
            transactionsString.append(", ");
        }
        
        // replace last comma with [ 
        transactionsString.setCharAt(transactionsString.length() -1 , ']');
        
        return transactionsString.toString();
    }
    
    /**
     * Generate the JSON file
     */
    public void generateOutput() throws JSONException
    {
        JSONObject tList = new JSONObject();
        String[] tString;
        StringBuilder transactionsString = new StringBuilder();
        JSONArray arr = new JSONArray();
        transactionsString.append("[");
        
        for (Transaction t: transactionList)
        {   
            transactionsString.append(t.toJSON());
            transactionsString.append(", ");
            
            
/*            tString = t.getTransactionAsStrArray(); 
            for (int i=0;i<tString.length;i++)
            {
                //arr.put(labels[i], tString[i]);
            }*/
        }
        
        // replace last comma with [ 
        transactionsString.setCharAt(transactionsString.length() -1 , ']');
        
        System.out.println(transactionsString.toString());
        jsonObj.put("transactions", transactionsString.toString());
        
        try
        {
            FileWriter file = new FileWriter("codejam.json");
            file.write(jsonObj.toString());
            file.flush();
            file.close();
        } 
        catch (IOException e)
        {
            System.out.println("Error while writing JSON output to file codejam.json");
        }
    }
}
