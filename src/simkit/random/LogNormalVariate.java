package simkit.random;

/**
 *
 * <p>Generates random variates from the Log Normal(&mu;, &sigma;) distribution
 * based on the exponential transformation of a Normal.  Note that the
 * parameters &mu; and &sigma; are the corresponding Normal parameters
 * and not the mean and standard deviation of the distribution itself.
 *
 * <p>Two convenience static methods are provided to transform a desired 
 * mean and standard deviation into the corresponding Normal parameters,
 * as well as the inverse, to transform a given mean and standard deviation
 * for the normal into the actual mean and standard deviation for the
 * distribution. 
 *
 * @version $Id$
 * @author ahbuss
 */
public class LogNormalVariate extends NormalVariate {
    
    public LogNormalVariate() {
    }

    /**
     * Generates the next Log Normal random variate
     * @return Random variate generated from LogNormal(&mu;, &sigma;)
     */
    public double generate() {
        return Math.exp(super.generate());
    }
    
    /**
     * Convenience method to conver a given desired mean and standard
     * deviation into the corresponding Normal mean and standard deviation.
     * 
     * @param mean The mean of the log normal distribution
     * @param stdDeviation The standard deviation of the log normal
     * distribution
     * @return The mean and standard deviation for the underlying Normal
     *      distribution whose exponential transformation will result in
     *      these values.  They are in order: [mean, std dev]
     * @throws IllegalArgumentException if mean < 0.0
     */
    public static double[] getNormalParameters(double mean, double stdDeviation) {
        if (mean <= 0.0) {
            throw new IllegalArgumentException("Log normal distribution must" +
                    " have a positive mean: " + mean);
        }
        double ratioSquared = mean * mean / (stdDeviation * stdDeviation);
        double log = Math.log(ratioSquared + 1.0);
        return new double[]{
            Math.log(mean) - 0.5 * log, Math.sqrt(log)
        };
    }
    
    /**
     * A convenience method to convert the underlying Normal parameters
     * into the actual mean and standard deviation for the log normal
     * distribution.
     * 
     * @param mean The mean of the underlying Normal
     * @param stdDeviation The standard deviation of th eunderlying Normal
     * @return The mean and standard deviation of the log normal distribution
     *      from the given parameters.
     */
    public static double[] getLogNormalParameters(double mean, double stdDeviation) {
        double meanLN = Math.exp(mean + 0.5 * stdDeviation * stdDeviation);
        return new double[] {
            meanLN,
            meanLN * 
               Math.sqrt(Math.exp(stdDeviation * stdDeviation) - 1.0)
        };
    }

    /**
     * Returns the name and parameters of the distribution
     * @return The name and parameters of the distribution - note that they
     *          are <i>not</i> the mean and standard deviaiton of the
     *          log normal, but of the underlying normal.
     */
    public String toString() {
        return super.toString().replaceAll("Normal", "Log Normal");
    }
}