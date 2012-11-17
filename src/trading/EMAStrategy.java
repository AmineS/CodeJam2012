package trading;

public class EMAStrategy extends AStrategy {

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
		slow = new float[SIZE];
		fast = new float[SIZE];
		id = "EMA";
		this.price = prices;

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
		if (FastGreaterThanSlow != oldInv) {
			crossover(FastGreaterThanSlow);
		}

	}

	private float compute(int N) {
		int alpha = 2 / (N + 1);
		float ema = (N == slowN) ? slow[curTick - 1] : fast[curTick - 1];
		return (ema + alpha * (price.GetPrice(curTick) - ema));
	}

	@Override
	public int getTick() {
		return curTick;
	}

	@Override
	public void write(int tick, char type, float price, int strategy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void crossover(boolean FastGreaterThanSlow) {
		if (FastGreaterThanSlow) {
			// buy
		} else {
			// sell
		}
	}

	public void test() {
		double[] ps = { 61.590, 61.440, 61.320, 61.670, 61.920, 62.610, 62.880,
				63.060, 63.290, 63.320, 63.260, 63.120, 62.240, 62.190, 62.890 };
		this.curTick = 0;
		for (double d : ps) {
			price.SetPrice(this.curTick++, (float) d);
			runStrategy();
		}
		for (float p : fast) {
			System.out.println(p);
		}
	}

	public static void main(String[] args) {
		Prices p = Prices.GetPrices();
		(new EMAStrategy(p)).test();
	}

}
