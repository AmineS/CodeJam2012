package trading;

/**
 * Class for Triangular Moving Strategy (TMA)
 * @author dbhage
 */
public class TMAStrategy extends AStrategy implements Runnable
{
    private float[] SMAValuesSlow = new float[Prices.MAX_SECONDS];
    private float[] SMAValuesFast = new float[Prices.MAX_SECONDS];
    private final int N_FAST = 5;
    private final int N_SLOW = 20;
    private int currentTick = 0;

    private float[] TMAValuesSlow = new float[Prices.MAX_SECONDS];
    private float[] TMAValuesFast = new float[Prices.MAX_SECONDS];
    private float latestTMAValueSlow = 0;
    private float latestTMAValueFast = 0;
    
    private boolean fasterGTSlower = false;
    
    /**
     * Constructor
     */
    public TMAStrategy()
    {
        SMAValuesSlow = SMAStrategy.getSlow();
        SMAValuesFast = SMAStrategy.getFast();
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
        if (currentTick != 0)
        {
            if (TMAValuesFast[currentTick] > TMAValuesSlow[currentTick])
            {
                fasterGTSlower = true;
            }
            else
            {
                fasterGTSlower = false;
            }
        }

        updateSlow(currentTick, N_SLOW);
        updateFast(currentTick, N_FAST);
        crossover(fasterGTSlower);
        currentTick++;
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
            for (int k=0; k<t; k++)
            {
                TMAValuesSlow[t] += SMAValuesSlow[t]/t;
            }
        }
        else
        {
            // TMA's formula
            latestTMAValueSlow = TMAValuesSlow[t-1];
            latestTMAValueSlow = latestTMAValueSlow - (SMAValuesSlow[t-n]/n) + (SMAValuesSlow[t]/n);
            TMAValuesSlow[t] = latestTMAValueSlow;
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
            // calculation similar to LWMA
            for (int k=0; k<t; k++)
            {
                TMAValuesFast[t] += SMAValuesFast[t]/t;
            }
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
     * 
     */
    public void write(int tick, char type, float price, int strategy)
    {
        // TODO Auto-generated method stub
    }

    /**
     * Check for crossover
     * @return 'B' for buy, 'S' for sell, 'D' for do nothing
     */
    public void crossover(boolean fastGreaterThanSlow)
    {
        if (fastGreaterThanSlow && TMAValuesFast[currentTick] < TMAValuesSlow[currentTick])
        {
            // downward trend - report sell
            System.out.println("Sell");            
        }
        else if(!fastGreaterThanSlow && TMAValuesFast[currentTick] > TMAValuesSlow[currentTick])
        {
            // upward trend - report buy
            System.out.println("Buy");            
        }
        else
        {
            // do nothing
            System.out.println("Do nothing");            
        }
    }  
}



