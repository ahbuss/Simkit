/*
 * BinomialVariate.java
 *
 * Created on March 29, 2002, 3:31 PM
 */

package simkit.random;
/**
 *
 * Generates Binomial(n, p) random variates (representing the number
 * of successes in n iid Bernoulli(p) trials.<br>
 * Parameters:
 * <ul><li><code>n</code> = number of Bernoulli trials</li>
 * <li> <code>probability</code> = P{X=1} for Bernoulli trials.</li></ul>
 * <p>Note: This implementation uses the convolution method and
 * has an execution time proportional to n.</p>
 * @author  Arnold Buss
 * @version $Id$
 */
public class BinomialVariate implements DiscreteRandomVariate {
    
/**
* The parameter representing the number of Bernoulli trials.
**/
    protected int n;

/**
* The BernoulliVariate that this BinomialVariate is based on.
**/
    protected BernoulliVariate bernoulli;
    
/** Creates new BinomialVariate with n and p set to zero. 
* This RandomVariate should be instantiated with RandomVariateFactory.
* If not, the parameters should be set prior to generating.
 */
    public BinomialVariate() {
        bernoulli = new BernoulliVariate();
    }
    
    /**
     * Gets the instance of RandomNumber that supports the underlying
     * BernoulliVariate.
     * @return The underlying RandomNumber instance
     */
    public RandomNumber getRandomNumber() { return bernoulli.getRandomNumber(); }
    
    /** 
     * Sets the instance of RandomNumber that supports the underlying
     * BernoulliVariate.
     * @param rng The RandomNumber instance supporting the generating algorithm
     */
    public void setRandomNumber(RandomNumber rng) { bernoulli.setRandomNumber(rng); }
    
    /**
     * First element is n, wrapped in <code>Integer</code> second is probability,
     * wrapped in <code>Double</code>
     * @return the array of parameters as an Object[].
     */
    public Object[] getParameters() {
        return new Object[] { new Integer(n), new Double(bernoulli.getProbability()) };
    }
    
    /** Sets parameters - first is n, the second is probability. If the given n
     * is not an integer, then it is truncated.
     * @param params (n, probability)
     * @throws IllegalArgumentException If the array does not contain exactly 2
     * elements, if the elements are not Numbers, if n is not positive, or if
     * the probability is not between 0 and 1.
     */
    public void setParameters(Object[] params) {
        if (params.length != 2 ){
            throw new IllegalArgumentException("Binomial needs 2 parameters: " + params.length);
        }
        if (!( params[0] instanceof Number ) ) {
            throw new IllegalArgumentException("Number needed for params[0]: " + params[0].getClass());
        }
        if (!( params[1] instanceof Number ) ) {
            throw new IllegalArgumentException("Number needed for params[1]: " + params[1].getClass());
        }
        setN( ((Number)params[0]).intValue());
        setProbability( ((Number)params[1]).doubleValue());
    }
    
    /** Adds n Bernoulli variates together.
     * @return generated binomial variate
     */
    public int generateInt() {
        int sum = 0;
        for (int i = 0; i < n; i++) {
            sum += bernoulli.generateInt();
        }
        return sum;
    }
    
    /**
     * Generate a random variate having this class's distribution.
     * @return The generated random variate
     */
    public double generate() {
        return generateInt();
    }
    
    /**
     * Sets the number of Bernoulli trials.
     * @param n # of Bernoulli trials
     * @throws IllegalArgumentException If n is not positive.
    */
    public void setN(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Binomial variate must have positive n: " + n);
        }
        this.n = n;
    }
    
    /**
     * Returns the number of Bernoulli trials.
     * @return number of Bernoulli trials */
    public int getN() { return n; }
    
    /**
     * Sets the probability of success of one trial.
     * @param p probability of success  
     * @throws IllegalArgumentException If the argument is not between 0.0 and 1.0.
     */
    public void setProbability(double p) {
        if (p >= 0.0 && p <= 1.0) {
            bernoulli.setProbability(p);
        }
        else {
            throw new IllegalArgumentException("Binomial p must be between 0.0 and 1.0: " + p);
        }
    }
    
    /**
     * Returns the probability of success of one Bernoulli trial.
     */
    public double getProbability() { return bernoulli.getProbability(); }
    
    /**
      * Returns a String containing the name of the distribution and the parameters
      * of this RandomVariate.
      */
    public String toString() { return "Binomial (" + n + ", " + bernoulli.getProbability() + ")"; }
}
