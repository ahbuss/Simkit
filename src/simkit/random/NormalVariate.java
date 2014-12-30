package simkit.random;

/**
 * Generates Normal(&mu;, &sigma;) random variate using the Box-Muller algorithm.
 * The parameters are the mean and standard deviation.
 * <p>
 * Uses a constant number of RandomNumbers per draw.</p>
 * <p>
 * Removed saved value "feature" for better synchronization.
 *
 * @author Arnold Buss
 * @version $Id$
 */
public class NormalVariate extends RandomVariateBase {

    /**
     * The mean of this normal variate.
*
     */
    private double mean;

    /**
     * The standard deviation of this normal variate.
*
     */
    private double standardDeviation;

    /**
     * Creates a new NormalVariate. Parameters must be set prior to generating.
*
     */
    public NormalVariate() {
    }

    /**
     * Sets the mean and standard deviation for this NormalVariate.
     *
     * @param params A two element array containing the mean and standard
     * deviation as Numbers.
     * @throws IllegalArgumentException If the array doesn't have exactly 2
     * elements, either element is not a number, or if the standard deviation is
     * negative.
     */
    @Override
    public void setParameters(Object... params) {
        if (params.length != 2) {
            throw new IllegalArgumentException("Need (mean, std. dev.), received "
                    + params.length + " parameters");
        } else {
            setMean(((Number) params[0]).doubleValue());
            setStandardDeviation(((Number) params[1]).doubleValue());
        }
    }

    /**
     * Returns an array containing the mean and standard deviation.
     *
     * @return A 2 element array with the mean and standard deviation wrapped as
     * Doubles.
     */
    @Override
    public Object[] getParameters() {
        return new Object[]{getMean(), getStandardDeviation()};
    }

    /**
     * Generates the next normal
     * @return y distributed value.
*
     */
    @Override
    public double generate() {
        double value;
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
        return value * standardDeviation + mean;
    }

    /**
     * @param mean the mean for this NormalVariate.
     */
    public void setMean(double mean) {
        this.mean = mean;
    }

    /**
     *
     * @param std the standard deviation for this NormalVariate.
     * @throws IllegalArgumentException If the standard deviation is negative.
     */
    public void setStandardDeviation(double std) {
        if (std >= 0.0) {
            this.standardDeviation = std;
        } else {
            throw new IllegalArgumentException("Standard Deviation must be \u2265 0.0");
        }
    }

    /**
     * @return the mean of this Normal variate.
     */
    public double getMean() {
        return mean;
    }

    /**
     * @return the standard deviation of this Normal variate.
     */
    public double getStandardDeviation() {
        return standardDeviation;
    }

    /**
     * @return the variance of this Normal variate.
     */
    public double getVariance() {
        return standardDeviation * standardDeviation;
    }

    /**
     * @return the name of this distribution with its mean and standard
     * deviation.
     */
    @Override
    public String toString() {
        return String.format("Normal (%.3f, %.3f)", getMean(), getStandardDeviation());
    }

}
