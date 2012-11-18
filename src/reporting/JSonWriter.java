package reporting;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The JSonWriter
 * @author dbhage
 */
public class JSonWriter
{
    private ArrayList<Transaction> transactionList = null;
    private String email;
    
    private String JSONOutputString = "";
    
    /**
     * Constructor
     * @param tl - a transaction list and the destination email
     */
    public JSonWriter(ArrayList<Transaction> tl, String em)
    {
        email = em;
        transactionList = tl;
        System.out.println(tl.get(0).toJSON());
    }

    /**
     * Makes a string from all transactions
     * @return transactions as a string in JSON format
     * @throws InterruptedException 
     */
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
        transactionsString.setCharAt(transactionsString.length() -2 , ']');
        
        return transactionsString.toString();
    }
    
    /**
     * Generate the JSON file
     */
    public void generateOutput()
    {
	
        String transactions = transactionsJSONString();
        String newString = "{";
        newString += "\"team\": \"Team007\",";
        newString += "\"destination\": \"" + email + "\",";
        newString += "\"transactions\": " + transactions + "}";
        //JSONOutputString = newString;
        
        try
        {
            FileWriter file = new FileWriter("codejam.json");
            file.write(newString);
            file.flush();
            file.close();
        } 
        catch (IOException e)
        {
            System.out.println("Error while writing JSON output to file codejam.json");
        }
    }
    
    public String getOutputString()
    {
    	return JSONOutputString;
    }
}
