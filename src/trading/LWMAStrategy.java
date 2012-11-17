
package trading;

public class LWMAStrategy extends AStrategy implements Runnable
{
	static final int SIZE = Prices.MAX_SECONDS;
	private static int tick = 1;
	private int i, limit;
	private float[] slow, fast;
	private float sumI, sumPrices;
	private float numerator;
	private int denominator;
	private Prices prices;
	String strategy;
	
	public LWMAStrategy(Prices prices) {
		slow = new float[SIZE];
		fast = new float[SIZE];
		strategy = "LWMA";
		this.prices = prices;
	}
	
	public void run(){
    	while (tick <= 32400){
    		runStrategy();
    	}
	}
	
    @Override
    public void runStrategy()
    {
    	// to calculate LWMA5
    	slow[tick] = compute(5);
    	// to calculate LWMA20
    	fast[tick] = compute(20);
    	// increment tick
    	tick++;
    	/* DEBUG
        System.out.println("The LWMA5 is " + slow[tick]);
        System.out.println("The LWMA20 is " + fast[tick]);
        */
    }
    
    private float compute(int n){
    	sumI = 0;
    	sumPrices = 0;
		if (tick < n){
			limit = tick;
		}
		else {
			limit = n;
		}
		for (i = 0; i < limit; i++) {
			// the sum actually starts at 1
			sumI += i + 1;
			// but to comply with the arrays, the index starts at 0
			sumPrices += prices.GetPrice(tick - limit + i) * (i + 1);
		}
		return sumPrices/sumI;
    }
    
    /*
    private float betterCompute(int n){
		if (tick < n){
			numerator = (slow[tick - 1] * denominator + prices.GetPrice(tick) * tick);
			denominator += tick;
		}
		else {
			numerator = (slow[tick - 1] * denominator - prices.GetPrice(tick - n - 1) * (tick - n - 1) + prices.GetPrice(tick) * tick);
			denominator += n;
		}
		return numerator / denominator;
    }
    */

    @Override
    public int getTick()
    {
        return tick;
    }

    @Override
    public void write(int tick, char type, float price, int strategy)
    {
        // TODO Auto-generated method stub
    }
    
    @Override
    public void crossover(boolean _)
    {
     	if ((slow[tick - 1] > fast[tick - 1]) && (slow[tick] < fast[tick])){
     		// buy
     	}
     	else if ((slow[tick - 1] < fast[tick - 1]) && (slow[tick] > fast[tick])){
     		// sell
     	}
     	else {
     		// do nothing
     	}
    }

}


