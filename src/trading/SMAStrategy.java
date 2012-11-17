package trading;

public class SMAStrategy extends AStrategy implements Runnable
{
    
    @Override
    public void run()
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override 
    public void runStrategy(Prices prices)
    {
        computeSlowSMA(prices);
        computeFastSMA(prices);
        
        if(currentTick > 1)
        {
            boolean currentFasterThenSlower = fastSMAValues[currentTick] > slowSMAValues[currentTick];
            if(currentFasterThenSlower != fasterThenSlower)
            {
                crossover(currentFasterThenSlower);
            }
        }
        else
        {
            fasterThenSlower = fastSMAValues[currentTick] > slowSMAValues[currentTick];
        }
        
        currentTick++;
    }
    
    public float computeSlowSMA(Prices prices)
    {
        if (currentTick <= 5)
        {
            for(int i = 0; i < currentTick; i++)
            {
                slowSMAValues[currentTick] += prices.GetPrice(currentTick);
            }
            slowSMAValues[currentTick] /= currentTick;
        }
        else
        {
            slowSMAValues[currentTick] = slowSMAValues[currentTick - 1] + (prices.GetPrice(currentTick) - prices.GetPrice(currentTick - 5)) / 5;
        }
        
        return slowSMAValues[currentTick];
    }
    
    public float computeFastSMA(Prices prices)
    {
        if (currentTick <= 20)
        {
            for(int i = 0; i < currentTick; i++)
            {
                fastSMAValues[currentTick] += prices.GetPrice(currentTick);
            }
            fastSMAValues[currentTick] /= currentTick;
        }
        else
        {
            fastSMAValues[currentTick] = fastSMAValues[currentTick - 1]  + (prices.GetPrice(currentTick) - prices.GetPrice(currentTick - 20)) / 20;
        }
        
        return fastSMAValues[currentTick];
    }
    
    @Override
    public void crossover(boolean FastGreaterThanSlow)
    {
        if(FastGreaterThanSlow){
            // buy
        }else{
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

    
    private int currentTick;
    private boolean fasterThenSlower;
    public static float[] slowSMAValues;
    public static float[] fastSMAValues;

}
