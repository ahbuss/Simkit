/*
 * BernoulliVariate.java
 *
 * Created on August 31, 2001, 7:20 PM
 */

package simkit.random;

/**
 * Generates Bernoulli random variates (either int or double).
 * @author  Arnold Buss
 */
public class BernoulliVariate extends RandomVariateBase implements DiscreteRandomVariate {

    private double probability;
    
    /** Creates new BernoulliVariate */
    public BernoulliVariate() {
    }

    /**
     * @return  integer Bernoulli variate*/    
    public int generateInt() {
        return rng.draw() < probability ? 1 : 0;
    }
    
    /** Generate a random variate having this class's distribution.
     * @return Bernoulli variate as double
     */
    public double generate() {
        return generateInt();
    }
    
    /**
     * Returns the array of parameters as an Object[].
 */
    public Object[] getParameters() {
        return new Object[] { new Double(probability) }; 
    }
    
    /** Sets the random variate's parameters.
     * Alternatively, the parameters could be set in the constructor or
     * in additional methods provided by the programmer.
     * @param params The probability of '1', as a double in the range [0,1].
     */
    public void setParameters(Object[] params) {
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
     * @param prob The probability of 1, in range [0,1]
     */    
    public void setProbability(double prob) {
        if (prob <= 0.0) {
            throw new IllegalArgumentException("Probability must be positive");
        }
        else {
            probability = prob;
        }
    }
    
    /**
     * @return  */    
    public double getProbability() { return probability; }
    
    /**
     * @return  */    
    public String toString() { return "Bernoulli (" + getProbability() + ")"; }
        
}
