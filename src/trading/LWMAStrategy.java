
package trading;

public class LWMAStrategy extends AStrategy implements Runnable
{
	private final int SIZE = Prices.MAX_SECONDS;
	private int tick = 0;
	private int i, limit;
	private float[] slow, fast;
	private float sumI, sumPrices;

	private Prices prices;
	String strategy;
	
	public LWMAStrategy(Prices prices) {
		tick = 0;
		slow = new float[SIZE];
		fast = new float[SIZE];
		strategy = "LWMA";
		this.prices = prices;
	}
	
	public void run(){
    	while (tick < 32400){
    		runStrategy();
    		crossover(true);
        	// increment tick
        	tick++;
    	}
	}
	
    @Override
    public void runStrategy()
    {
    	// to calculate LWMA5
    	slow[tick] = compute(5);
    	// to calculate LWMA20
    	fast[tick] = compute(20);
    	/* DEBUG
        System.out.println("The LWMA5 is " + slow[tick]);
        System.out.println("The LWMA20 is " + fast[tick]);
        */
    }
	
    private float compute(int n){
    	sumI = 0;
    	sumPrices = 0;
		if (tick < n){
			limit = tick + 1;
		}
		else {
			limit = n;
		}
		for (i = 0; i < limit; i++) {
			// the sum actually starts at 1
			sumI += i + 1;
			// but to comply with the arrays, the index starts at 0
			sumPrices += prices.GetPrice(tick - limit + i + 1) * (i + 1);
		}
		return (float) (Math.round(sumPrices/sumI * 1000) / 1000.0);
    }

    @Override
    public int getTick()
    {
        return tick;
    }
    
    @Override
    public void crossover(boolean _)
    {
    	if (tick > 0){
    		if ((slow[tick - 1] > fast[tick - 1]) && (slow[tick] < fast[tick])){
     			// buy
     	    	write(tick, 'B', Trader.getTrader().trade('B'));
     		}
     		else if ((slow[tick - 1] < fast[tick - 1]) && (slow[tick] > fast[tick])){
     			// sell
     	    	write(tick, 'S', Trader.getTrader().trade('S'));
     		}
     		else {
     			// do nothing
     			write(tick, 'D', prices.GetPrice(tick));
     		}
    	}
    }
    
    public float[] getLWMAFastArr()
    {
        return fast;
    }
    
    public float[] getLWMASlowArr()
    {
        return slow;
    }

}


