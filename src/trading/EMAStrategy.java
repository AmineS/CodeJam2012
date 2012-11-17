package trading;

public class EMAStrategy extends AStrategy {

	static final int SIZE = Prices.MAX_SECONDS;
	static final float ERROR = (float) 0.001;

	// class variables
	int curTick = 0;
	int slowN = 20, fastN =5;
	float[] slow;
	float[] fast;
	String id;
	boolean FastGreaterThanSlow;
	int alpha;
	Prices price;

	public EMAStrategy(Prices prices) {
		curTick = 0;
		slow = new float[SIZE];
		fast = new float[SIZE];
		id = "EMA";
		this.price = prices;
		
	}

	@Override
    public void runStrategy()
    {
		if(curTick==0){
			slow[curTick] = price.GetPrice(curTick);
			fast [curTick] = slow[curTick];
			return;
		}
			slow[curTick] = compute(slowN);
			fast[curTick] = compute(fastN);
			if(curTick ==1){
				FastGreaterThanSlow = fast[curTick] > slow[curTick];
				return;
			}
			boolean oldInv = FastGreaterThanSlow;
			FastGreaterThanSlow = fast[curTick] > slow[curTick];
			if(FastGreaterThanSlow != oldInv){
				crossover(FastGreaterThanSlow);
			}

    }
	
	private float compute(int N){
		int alpha = 2/(N+1);
		float ema = (N == slowN) ? slow[curTick-1]: fast[curTick-1];
		return (ema + alpha*(price.GetPrice(curTick) - ema));
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
		if(FastGreaterThanSlow){
			// buy
		}else{
			// sell
		}
	}
	
	public void test(int prices){
		
	}
	
}
