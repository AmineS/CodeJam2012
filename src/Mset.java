
import dispatcher.Dispatcher;
import trading.*;
import scheduling.*;
import reporting.*;


/**
 * @author dbhage
 *
 */
public class Mset
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        int TradingPort = 3001;                
        int PricesPort = 3000;

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

        // launch JSON Writer
        JSonWriter jsw = new JSonWriter(tc.getTransactionList(), "neerav789@gmail.com");
        jsw.generateOutput();

        // launch GUI 

        // launch Exchange Server 
    }
}


