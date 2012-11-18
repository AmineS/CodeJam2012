package program;
import java.io.*; 
import java.net.*;
import java.util.ArrayList;

import trading.*;

public class Dispatcher implements Runnable 
{
    Socket pricesSocket;
    BufferedReader pricesStream; 
    StringBuffer priceString; 
    PrintWriter output;
    int tick;
    ArrayList<Thread> threads;
    
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
            
            prices.setStop(true);
            pricesStream.close();
            output.close();
            pricesSocket.close();
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
    
    public void addThread(Thread t)
    {
        threads.add(t);
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
            threads = new ArrayList<Thread>();
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
