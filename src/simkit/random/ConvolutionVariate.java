package simkit.random;
import simkit.random.*;
/**
 * Generates the convolution (sum) of a number of RandomVariates. An
 * array of instances
 * of RandomVariates are supplied as the parameter.
 * Each RandomVariate instance is passed the same RandomNumber instance, to
 * avoid seed management problems.
 * @author  Arnold Buss
 * @version $Id$
 */
public class ConvolutionVariate extends RandomVariateBase {
    
/**
* The array of RandomVariates that are summed.
**/
    private RandomVariate[] rv;
    
/** 
* Creates a new instance of ConvolutionVariate. Generate will return 0.0 until
* the parameter is set.
 */
    public ConvolutionVariate() {
    }
    
    /**
     * Generates the next value which is the sum of the values of the
     * underlying RandomVariate instances.
     * @return sum of a draw from each RandomVariate instance
     */    
    public double generate() {
        double value = 0.0;
        for (int i = 0; i < rv.length; ++i) {
            value += rv[i].generate();
        }
        return value;
    }
    
    /**
     * Returns a single element Object array that contains a clone
     * of the RandomVariate array.
     * @return clone of RandomVariate[] array in Object[] array
     */    
    public Object[] getParameters() {
        return new Object[] { rv.clone() };
    }
    
    /**
     * Sets the underlying RandomVariates. 
     * @param obj Must be a single instance of RandomVariate[]
     * @throws IllegalArgumentException If the array doesn't have 1 element, or if
     * the element is not an array of RandomVariates.
     */    
    public void setParameters(Object[] obj) {
        if (obj.length != 1) {
            throw new IllegalArgumentException("Require 1 parameter: " + obj.length);
        }
        if (!(obj[0] instanceof RandomVariate[])) {
            throw new IllegalArgumentException("Require RandomVariate[]: " + obj[0].getClass().getName());
        }
        
        setRandomVariates( (RandomVariate[]) obj[0] );
    }
    
    /**
     * Sets the array of RandomVariates.
     * @param rand Array of RandomVariate[] instances
     */    
    public void setRandomVariates(RandomVariate[] rand) {
        rv = (RandomVariate[]) rand.clone();
        for (int i = 0; i < rv.length; ++i) {
            rv[i].setRandomNumber(rng);
        }
    }
    
    /**
     * Gets a clone of the array of RandomVariates.
     * @return clone of RandomVariate[] array
     */    
    public RandomVariate[] getRandomVariates() { return (RandomVariate[]) rv.clone(); }
    
    /**
     * Sets the supporting RandomNumber of each underlying RandomVariate.
     */    
    public void setRandomNumber(RandomNumber rand) {
        super.setRandomNumber(rand);
        for (int i = 0; i < rv.length; ++i) {
            rv[i].setRandomNumber(rng);
        }
    }
    
    /**
     * Return a String containing information about the underlying 
     * RandomVariates.
     * @return String including each RandomVariate's toString() on
     * a separate line.
     */    
    public String toString() {
        StringBuffer buf = new StringBuffer("ConvolutionRandomVariate");
        for (int i = 0; i < rv.length; ++i) {
            buf.append('\n');
            buf.append('\t');
            buf.append(rv[i]);
        }
        return buf.toString();
    }
}
