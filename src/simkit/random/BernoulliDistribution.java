/*
 * BernoulliDistribution.java
 *
 * Created on January 24, 2002, 6:16 PM
 */

package simkit.random;

/**
 *
 * @author  Arnold Buss
 */
public class BernoulliDistribution implements DiscreteDistribution {
    
    private double probabilityOfSuccess;

    /** 
     *  Creates new BernoulliDistribution 
     */
    public BernoulliDistribution() {
    }
    public Object[] getParameters() { return new Object[] { new Double(probabilityOfSuccess) }; }
    
    public double getStandardDeviation() { return Math.sqrt(getVariance()); }
    
    public double getMean() { return probabilityOfSuccess; } 
    
    public double getMedian() { return 0.5; }
    
    public void setVariance(double variance) {
    }
    
    public void setParameters(Object[] params) {
        if (params.length == 1 && params[0] instanceof Number) {
            setMean(((Number) params[0]).doubleValue());
        }
        else {
            throw new IllegalArgumentException("array has length != 1 or " +
                "contents are not a Number");                
        }
    }
    
    public double pmf(int x) {
        double value = 0.0;
        switch (x) {
            case 0:
                value = 1.0 - probabilityOfSuccess;
                break;
            case 1:
                value = probabilityOfSuccess;
                break;
        }
        return value;
    }
    
    /**
     *
     * @return the name of this object.
     *
     */
    public String getName() {
        return "Bernoulli (" + probabilityOfSuccess + ")" ;
    }
    
    public void setMean(double mean) { probabilityOfSuccess = mean;  }
    
    public void setName(String a_name) {  }
    
    public double cdf(double x) {
        if (x < 0.0) { return 0.0; }
        else if (x < 1.0) { return 1.0 - probabilityOfSuccess; }
        else { return 1.0; }
    }
    
    public double ccdf(double x) {
        return 1.0 - cdf(x);
    }
    
    public void setStandardDeviation(double std) { }  
    
    public Object[] getCannonicalParameters() { return getParameters(); }
    
    public void setCannonicalParameters(Object[] params) {
        setParameters(params);
    }
    
    public double getVariance() { return probabilityOfSuccess * (1.0 - probabilityOfSuccess) ; }
    
    public double getMaximum() { return 1.0; }
    
    public double getMinimum() { return 0.0; }
    
}
