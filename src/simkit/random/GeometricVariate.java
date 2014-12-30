package simkit.random;

/**
 * Generates random variates having a Geometric distribution. The number of
 * failures before the first success of iid Berboulli trials.
 *
 * @author Arnold Buss
 * @version $Id$
 */
public class GeometricVariate implements DiscreteRandomVariate {

    /**
     * The probability of success for a single trial.
     *
     */
    private double p;

    /**
     * A value pre-calculated from p.
     *
     */
    private double multiplier;

    /**
     * The supporting RandomNumber instance.
     *
     */
    private RandomNumber rng;

    /**
     * Creates a new GeometricVariate with the default supporting RandomNumber.
     * The value of <code>p</code> must be set prior to generating.
     *
     */
    public GeometricVariate() {
        setRandomNumber(RandomVariateFactory.getDefaultRandomNumber());
    }

    /**
     * Returns the instance of the supporting RandomNumber.
     *
     * @return The underlying RandomNumber instance.
     */
    public RandomNumber getRandomNumber() {
        return rng;
    }

    /**
     * Sets the supporting RandomNumber instance
     *
     * @param rng The RandomNumber instance supporting the generating algorithm
     */
    @Override
    public void setRandomNumber(RandomNumber rng) {
        this.rng = rng;
    }

    /**
     * Returns a single element array containing <code>p</code>.
     */
    @Override
    public Object[] getParameters() {
        return new Object[]{getP()};
    }

    /**
     * Sets the probability of success for a single trial for this GammaVariate.
     *
     * @param params A single element array containing <code>p</code> as a
     * Number.
     * @throws IllegalArgumentException If the array does not contain a single
     * element, or if the given probability is not between 0 and 1 inclusive.
     * @throws ClassCastException If the element cannot be cast to a Number.
     */
    @Override
    public void setParameters(Object... params) {
        if (params == null) {
            throw new NullPointerException("Parameters to GeometricVariate null");
        } else if (params.length != 1) {
            throw new IllegalArgumentException("1 parameters needed: " + params.length);
        }
        setP(((Number) params[0]).doubleValue());
    }

    /**
     *
     * @return the next value as an int.
     */
    @Override
    public int generateInt() {
        return (int) (Math.log(rng.draw()) * multiplier);
    }

    /**
     * Generates the next value cast to a <code>double</code>.
     */
    @Override
    public double generate() {
        return generateInt();
    }

    /**
     * Sets the probability of success of a single trial to the given value.
     *
     * @throws IllegalArgumentException If the given probability is not between
     * 0 and 1 inclusive.
     *
     */
    public void setP(double prob) {
        if (p < 0.0 || p > 1.0) {
            throw new IllegalArgumentException("p must be in [0, 1]: " + prob);
        }
        p = prob;
        multiplier = 1.0 / Math.log(1.0 - p);
    }

    /**
     * Returns the probability of success of a single trial.
     *
     * @return 
     */
    public double getP() {
        return p;
    }

    /**
     * Returns a String containing the name of this distribution and the value
     * of <code>p</code>.
     *
     */
    @Override
    public String toString() {
        return String.format("Geometric (%.3f)", getP());
    }
}
