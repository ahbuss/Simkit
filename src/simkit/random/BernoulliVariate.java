package simkit.random;

/**
 * Generates Bernoulli random variates (either int or double).<br>
 * Parameter:
 * <ul><li><code>probability</code> = P{X = 1}</li>
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
    
    /** 
     * @return Bernoulli variate as double
     */
    public double generate() {
        return generateInt();
    }
    
    /**
     * Returns the array of parameters as an Object[].
     * @return P{X = 1} wrapped as Double in Object[]
 */
    public Object[] getParameters() {
        return new Object[] { new Double(probability) }; 
    }
    
    /** 
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
     * @return P{X = 1}
     */    
    public double getProbability() { return probability; }
    
    /**
     * @return &quot;Bernoulli (p)&quot;
     */    
    public String toString() { return "Bernoulli (" + getProbability() + ")"; }
        
}
