/*
 * Normal02Variate.java
 *
 * Created on March 31, 2002, 8:40 PM
 */

package simkit.random;

/**
 * @author  Arnold Buss
 */
public class Normal02Variate implements RandomVariate {
    
    protected RandomNumber rng;
    protected double mean;
    protected double stdDev;
    
    /** Creates new Normal02Variate */
    public Normal02Variate() {
        rng = RandomNumberFactory.getInstance();
    }
    
    /**
     * @return The underlying RandomNumber instance (should be a copy)
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
     * Returns the array of parameters as an Object[].
     * @return the array of parameters as an Object[].
     */
    public Object[] getParameters() {
        return new Object[] {new Double(getMean()), new Double(getStandardDeviation())};
    }
    
    /**
     * Sets the random variate's parameters.
     * Alternatively, the parameters could be set in the constructor or
     * in additional methods provided by the programmer.
     * @param params the array of parameters, wrapped in objects.
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
    
    public void setMean(double mean) { this.mean = mean; }
    
    public double getMean() { return mean; }
    
    public void setStandardDeviation(double sd) {
        if (sd > 0.0) {
            this.stdDev = sd; 
        }
        else {
            throw new IllegalArgumentException("Standard Deviation must be > 0.0: " +
                sd);
        }
    }
    
    public double getStandardDeviation() { return stdDev; }
    
    public String toString() { return "Normal (" + getMean() + ", " + getStandardDeviation() + ")"; }
}
