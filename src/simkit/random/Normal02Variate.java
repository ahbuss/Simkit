/*
 * Normal02Variate.java
 *
 * Created on March 31, 2002, 8:40 PM
 */

package simkit.random;

/**
 * Generates a Normal random variate.
 * @author  Arnold Buss
 * @version $Id$
 */
public class Normal02Variate implements RandomVariate {
    
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

    public Normal02Variate() {
        rng = RandomNumberFactory.getInstance();
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
        return new Object[] {new Double(getMean()), new Double(getStandardDeviation())};
    }
    
    /**
     * Sets the random variate's parameters.
     * @param params A 2 element array with the mean in the first element and
     * the standard deviation as the second, both as Numbers.
     * @throws IllegalArgumentException If the array does not contain exactly 2
     * elements, if either of the elements is not a number, or if the standard
     * deviation is not positive.
     */
    public void setParameters(Object[] params) {
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
                y = Math.log(2.0 * v);
            }
            else {
                y = - Math.log(2.0 * v - 1.0);
            }
        } while (rng.draw() > Math.exp(-0.5 * Math.pow((Math.abs(y) - 1.0), 2)));
        return getMean() + y * getStandardDeviation();
    }
    
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {}
        throw new InternalError();
    }
    
/**
* Sets the mean of this normal variate.
**/
    public void setMean(double mean) { this.mean = mean; }
    
/**
* Returns the mean of this normal variate.
**/
    public double getMean() { return mean; }
    
/**
* Sets the standard deviation of this normal.
* @throws IllegalArgumentException if the standard deviation is not positive.
**/
    public void setStandardDeviation(double sd) {
        if (sd > 0.0) {
            this.stdDev = sd; 
        }
        else {
            throw new IllegalArgumentException("Standard Deviation must be > 0.0: " +
                sd);
        }
    }
    
/**
* Gets the standard deviation for this normal variate.
**/
    public double getStandardDeviation() { return stdDev; }
    
/**
* Returns a String containing the name, mean, and standard deviation of this variate.
**/
    public String toString() { 
      return "Normal (" + getMean() + ", " + getStandardDeviation() + ")"; 
    }
}
