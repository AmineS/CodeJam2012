package trading;

public class SMAStrategy extends AStrategy implements Runnable
{
    private int currentTick;
    private boolean fasterThenSlower;
    private Prices price;
    public static float[] slowSMAValues;
    public static float[] fastSMAValues;
    private final int FAST_N = 5;
    private final int SLOW_N = 20;

    public SMAStrategy(Prices price)
    {
        slowSMAValues = new float[Prices.MAX_SECONDS];
        fastSMAValues = new float[Prices.MAX_SECONDS];
        this.price = price;
    }
    
    @Override
    public void run()
    {
        // TODO Auto-generated method stub
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
            }
        }
        else
        {
            fasterThenSlower = fastSMAValues[currentTick] > slowSMAValues[currentTick];
        }
        
        currentTick++;
    }
    
    public float computeSlowSMA()
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
        
        return slowSMAValues[currentTick];
    }
    
    public float computeFastSMA()
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
        
        return fastSMAValues[currentTick];
    }
    
    @Override
    public void crossover(boolean FastGreaterThanSlow)
    {
        if(FastGreaterThanSlow)
        {
            // buy
        }
        else
        {
            // sell
        }
    }
        
    @Override
    public int getTick()
    {
        return currentTick;
    }
    
    public static float[] getSlow()
    {
        return slowSMAValues;
    }
    
    public static float[] getFast()
    {
        return fastSMAValues;
    }

    public void write(int tick, char type, float actualPrice)
    {
        
    }
    
    

}
