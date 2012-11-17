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
        
        // launch Manager Scheduling Algorithm         
        

        // launch Strategies 
        
        // setup trader 
        Trader.setTraderConnection(TradingPort);
        
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
