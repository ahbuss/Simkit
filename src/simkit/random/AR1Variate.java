package simkit.random;

/**
 *  A simple AR(1) process.<br>
 *  To do: make error variate generic, implement general AR(m) process.         
 *
 *  @author  Arnold Buss
 */
public class AR1Variate extends RandomVariateBase {
    
    private double lastValue;
    private double alpha;
    private RandomVariate error;

    /** Creates new AR1Variate */
    public AR1Variate() {
        error = RandomVariateFactory.getInstance("simkit.random.NormalVariate",
            new Object[] { new Double(0.0), new Double(1.0) }, rng);
    }

    /**
     * Returns the array of parameters as an Object[].
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
     * Generate a random variate having this class's distribution.
     * @return The generated random variate
     */
    public double generate() {
        return lastValue = alpha * lastValue + error.generate();
    }
    
    public void setAlpha(double a) { alpha = a; }
    
    public double getAlpha() { return alpha; }
    
    public void setLastValue(double lv) { lastValue = lv; }
    
    public double getLastValue() { return lastValue; }
        
}
