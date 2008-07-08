package simkit.random;

/**
 * Generates Bernoulli random variates (either int or double).<br>
 * Parameter:
 * <ul><li><code>probability</code> = P{X = 1} or the probability of
 * "success" in a single trial.</li></ul>
 *
 * @version $Id$
 * @author  Arnold Buss
 */
public class BernoulliVariate extends RandomVariateBase implements DiscreteRandomVariate {

/**
* The probability of success.
**/
    private double probability;
    
/** 
* Creates new BernoulliVariate with a probability of success of 0.
* Use setParameters or setProbability to change.
 */
    public BernoulliVariate() {
    }

    /**
     * Generate the next value as an integer (0 or 1)
     * @return  integer Bernoulli variate*/    
    public int generateInt() {
        return rng.draw() < probability ? 1 : 0;
    }
    
    /** 
     * Generate the next value cast to <code>double</code>.
     * @return Bernoulli variate as double
     */
    public double generate() {
        return generateInt();
    }
    
    /**
     * Returns a single element array containing the probability of success wrapped
     * as an Object.
     * @return P{X = 1} wrapped as Double in Object[]
 */
    public Object[] getParameters() {
        return new Object[] { new Double(probability) }; 
    }
    
    /** 
     * Set the probability of success to the value of the element of the 
     * given array
     * @param params The probability of '1', as a double in the range [0,1].
     * @throws IllegalArgumentException If the array does not contain exactly
     * one element or the element is not a Number with a value between 0 and 1
     * inclusive.
     */
    public void setParameters(Object... params) {
        if (params.length != 1) {
            throw new IllegalArgumentException("One parameter needed for Bernoulli");
        }
        else if (params[0] instanceof Number) {
            setProbability( ((Number)params[0]).doubleValue());
        }
        else {
            throw new IllegalArgumentException("Number object required");
        }
    }
    
    /**
     * Sets the probability of success.
     * @param prob The probability of 1, in range [0,1]
     * @throws IllegalArgumentException If the argument is not between
     * 0 and 1 inclusive.
     */    
    public void setProbability(double prob) {
        if (prob < 0.0) {
            throw new IllegalArgumentException("Probability must be >= 0.0: " + prob);
        }
        else if (prob > 1.0) {
            throw new IllegalArgumentException("Probability must be <= 1.0: " + prob);
        }
        else {
            probability = prob;
        }
    }
    
    /**
     * Returns the value of the probability.
     * @return P{X = 1}
     */    
    public double getProbability() { return probability; }
    
    /**
     * Returns the name of the distribution and the value of the probability.
     * @return &quot;Bernoulli (p)&quot;
     */    
    public String toString() { return "Bernoulli (" + getProbability() + ")"; }
}
