/*
 * Distribution.java
 *
 * Created on September 19, 2001, 4:01 PM
 */

package simkit.random;

/**
 *
 * @author  Arnold Buss
 * @version 
 */
public interface Distribution extends simkit.Named {

    public double getMean();
    
    public void setMean(double mean);
    
    public double getVariance();
    
    public void setVariance(double variance);
    
    public double getStandardDeviation();
    
    public void setStandardDeviation(double std);
    
    public double getMedian();
    
    public void setCannonicalParameters(Object[] params);
    
    public Object[] getCannonicalParameters();
    
    public void setParameters(Object[] params);
    
    public Object[] getParameters();
    
    public double cdf(double x);
    
    public double ccdf(double x);
}

