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
	private boolean FastGreaterThanSlow;
	private int alpha;
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
		if (curTick == 1) {
			FastGreaterThanSlow = fast[curTick] > slow[curTick];
			return;
		}
		boolean oldInv = FastGreaterThanSlow;
		FastGreaterThanSlow = fast[curTick] > slow[curTick];
		cross(FastGreaterThanSlow, oldInv);

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
		
	private void cross(boolean FastGreaterThanSlow, boolean oldFastGreaterThanSlow ){
		if(FastGreaterThanSlow == oldFastGreaterThanSlow){
			  // do nothing
            write(curTick,'D',price.GetPrice(curTick));
		}else if(!FastGreaterThanSlow){
			// downward trend - report sell
            write(curTick, 'S', Trader.getTrader().trade('S')); 
		}else{
			// upward trend - report buy
            write(curTick, 'B', Trader.getTrader().trade('B')); 
		}
	}

	public static float round(float x) {
		return ((float) Math.round(x * 1000) / 1000);
	}

	public void run() {
		while (curTick != Prices.MAX_SECONDS) {
			runStrategy();
		}
	}
	
	
}
