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

        TMAStrategy tma = new TMAStrategy(prices);
        EMAStrategy ema = new EMAStrategy(prices);
        LWMAStrategy lwma = new LWMAStrategy(prices);
        SMAStrategy sma = new SMAStrategy(prices);
        
         smaThread = new Thread(sma);
         tmaThread = new Thread(tma);
         emaThread = new Thread(ema);
         lwmaThread = new Thread(lwma);
       
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
        
        try
        {
            dispatcherThread.join();
            lwmaThread.join();
            emaThread.join();
            smaThread.join();
            tmaThread.join();
            tcThread.join();
        } catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        System.out.println("Woa");
        
        
        /*** TO DELETE
         * debugging code. 
         * Thread tester = new Thread(new TraderTest(Trader.getTrader()));
         * tester.start();  
        **/
    }

}
