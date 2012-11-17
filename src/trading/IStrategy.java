package trading;

public interface IStrategy
{
    void runStrategy(Prices prices);
    int getTick(); 
    void write(int tick, char type, float price, int strategy);
}