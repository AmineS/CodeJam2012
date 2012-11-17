package trading;

/**
 * Class for Triangular Moving Strategy (TMA)
 * @author dbhage
 */
public class TMAStrategy extends AStrategy implements Runnable
{
    /** Float Arrays for SMA Values */
    private float[] slowSMAValues = new float[Prices.MAX_SECONDS];
    private float[] fastSMAValues = new float[Prices.MAX_SECONDS];

    /** Values of N */
    private final int N_FAST = 5;
    private final int N_SLOW = 20;

    /** the tick we are at */
    private int currentTick = 0;

    /** Float Arrays for TMA Values */
    private float[] slowTMAValues = new float[Prices.MAX_SECONDS];
    private float[] fastTMAValues = new float[Prices.MAX_SECONDS];

    /** latest TMA Value - used when updating */
    private float latestTMAValueSlow = 0;
    private float latestTMAValueFast = 0;
    
    /** checking whether the faster TMA has a greater value than the slower TMA */
    private boolean fasterThenSlower;
  
    /** Prices object to get current price */
    private Prices prices = null;
    
    /**
     * Constructor
     */
    public TMAStrategy(Prices priceList)
    {
       prices = priceList;
       for (int i=0;i<Prices.MAX_SECONDS;i++)
       {
           slowTMAValues[i] = -1;
           fastTMAValues[i] = -1;
       }
    }
    
    /**
     * Run
     */
    public void run()
    {
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
        computeSlowSMA();
        computeFastSMA();
        
        computeSlowTMA(currentTick, N_SLOW);
        computeFastTMA(currentTick, N_FAST);

        if(currentTick > 1)
        {
            boolean currentFasterThenSlower = fastSMAValues[currentTick] > slowSMAValues[currentTick];
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
            fasterThenSlower = fastSMAValues[currentTick] > slowSMAValues[currentTick];
        }
        
        ++currentTick;
    }
    
    /**
     * update TMA(20)
     * @param t
     */
    public void computeSlowTMA(int t, int n)
    {
        if (t<n)
        {
            // calculation similar to LWMA
            if (t==0)
            {
                slowTMAValues[t] = slowSMAValues[t];
            }
            else
            {
                for (int k=0; k<=t; k++)
                {
                    slowTMAValues[t] += slowSMAValues[t];
                }
            }
            slowTMAValues[t] /= t+1;
            latestTMAValueSlow = slowTMAValues[t];
        }
        else
        {
            // TMA's formula
            latestTMAValueSlow = slowTMAValues[t-1];
            latestTMAValueSlow = latestTMAValueSlow - (slowSMAValues[t-n]/n) + (slowSMAValues[t]/n);
            slowTMAValues[t] = latestTMAValueSlow;
        }
        
        if (t<10)
        {
            System.out.println("SMA:"+slowSMAValues[t]);
            System.out.println("TMA:"+slowTMAValues[t]);
        }
        
    }
    
    /**
     * update TMA(5)
     * @param t - the tick
     * @param n - N
     */
    public void computeFastTMA(int t, int n)
    {
        if (t<n)
        {
            if (t==0)
            {
                fastTMAValues[t] = fastSMAValues[t];
            }
            else
            {
                for (int k=0; k<=t; k++)
                {
                    fastTMAValues[t] += fastSMAValues[t];
                }
                fastTMAValues[t] /= t+1;
            }
            latestTMAValueFast = fastTMAValues[t];
        }
        else
        {
            // TMA's formula
            latestTMAValueFast = fastTMAValues[t-1];
            latestTMAValueFast = latestTMAValueFast - (fastSMAValues[t-n]/n) + (fastSMAValues[t]/n);
            fastTMAValues[t] = latestTMAValueFast;
        }
    }
    
    /**
     * Compute Slow SMA
     */
    public void computeSlowSMA()
    {
        if (currentTick < N_SLOW)
        {
            for (int i = 0; i <= currentTick; i++)
            {
                slowSMAValues[currentTick] += prices.GetPrice(i);
            }
            if (currentTick > 0)
            {
                slowSMAValues[currentTick] /= currentTick + 1;
            }
        }
        else
        {
            slowSMAValues[currentTick] = slowSMAValues[currentTick - 1] + (prices.GetPrice(currentTick) - prices.GetPrice(currentTick - N_SLOW)) / N_SLOW;
        }
    }
    
    /**
     * Compute Fast SMA
     */
    public void computeFastSMA()
    {
        if (currentTick < N_FAST)
        {
            for (int i = 0; i <= currentTick; i++)
            {
                fastSMAValues[currentTick] += prices.GetPrice(i);
            }
            if (currentTick != 0)
            {
                fastSMAValues[currentTick] /= currentTick + 1;
            }
        }
        else
        {
            fastSMAValues[currentTick] = fastSMAValues[currentTick - 1]  + (prices.GetPrice(currentTick) - prices.GetPrice(currentTick - N_FAST)) / N_FAST;
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
