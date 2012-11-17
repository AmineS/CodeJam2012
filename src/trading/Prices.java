package trading;

public class Prices
{

    private static int MAX_SECONDS = 32400; 
    private float[] prices;

    
    public static Prices GetPrices()
    {
        if(Prices.pricesInstance == null) Prices.pricesInstance = new Prices();
        return pricesInstance;
    }
    
    
    private static Prices pricesInstance = new Prices(); 
    private Prices()
    {
        prices = new float[Prices.MAX_SECONDS];
        for(int i=0; i<prices.length; ++i)
        {
            prices[i] = -1f;
        }
    }
    
    public float GetPrice(int tick)
    {
        return prices[tick];
    }  
    public void SetPrice(int tick, float value)
    {
        prices[tick] = value; 
    }

    public void printPrices()
    {
       for(int i = 0; i<prices.length; ++i)
       {
           System.out.println("The price at tick " + i + " is " + prices[i]);
       }
    }
}
