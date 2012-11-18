package trading;

public abstract class AStrategy implements IStrategy
{
    private char[] typeWriteArray = new char[Prices.MAX_SECONDS];
    private float[] priceWriteArray = new float[Prices.MAX_SECONDS];
 
    private Object typeLock = new Object();
    private Object priceLock = new Object();
    
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
        synchronized(typeLock) 
        {                
            typeLock.notifyAll();
        }        
    }
    
    /**
     * Get the array of transaction types
     * @return
     */
    public char getTypeAtTick(int tt)
    {
        while(typeWriteArray[tick]=='N')
        {
            try
            {
                synchronized(typeLock) 
                {                
                        typeLock.wait();
             
                } 
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return typeWriteArray[tick];
    }
    
    /**
     * Get the array of prices
     * @return
     */
    public float getPriceAtTick(int tick)
    {
        //forces the thread to lock if the price is wrong 
        getTypeAtTick(tick);
        
        return priceWriteArray[tick];
    }

    
    public void crossover(boolean FastGreaterThanSlow)
    {
     // TODO Auto-generated method stub
    }
    
    public static float round(float x) {
        return ((float) Math.round(x * 1000) / 1000);
    }
    
    public char[] getTypeArr()
    {
        return typeWriteArray;
    }
}
