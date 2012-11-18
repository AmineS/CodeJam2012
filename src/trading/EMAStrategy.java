package trading;

public class EMAStrategy extends AStrategy implements Runnable {

	private final int SIZE = Prices.MAX_SECONDS;
	private final float ERROR = (float) 0.001;

	// class variables
	private int curTick = 0;
	private int slowN = 20, fastN = 5;
	private float[] slow;
	private float[] fast;
	private String id;
	private Prices price;

	public EMAStrategy(Prices prices) {
		curTick = 0;
		slow = init();
		fast = init();
		id = "EMA";
		this.price = prices;

	}

	private float[] init() {
		float[] a = new float[SIZE];
		for (int i = 0; i < a.length; ++i) {
			a[i] = -1f;
		}
		return a;
	}

	@Override
	public void runStrategy() {
		if (curTick == 0) {
			slow[curTick] = price.GetPrice(curTick);
			fast[curTick] = slow[curTick];
			return;
		}
		slow[curTick] = compute(slowN);
		fast[curTick] = compute(fastN);
		detectCross();

	}

	private float compute(int N) {
		float alpha = (float) (2.0 / (float) (N + 1));
		float ema = (N == slowN) ? slow[curTick - 1] : fast[curTick - 1];
		float pt = price.GetPrice(curTick);
		float res = ema + alpha * (pt - ema);
		return round(res);
	}

	@Override
	public int getTick() {
		return curTick;
	}
	
	public static float round(float x) {
		return ((float) Math.round(x * 1000) / 1000);
	}
		
	private void detectCross(){
		if(fast[curTick] > slow[curTick] && slow[curTick-1] > fast[curTick-1]){
			// upward trend - report buy
//            write(curTick, 'B', Trader.getTrader().trade('B'));
			System.out.println("Time: "+curTick+" --  Buy");
		}else if(fast[curTick] < slow[curTick] && slow[curTick-1] < fast[curTick-1]){
			// downward trend - report sell
//            write(curTick, 'S', Trader.getTrader().trade('S'));
            System.out.println("Time: "+curTick+" --  Sell");
		}else{
			 // do nothing
//            write(curTick,'D',price.GetPrice(curTick));
            System.out.println("Time: "+curTick+" --  Nothing");
		}
	}

	public void run() {
		while (curTick != Prices.MAX_SECONDS) {
			runStrategy();
			++curTick;
		}
	}
	
/*	public void test() {
		double[] ps = { 61.590, 61.440, 61.320, 61.670, 61.920, 62.610, 62.880,
				63.060, 63.290, 63.320, 63.260, 63.120, 62.240, 62.190, 62.890 };
		this.curTick = 0;
		for (double d : ps) {
			price.SetPrice(this.curTick, (float) d);
			runStrategy();
			System.out.println(fast[curTick]+" --- "+slow[curTick]);
			curTick++;
		}
		for (int i = 0; i < ps.length; i++) {
			System.out.print(fast[i]+" - ");
		}
		System.out.println();
		for (int i = 0; i < ps.length; i++) {
			System.out.print(slow[i]+" - ");
		}
	}

	public static void main(String[] args) {
		Prices p = Prices.GetPrices();
		(new EMAStrategy(p)).test();
	}*/
	


	public float getEMAFastValue(int t)
    {
        return fast[t];
    }
    
	public float getEMASlowValue(int t)
	{
        return slow[t];
    }
}
