package trading;

public class Prices
{
    static int MAX_SECONDS = 32400; 
    private float[] prices = new float[MAX_SECONDS];
    
    public static Prices GetPrices()
    {
        return pricesInstance;
    }
    
    
    private static Prices pricesInstance = new Prices(); 
    private Prices()
    {
    }
    
    public float GetPrice(int tick)
    {
        return prices[tick];
    }
    
    public void SetPrice(int tick, float value)
    {
        prices[tick] = value; 
    }

}
