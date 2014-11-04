package simkit.random;

/**
* Generates continuous uniform random variates.
*
* @version $Id$
**/
public class UniformVariate extends RandomVariateBase {
    
/**
* Not currently used.
**/
    private static final double DEFAULT_LOW = 0.0;
    
/**
* Not currently used.
**/
    private static final double DEFAULT_HIGH = 1.0;
    
/**
* The lowest possible value.
**/
    private double minimum;
    
/**
* The highest possible value.
**/
    private double maximum;

/**
* The difference between the maximum and minimum values. (Precalculated)
**/
    private double range;
    
/**
* Creates a new UniformVariate, parameters must be set prior to use.
**/
    public UniformVariate() {
    }
    
/**
* Sets the minimum and maximum values.
* @param params A 2 element array containing the minimum and the maximum as Numbers.
* @throws IllegalArgumentException If the array doesn't contain exactly 2 elements, if 
 the elements are not Numbers, or if the minimum is larger than the maximum.
**/
    @Override
    public void setParameters(Object... params) {
        double newLow = 0.0;
        double newHigh = 1.0;
        if (params.length != 2) {
            throw new IllegalArgumentException("Requires 2 elements, supplied " + params.length);
        }
        if (params[0] instanceof Number && params[1] instanceof Number) {
           newLow = ((Number)params[0]).doubleValue();
           newHigh = ((Number)params[1]).doubleValue();
        } else {
           throw new IllegalArgumentException("The array elements must be Numbers (" + 
                     params[0].getClass().getName() + ", " + 
                     params[1].getClass().getName() + ") supplied. ");
        }
        
        if (newLow > newHigh) {
            throw new IllegalArgumentException("Uniform parameters must have low < high: (" +
            newLow + ", " + newHigh + ")");
        }
        this.minimum = newLow;
        this.maximum = newHigh;
        range = maximum - minimum;
    }
    /**
     * 
     * @return  a 2 element array containing the minimum and maximum limits.
     */
    @Override
    public Object[] getParameters() {
        return new Object[] {new Double(minimum), new Double(maximum)};
    }
    
//javadoc inherited.
    @Override
    public double generate() {
        return minimum + range * rng.draw();
    }
    
/**
* Sets the minimum value. Causes internal precalculated values to be updated.
**/
    public void setMinimum(double min) {
        minimum = min;
        range = maximum - minimum;
    }
    
/**
* Returns the minimum value.
**/
    public double getMinimum() { return minimum; }
    
/**
* Sets the maximum value. Causes internal precalculated values to be updated.
**/
    public void setMaximum(double max) {
        maximum = max;
        range = maximum - minimum;
    }
    
    public double getMaximum() {
        return maximum;
    }

/**
* Returns a String containing the name of this distribution with the minimum and maximum 
 parameters.
**/
    public String toString() { return "Uniform (" + minimum + ", " + maximum + ")"; }
}
