package simkit.random;

import simkit.util.Math64;

/**
 * Generates a Normal random variate.
 * <p>Note: Uses a random number of RandomNumbers per draw.</p>
 * <P>Uses simkit.util.Math64.log() function for replicability on 64-bit
 * platforms.
 * @author  Arnold Buss
 * 
 */
public class Normal02_64Variate implements RandomVariate {
    
/**
* The instance of the supporting RandomNumber.
**/
    protected RandomNumber rng;

/**
* The mean of this normal random variate.
**/
    protected double mean;

/**
* The standard deviation of this normal random variate.
**/
    protected double stdDev;
    
/** 
* Creates new Normal02Variate with 0 mean and standard deviation.
**/

    public Normal02_64Variate() {
        setRandomNumber(RandomVariateFactory.getDefaultRandomNumber());
    }
    
    /**
     * Returns the instance of the supporting RandomNumber.
     * @return The underlying RandomNumber instance
     */
    public RandomNumber getRandomNumber() {
        return rng;
    }
    
    /** Sets the supporting RandomNumber object
     * @param rng The RandomNumber instance supporting the generating algorithm
     */
    public void setRandomNumber(RandomNumber rng) {
        this.rng = rng;
    }
    
    /**
     * Returns a 2 element array with the mean and the standard deviation.
     */
    public Object[] getParameters() {
        return new Object[] {getMean(), getStandardDeviation()};
    }
    
    /**
     * Sets the random variate's parameters.
     * @param params A 2 element array with the mean in the first element and
     * the standard deviation as the second, both as Numbers.
     * @throws IllegalArgumentException If the array does not contain exactly 2
     * elements, if either of the elements is not a Number, or if the standard
     * deviation is not positive.
     */
    public void setParameters(Object... params) {
        if (params == null) { throw new NullPointerException(); }
        else if (params.length == 2) {
            if (params[0] instanceof Number && params[1] instanceof Number) {
                setMean(((Number)params[0]).doubleValue());
                setStandardDeviation(((Number)params[1]).doubleValue());
            }
            else {
                throw new IllegalArgumentException("Need (Number, Number) paramerets: (" +
                   params[0].getClass().getName() + "," +  params[1].getClass().getName() + ")" );
            }
        }
        else {
            throw new IllegalArgumentException("Need 2 parameters: " + params.length);
        }
    }
    
    /**
     * Generate a random variate having this class's distribution.
     * @return The generated random variate
     */
    public double generate() {
        double y = Double.NaN;
        do {
            double v = rng.draw();            
            if (v < 0.5) {
                y = Math64.log(2.0 * v);
            }
            else {
                y = - Math64.log(2.0 * v - 1.0);
            }
        } while (rng.draw() > Math.exp(-0.5 * Math.pow((Math.abs(y) - 1.0), 2)));
        return getMean() + y * getStandardDeviation();
    }

    /**
     * 
     * @param mean the mean of this normal variate.
     */
    public void setMean(double mean) { this.mean = mean; }
    
    /**
     * 
     * @return the mean of this normal variate.
     */
    public double getMean() { return mean; }
    
    /**
     * 
     * @param stdDev the standard deviation of this normal.
     * @throws IllegalArgumentException if the standard deviation is &lt; 0.
     */
    public void setStandardDeviation(double stdDev) {
        if (stdDev >= 0.0) {
            this.stdDev = stdDev; 
        }
        else {
            throw new IllegalArgumentException("Standard Deviation must be \u2265 0.0: " +
                stdDev);
        }
    }
    
    /**
     * 
     * @return the standard deviation for this normal variate.
     */
    public double getStandardDeviation() { 
        return this.stdDev; 
    }
    
    @Override
    public String toString() { 
      return "Normal02_64 (" + getMean() + ", " + getStandardDeviation() + ")";
    }

}
