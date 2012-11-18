package reporting;

import java.util.ArrayList;
import java.util.Collections;
import scheduling.Scheduler;

import trading.AStrategy;
import trading.EMAStrategy;
import trading.LWMAStrategy;
import trading.Prices;
import trading.SMAStrategy;
import trading.TMAStrategy;
import reporting.Transaction;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Collect transactions from each strategy's buffer
 * Also writes tick no, slow value, fast value to a file.
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
    private Scheduler scheduler;
    private FileWriter writerSMA, writerLWMA, writerEMA, writerTMA;
    private String SMAFileName = "SMA.txt", LWMAFileName = "LWMA.txt", EMAFileName = "EMA.txt",  TMAFileName = "TMA.txt";
    
    private AStrategy as;
    
    /** ArrayList of Transactions */
    ArrayList<Transaction> transactionList;
    
    /**
     * Constructor
     * @param s
     * @param l
     * @param e
     * @param t
     */
    public TransactionCollector(AStrategy s)
    {
        as = s;
        
        if (as instanceof SMAStrategy)
        {
            sma = (SMAStrategy)as;
        }

        if (as instanceof LWMAStrategy)
        {
            lwma = (LWMAStrategy)as;
        }

        if (as instanceof EMAStrategy)
        {
            ema = (EMAStrategy)as;
        }

        if (as instanceof TMAStrategy)
        {
            tma = (TMAStrategy)as;
        }
        
        transactionList = new ArrayList<Transaction>();
        scheduler = new Scheduler();
        
        try
        {
            if (as instanceof SMAStrategy)
            {
                writerSMA = new FileWriter(SMAFileName);
                writerSMA.write("");
                writerSMA.close();
                writerSMA = new FileWriter(SMAFileName, true);
            }

            if (as instanceof LWMAStrategy)
            {
                writerLWMA = new FileWriter(LWMAFileName);
                writerLWMA.write("");
                writerLWMA.close();
                writerLWMA = new FileWriter(LWMAFileName, true);
            }

            if (as instanceof EMAStrategy)
            {
                writerEMA = new FileWriter(EMAFileName);
                writerEMA.write("");
                writerEMA.close();
                writerEMA = new FileWriter(EMAFileName, true);
            }

            if (as instanceof TMAStrategy)
            {
                writerTMA = new FileWriter(TMAFileName);
                writerTMA.write("");
                writerTMA.close();
                writerTMA = new FileWriter(TMAFileName, true);
            }
        }
        catch(IOException ex)
        {
            System.out.println("Error while creating filewriters in TransactionCollector");
            System.exit(-1);
        }
    }
    
    @Override
    public void run()
    {
        while(!doneCollecting)
        {
            if ((as instanceof SMAStrategy) && SMACurrentIndex < Prices.MAX_SECONDS)
            {
                collectSMA();
            }
            if ((as instanceof LWMAStrategy) && LWMACurrentIndex < Prices.MAX_SECONDS)
            {
                collectLWMA();
            }
            if ((as instanceof EMAStrategy) && EMACurrentIndex < Prices.MAX_SECONDS)
            {
                collectEMA();
            }
            if ((as instanceof TMAStrategy) && TMACurrentIndex < Prices.MAX_SECONDS)
            {
                collectTMA();
            }
            
            if (SMACurrentIndex == Prices.MAX_SECONDS  || 
                    LWMACurrentIndex == Prices.MAX_SECONDS || 
                        EMACurrentIndex == Prices.MAX_SECONDS || 
                            TMACurrentIndex == Prices.MAX_SECONDS)
            {
                doneCollecting = true;
            }
        }
        
        try
        {
            if (as instanceof SMAStrategy)
                writerSMA.close();
            if (as instanceof TMAStrategy)
                writerTMA.close();
            if (as instanceof EMAStrategy)
                writerEMA.close();
            if (as instanceof LWMAStrategy)
                writerLWMA.close();
        }
        catch(IOException ex)
        {
            System.out.println("Error while closing files in TransactionCollector.");
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
            try
            {
                writerSMA.write(SMACurrentIndex + " " + sma.getSMASlowValue(SMACurrentIndex) + " " + sma.getSMAFastValue(SMACurrentIndex) + "\n");
                writerSMA.flush();
            }
            catch(IOException ex)
            {
                System.out.println("Error while writing to file.");
            }
            
            SMACurrentIndex++;
            return;
        }
        else if (sma.getTypeAtTick(SMACurrentIndex)=='B')
        {
            // a buy occurred at this tick
            transactionList.add(new Transaction(SMACurrentIndex, 'B', sma.getPriceAtTick(SMACurrentIndex), scheduler.getManager(SMACurrentIndex, 1), 1));
            try
            {
                writerSMA.write(SMACurrentIndex + " " + sma.getSMASlowValue(SMACurrentIndex) + " " + sma.getSMAFastValue(SMACurrentIndex) + "\n");
                writerSMA.flush();
            }
            catch(IOException ex)
            {
                System.out.println("Error while writing to file.");
            }

            SMACurrentIndex++;
            return;
        }
        else if (sma.getTypeAtTick(SMACurrentIndex)=='S')
        {
            // a sell occurred at this tick
            transactionList.add(new Transaction(SMACurrentIndex, 'S', sma.getPriceAtTick(SMACurrentIndex), scheduler.getManager(SMACurrentIndex, 1), 1));
            try
            {
                writerSMA.write(SMACurrentIndex + " " + sma.getSMASlowValue(SMACurrentIndex) + " " + sma.getSMAFastValue(SMACurrentIndex) + "\n");
                writerSMA.flush();
            }
            catch(IOException ex)
            {
                System.out.println("Error while writing to file.");
            }

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
        }
        else if (lwma.getTypeAtTick(LWMACurrentIndex)=='D')
        {
            // this tick has been taken care of but there was no transaction, so move to next
            try
            {
                writerLWMA.write(LWMACurrentIndex + " " + lwma.getLWMASlowValue(LWMACurrentIndex) + " " + lwma.getLWMAFastValue(LWMACurrentIndex) + "\n");
                writerLWMA.flush();
            }
            catch(IOException ex)
            {
                System.out.println("Error while writing to file.");
            }

            ++LWMACurrentIndex;
        }
        else if (lwma.getTypeAtTick(LWMACurrentIndex)=='B')
        {
            // a buy occurred at this tick
            transactionList.add(new Transaction(LWMACurrentIndex, 'B', lwma.getPriceAtTick(LWMACurrentIndex), scheduler.getManager(LWMACurrentIndex, 2), 2));
            try
            {
                writerLWMA.write(LWMACurrentIndex + " " + lwma.getLWMASlowValue(LWMACurrentIndex) + " " + lwma.getLWMAFastValue(LWMACurrentIndex) + "\n");
                writerLWMA.flush();
            }
            catch(IOException ex)
            {
                System.out.println("Error while writing to file.");
            }

            ++LWMACurrentIndex;
        }
        else if (lwma.getTypeAtTick(LWMACurrentIndex)=='S')
        {
            // a sell occurred at this tick
            transactionList.add(new Transaction(LWMACurrentIndex, 'S', lwma.getPriceAtTick(LWMACurrentIndex), scheduler.getManager(LWMACurrentIndex, 2), 2));
            try
            {
                writerLWMA.write(LWMACurrentIndex + " " + lwma.getLWMASlowValue(LWMACurrentIndex) + " " + lwma.getLWMAFastValue(LWMACurrentIndex) + "\n");
                writerLWMA.flush();
            }
            catch(IOException ex)
            {
                System.out.println("Error while writing to file.");
            }

            ++LWMACurrentIndex;
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
            try
            {
                writerEMA.write(EMACurrentIndex + " " + ema.getEMASlowValue(EMACurrentIndex) + " " + ema.getEMAFastValue(EMACurrentIndex) + "\n");
                writerEMA.flush();
            }
            catch(IOException ex)
            {
                System.out.println("Error while writing to file.");
            }

            EMACurrentIndex++;
            return;
        }
        else if (ema.getTypeAtTick(EMACurrentIndex)=='B')
        {
            // a buy occurred at this tick
            transactionList.add(new Transaction(EMACurrentIndex, 'B', ema.getPriceAtTick(EMACurrentIndex), scheduler.getManager(EMACurrentIndex, 3), 3));
            try
            {
                writerEMA.write(EMACurrentIndex + " " + ema.getEMASlowValue(EMACurrentIndex) + " " + ema.getEMAFastValue(EMACurrentIndex) + "\n");
                writerEMA.flush();
            }
            catch(IOException ex)
            {
                System.out.println("Error while writing to file.");
            }

            EMACurrentIndex++;
            return;
        }
        else if (ema.getTypeAtTick(EMACurrentIndex)=='S')
        {
            // a sell occurred at this tick
            transactionList.add(new Transaction(EMACurrentIndex, 'S', ema.getPriceAtTick(EMACurrentIndex), scheduler.getManager(EMACurrentIndex, 1), 3));
            try
            {
                writerEMA.write(EMACurrentIndex + " " + ema.getEMASlowValue(EMACurrentIndex) + " " + ema.getEMAFastValue(EMACurrentIndex) + "\n");
                writerEMA.flush();
            }
            catch(IOException ex)
            {
                System.out.println("Error while writing to file.");
            }

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
            try
            {
                writerTMA.write(TMACurrentIndex + " " + tma.getTMASlowValue(TMACurrentIndex) + " " + tma.getTMAFastValue(TMACurrentIndex) + "\n");
                writerTMA.flush();
            }
            catch(IOException ex)
            {
                System.out.println("Error while writing to file.");
            }

            TMACurrentIndex++;
            return;
        }
        else if (tma.getTypeAtTick(TMACurrentIndex)=='B')
        {
            // a buy occurred at this tick
            transactionList.add(new Transaction(TMACurrentIndex, 'B', tma.getPriceAtTick(TMACurrentIndex), scheduler.getManager(TMACurrentIndex, 4), 4));
            try
            {
                writerTMA.write(TMACurrentIndex + " " + tma.getTMASlowValue(TMACurrentIndex) + " " + tma.getTMAFastValue(TMACurrentIndex) + "\n");
                writerTMA.flush();
            }
            catch(IOException ex)
            {
                System.out.println("Error while writing to file.");
            }

            TMACurrentIndex++;
            return;
        }
        else if (tma.getTypeAtTick(TMACurrentIndex)=='S')
        {
            // a sell occurred at this tick
            transactionList.add(new Transaction(TMACurrentIndex, 'S', tma.getPriceAtTick(TMACurrentIndex), scheduler.getManager(TMACurrentIndex, 4), 4));
            try
            {
                writerTMA.write(TMACurrentIndex + " " + tma.getTMASlowValue(TMACurrentIndex) + " " + tma.getTMAFastValue(TMACurrentIndex) + "\n");
                writerTMA.flush();
            }
            catch(IOException ex)
            {
                System.out.println("Error while writing to file.");
            }

            TMACurrentIndex++;
            return;
        }
        System.out.println(TMACurrentIndex + "");
    }
}