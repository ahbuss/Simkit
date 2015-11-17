package simkit.random;

import simkit.util.Math64;

/**
 * Generates random variates having a Geometric distribution.
 * The number of failures before the first success of iid Berboulli trials.
 * <P>Uses simkit.util.Math64.log() function for replicability on 64-bit
 * platforms.

 * @author  Arnold Buss
 * @version $Id$
 */
public class Geometric_64Variate implements DiscreteRandomVariate {

/**
* The probability of success for a single trial.
**/
    private double p;

/**
* A value pre-calculated from p.
**/
    private double multiplier;

/**
* The supporting RandomNumber instance.
**/
    private RandomNumber rng;
    
/**
* Creates a new GeometricVariate with the default supporting RandomNumber. 
* The value of <code>p</code> must be set prior to generating.
**/
    public Geometric_64Variate() {
        setRandomNumber(RandomVariateFactory.getDefaultRandomNumber());
    }

    /**
     * Returns the instance of the supporting RandomNumber.
     * @return The underlying RandomNumber instance.
     */
    public RandomNumber getRandomNumber() {
        return rng;
    }
    
    /** Sets the supporting RandomNumber instance
     * @param rng The RandomNumber instance supporting the generating algorithm
     */
    public void setRandomNumber(RandomNumber rng) {
        this.rng = rng;
    }
    
    /**
     * Returns a single element array containing <code>p</code>.
     */
    public Object[] getParameters() {
        return new Object[] { new Double(p) };
    }
    
    /**
     * Sets the probability of success for a single trial for this GammaVariate.
     * @param params A single element array containing <code>p</code> as a Number.
     * @throws IllegalArgumentException If the array does not contain a single element,
     * or if the given probability is not between 0 and 1
     * inclusive.
     * @throws ClassCastException If the element cannot be cast to a Number.
     */
    public void setParameters(Object... params) {
        if (params == null) {
            throw new NullPointerException("Parameterst to GeometricVariate null");
        }
        else if (params.length != 1) {
            throw new IllegalArgumentException("1 parameters needed: " + params.length);
        }
        setP(((Number) params[0]).doubleValue());
    }
    
/**
* Generates the next value as an int.
**/
    public int generateInt() {
        return (int) (Math64.log(rng.draw()) * multiplier);
    }
    
    /**
     * Generates the next value cast to a <code>double</code>.
     */
    public double generate() {
        return generateInt();
    }
    
    /**
     * 
     * @param prob the probability of success of a single trial to the given value.
     * @throws IllegalArgumentException If the given probability is not between 
     * 0 and 1 inclusive.
     */
    public void setP(double prob) {
        if (p < 0.0 || p > 1.0) {
            throw new IllegalArgumentException("p must be in [0, 1]: " + prob);
        }
        this.p = prob;
        this.multiplier = 1.0 / Math64.log(1.0 - p);
    }
    
    /**
     * 
     * @return the probability of success of a single trial.
     */
    public double getP() { return p; }

/**
* Returns a String containing the name of this distribution and the value of <code>p</code>.
**/
    public String toString() { return "Geometric_64 (" + p + ")"; }
}
