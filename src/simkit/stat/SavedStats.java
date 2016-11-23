package simkit.stat;

/**
 * A lightweight, read-only SimpleStats that is meant for saving the
 * values of SimpleStats results.  Can contain any kind of SimpleStats.
 * @version $Id$
 * @author ahbuss
 */
public class SavedStats extends AbstractSimpleStats {
    
    protected double mean;
    protected double variance;
    protected SamplingType samplingType;
    
    public SavedStats(SampleStatistics original) {
        this.name = original.getName();
        this.count = original.getCount();
        this.minObs = original.getMinObs();
        this.maxObs = original.getMaxObs();
        this.mean = original.getMean();
        this.variance = original.getVariance();
        this.samplingType = original.getSamplingType();
    }
    
    @Override
    public double getMean() { return mean; }
    
    @Override
    public simkit.stat.SamplingType getSamplingType() { return samplingType; }
    
    @Override
    public double getStandardDeviation() {
        return Math.sqrt(getVariance());
    }
    
    @Override
    public double getVariance() { return variance; }
    
}
