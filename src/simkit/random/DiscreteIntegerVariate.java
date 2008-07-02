package simkit.random;

import simkit.random.DiscreteRandomVariate;
import simkit.random.RandomVariateBase;

/** <p>Generates random variates having an arbitrary
 * discrete distribution with integer values.  The input is
 * given by a set of values and a set of either probabilities or
 * frequencies.
 *
 * <p>The parameters should have signature (int[], double[]), where
 * the int[] array contains the possible values and the double[] array
 * contains the frequencies, which may or may not sum to 1, but which
 * must at least be non-negative.  These are normalized to be a cdf inside
 * the class.
 *
 * @version $Id$
 * @author ahbuss
 */
public class DiscreteIntegerVariate extends RandomVariateBase
        implements DiscreteRandomVariate{
    
    private int[] value;
    
    private double[] cdf;
    
    /** Creates a new instance of DiscreteIntegerVariate */
    public DiscreteIntegerVariate() {
    }
    
    public double generate() {
        return (double) generateInt();
    }
    /**
     * Sets the cdf for this DiscreteRandomVariate.
     * First argument is an array of doubles that define where the cdf changes.
     * The second argument is an array of doubles that specify either
     * frequencies or probabilities at the points given.
     *
     * If (values, frequencies) are given, then the frequencies
     * are normalized to sum to 1.
     * @param params (values, prob) as (double[], double[])
     * @throws IllegalArgumentException If the given array does not have 2 elements,
     * if the elements are not arrays of doubles, or if the two double arrays are not
     * the same length.
     * @throws IllegalArgumentException If any of the probabilities/frequencies are
     * negative or they're sums are zero.
     */
    public void setParameters(Object... params) {
        if (params.length != 2) {
            throw new IllegalArgumentException(
                    "2 Arguments needed: " + params.length);
        }
        if (!(params[0] instanceof int[] && params[1] instanceof double[])) {
            throw new IllegalArgumentException(
                    "Parameters muct be {int[], double[]}: {" +
                    params[0].getClass().getName() + ", " +
                    params[1].getClass().getName() + "}");
        }
        int[] val = (int[]) params[0];
        double[] freq = (double[]) params[1];
        if (freq.length != val.length) {
            throw new IllegalArgumentException(
                    "Values and frequency parameters not same length: " +
                    val.length + " != " + freq.length );
        }
        setValue(val);
        setCdf(normalize(freq));
    }
    
    public Object[] getParameters() {
        return new Object[] { getValue(), getCdf() };
    }
    
    public int generateInt() {
        int index;
        double uniform = this.rng.draw();
        for (index = 0; (uniform > cdf[index]) && (index < cdf.length - 1); index++) ;
        return value[index];
    }
    
    /**
     * Convert the given array of probabilities/frequencies to a cdf.
     *
     * @throws IllegalArgumentException If any of the probabilities/frequencies are
     * negative or they sum to zero.
     */
    public static double[] normalize(double[] freq) {
        double[] norm = null;
        double sum = 0.0;
        norm = new double[freq.length];
        for (int i = 0; i < freq.length; i++) {
            if (freq[i] >= 0.0) {
                sum += freq[i];
                norm[i] = sum;
            } else {
                throw new IllegalArgumentException("Bad frequency value at index " +
                        i + " (value = " + freq[i] + ")");
            }
        }
        if (sum > 0.0) {
            for (int i = 0; i < norm.length; i++) {
                norm[i] = norm[i] / sum;
            }
        } else {
            throw new IllegalArgumentException("Frequency sum not positive: " + sum);
        }
        return norm;
    }
    
    public int[] getValue() {
        return (int[]) value.clone();
    }
    
    public void setValue(int[] value) {
        this.value = (int[]) value.clone();
    }
    
    public double[] getCdf() {
        return (double[]) cdf.clone();
    }
    
    public void setCdf(double[] cdf) {
        this.cdf = (double[]) cdf.clone();
    }
    /**
     * Returns a String containing a table representation of the pdf and cdf.
     **/
    public String toString() {
        java.text.DecimalFormat df = new java.text.DecimalFormat("0.000");
        StringBuffer buf = new StringBuffer();
        buf.append("x    \tf(x)   \tF(x)\n");
        for (int i = 0; i < cdf.length; i++) {
            buf.append(value[i]);
            buf.append('\t');
            buf.append(df.format(i == 0 ? cdf[i] : cdf[i] - cdf[i-1]));
            buf.append('\t');
            buf.append(df.format(cdf[i]));
            buf.append('\n');
        }
        return buf.toString();
    }

    @Override
    public void setParameter(String paramName, Object paramValue) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}