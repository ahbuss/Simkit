package simkit.random;

/*
 *  TODO make error variate generic, implement general AR(m) process.         
*/
/**
 *  A simple first order auto regressive AR(1) process.
 *  The next value of the AR1Variate is alpha * last value + random error.
 *  <br>The distribution of the random error is N(0,1).
 *
 *  @author  Arnold Buss
 *  @version $Id$
 */
public class AR1Variate extends RandomVariateBase {
    
    private double lastValue;
    private double alpha;
    private double noiseVariance;
    private final RandomVariate error;

/** 
* Creates new AR1Variate with a normal(0,1) error distribution.
*/
    public AR1Variate() {
        error = RandomVariateFactory.getInstance(
                "Normal", 0.0, 1.0);
    }

    /**
     * Returns the current value of the parameters as an array of Objects.
     * The first element is the multiplication factor, the second element
     * is the current value.
     * @return The array of parameters as an Object[].
     */
    @Override
    public Object[] getParameters() {
        return new Object[] { getAlpha(), getLastValue() };
    }
    
    /**
     * Sets the random variate's parameters.
     * The first parameter is alpha, the multiplier; the second parameter
     * is the starting value.
     * @param params The array of parameters, wrapped in objects.
     * @throws IllegalArgumentException If the parameter array has
     * more than 2 elements. Note: If the array has no parameters, then
     * does nothing. If the array has 1 element, alpha is set and the current
     * value is unchanged. If the first parameter is not a
     * Number, then it is ignored.
     */
    public void setParameters(Object... params) {
        if (params.length != 3) {
            throw new IllegalArgumentException("Need 3 parameters, "
                    + params.length + " given.");
        }
        if (params[0] instanceof Number) {
            this.setAlpha(((Number) params[0]).doubleValue());
        } else {
            throw new IllegalArgumentException("\u03B1 must be numeric: "
                    + params[0].getClass().getName());
        }
        if (params[1] instanceof Number) {
            this.setNoiseVariance(((Number) params[1]).doubleValue());
        } else {
            throw new IllegalArgumentException("\u03C3^2 must be numeric: "
                    + params[1].getClass().getName());
        }

        if (params[2] instanceof Number) {
            this.setLastValue(((Number) params[1]).doubleValue());
        } else {
            throw new IllegalArgumentException("X_0 must be numeric: "
                    + params[2].getClass().getName());
        }

    }
    
    /**
     * Generate the next value.
     * @return The generated random variate
     */
    @Override
    public double generate() {
        return lastValue = alpha * lastValue + error.generate();
    }
    
/**
* Sets the multiplication factor.
**/
    public void setAlpha(double a) { alpha = a; }
    
/**
* Gets the multiplication factor.
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
        
    
    @Override
    public String toString() {
        return String.format("AR(1) Variate (\u03B1 = %.2f \u03C3 = %.2f)", 
                getAlpha(), getNoiseVariance());
    }

    public double getNoiseVariance() {
        return noiseVariance;
    }

    public void setNoiseVariance(double noiseVariance) {
        if (noiseVariance <= 0.0) {
            throw new IllegalArgumentException("noiseVariance must be > 0.0: " +
                    noiseVariance);
        }
        this.noiseVariance = noiseVariance;
    }
}
