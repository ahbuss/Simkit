package simkit.random;

/**
 * Generates random variates having a Poisson distribution.
 *
 * @author Arnold Buss
 * @version $Id$
 */
public class PoissonVariate extends RandomVariateBase implements DiscreteRandomVariate {

    /**
     * The instance of the supporting RandomNumber.
*
     */
    protected RandomNumber rng;

    /**
     * The desired mean of this PoissonVariate.
*
     */
    protected double mean;

    /**
     * A precalculated value to aid in generation. e<sup>-mean</sup>
*
     */
    protected double a;

    /**
     * Creates new PoissonVariate; the mean must be set prior to use.
     */
    public PoissonVariate() {
    }

    /**
     * Returns a single element array containing the mean as a Double.
     */
    @Override
    public Object[] getParameters() {
        return new Object[]{getMean()};
    }

    /**
     * Sets the desired mean of the RadomVariate.
     *
     * @param params A single element array containing the mean as a Number.
     * @throws IllegalArgumentException If the array does not have exactly 1
     * element, if the element is not a Number, or if the mean if not positive.
     */
    @Override
    public void setParameters(Object... params) {
        if (params == null) {
            throw new NullPointerException();
        } else if (params.length != 1) {
            throw new IllegalArgumentException("PoissonVariate needs exatly 1 parameter: " + params.length);
        }
        if (params[0] instanceof Number) {
            setMean(((Number) params[0]).doubleValue());
        } else {
            throw new IllegalArgumentException("The parameter must be a Number.");
        }
    }

    /**
     * @return the next value as an <code>int</code>.
     */
    @Override
    public int generateInt() {
        int x = 0;
        for (double y = rng.draw(); y >= a; y *= rng.draw()) {
            x++;
        }
        return x;
    }

    /**
     * Generates a random variate having this class's distribution.
     * @return The generated random variate
     */
    @Override
    public double generate() {
        return generateInt();
    }

    /**
     *
     * @param mean the desired mean.
     * @throws IllegalArgumentException If the mean is not positive.
     */
    public void setMean(double mean) {
        if (mean <= 0.0) {
            throw new IllegalArgumentException("PoissonVariate must have positive mean: " + mean);
        }
        this.mean = mean;
        a = Math.exp(-mean);
    }

    /**
     * @return the value of the desired mean.
     */
    public double getMean() {
        return mean;
    }

    /**
     * Returns a String containing the name and mean of this RandomVariate.
     */
    @Override
    public String toString() {
        return String.format("Poisson (%.3f)", getMean());
    }

}
