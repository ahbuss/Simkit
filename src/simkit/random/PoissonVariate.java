/*
 * PoissonVariate.java
 *
 * Created on March 30, 2002, 3:34 AM
 */

package simkit.random;

/**
 *
 * @author  Arnold Buss
 * @version
 */
public class PoissonVariate implements DiscreteRandomVariate {
    
    protected RandomNumber rng;
    protected double mean;
    protected double a;
    
    /** Creates new PoissonVariate */
    public PoissonVariate() {
        rng = RandomNumberFactory.getInstance();
    }
    
    /**
     * @return The underlying RandomNumber instance (should be a copy)
     */
    public RandomNumber getRandomNumber() { return rng; }
    
    /** Sets the supporting RandomNumber object
     * @param rng The RandomNumber instance supporting the generating algorithm
     */
    public void setRandomNumber(RandomNumber rng) { this.rng = rng; }
    
    /**
     * Returns the array of parameters as an Object[].
     * @return the array of parameters as an Object[].
     */
    public Object[] getParameters() {
        return new Object[] { new Double(mean) };
    }
    
    /**
     * Sets the random variate's parameters.
     * Alternatively, the parameters could be set in the constructor or
     * in additional methods provided by the programmer.
     * @param params the array of parameters, wrapped in objects.
     */
    public void setParameters(Object[] params) {
        if (params == null) { throw new NullPointerException(); }
        else if (params.length != 1) {
            throw new IllegalArgumentException("PoissonVariate needs exatly 1 parameter: " + params.length);
        }
        setMean(((Number) params[0]).doubleValue());
    }
    
    public int generateInt() {
        int x = 0;
        for (double y = rng.draw(); y >= a; y *= rng.draw()) {
            x++;
        }
        return x;
    }
    
    /**
     * Generate a random variate having this class's distribution.
     * @return The generated random variate
     */
    public double generate() {
        return generateInt();
    }
    
    public void setMean(double mean) {
        if (mean <= 0.0) {
            throw new IllegalArgumentException("PoissonVariate must have positive mean: " + mean);
        }
        this.mean = mean;
        a = Math.exp(-mean);
    }
    
    public double getMean() { return mean; }
    
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {}
        throw new InternalError();
    }
    
    public String toString() { return "Poisson (" + getMean() + ")"; }
}
