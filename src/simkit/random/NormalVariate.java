package simkit.random;

/** Generates Normal(mean, std) random variate using the
 * Box-Muller algorithm.
 * <p>Uses a constant number of RandomNumbers per draw.</p>
 * <p> Removed saved value "feature" for better synchronization.
 *
 * @author Arnold Buss
 * @version $Id$
 */
public class NormalVariate extends RandomVariateBase {
    
/**
* The mean of this normal variate.
**/
    private double mean;

/**
* The standard deviation of this normal variate.
**/
    private double sigma;
    
/**
* Creates a new NormalVariate. Parameters must be set prior to generating.
**/
    public NormalVariate() {
    }
    
    /**
     * Sets the mean and standard deviation for this NormalVariate.
     * @param params A two element array containing the mean and standard deviation as
     * Numbers.
     * @throws IllegalArgumentException If the array doesn't have exactly 2 elements,
     * either element is not a number, or if the standard deviation is negative.
     */    
    public void setParameters(Object[] params) {
        if (params.length != 2) {
            throw new IllegalArgumentException("Need (mean, std. dev.), received " +
            params.length + " parameters");
        }
        else {
            setMean( ((Number) params[0]).doubleValue());
            setStandardDeviation(((Number) params[1]).doubleValue());
        }
    }
    
    /**
     * Returns an array containing the mean and standard deviation.
     * @return A 2 element array with the mean and standard deviation wrapped as Doubles.
     */    
    public Object[] getParameters() { return new Object[] { new Double(mean), new Double(sigma) }; }
    
/**
* Generates the next normally distributed value.
**/
    public double generate() {
        double value = Double.NaN;
        double w;
        double v1;
        double v2;
        do {
            v1 = 2.0 * rng.draw() - 1.0;
            v2 = 2.0 * rng.draw() - 1.0;
            w = v1 * v1 + v2 * v2;
        } while (w > 1.0);
        double y = Math.sqrt(-2.0 * Math.log(w) / w);
        value = v1 * y;
        return value * sigma + mean;
    }
    
/**
* Sets the mean for this NormalVariate.
**/
    public void setMean(double mean) { this.mean = mean; }
    
/**
* Sets the standard deviation for this NormalVariate.
* @throws IllegalArgumentException If the standard deviation is negative.
**/
    public void setStandardDeviation(double std) {
        if (std >= 0.0) {
            sigma = std;
        }
        else {
            throw new IllegalArgumentException("Standard Deviation must be > 0.0");
        }
    }
    
/**
* Sets the variance of this NormalVariate.
* @throws IllegalArgumentException If the variance is negative.
**/
    public void setVariance(double var) {
        if (var >= 0.0) {
            sigma = Math.sqrt(var);
        }
        else {
            throw new IllegalArgumentException("Variance must be > 0.0");
        }
    }
    
/**
* Returns the mean of this Normal variate.
**/
    public double getMean() { return mean; }
    
/**
* Returns the standard deviation of this Normal variate.
**/
    public double getStandardDeviation() { return sigma; }
    
/**
* Returns the variance of this Normal variate.
**/
    public double getVariance() { return sigma * sigma; }
    
/**
* Returns the name of this distribution with its mean and standard deviation.
**/
    public String toString() { return "Normal (" + mean + ", " + sigma + ")"; }
    
}

