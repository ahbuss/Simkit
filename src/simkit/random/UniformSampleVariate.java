/*
 * UniformSampleVariate.java
 *
 * Created on August 30, 2001, 8:32 PM
 */

package simkit.random;

/**
 * Generates a random variate by choosing, with equal likelyhood, values
 * from a supplied sample array.
 * @author  ahbuss
 * @version $Id$
 */
public class UniformSampleVariate extends simkit.random.RandomVariateBase {

/**
* The array holding the values to choose from.
**/
    private double[] sample;
    
    /** 
      * Creates new UniformSampleVariate with no sample array.
      * The sample array must be set prior to use.
       */
    public UniformSampleVariate() {
    }

    /**
     * Generates a random variate having this class's distribution.
    */
    public double generate() {
        return sample[(int) (sample.length * rng.draw())];
    }
    
    /**
      * Returns a single element array holding a copy of the sample array.
    */
    public Object[] getParameters() {
        return new Object[] { sample.clone() };
    }
    
    /**
      * Sets the values to sample from.
      * @param params A single element array containing either an array of
      * <code>doubles</code> or an array of <code>ints</code>.
      * @throws IllegalArgumentException If the array doesn't have exactly one element,
      * or if the element is neither a <code>double[]</code> or an <code>int[]</code>.
    */
    public void setParameters(Object[] params) {
        if (params.length != 1) {
            throw new IllegalArgumentException("The array must contain exactly 1 element"
                                                + " it had " + params.length);
        }
        if (params[0] instanceof double[]) {
            sample = (double[]) ((double[]) params[0]).clone();
        }
        else if (params[0] instanceof int[]) {
            sample = new double[((int[])params[0]).length];
            for (int i = 0; i < sample.length; i++) {
                sample[i] = (double) ((int[])params[0])[i];
            }
        } else {
           throw new IllegalArgumentException("The element in the array must be either a"
                                       + " double[] or an int[], it was a " + 
                                    params[0].getClass().getName());
       }
    }
    
/**
* Sets the sample array.
* @param sample An array of the values to sample from.
**/
    public void setSample(double[] sample) { this.sample = (double[]) sample.clone(); }
    
/**
* Returns a copy of the sample array.
**/
    public double[] getSample() { return (double[]) sample.clone(); }
    
/**
* Returns a String listing the contents of the sample array.
**/
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
