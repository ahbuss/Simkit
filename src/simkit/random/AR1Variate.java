package simkit.random;

/*
 *  TODO make error variate generic, implement general AR(m) process.         
*/
/**
 *  A simple first order auto regressive AR(1) process.<br>
 *  The next value of the AR1Variate is alpha * last value + random error.
 *  <br/>The distribution of the random error is N(0,1).
 *
 *  @author  Arnold Buss
 *  @version $Id$
 */
public class AR1Variate extends RandomVariateBase {
    
    private double lastValue;
    private double alpha;
    private RandomVariate error;

/** 
* Creates new AR1Variate with a normal(0,1) error distribution.
*/
    public AR1Variate() {
        error = RandomVariateFactory.getInstance("simkit.random.NormalVariate",
            new Object[] { new Double(0.0), new Double(1.0) }, rng);
    }

    /**
     * Returns the currect value of the parameters as an array of Objects.
     * The first element is the multiplication factor, the second element
     * is the current value.
     * @return the array of parameters as an Object[].
     */
    public Object[] getParameters() {
        return new Object[] { new Double(alpha), new Double(lastValue) };
    }
    
    /**
     * Sets the random variate's parameters.
     * Alternatively, the parameters could be set in the constructor or
     * in additional methods provided by the programmer.
     * @param params the array of parameters, wrapped in objects.
     */
    public void setParameters(Object[] params) {
        if (params.length > 2) {
            throw new IllegalArgumentException("Need 1 or 2 parameters, " + 
                params.length + " given.");
        }
        if (params.length > 0) {
            if (params[0] instanceof Number) {
                this.setAlpha(((Number)params[0]).doubleValue());
            }
        }
        if (params.length == 2) {
            this.setLastValue(((Number)params[1]).doubleValue());
        }
    }
    
    /**
     * Generate a the next value.
     * @return The generated random variate
     */
    public double generate() {
        return lastValue = alpha * lastValue + error.generate();
    }
    
/**
* Sets the multiplication factor.
**/
    public void setAlpha(double a) { alpha = a; }
    
/**
* Gets the multplication factor.
**/
    public double getAlpha() { return alpha; }
    
/**
* Sets the current value.
**/
    public void setLastValue(double lv) { lastValue = lv; }
    
/**
* Returns the current value.
**/
    public double getLastValue() { return lastValue; }
        
}
