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
 * <li> <code>probability</code> = P{X=1} for Bernoulli trials.
 * @author  Arnold Buss
 */
public class BinomialVariate implements DiscreteRandomVariate {
    
    protected int n;
    protected BernoulliVariate bernoulli;
    
    /** Creates new BinomialVariate */
    public BinomialVariate() {
        bernoulli = new BernoulliVariate();
    }
    
    /**
     * @return The underlying RandomNumber instance
     */
    public RandomNumber getRandomNumber() { return bernoulli.getRandomNumber(); }
    
    /** 
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
    
    /** Sets parameters - first is n, the second is probability
     * @param params (n, probability)
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
     * @param n # of Bernoulli trials */
    public void setN(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Binomial variate must have positive n: " + n);
        }
        this.n = n;
    }
    
    /**
     * @return number of Bernoulli trials */
    public int getN() { return n; }
    
    /**
     * @param p probability of success  */
    public void setProbability(double p) {
        if (p >= 0.0 && p <= 1.0) {
            bernoulli.setProbability(p);
        }
        else {
            throw new IllegalArgumentException("Binomial p must be between 0.0 and 1.0: " + p);
        }
    }
    
    /**
     * @return  */
    public double getProbability() { return bernoulli.getProbability(); }
    
    /**
     * @return  */
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {}
        throw new InternalError();
    }
    
    /**
     * @return  */
    public String toString() { return "Binomial (" + n + ", " + bernoulli.getProbability() + ")"; }
}
