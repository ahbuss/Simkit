package simkit.random;

/** Generates Exponential random variate using log transform.
 *
 * @author Arnold Buss
 */
public class ExponentialVariate extends RandomVariateBase implements RandomVariate {
    
    private double mean;
    
    public ExponentialVariate() {
    }
    
    public double generate() {
        return - mean * Math.log(rng.draw());
    }
    
    /** Note that the value is the mean rather than the rate.
     * @param params mean as single element of array.
     */    
    public void setParameters(Object[] params) {
        if (params.length != 1) {
            throw new IllegalArgumentException("Should be only one parameter for Exponential: " +
            params.length + " passed.");
        }
        if (params[0] instanceof Number) {
                setMean(((Number) params[0]).doubleValue());
        }
        else {
            throw new IllegalArgumentException("Parameters must be a Number");
        }
    }
    
    public Object[] getParameters() { return new Object[] { new Double(mean) }; }
    
    public void setMean(double mean) { 
        if (mean > 0.0) {
            this.mean = mean;
        }
        else {
            throw new IllegalArgumentException("Exponential mean must be positive: " + mean);
        }
    }
    
    public double getMean() { return mean; }
    
    public String toString() { return "Exponential (" + mean + ")"; }
    
}