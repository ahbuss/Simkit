/*
 * BernoulliDistribution.java
 *
 * Created on January 24, 2002, 6:16 PM
 */

package simkit.random;

/**
 * A representation of the Bernoulli Distribution. A random event with two possible
 * outcomes. A value of 0 represents failure, a value of 1 represents success. Has
 * a single paramater, the probability of success.
 * @author  Arnold Buss
 * @version $Id$
 */
public class BernoulliDistribution implements DiscreteDistribution {
    
    private double probabilityOfSuccess;

    /** 
     *  Creates new BernoulliDistribution with a probability of success of 0.
     * Use setMean of setParameters to set the desired probability of success.
     */
    public BernoulliDistribution() {
    }

/**
* Returns a single element array containing the value of the parameter probability of success.    
**/
    public Object[] getParameters() { return new Object[] { new Double(probabilityOfSuccess) }; }
    
//Javadoc inherited
    public double getStandardDeviation() { return Math.sqrt(getVariance()); }
    
//Javadoc inherited
    public double getMean() { return probabilityOfSuccess; } 
    
//Javadoc inherited
    public double getMedian() { return 0.5; }
    
/**
* Does nothing. The variance is a function of the mean and cannot be directly set.
**/
    public void setVariance(double variance) {
    }
    
/**
* Sets the probability of success of this distribution.
* @param params A single element array containing the probability of success as
* a Number.
* @throws IllegalArgumentException If the array does not contain exactly one
* element or the element is not a Number.
**/
    public void setParameters(Object[] params) {
        if (params.length == 1 && params[0] instanceof Number) {
            setMean(((Number) params[0]).doubleValue());
        }
        else {
            throw new IllegalArgumentException("array has length != 1 or " +
                "contents are not a Number");                
        }
    }
    
//Javadoc inherited
    public double pmf(double x) {
        double value = 0.0;
        switch ( (int) x) {
            case 0:
                value = 1.0 - probabilityOfSuccess;
                break;
            case 1:
                value = probabilityOfSuccess;
                break;
        }
        return value;
    }
    
//Javadoc inherited
    /**
     *
     * @return the name of this object.
     *
     */
    public String getName() {
        return "Bernoulli (" + probabilityOfSuccess + ")" ;
    }
    
/**
* Sets the probability of success of this Bernoulli distribution.
**/
    public void setMean(double mean) { probabilityOfSuccess = mean;  }
    
//Javadoc inherited
    public void setName(String a_name) {  }
    
//Javadoc inherited
    public double cdf(double x) {
        if (x < 0.0) { return 0.0; }
        else if (x < 1.0) { return 1.0 - probabilityOfSuccess; }
        else { return 1.0; }
    }
    
//Javadoc inherited
    public double ccdf(double x) {
        return 1.0 - cdf(x);
    }
    
/**
* Does nothing. The standard deviation is a function of the probability of
* success and cannot be directly set.
**/
    public void setStandardDeviation(double std) { }  

/**
* Returns a single element array containing the value of the parameter probability of success.    
**/
    public Object[] getCannonicalParameters() { return getParameters(); }
    
/**
* Sets the probability of success of this distribution.
* @param params A single element array containing the probability of success as
* a Number.
* @throws IllegalArgumentException If the array does not contain exactly one
* element or the element is not a Number.
**/
    public void setCannonicalParameters(Object[] params) {
        setParameters(params);
    }
    
//Javadoc inherited
    public double getVariance() { return probabilityOfSuccess * (1.0 - probabilityOfSuccess) ; }
    
//Javadoc inherited
    public double getMaximum() { return 1.0; }
    
//Javadoc inherited
    public double getMinimum() { return 0.0; }
        
}
