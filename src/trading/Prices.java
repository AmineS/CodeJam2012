package trading;
import java.util.concurrent.locks.Condition;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Prices
{

    public static int MAX_SECONDS = 32400; 
    private float[] prices;

    boolean stop = false;
    
    private Object lock = new Object();
    
    public static Prices GetPrices()
    {
        if(Prices.pricesInstance == null) Prices.pricesInstance = new Prices();
        return pricesInstance;
    }
    
    
    private static Prices pricesInstance = new Prices(); 
    
    private Prices()
    {
        prices = new float[Prices.MAX_SECONDS];
        for(int i = 0; i < prices.length; ++i)
        {
            prices[i] = -1f;
        }
    }
    
    public float GetPrice(int tick)
    {
        // if the price is not set then wait for it 
        while(prices[tick] < 0 )
        {
            try
            {
                synchronized(lock) 
                {                
                    lock.wait();
                }
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        
        return prices[tick];
    }
    
    public void SetPrice(int tick, float value)
    {
        prices[tick] = value; 
        synchronized(lock) 
        {                
            lock.notifyAll();
        }
    }

    public void printPrices()
    {
       for(int i = 0; i<prices.length; ++i)
       {
           System.out.println("The price at tick " + i + " is " + prices[i]);
       }
    }
    
    public  boolean getStop()
    {
        return toggleStop(false);
    }
    
    public void setStop(boolean stop_)
    {
        if(stop_ && !stop) toggleStop(true);
    }
    
    private synchronized boolean toggleStop(boolean toggle_)
    {
        if(toggle_) stop = !stop;        
        return stop;
    }
}
