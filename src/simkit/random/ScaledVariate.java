/*
 * ScaledVariate.java
 *
 * Created on July 30, 2002, 12:51 PM
 */

package simkit.random;

/**
 * Given a RandomVariate, this will scale and (optionally) shift it.  
 * @author  Arnold Buss
 * @version $Id$
 */
public class ScaledVariate implements RandomVariate {
    
/**
* The instance of the underlying RandomVariate.
**/
    private RandomVariate rv;

/**
* The amount to shift the underlying RandomVariate by.
**/
    private double shift;

/**
* The amount to scale the underlying RandomVariate by.
**/
    private double scale;
    
    /** 
      * Creates a new instance of ScaledVariate with zero shift and 1.0 scale. Does
      * not set the underlying RandomVariate.
    */
    public ScaledVariate() {
        setShift(0.0);
        setScale(1.0);
    }
    
    /** Generate a random variate having this class's distribution.
     * @return The generated random variate
     */
    public double generate() {
        return shift + scale * rv.generate();
    }
    
/**
* Returns a 2 element array with the shift and the scale as Doubles.
**/
    public Object[] getParameters() {
        return new Object[] { new Double(getShift()), new Double(getScale())};
    }
    
/**
* Sets the underlying RandomVariate, the scale, and the shift. If the shift
* is not specified, it is left unchanged.
* @param params A 2 or 3 element array containing: The instance of the 
* underlying RandomVariate, the scale as a Number, and (optionally) the shift
* as a Number.
* @throws IllegalArgumentException If the array does not have 2 or 3 elements,
* if the first element is not a RandomVariate, if either the 2nd or 3rd (if 
* specified) is not a Number, or if the scale is not positive.
**/
    public void setParameters(Object[] params) {
        if (params.length != 2 || params.length != 3) {
            throw new IllegalArgumentException("Needs 2 or 3 parameters");
        }
        if (params[0] instanceof RandomVariate) {
            setRandomVariate((RandomVariate) params[0]);
        }
        else {
            throw new IllegalArgumentException("Must give a RandomVariate instance: " +
                params[0].getClass().getName());
        }
        if (params[1] instanceof Number) {
            setScale(((Number) params[1]).doubleValue());
        }
        else {
            throw new IllegalArgumentException("Scale must be a Number: " +
            params[1].getClass().getName());
        }
        if (params.length == 3) {
            if (params[2] instanceof Number) {
                setShift( ((Number) params[2]).doubleValue());
            }
            else {
                throw new IllegalArgumentException("Shift must be a Number: " +
                params[2].getClass().getName());
            }
        }
    }
    
    /** 
      * Returns the instance of the supporting RandomNumber.
      * @return The underlying RandomNumber instance
     */
    public RandomNumber getRandomNumber() {
        return rv.getRandomNumber();
    }
    
    /** Sets the supporting RandomNumber object
     * @param rng The RandomNumber instance supporting the generating algorithm
     */
    public void setRandomNumber(RandomNumber rng) {
        rv.setRandomNumber(rng);
    }
    
/**
* Sets the amount to shift the RandomVariate.
**/
    public void setShift(double s) { shift = s; }
    
/**
* Returns the current value of the shift.
**/
    public double getShift() { return shift; }
    
/**
* Sets the amount to scale the RandomVaraite by.
* @throws IllegalArgumentException If the scale is not positive.
**/
    public void setScale(double s) {
        if (s > 0.0) {
            scale = s;
        }
        else {
            throw new IllegalArgumentException("Scale must be positive: " + s);
        }
    }
    
/**
* Returns the current value of the scale.
**/
    public double getScale() { return scale; }
    
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {}
        return null;
    }
    
/**
* Sets the underlying RandomVariate.
**/
    public void setRandomVariate(RandomVariate r) { rv = r; }
    
/**
* Gets the instance of the underlying RandomVariate.
**/
    public RandomVariate getRandomVariate() { return rv; }
    
/**
* Returns a String with the scale, the shift, and information about the
* underlying RandomVariate.
**/
    public String toString() {
        return "Scaled [" + getScale() + ", " + getShift() + "] " + rv;
    }
}
