/*
 * SequentialVariate.java
 *
 * Created on March 14, 2002, 11:17 PM
 */

package simkit.test;
import simkit.random.RandomNumber;
import simkit.random.RandomVariate;
import simkit.random.Sequential;
/**
 *
 * @author  Arnold Buss
 * @version $Id$
 */
public class SequenceVariate implements RandomVariate {

    private RandomNumber rng;
    
    /** Creates new SequentialVariate */
    public SequenceVariate() {
        rng = new Sequential();
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
        return null;
    }
    
    /**
     * Sets the random variate's parameters.
     * Alternatively, the parameters could be set in the constructor or
     * in additional methods provided by the programmer.
     * @param params the array of parameters, wrapped in objects.
     */
    public void setParameters(Object[] params) {
    }
    
    /**
     * Generate a random variate having this class's distribution.
     * @return The generated random variate
     */
    public double generate() {
        rng.draw();
        return (double) rng.getSeed();
    }
    
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {}
        return null;
    }
    
}
