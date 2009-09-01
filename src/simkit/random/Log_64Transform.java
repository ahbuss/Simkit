package simkit.random;

import simkit.util.Math64;

/**
 * Generates random variates that are the log transform of another random variate.
 * The <code>generate</code> method generates the underlying RandomVariate
 * and if a positive number returns the natural log of it, if negative or zero
 * it returns <code>Double.NaN</code>.
 *
 * <P>Uses simkit.Math64.log() function for replicability on 64-bit
 * platforms.
 *
 * @version $Id$
 **/
public class Log_64Transform  implements RandomVariate {
    
    /**
     * The instance of the underlying RandomVariate.
     **/
    private RandomVariate rv;
    
    /**
     * Constructs a new LogTransform.
     **/
    public Log_64Transform() {
    }
    
    /**
     * Generates the next LogTransform variate. If the underlying RandomVariate
     * generates zero or a negative number, returns <code>Double.NaN</code>.
     **/
    public double generate() {
        double original = rv.generate();
        if (original > 0) {
            return Math64.log(original);
        }
        else {
            return Double.NaN;
        }
    }
    
    /**
     * Returns an array containing the parameters of the underlying RandomVariate.
     **/
    public Object[] getParameters() {
        return new Object[] { getRandomVariate() };
    }
    
    /**
     * Sets the parameters of the underlying RandomVariate.
     **/
    public void setParameters(Object... params) {
        if (params.length != 1) {
            throw new IllegalArgumentException("Need 1 parameter: " + params.length);
        }
        if (! (params[0] instanceof simkit.random.RandomVariate) ) {
            throw new IllegalArgumentException("RandomVariate instance needed: " + params[0].getClass().getName());
        }
        setRandomVariate( (RandomVariate) params[0]);
    }
    
    /**
     *
     * @param randVar Underlying RandomVariate instance
     */    
    public void setRandomVariate(RandomVariate randVar) { rv = randVar; }
    
    /**
     *
     * @return Underlying RandomVariate instance
     */    
    public RandomVariate getRandomVariate() { return rv; }
    
    /**
     * Returns the instance of RandomNumber that supports the underlying
     * RandomVariate.
     **/
    public RandomNumber getRandomNumber() { return rv.getRandomNumber(); }
    
    /**
     * Sets the instance of RandomNumber that supports the underlying
     * RandomVariate.
     **/
    public void setRandomNumber(RandomNumber rng) { rv.setRandomNumber(rng); }
    
    /**
     * Form is "Log { underlying RandomVariate toString() }"
     * @return Description
     */    
    public String toString() {
        return "Log_64 { " + getRandomVariate() + " }";
    }
}
