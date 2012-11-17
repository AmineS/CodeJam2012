package trading;

public abstract class AStrategy implements IStrategy
{
    private char[] typeWriteArray = new char[Prices.MAX_SECONDS];
    private float[] priceWriteArray = new float[Prices.MAX_SECONDS];
 
    public AStrategy()
    {
        for (int i=0;i<typeWriteArray.length;i++)
        {
            typeWriteArray[i] = 'N';
        }
    }
    
    @Override
    public void runStrategy()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public int getTick()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    /**
     * Write -1 if not taken care of yet, -2 if taken care of (no crossover)
     */
    public void write(int tick, char type, float actualPrice)
    {
        typeWriteArray[tick] = type;
        priceWriteArray[tick] = actualPrice;
    }
    
    /**
     * Get the array of transaction types
     * @return
     */
    public char getTypeAtTick(int tick)
    {
        return typeWriteArray[tick];
    }
    
    /**
     * Get the array of prices
     * @return
     */
    public float getPriceAtTick(int tick)
    {
        return priceWriteArray[tick];
    }

    
    public void crossover(boolean FastGreaterThanSlow)
    {
     // TODO Auto-generated method stub
    }
}
