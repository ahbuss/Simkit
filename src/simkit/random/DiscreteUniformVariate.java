package simkit.random;

/**
 * Generates Discrete Uniform (a, b) random variates.<br>
 * Parameters:
 * <ul> <li><code>minimum</code> = smallest possible value (integer).</li>
 * <li> <code>maximum</code> = largest possible value (integer).</li></ul>
 * @author Arnold Buss
 */
public class DiscreteUniformVariate extends RandomVariateBase implements DiscreteRandomVariate {

    protected int minimum;
    protected int maximum;
    private int range;

    public DiscreteUniformVariate() {
    }

    /**
     * @param params (minimum, maximum) */    
    public void setParameters(Object[] params) {
        if (params.length != 2) {
            throw new IllegalArgumentException("Two parameters needed, " +
                params.length + " given");
        }
        if (params[0] instanceof Number && params[1] instanceof Number) {
            minimum = ((Integer)params[0]).intValue();
            maximum = ((Integer)params[1]).intValue();
            range = maximum - minimum + 1;
        }
        else {
            throw new IllegalArgumentException("Parameters must be Integer");
        }
    }
    
    /**
     * @return (minimum, maximum) wrapped as Doubles in Object[]
     */    
    public Object[] getParameters() {
        return new Object[] { new Integer(minimum), new Integer(maximum) };
    }

    /**
     * @return generated Discrete Uniform(a,b) variate as double
     */    
    public double generate() {
        return (double) generateInt();
    }

    public String toString() {
        return "Discrete Uniform (" + minimum + ", " + maximum + ")";
    }

    /**
     * @return generated Discrete Uniform(a, b) variate as integer
     */    
    public int generateInt() {
        return (int) Math.floor(minimum + range * rng.draw());
    }
    
    /**
     * @param min smallest possible value
     */    
    public void setMinimum(int min) {
            minimum = min;
            range = maximum - minimum;
    }

    /**
     * @param max largest possible value
     */    
    public void setMaximum(int max) {
            maximum = max;
            range = maximum - minimum;
    }

    /**
     * @return largest possible value
     */    
    public int getMaximum() { return maximum; }
    /**
     * @return smallest possible value
     */    
    public int getMinimum() { return minimum; }
}
