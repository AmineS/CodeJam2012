package program;

import org.json.JSONException;

import reporting.JSonWriter;
import reporting.TransactionCollector;
import trading.*;

public class Main
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        /*******************************
         * For testing only 
         * *****************************/        
        int PricesPort = 3000; 
        int TradingPort = 3001;                
        /************************************/
        
        Thread dispatcherThread;
        Thread traderThread; 
        Thread smaThread, lwmaThread, emaThread, tmaThread, tcThread; 
        Thread jsonWriter; 
        
        // Price array 
        Prices prices = Prices.GetPrices();
        // setup trader 
        Trader.setTraderConnection(TradingPort);
        
        // launch Strategies

        SMAStrategy sma = new SMAStrategy(prices);
        EMAStrategy ema = new EMAStrategy(prices);
        LWMAStrategy lwma = new LWMAStrategy(prices);
        TMAStrategy tma = new TMAStrategy(prices);
        
         smaThread = new Thread(sma);
         emaThread = new Thread(ema);
         lwmaThread = new Thread(lwma);
         tmaThread = new Thread(tma);
       
         smaThread.start();
         lwmaThread.start();
         emaThread.start();
         tmaThread.start(); 
        
         TransactionCollector tc = new TransactionCollector(sma, lwma, ema, tma);
         tcThread = new Thread(tc);
         tcThread.start();
         
         // launch Dispatcher 
         dispatcherThread = new Thread(new Dispatcher(PricesPort));
         dispatcherThread.start();
         
        try
        {
            smaThread.join();
            tmaThread.join();
            emaThread.join();
            lwmaThread.join();
            tcThread.join();
            dispatcherThread.join();
        } catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
         
         
         int buys=0,sells=0,d=0;
         char[] arr = ema.getTypeArr();
         for(int i=0; i<arr.length; i++)
         {
             if (arr[i]=='N')
             {
                 System.out.println("Error");
                 System.exit(-1);
             }
             
             if(arr[i]=='B')
             {
                 buys++;
             }
             else if(arr[i]=='S')
             {
                 sells++;
             }
             else if(arr[i]=='D')
             {
                 d++;
             }
         }
         System.out.println("Buys:" + buys + "\nSells:" + sells + "\ndonothings:" + d);
         
         
        // launch JSON Writer
         /*
         try
         {
             JSonWriter jsw = new JSonWriter(tc.getTransactionList(), "neerav789@gmail.com");
             jsw.generateOutput();
         }
         catch(JSONException ex)
         {
             ex.printStackTrace();
         }*/
         
        // launch GUI 
        
        // launch Exchange Server 
        System.out.println("Woa");
        

        
        
        /*** TO DELETE
         * debugging code. 
         * Thread tester = new Thread(new TraderTest(Trader.getTrader()));
         * tester.start();  
        **/
    }

}
