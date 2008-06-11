package simkit.random;

/**
* Generates continuous uniform random variates.
*
* @version $Id: UniformVariate.java 1000 2007-02-15 19:43:11Z ahbuss $
**/
public class UniformVariate extends RandomVariateBase {
    
/**
* Not currently used.
**/
    private static double DEFAULT_LOW;
    
/**
* Not currently used.
**/
    private static double DEFAULT_HIGH;
    
/**
* The lowest possible value.
**/
    private double low;
    
/**
* The highest possible value.
**/
    private double high;

/**
* The difference between the high and low values. (Precalculated)
**/
    private double range;
    
    static {
        DEFAULT_LOW = 0.0;
        DEFAULT_HIGH = 1.0;
    }
    
/**
* Creates a new UniformVariate, parameters must be set prior to use.
**/
    public UniformVariate() {
    }
    
/**
* Sets the low and high values.
* @param params A 2 element array containing the low and the high as Numbers.
* @throws IllegalArgumentException If the array doesn't contain exactly 2 elements, if 
* the elements are not Numbers, or if the low is larger than the high.
**/
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
        this.low = newLow;
        this.high = newHigh;
        range = high - low;
    }
    
/**
* Returns a 2 element array containing the low and high limits.
**/
    public Object[] getParameters() {
        return new Object[] {new Double(low), new Double(high)};
    }
    
//javadoc inherited.
    public double generate() {
        return low + range * rng.draw();
    }
    
/**
* Sets the minimum value. Causes internal precalculated values to be updated.
**/
    public void setMinimum(double min) {
        low = min;
        range = high - low;
    }
    
/**
* Returns the minimum value.
**/
    public double getMinimum() { return low; }
    
/**
* Sets the maximum value. Causes internal precalculated values to be updated.
**/
    public void setMaximum(double max) {
        high = max;
        range = high - low;
    }
    
/**
* Returns the minimum value.
**/
    public double getLow() { return low; }

/**
* Returns the maximum value.
**/
    public double getHigh() { return high; }
    
/**
* Returns a String containing the name of this distribution with the low and high 
* parameters.
**/
    public String toString() { return "Uniform (" + low + ", " + high + ")"; }
}
