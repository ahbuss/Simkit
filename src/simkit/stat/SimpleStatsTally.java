package simkit.stat;

import java.text.DecimalFormat;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
/**
 *  Basic class for collecting tally statistics as produced by a typical
 *  simulation model, that is, one observation at a time.  The algorithm is
 *  not the naive one, keeping the sum, sum of squares, etc., but rather
 *  keeps the mean and variance themselves.  They are updated by a more stable
 *  algorithm that is less prone to overflow, particularly for the sum of
 *  squares.  All counters have approximately the same order of magnitude as the
 *  quantities they are estimating.
 *
 *  <P> A frequent use is as a PropertyChangeListener.  In this capacity, an instance
 *  listens for a single property name and adds a new observation if the newValue
 *  is of type <CODE>Number</CODE>
 *  @author Arnold Buss
 *  @version $Date$
 **/
public class SimpleStatsTally extends AbstractSimpleStats {
    
    private double mean;
    private double variance;
    private double diff;
    
/**
 *  Construct a SimpleStatsTally with the default name.  Note:  The name can
 *  be set after instantiation using setName().
 **/
    public SimpleStatsTally() {
        this(DEFAULT_NAME);
    }
/**
 *  Construct a SimpleStatsTally with the name <CODE>name</CODE>
 *  @param name The property name that will be listened too.
 **/
    public SimpleStatsTally(String name) {
        super(name);
    }
/**
 *  Update counters with a new observation.
 *  @param x The new (primitive) ovservation.
 **/
    public void newObservation(double x) {
        super.newObservation(x);
        
        diff = x - mean;
        variance += (count == 1) ? 0.0 : diff * diff / count - 1.0 / (count - 1) * variance;
        mean += diff / count;
    }

/** 
 * @return Current mean
 */    
    public double getMean() { return mean; }
/**
 * @return Current variance of observations
 */    
    public double getVariance() { return variance; }
/**
 * @return Current Standard Deviation of observations
 */    
    public double getStandardDeviation() { return Math.sqrt(getVariance()); }

    public SamplingType getSamplingType() { return SamplingType.TALLY; }
    
    public void reset() {
        super.reset();
        diff = 0.0;
        mean = 0.0;
        variance = 0.0;
    }

}