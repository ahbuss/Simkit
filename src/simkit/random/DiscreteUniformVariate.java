package simkit.random;

/**
 * Generates Discrete Uniform (a, b) random variates.<br>
 * Parameters:
 * <ul> <li><code>minimum</code> = smallest possible value (integer).</li>
 * <li> <code>maximum</code> = largest possible value (integer).</li></ul>
 * @author Arnold Buss
 * @version $Id$
 */
public class DiscreteUniformVariate extends RandomVariateBase implements DiscreteRandomVariate {

/**
* The smallest value of this RandomVariate.
**/
    protected int minimum;

/**
* The largest value of this RandomVariate.
**/
    protected int maximum;

/**
* The difference between the min and max, precalculated for performance.
**/
    private int range;

/**
* Creates a new DiscreteUniformVariate with min and max both zero.
**/
    public DiscreteUniformVariate() {
    }

    /**
     * Sets the minimum and maximum values for this RandomVariate.
     * @param params A two element array with the minimum and maximum values
     * as Integers.
     * @throws IllegalArgumentException If the array does not contain 2 elements
     * or the elements are not Integers.
   **/
    public void setParameters(Object... params) {
        if (params.length != 2) {
            throw new IllegalArgumentException("Two parameters needed, " +
                params.length + " given");
        }
        if (params[0] instanceof Integer && params[1] instanceof Integer) {
            minimum = ((Integer)params[0]).intValue();
            maximum = ((Integer)params[1]).intValue();
            range = maximum - minimum + 1;
        }
        else {
            throw new IllegalArgumentException("Parameters must be Integer");
        }
    }
    
    /**
     * Returns an array containing the minimum and maximum as Intergers.
     *
     * @return (minimum, maximum) wrapped as Doubles in Object[]
     */    
    public Object[] getParameters() {
        return new Object[] { new Integer(minimum), new Integer(maximum) };
    }

    /**
     * Generates and returns the next value, cast to a double.
     * @return generated Discrete Uniform(a,b) variate as double
     */    
    public double generate() {
        return (double) generateInt();
    }

/**
* Returns a String containing the name of this variate with the 
* minimum and maximum values.
**/
    public String toString() {
        return "Discrete Uniform (" + minimum + ", " + maximum + ")";
    }

    /**
     * Generates and returns the next value.
     * @return generated Discrete Uniform(a, b) variate as integer
     */    
    public int generateInt() {
        return (int) Math.floor(minimum + range * rng.draw());
    }
    
    /**
     * Sets the smallest value.
     * @param min smallest possible value
     */    
    public void setMinimum(int min) {
            minimum = min;
            range = maximum - minimum;
    }

    /**
     * Sets the largest value.
     * @param max largest possible value
     */    
    public void setMaximum(int max) {
            maximum = max;
            range = maximum - minimum;
    }

    /**
     * Returns the largest value.
     * @return largest possible value
     */    
    public int getMaximum() { return maximum; }

    /**
     * Returns the smallest value.
     * @return smallest possible value
     */    
    public int getMinimum() { return minimum; }
}
