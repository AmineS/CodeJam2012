package trading;

public class SMAStrategy extends AStrategy implements Runnable
{
    
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
        if (currentTick <= SLOW_N)
        {
            for(int i = 0; i < currentTick; i++)
            {
                slowSMAValues[currentTick] += price.GetPrice(currentTick);
            }
            slowSMAValues[currentTick] /= currentTick;
        }
        else
        {
            slowSMAValues[currentTick] = slowSMAValues[currentTick - 1] + (price.GetPrice(currentTick) - price.GetPrice(currentTick - SLOW_N)) / SLOW_N;
        }
        
        return slowSMAValues[currentTick];
    }
    
    public float computeFastSMA()
    {
        if (currentTick <= FAST_N)
        {
            for(int i = 0; i < currentTick; i++)
            {
                fastSMAValues[currentTick] += price.GetPrice(currentTick);
            }
            fastSMAValues[currentTick] /= currentTick;
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
    
    public void test(){
        double[] ps = {61.590, 61.440, 61.320, 61.670, 61.920, 62.610, 62.880, 63.060, 63.290, 63.320, 63.260, 63.120, 62.240, 62.190, 62.890};
        int tick =0;
        for(double d : ps){
            price.SetPrice(tick++, (float) d);
            runStrategy();
        }
        for(float p: fastSMAValues){
            System.out.println(p);
        }
    }

    private int currentTick;
    private boolean fasterThenSlower;
    private Prices price;
    public static float[] slowSMAValues;
    public static float[] fastSMAValues;
    private final int FAST_N = 5;
    private final int SLOW_N = 20;

}
