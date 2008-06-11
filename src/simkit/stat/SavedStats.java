package simkit.stat;

import simkit.stat.AbstractSimpleStats;
import simkit.stat.SampleStatistics;
import simkit.stat.SamplingType;

/**
 * A lightweight, read-only SimpleStats that is meant for saving the
 * values of SimpleStats results.  Can contain any kind of SimpleStats.
 * @version $Id: SavedStats.java 849 2006-03-15 20:41:19Z ahbuss $
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
    
    public double getMean() { return mean; }
    
    public simkit.stat.SamplingType getSamplingType() { return samplingType; }
    
    public double getStandardDeviation() {
        return Math.sqrt(getVariance());
    }
    
    public double getVariance() { return variance; }
    
}
