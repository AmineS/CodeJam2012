package trading;
import java.util.concurrent.locks.Lock;

/**
 * Class for Triangular Moving Strategy (TMA)
 * @author dbhage
 */
public class TMAStrategy extends AStrategy implements Runnable
{
    /** SMA Values */
    private float[] SMAValuesSlow = new float[Prices.MAX_SECONDS];
    private float[] SMAValuesFast = new float[Prices.MAX_SECONDS];

    /** Values of N */
    private final int N_FAST = 5;
    private final int N_SLOW = 20;

    /** the tick we are at */
    private int currentTick = 0;

    /** TMA Values */
    private float[] TMAValuesSlow = new float[Prices.MAX_SECONDS];
    private float[] TMAValuesFast = new float[Prices.MAX_SECONDS];

    /** latest TMA Value - used when updating */
    private float latestTMAValueSlow = 0;
    private float latestTMAValueFast = 0;
    
    /** checking whether the faster TMA has a greater value than the slower TMA */
    private boolean fasterGTSlower = false;
  
    /** Prices object to get current price */
    private Prices prices = null;
    private boolean fasterThenSlower;
    /**
     * Constructor
     */
    public TMAStrategy(Prices priceList)
    {
       SMAValuesSlow = SMAStrategy.getSlow();
       SMAValuesFast = SMAStrategy.getFast();
       prices = priceList;
       for (int i=0;i<Prices.MAX_SECONDS;i++)
       {
           TMAValuesSlow[i] = -1;
           TMAValuesFast[i] = -1;
       }
    }
    
    /**
     * Run
     */
    public void run()
    {
        try
        {
            Thread.sleep(100);
        } catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        while(currentTick != Prices.MAX_SECONDS)
        {
            runStrategy();
        }
    }
    
    /**
     * Run the TMA strategy
     */
    public void runStrategy()
    {
        updateSlow(currentTick, N_SLOW);
        updateFast(currentTick, N_FAST);
        if(currentTick > 1)
        {
            boolean currentFasterThenSlower = SMAValuesFast[currentTick] > SMAValuesSlow[currentTick];
            if(currentFasterThenSlower != fasterThenSlower)
            {
                crossover(fasterThenSlower);
                fasterThenSlower = currentFasterThenSlower;
            }
            else
            {
                // do nothing
                write(currentTick,'D',prices.GetPrice(currentTick));
            }
        }
        else
        {
            fasterThenSlower = SMAValuesFast[currentTick] > SMAValuesSlow[currentTick];
        }
        
        ++currentTick;
    }
    
    /**
     * update TMA(20)
     * @param t
     */
    public void updateSlow(int t, int n)
    {
        if (t<n)
        {
            // calculation similar to LWMA
            if (t==0)
            {
                TMAValuesSlow[t] = SMAValuesSlow[t];
            }
            else
            {
                for (int k=0; k<=t; k++)
                {
                    TMAValuesSlow[t] += SMAValuesSlow[t];
                }
            }
            TMAValuesSlow[t] /= t+1;
            latestTMAValueSlow = TMAValuesSlow[t];
        }
        else
        {
            // TMA's formula
            latestTMAValueSlow = TMAValuesSlow[t-1];
            latestTMAValueSlow = latestTMAValueSlow - (SMAValuesSlow[t-n]/n) + (SMAValuesSlow[t]/n);
            TMAValuesSlow[t] = latestTMAValueSlow;
        }
        
        if (t<10)
        {
            System.out.println("SMA:"+SMAValuesSlow[t]);
            System.out.println("TMA:"+TMAValuesSlow[t]);
        }
        
    }
    
    /**
     * update TMA(5)
     * @param t
     */
    public void updateFast(int t, int n)
    {
        if (t<n)
        {
            if (t==0)
            {
                TMAValuesFast[t] = SMAValuesFast[t];
            }
            else
            {
                for (int k=0; k<=t; k++)
                {
                    TMAValuesFast[t] += SMAValuesFast[t];
                }
                TMAValuesFast[t] /= t+1;
            }
            latestTMAValueFast = TMAValuesFast[t];
        }
        else
        {
            // TMA's formula
            latestTMAValueFast = TMAValuesFast[t-1];
            latestTMAValueFast = latestTMAValueFast - (SMAValuesFast[t-n]/n) + (SMAValuesFast[t]/n);
            TMAValuesFast[t] = latestTMAValueFast;
        }
    }
    
    /**
     * Get the current tick
     * @return the current tick
     */
    public int getTick()
    {
        return currentTick;
    }

    /**
     * Check for crossover
     * @return 'B' for buy, 'S' for sell, 'D' for do nothing
     */
    public void crossover(boolean fastGreaterThanSlow)
    {
        if(fastGreaterThanSlow)
        {
            // buy
            write(currentTick, 'B', Trader.getTrader().trade('B'));            
        }
        else
        {
            // sell
            write(currentTick, 'S', Trader.getTrader().trade('S')); 
        }
    }  
}
