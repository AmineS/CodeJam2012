package trading;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Trader
{
    Socket pricesSocket;
    BufferedReader transactionPriceStream; 
    StringBuffer priceString; 
    PrintWriter transactionStream;
    
    /**
     * Thread safe method that initiates a buy or sell trade communication with the exchange server.
     * If an invalid trade operation is specified (other than 'B' or 'S' or if the server is closed) 
     * a -1.0 float is returned. 
     * @param t character value has to be 'B' or 'S' 
     * @return
     */
    public synchronized float trade(char t)
    {               
        float price = -1f;
        if(!(t=='B' || t=='S') || Prices.GetPrices().getStop()) return -1f;                
        try 
        {
            transactionStream.println(t);
            
            char nextChar = (char)transactionPriceStream.read();
            if(nextChar == 'E') return -1f;
            
            while(nextChar != '.')
            {
                priceString.append(nextChar);
                nextChar = (char)transactionPriceStream.read();                
            }           
                        
            priceString.append(nextChar);
            
            // read the next three characters after the decimal
            priceString.append((char)transactionPriceStream.read());
            priceString.append((char)transactionPriceStream.read());
            priceString.append((char)transactionPriceStream.read());
            
            price = Float.parseFloat(priceString.toString());
            priceString.delete(0, priceString.length());        
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return price;
    }
    
    public static void setTraderConnection(int port)
    {
        try
        {
            Trader trader = Trader.getTrader();        
            trader.pricesSocket = new Socket("localhost", port);
            trader.transactionPriceStream = new BufferedReader(new InputStreamReader(trader.pricesSocket.getInputStream()));
            trader.priceString = new StringBuffer();
            trader.transactionStream = new PrintWriter(trader.pricesSocket.getOutputStream(), true);

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
    }              
    public static Trader getTrader()
    {
        if(Trader.traderInstance == null) Trader.traderInstance = new Trader(); 
        return Trader.traderInstance;
    }
    
    private static Trader traderInstance;
    private Trader()
    {                     
    }
}
