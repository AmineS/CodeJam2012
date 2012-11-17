package scheduling;

public class MovingAverages
{
    private float[] movingAverage = new float[32400];
    
    public float getMA(int tick)
    {
        return movingAverage[tick];
    }
    
    public void setMA(int tick, float value)
    {
        movingAverage[tick] = value;
    }
}
