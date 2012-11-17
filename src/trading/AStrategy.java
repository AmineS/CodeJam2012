package trading;

public abstract class AStrategy implements IStrategy
{

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
    public void write(int tick, char type, float price, int strategy)
    {
        // TODO Auto-generated method stub

    }
    
    public void crossover(boolean FastGreaterThanSlow)
    {
     // TODO Auto-generated method stub
    }
}
