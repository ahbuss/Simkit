/*
 * UniformSampleVariate.java
 *
 * Created on August 30, 2001, 8:32 PM
 */

package simkit.random;

/**
 *
 * @author  ahbuss
 * @version 
 */
public class UniformSampleVariate extends simkit.random.RandomVariateBase {

    private double[] sample;
    
    /** Creates new UniformSampleVariate */
    public UniformSampleVariate() {
    }

    /**
     * Generate a random variate having this class's distribution.
 */
    public double generate() {
        return sample[(int) (sample.length * rng.draw())];
    }
    
    /**
     * Returns the array of parameters as an Object[].
 */
    public Object[] getParameters() {
        return new Object[] { sample.clone() };
    }
    
    /**
     * Sets the random variate's parameters.
     * Alternatively, the parameters could be set in the constructor or
     * in additional methods provided by the programmer.
     * @param params the array of parameters, wrapped in objects.
 */
    public void setParameters(Object[] params) {
        if (params.length != 1) {
            throw new IllegalArgumentException();
        }
        if (params[0] instanceof double[]) {
            sample = (double[]) ((double[]) params[0]).clone();
        }
        else if (params[0] instanceof int[]) {
            sample = new double[((int[])params[0]).length];
            for (int i = 0; i < sample.length; i++) {
                sample[i] = (double) ((int[])params[0])[i];
            }
        }
    }
    
    public void setSample(double[] sample) { this.sample = (double[]) sample.clone(); }
    
    public double[] getSample() { return (double[]) sample.clone(); }
    
    public String toString() {
        StringBuffer buf = new StringBuffer("Uniform Sample Distribution\n{");
        for (int i = 0; i < sample.length; i++ ) {
            buf.append(sample[i]);
            buf.append(',');
            buf.append(' ');
            if (i % 20 == 19) { buf.append('\n'); }
        }
        buf.append('}');
        return buf.toString();
    }
}
