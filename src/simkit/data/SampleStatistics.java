package simkit.data;
/**
 * @deprecated This class has been replaced by <CODE>simkit.random.SampleStatistics</CODE>
 * @see simkit.random.SampleStatistics
**/
public interface SampleStatistics extends java.beans.PropertyChangeListener {
   public void reset();
   public void newObservation(double newObs);
   public void newObservation(Number newObs);
   public double getMean();
   public double getVariance();
   public double getStandardDeviation();
   public int getCount();
   public double getMinObs();
   public double getMaxObs();
   public void setFormat(String format);
   public void setSamplingType(SamplingType type);
   public SamplingType getSamplingType();
//    public double getCIHalfWidth(double alpha);
}