/*
 * ScaledVariate.java
 *
 * Created on July 30, 2002, 12:51 PM
 */

package simkit.random;

/**
 * Given a RandomVariate, this will scale and (optionally) shift it.  
 * @author  Arnold Buss
 */
public class ScaledVariate implements RandomVariate {
    
    private RandomVariate rv;
    private double shift;
    private double scale;
    
    /** Creates a new instance of ScaledVariate */
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
    
    /** Returns the array of parameters as an Object[].
     * @return the array of parameters as an Object[].
     */
    public Object[] getParameters() {
        return new Object[] { new Double(getShift()), new Double(getScale())};
    }
    
    /**  Sets the random variate's parameters.
     *  Alternatively, the parameters could be set in the constructor or
     *  in additional methods provided by the programmer.
     * @param params the array of parameters, wrapped in objects.
     */
    public void setParameters(Object[] params) {
        if (params.length != 2 || params.length != 3) {
            throw new IllegalArgumentException("Needs 1 or 2 parameters");
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
    
    /** @return The underlying RandomNumber instance (should be a copy)
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
    
    public void setShift(double s) { shift = s; }
    
    public double getShift() { return shift; }
    
    public void setScale(double s) {
        if (s > 0.0) {
            scale = s;
        }
        else {
            throw new IllegalArgumentException("Scale must be positive: " + s);
        }
    }
    
    public double getScale() { return scale; }
    
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {}
        return null;
    }
    
    public void setRandomVariate(RandomVariate r) { rv = r; }
    
    public RandomVariate getRandomVariate() { return rv; }
    
    public String toString() {
        return "Scaled [" + getScale() + ", " + getShift() + "] " + rv;
    }
}
