/*
 * SequentialVariate.java
 *
 * Created on March 14, 2002, 11:17 PM
 */

package simkit.random;
/**
 *
 * Generates sequence of numbers starting with 0 and increasing by 1 
 * 
 * @author  Arnold Buss
 * 
 */
public class SequenceVariate implements DiscreteRandomVariate {

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
    public void setRandomNumber(RandomNumber rng) { }
    
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
    @Override
    public void setParameters(Object... params) {
    }
    
    /**
     * Generate a random variate having this class's distribution.
     * @return The generated random variate
     */
    @Override
    public double generate() {
        return generateInt();
    }
    
    @Override
    public String toString() {
        return "Sequence";
    }

    @Override
    public int generateInt() {
        int value = (int) rng.getSeed();
        rng.draw();
        return value;
    }
}
