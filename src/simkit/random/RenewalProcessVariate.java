/*
 * RenewalProcessVariate.java
 *
 * Created on March 29, 2002, 7:46 AM
 */

package simkit.random;

/**
 *
 * @author  Arnold Buss
 * @version 
 */
public class RenewalProcessVariate implements RandomVariate {
    
    protected RandomVariate interarrivalTime;
    protected double lastTime;

    /** Creates new RenewalProcessVariate */
    public RenewalProcessVariate() {
        lastTime = 0.0;
    }

    /**
     * @return The underlying RandomNumber instance (should be a copy)
     */
    public RandomNumber getRandomNumber() {
        return interarrivalTime.getRandomNumber();
    }
    
    /** Sets the supporting RandomNumber object
     * @param rng The RandomNumber instance supporting the generating algorithm
     */
    public void setRandomNumber(RandomNumber rng) {
        interarrivalTime.setRandomNumber(rng);
    }
    
    /**
     * Returns the array of parameters as an Object[].
     * @return the array of parameters as an Object[].
     */
    public Object[] getParameters() {
        return new Object[] { interarrivalTime };;
    }
    
    /**
     * Sets the random variate's parameters.
     * Alternatively, the parameters could be set in the constructor or
     * in additional methods provided by the programmer.
     * @param params the array of parameters, wrapped in objects.
     */
    public void setParameters(Object[] params) {
        if (params == null) {
            throw new NullPointerException("RenewalProcess params cannot be null");
        }
        if(params.length != 1) {
            throw new IllegalArgumentException("RenewalProcess needs 1 parameter: " + params.length);
        }
        if (!(params[0] instanceof RandomVariate)) {
            throw new IllegalArgumentException("RenewalProcess needs RandomVariate instance: " + params[0].getClass().getName());
        }
        this.setInterarrivalTime((RandomVariate) params[0]);
    }
    
    /**
     * Generate a random variate having this class's distribution.
     * @return The generated random variate
     */
    public double generate() {
        lastTime += interarrivalTime.generate();
        return lastTime;
    }
    
    public void setInterarrivalTime(RandomVariate rv) {
        interarrivalTime = (RandomVariate) rv.clone();
        interarrivalTime.setRandomNumber(rv.getRandomNumber());
    }
    
    public RandomVariate getInterarrivalTime() { return interarrivalTime; }
    
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {}
        return null;
    }
    
}
