package program;

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
        
        Thread dispatcher;
        Thread trader; 
        Thread smaThread, lwmaThread, emaThread, tmaThread; 
        Thread jsonWriter; 
        
        // launch Manager Scheduling Algorithm         
        

        // launch Strategies 
        
        // launch Trader 
        
        // launch JSON Writer          
        
        // launch GUI 
        
        // launch Exchange Server 
        
        // launch Dispatcher 
        dispatcher = new Thread(new Dispatcher(PricesPort));
        dispatcher.start();
    }

}
