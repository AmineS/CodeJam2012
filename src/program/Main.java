package program;

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
        Thread smaThread, lwmaThread, emaThread, tmaThread; 
        Thread jsonWriter; 
        
        // Price array 
        Prices prices = Prices.GetPrices();
        // setup trader 
        Trader.setTraderConnection(TradingPort);
        
        // launch Strategies
//         smaThread = new Thread(new SMAStrategy(prices));
//         tmaThread = new Thread(new TMAStrategy(prices));
         emaThread = new Thread(new EMAStrategy(prices));
//          lwmaThread = new Thread(new LWMAStrategy(prices));
       
//         smaThread.start();
//         tmaThread.start(); 
         emaThread.start();
//          lwmaThread.start();
        
        // launch JSON Writer          
        
        // launch GUI 
        
        // launch Exchange Server 
        
        // launch Dispatcher 
        dispatcherThread = new Thread(new Dispatcher(PricesPort));
        dispatcherThread.start();
        
        
        /*** TO DELETE
         * debugging code. 
         * Thread tester = new Thread(new TraderTest(Trader.getTrader()));
         * tester.start();  
        **/
    }

}
