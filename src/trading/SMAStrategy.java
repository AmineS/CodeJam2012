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
            slowSMAValues[i] = 0;
            fastSMAValues[i] = 0;
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
        
       /* if(currentTick > 1)
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
                //write(currentTick,'D',price.GetPrice(currentTick));
            	System.out.println("Time: "+currentTick+" --  Nothing");
            }
        }
        else
        {
            fasterThenSlower = fastSMAValues[currentTick] > slowSMAValues[currentTick];
        }*/
        
        detectCross();
        
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
    
	private void detectCross(){
		if(fastSMAValues[currentTick] > slowSMAValues[currentTick] && slowSMAValues[currentTick-1] > fastSMAValues[currentTick-1]){
			// upward trend - report buy
//            write(curTick, 'B', Trader.getTrader().trade('B'));
			System.out.println("Time: "+currentTick+" --  Buy");
		}else if(fastSMAValues[currentTick] < slowSMAValues[currentTick] && slowSMAValues[currentTick-1] < fastSMAValues[currentTick-1]){
			// downward trend - report sell
//            write(curTick, 'S', Trader.getTrader().trade('S'));
            System.out.println("Time: "+currentTick+" --  Sell");
		}else{
			 // do nothing
//            write(curTick,'D',price.GetPrice(curTick));
            System.out.println("Time: "+currentTick+" --  Nothing");
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
        if(!FastGreaterThanSlow)
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
    
/*    public void test() {
		double[] ps = { 61.590, 61.440, 61.320, 61.670, 61.920, 62.610, 62.880,
				63.060, 63.290, 63.320, 63.260, 63.120, 62.240, 62.190, 62.890 };
		this.currentTick = 0;
		for (double d : ps) {
			price.SetPrice(this.currentTick, (float) d);
			runStrategy();
			System.out.println(fastSMAValues[currentTick-1]+" --- "+slowSMAValues[currentTick-1]);
		}
		for (int i = 0; i < ps.length; i++) {
			System.out.print(fastSMAValues[i]+" - ");
		}
		System.out.println();
		for (int i = 0; i < ps.length; i++) {
			System.out.print(slowSMAValues[i]+" - ");
		}
	}
    

	public static void main(String[] args) {
		Prices p = Prices.GetPrices();
		(new SMAStrategy(p)).test();
	}*/
}
