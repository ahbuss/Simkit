package simkit.stat;

public interface IndexedSampleStatistics extends SampleStatistics {

    public void newObservation(double value, int index);
    public void newObservation(Number newObs, int index);
    public double getMean(int index);
    public double getVariance(int index);
    public double getStandardDeviation(int index);
    public int getCount(int index);
    public double getMinObs(int index);
    public double getMaxObs(int index);

    public double[] getAllMean();
    public double[] getAllVariance();
    public double[] getAllStandardDeviation();
    public int[] getAllCount();
    public double[] getAllMinObs();
    public double[] getAllMaxObs();

    public SampleStatistics[] getAllSampleStat();
}
