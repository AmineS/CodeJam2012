package reporting;

import java.util.ArrayList;
import java.util.Collections;

import trading.EMAStrategy;
import trading.LWMAStrategy;
import trading.Prices;
import trading.SMAStrategy;
import trading.TMAStrategy;
import reporting.Transaction;

/**
 * Collect transactions from each strategy's buffer
 * @author dbhage
 */
public class TransactionCollector implements Runnable
{
    private boolean doneCollecting = false;
    private int SMACurrentIndex = 0, LWMACurrentIndex = 0, EMACurrentIndex = 0, TMACurrentIndex = 0;
    private SMAStrategy sma;
    private LWMAStrategy lwma;
    private EMAStrategy ema;
    private TMAStrategy tma;
    
    /** ArrayList of Transactions */
    ArrayList<Transaction> transactionList;
    
    /**
     * Constructor
     * @param s
     * @param l
     * @param e
     * @param t
     */
    public TransactionCollector(SMAStrategy s, LWMAStrategy l, EMAStrategy e, TMAStrategy t)
    {
        sma = s;
        lwma = l;
        ema = e;
        tma = t;
        transactionList = new ArrayList<Transaction>();
    }
    
    @Override
    public void run()
    {
        while(!doneCollecting)
        {
            if (SMACurrentIndex!=Prices.MAX_SECONDS)
                collectSMA();
            if (LWMACurrentIndex!=Prices.MAX_SECONDS)
                collectLWMA();
            if (EMACurrentIndex!=Prices.MAX_SECONDS)
                collectEMA();
            if (TMACurrentIndex!=Prices.MAX_SECONDS)
                collectTMA();
            
            if (SMACurrentIndex == Prices.MAX_SECONDS && LWMACurrentIndex == Prices.MAX_SECONDS && EMACurrentIndex == Prices.MAX_SECONDS && TMACurrentIndex == Prices.MAX_SECONDS)
            {
                doneCollecting = true;
            }
        }
    }
    
    /**
     * Get the transaction list
     * @return the sorted transaction list
     */
    public ArrayList<Transaction> getTransactionList()
    {
        Collections.sort(transactionList);
        return transactionList;
    }
    
    /**
     * Collect SMA transactions
     */
    public void collectSMA()
    {
        if (sma.getTypeAtTick(SMACurrentIndex)=='N')
        {
            // this tick has not been taken care of yet, so do nothing.
            return;
        }
        else if (sma.getTypeAtTick(SMACurrentIndex)=='D')
        {
            // this tick has been taken care of but there was no transaction, so move to next
            SMACurrentIndex++;
            return;
        }
        else if (sma.getTypeAtTick(SMACurrentIndex)=='B')
        {
            // a buy occurred at this tick
            transactionList.add(new Transaction(SMACurrentIndex, 'B', sma.getPriceAtTick(SMACurrentIndex), "", 4));
            SMACurrentIndex++;
            return;
        }
        else if (sma.getTypeAtTick(SMACurrentIndex)=='S')
        {
            // a sell occurred at this tick
            transactionList.add(new Transaction(SMACurrentIndex, 'S', sma.getPriceAtTick(SMACurrentIndex), "", 4));
            SMACurrentIndex++;
            return;
        }
    }
    
    /**
     * Collect LWMA transactions
     */
    public void collectLWMA()
    {
        if (lwma.getTypeAtTick(LWMACurrentIndex)=='N')
        {
            // this tick has not been taken care of yet, so do nothing.
            return;
        }
        else if (lwma.getTypeAtTick(LWMACurrentIndex)=='D')
        {
            // this tick has been taken care of but there was no transaction, so move to next
            LWMACurrentIndex++;
            return;
        }
        else if (lwma.getTypeAtTick(LWMACurrentIndex)=='B')
        {
            // a buy occurred at this tick
            transactionList.add(new Transaction(LWMACurrentIndex, 'B', lwma.getPriceAtTick(LWMACurrentIndex), "", 4));
            LWMACurrentIndex++;
            return;
        }
        else if (lwma.getTypeAtTick(LWMACurrentIndex)=='S')
        {
            // a sell occurred at this tick
            transactionList.add(new Transaction(LWMACurrentIndex, 'S', lwma.getPriceAtTick(LWMACurrentIndex), "", 4));
            LWMACurrentIndex++;
            return;
        }
    }
    
    /**
     * Collect transactions for EMAStrategy
     */
    public void collectEMA()
    {
        if (ema.getTypeAtTick(EMACurrentIndex)=='N')
        {
            // this tick has not been taken care of yet, so do nothing.
            return;
        }
        else if (ema.getTypeAtTick(EMACurrentIndex)=='D')
        {
            // this tick has been taken care of but there was no transaction, so move to next
            EMACurrentIndex++;
            return;
        }
        else if (ema.getTypeAtTick(EMACurrentIndex)=='B')
        {
            // a buy occurred at this tick
            transactionList.add(new Transaction(EMACurrentIndex, 'B', ema.getPriceAtTick(EMACurrentIndex), "", 4));
            EMACurrentIndex++;
            return;
        }
        else if (ema.getTypeAtTick(EMACurrentIndex)=='S')
        {
            // a sell occurred at this tick
            transactionList.add(new Transaction(EMACurrentIndex, 'S', ema.getPriceAtTick(EMACurrentIndex), "", 4));
            EMACurrentIndex++;
            return;
        }
    }
    
    /**
     * Collect transactions for TMA
     */
    public void collectTMA()
    {
        if (tma.getTypeAtTick(TMACurrentIndex)=='N')
        {
            // this tick has not been taken care of yet, so do nothing.
            return;
        }
        else if (tma.getTypeAtTick(TMACurrentIndex)=='D')
        {
            // this tick has been taken care of but there was no transaction, so move to next
            TMACurrentIndex++;
            return;
        }
        else if (tma.getTypeAtTick(TMACurrentIndex)=='B')
        {
            // a buy occurred at this tick
            transactionList.add(new Transaction(TMACurrentIndex, 'B', tma.getPriceAtTick(TMACurrentIndex), "", 4));
            TMACurrentIndex++;
            return;
        }
        else if (tma.getTypeAtTick(TMACurrentIndex)=='S')
        {
            // a sell occurred at this tick
            transactionList.add(new Transaction(TMACurrentIndex, 'S', tma.getPriceAtTick(TMACurrentIndex), "", 4));
            TMACurrentIndex++;
            return;
        }
    }
}