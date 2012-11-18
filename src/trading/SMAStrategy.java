package trading;

public class SMAStrategy extends AStrategy implements Runnable
{
    private int currentTick;
    private boolean fasterThenSlower;
    private Prices price;
    private float[] slowSMAValues;
    private float[] fastSMAValues;
    private final int FAST_N = 5;
    private final int SLOW_N = 20;

    public SMAStrategy(Prices price)
    {
        slowSMAValues = new float[Prices.MAX_SECONDS];
        fastSMAValues = new float[Prices.MAX_SECONDS];
        for (int i=0;i<Prices.MAX_SECONDS;i++)
        {
            slowSMAValues[i] = -1;
            fastSMAValues[i] = -1;
        }
        this.price = price;
    }
    
    @Override
    public void run()
    {
        while(currentTick!=Prices.MAX_SECONDS)
        {
            runStrategy();
        }
    }
    
    @Override 
    public void runStrategy()
    {
        computeSlowSMA();
        computeFastSMA();
        
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
                write(currentTick,'D',price.GetPrice(currentTick));
            }
        }
        else
        {
            fasterThenSlower = fastSMAValues[currentTick] > slowSMAValues[currentTick];
        }
        
        ++currentTick;
    }
    
    public void computeSlowSMA()
    {
        if (currentTick < SLOW_N)
        {
            for (int i = 0; i <= currentTick; i++)
            {
                slowSMAValues[currentTick] += price.GetPrice(i);
            }
            if (currentTick > 0)
            {
                slowSMAValues[currentTick] /= currentTick + 1;
            }
        }
        else
        {
            slowSMAValues[currentTick] = slowSMAValues[currentTick - 1] + (price.GetPrice(currentTick) - price.GetPrice(currentTick - SLOW_N)) / SLOW_N;
        }
    }
    
    public void computeFastSMA()
    {
        if (currentTick < FAST_N)
        {
            for (int i = 0; i <= currentTick; i++)
            {
                fastSMAValues[currentTick] += price.GetPrice(i);
            }
            if (currentTick != 0)
            {
                fastSMAValues[currentTick] /= currentTick + 1;
            }
        }
        else
        {
            fastSMAValues[currentTick] = fastSMAValues[currentTick - 1]  + (price.GetPrice(currentTick) - price.GetPrice(currentTick - FAST_N)) / FAST_N;
        }
    }
    
    @Override
    public void crossover(boolean FastGreaterThanSlow)
    {
        if(FastGreaterThanSlow)
        {
            // buy
//            write(currentTick, 'B', Trader.getTrader().trade('B'));            
        }
        else
        {
            // sell
//            write(currentTick, 'S', Trader.getTrader().trade('S')); 
        }
    }
        
    @Override
    public int getTick()
    {
        return currentTick;
    }
    
    public float getSMAFastValue(int t)
    {
        return fastSMAValues[t];
    }
    
    public float getSMASlowValue(int t)
    {
        return slowSMAValues[t];
    }
}
