/*
 * BernoulliVariate.java
 *
 * Created on August 31, 2001, 7:20 PM
 */

package simkit.random;

/**
 *
 * @author  Arnold Buss
 * @version 
 */
public class BernoulliVariate extends RandomVariateBase implements DiscreteRandomVariate {

    private double probability;
    
    /** Creates new BernoulliVariate */
    public BernoulliVariate() {
    }

    public int generateInt() {
        return rng.draw() < probability ? 1 : 0;
    }
    
    /**
     * Generate a random variate having this class's distribution.
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
    
    /**
     * Sets the random variate's parameters.
     * Alternatively, the parameters could be set in the constructor or
     * in additional methods provided by the programmer.
     * @param params the array of parameters, wrapped in objects.
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
    
    public void setProbability(double prob) {
        if (prob <= 0.0) {
            throw new IllegalArgumentException("Probability must be positive");
        }
        else {
            probability = prob;
        }
    }
    
    public double getProbability() { return probability; }
    
    public String toString() { return "Bernoulli (" + getProbability() + ")"; }
        
}
