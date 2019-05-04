package simkit.stat;

import simkit.util.StateChangeListener;

/**
 *
 * @author ahbuss
 * @param <S>
 */
public interface SimpleStats<S> extends StateChangeListener<Object, S>{
    
    public void reset();
    
    public void newObservation(S x);
    
    public int getCount();
    
    public double getMean();
    
    public S getMin();
    
    public S getMax();
    
    public double getVariance();
    
    public double getStandardDeviation();
        
}
