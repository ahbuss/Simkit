package simkit.stat;

public interface SampleStatistics extends java.beans.PropertyChangeListener {

    public static final String EOL = System.getProperty("line.separator", "\n");

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
}
