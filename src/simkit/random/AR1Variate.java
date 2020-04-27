package simkit.random;

/*
 *  TODO:  implement general AR(m) process.         
*/
/**
 *  A simple first order auto regressive AR(1) process.
 *  The next value of the AR1Variate is alpha * last value + random noiseGenerator.
  <br>The distribution of the random noiseGenerator is N(0, &sigma;<sup>2</sup>).
 *
 *  @author  Arnold Buss
 *  
 */
public class AR1Variate extends RandomVariateBase {
    
    protected double lastValue;
    private double alpha;
    private double noiseVariance;
    private double initialValue;
    private final RandomVariate noiseGenerator;

/** 
* Creates new AR1Variate with a normal(0,1) noiseGenerator distribution.
*/
    public AR1Variate() {
        noiseGenerator = RandomVariateFactory.getInstance(
                "Normal", 0.0, 1.0);
    }

    /**
     * Returns the current value of the parameters as an array of Objects.
     * The first element is the multiplication factor, the second element
 is the noiseGenerator variance, and the third element is the current (last) value.
     * @return The array of parameters as an Object[].
     */
    @Override
    public Object[] getParameters() {
        return new Object[] { getAlpha(), getNoiseVariance(), getInitialValue()};
    }
    
    /**
     * Sets the random variate's parameters.
     * The first parameter is alpha, the multiplier; the second is the variance of the
 Gaussian noiseGenerator term; the optional third parameter is the starting value,
 which defaults to 0.0 if none specified.
     * @param params The array of parameters, wrapped in objects.
     * @throws IllegalArgumentException If the parameter array has
     * more than 2 elements. Note: If the array has no parameters, then
     * does nothing. If the array has 1 element, alpha is set and the current
     * value is unchanged. If the first parameter is not a
     * Number, then it is ignored.
     */
    @Override
    public void setParameters(Object... params) {
        if (params.length > 3 || params.length < 2) {
            throw new IllegalArgumentException("Need 2 or 3 parameters, "
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

        if (params.length == 3 && params[2] instanceof Number) {
            this.setInitialValue(((Number) params[2]).doubleValue());
        } else if (params.length == 2) {
            this.setInitialValue(0.0);
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
        double generatedValue = getLastValue();
        lastValue = alpha * lastValue + noiseGenerator.generate();
        return generatedValue;
    }
    
    /**
     * 
     * @param alpha the multiplication factor.
     */
    public void setAlpha(double alpha) { this.alpha = alpha; }
    
    /**
     * 
     * @return the multiplication factor.
     */
    public double getAlpha() { return this.alpha; }
        
    /**
     * 
     * @return the current ("last") value.
     */
    public double getLastValue() { return this.lastValue; }
        
    
    @Override
    public String toString() {
        return String.format("AR1 Variate (\u03B1 = %.2f \u03C3\u00B2 = %.2f X\u2080 = %.2f)", 
                getAlpha(), getNoiseVariance(), getInitialValue());
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
        this.noiseGenerator.setParameters(0.0, getNoiseVariance());
    }

    /**
     * @return the initialValue
     */
    public double getInitialValue() {
        return initialValue;
    }

    /**
     * @param initialValue the initialValue to set
     */
    public void setInitialValue(double initialValue) {
        this.initialValue = initialValue;
        this.lastValue = initialValue;
    }
}
