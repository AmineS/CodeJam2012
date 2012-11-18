package program;
import java.io.*; 
import java.net.*;
import trading.*;

public class Dispatcher implements Runnable 
{
    Socket pricesSocket;
    BufferedReader pricesStream; 
    StringBuffer priceString; 
    PrintWriter output;
    int tick;
        
    public void run()
    {
        Prices prices = Prices.GetPrices();
                
        try
        {
            output.println('H');
            char nextChar = (char) pricesStream.read();
            while(nextChar!='C')
            {
                if(nextChar=='|')
                {
                   prices.SetPrice(tick, Float.parseFloat(priceString.toString()));
                   priceString.delete(0, priceString.length());
                   ++tick;
                }
                else
                {
                    priceString.append(nextChar);
                }     
                
                nextChar = (char) pricesStream.read();                
            }
            Trader.closeTraderConnection();
            prices.setStop(true);
            pricesStream.close();
            output.close();
            pricesSocket.close();
//            Trader.closeTraderConnection();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        
        /******
         * DEBUGGING.  DELETE AFTER
         */
/*         System.out.println("The final prices were:");
         prices.printPrices();*/
        /******/
    }

    // opens a socket with the given port number 
    public Dispatcher(int port)
    {
        try
        {
            pricesSocket = new Socket("localhost", port);
            pricesStream = new BufferedReader(new InputStreamReader(pricesSocket.getInputStream()));
            priceString = new StringBuffer();
            output = new PrintWriter(pricesSocket.getOutputStream(), true);
            tick = 0;
        }
        catch(UnknownHostException unknownHost)
        {
            System.err.println("ERROR: Unknown host connection attempted!");
        }
        catch(IOException ioException)
        {
            ioException.printStackTrace();
        }
    }
}
