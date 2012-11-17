package reporting;

/**
 * Transactions
 * Note: this class has a natural ordering that is inconsistent with equals
 * @author dbhage
 *
 */
public class Transaction implements Comparable<Transaction>
{
    private String manager;
    private char type;
    private float price;
    private int time, strategy;    
    
    /**
     * Constructor
     * @param ti - time
     * @param ty - type
     * @param p - price
     * @param m - manager
     * @param s - strategy
     */
    public Transaction(int ti, char ty, float p, String m, int s)
    {
        time = ti;
        type = ty;
        price = p;
        manager = m;
        strategy = 4;
    }
    
    @Override
    /**
     * Compare two transactions based on the time at which they occurred
     * @param t - the transaction object to compare to
     * @return -1 if this object less than t
     *          1 if this object greater than t
     *          0 if this object equal to t
     * @note (x.compareTo(y)==0) != (x.equals(y))
     */
    public int compareTo(Transaction t)
    {
        if (this.time < t.time)
        {
            return -1;
        }
        else if (this.time > t.time)
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }
    
    /**
     * Get the transaction fields as strings
     * @return an array of strings
     */
    public String[] getTransactionAsStrArray()
    {
        String[] transactionAsStr = new String[5];
        transactionAsStr[0] = this.time + "";
        transactionAsStr[1] = this.type + "";
        transactionAsStr[2] = this.price + "";
        transactionAsStr[3] = this.manager + "";
        transactionAsStr[4] = this.strategy + "";
        return transactionAsStr;
    }
}
