/*
 * BinomialVariate.java
 *
 * Created on March 29, 2002, 3:31 PM
 */

package simkit.random;

/**
 *
 * @author  Arnold Buss
 * @version
 */
public class BinomialVariate implements DiscreteRandomVariate {
    
    protected int n;
    /**
     */
    protected BernoulliVariate bernoulli;
    
    /** Creates new BinomialVariate */
    public BinomialVariate() {
        bernoulli = new BernoulliVariate();
    }
    
    /**
     * @return The underlying RandomNumber instance (should be a copy)
     */
    public RandomNumber getRandomNumber() { return bernoulli.getRandomNumber(); }
    
    /** Sets the supporting RandomNumber object
     * @param rng The RandomNumber instance supporting the generating algorithm
     */
    public void setRandomNumber(RandomNumber rng) { bernoulli.setRandomNumber(rng); }
    
    /**
     * Returns the array of parameters as an Object[].
     * @return the array of parameters as an Object[].
     */
    public Object[] getParameters() {
        return new Object[] { new Integer(n), new Double(bernoulli.getProbability()) };
    }
    
    /** Sets parameters - first is n, the second is p
     * @param params (n, p)
     */
    public void setParameters(Object[] params) {
        if (params == null) {
            throw new NullPointerException();
        }
        else if (params.length != 2 ){
            throw new IllegalArgumentException("Binomial needs 2 parameters: " + params.length);
        }
        setN( ((Number)params[0]).intValue());
        setProbability( ((Number)params[1]).doubleValue());
    }
    
    /** Dumb method, but works...
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
     * @param n  */
    public void setN(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Binomial variate must have positive n: " + n);
        }
        this.n = n;
    }
    
    /**
     * @return  */
    public int getN() { return n; }
    
    /**
     * @param p  */
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
