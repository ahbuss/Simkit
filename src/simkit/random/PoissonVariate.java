package simkit.random;

/**
 * Generates random variates having a Poisson distribution.
 * @author  Arnold Buss
 * @version $Id: PoissonVariate.java 1051 2008-02-27 00:14:47Z ahbuss $
 */
public class PoissonVariate implements DiscreteRandomVariate {
    
/**
* The instance of the supporting RandomNumber.
**/
    protected RandomNumber rng;

/**
* The desired mean of this PoissonVariate.
**/
    protected double mean;

/**
* A precalculated value to aid in generation. e<sup>-mean</sup>
**/
    protected double a;
    
    /** 
      * Creates new PoissonVariate; the mean must be set prior to use.
    */
    public PoissonVariate() {
        setRandomNumber(RandomVariateFactory.getDefaultRandomNumber());
    }
    
    /**
     * Returns the instance of the supporting RandomNumber
     * @return The underlying RandomNumber instance
     */
    public RandomNumber getRandomNumber() { return rng; }
    
    /** Sets the supporting RandomNumber object
     * @param rng The RandomNumber instance supporting the generating algorithm
     */
    public void setRandomNumber(RandomNumber rng) { this.rng = rng; }
    
    /**
     * Returns a single element array containing the mean as a Double.
     */
    public Object[] getParameters() {
        return new Object[] { new Double(mean) };
    }
    
    /**
      * Sets the desired mean of the RadomVariate.
      * @param params A single element array containing the mean as a Number.
      * @throws IllegalArgumentException If the array does not have exactly 1 element,
      * if the element is not a Number, or if the mean if not positive.
     */
    public void setParameters(Object... params) {
        if (params == null) { 
            throw new NullPointerException(); 
        }
        else if (params.length != 1) {
            throw new IllegalArgumentException("PoissonVariate needs exatly 1 parameter: " + params.length);
        }
        if (params[0] instanceof Number) {
           setMean(((Number) params[0]).doubleValue());
        } else {
           throw new IllegalArgumentException("The parameter must be a Number.");
        }
    }
    
/**
* Generates the next value as an <code>int</code>.
**/
    public int generateInt() {
        int x = 0;
        for (double y = rng.draw(); y >= a; y *= rng.draw()) {
            x++;
        }
        return x;
    }
    
    /**
     * Generates a random variate having this class's distribution.
     * @return The generated random variate
     */
    public double generate() {
        return generateInt();
    }
    
/**
* Sets the desired mean.
* @throws IllegalArgumentException If the mean is not positive.
**/
    public void setMean(double mean) {
        if (mean <= 0.0) {
            throw new IllegalArgumentException("PoissonVariate must have positive mean: " + mean);
        }
        this.mean = mean;
        a = Math.exp(-mean);
    }
    
/**
* Gets the value of the desired mean.
**/
    public double getMean() { return mean; }

/**
* Returns a String containing the name and mean of this RandomVariate.
**/
    public String toString() { return "Poisson (" + getMean() + ")"; }
}
