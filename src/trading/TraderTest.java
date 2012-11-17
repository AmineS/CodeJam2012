package trading;

public class TraderTest implements Runnable
{
    Trader trader;
    
    public void run()
    {
        for(int i = 0; i < 10; ++i)
        {
            System.out.println("The buy value is " + trader.trade('B'));
            System.out.println("The sell value is " + trader.trade('S'));
            try
            {
                Thread.sleep(1);
            } catch (InterruptedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    public TraderTest(Trader trader_) 
    {
        trader = trader_; 
    }

}
