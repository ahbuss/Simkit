package simkit.random;

/**
 *
 * @author  Arnold Buss
 * @version 
 */
public class GeometricVariate implements DiscreteRandomVariate {

    private double p;
    private double multiplier;
    private RandomNumber rng;
    
    /** Creates new Class */
    public GeometricVariate() {
        rng = RandomNumberFactory.getInstance();
    }

    /**
     * @return The underlying RandomNumber instance (should be a copy)
     */
    public RandomNumber getRandomNumber() {
        return rng;
    }
    
    /** Sets the supporting RandomNumber object
     * @param rng The RandomNumber instance supporting the generating algorithm
     */
    public void setRandomNumber(RandomNumber rng) {
        this.rng = rng;
    }
    
    /**
     * Returns the array of parameters as an Object[].
     * @return the array of parameters as an Object[].
     */
    public Object[] getParameters() {
        return new Object[] { new Double(p) };
    }
    
    /**
     * Sets the random variate's parameters.
     * Alternatively, the parameters could be set in the constructor or
     * in additional methods provided by the programmer.
     * @param params the array of parameters, wrapped in objects.
     */
    public void setParameters(Object[] params) {
        if (params == null) {
            throw new NullPointerException("Parameterst to GeometricVariate null");
        }
        else if (params.length != 1) {
            throw new IllegalArgumentException("1 parameters needed: " + params.length);
        }
        setP(((Number) params[0]).doubleValue());
    }
    
    public int generateInt() {
        return (int) (Math.log(rng.draw()) * multiplier);
    }
    
    /**
     * Generate a random variate having this class's distribution.
     * @return The generated random variate
     */
    public double generate() {
        return generateInt();
    }
    
    public void setP(double prob) {
        if (p < 0.0 || p > 1.0) {
            throw new IllegalArgumentException("p must be in [0, 1]: " + prob);
        }
        p = prob;
        multiplier = 1.0 / Math.log(1.0 - p);
    }
    
    public double getP() { return p; }
    
    public Object clone() {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException e) {}
        return null;
    }
    
    public String toString() { return "Geometric (" + p + ")"; }
}
