package simkit.random;

/** Generates random variates having an arbitrary
 * discrete distribution.  The distribution is
 * given by a set of values and a set of either probabilities or
 * frequencies.
 *
 * @version $Id$
 * @author Arnold Buss
 */
public class DiscreteVariate extends RandomVariateBase {

/**
* Holds the cdf defined for this RandomVariate.
**/
    private double[] cdf;

/**
* Holds the values at which the cdf changes for this RandomVariate.
**/
    private double[] value;

/**
* Constructs a new DiscreteVariate with an undefined cdf. Use setParameters to
* define this RandomVarite prior to use.
**/
    public DiscreteVariate() {}

/** 
* Defines the cdf for this RandomVariate.
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
    public void setParameters(Object[] params) {
        if (params.length != 2) {
            throw new IllegalArgumentException("Must have Object[] {double[], double[]}");
        }
        else if (params[0] instanceof double[] && params[1] instanceof double[]) {
            setValues((double[]) params[0]);
            setProbabilities((double[]) params[1]);
        }
        else {
            throw new IllegalArgumentException("Parameters not of type {double[], double[]}");
        }
        if (value.length != cdf.length) {
            throw new IllegalArgumentException("The 2 double arrays are not the same length.");
        }
    }
    
    /** 
     * Returns the values and probabilities that define this RandomVariate.
     * Returns probabilities as second element in array regardless
     * of how it was instantiated or set.
     * @return (values, probabilities)
     */    
    public Object[] getParameters() { return new Object[] { getValues(), getProbabilities() }; }
    
//javadoc inherited
    public double generate() {
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
    protected double[] normalize(double[] freq) {
        double[] norm = null;
        double sum = 0.0;
        norm = new double[freq.length];
        for (int i = 0; i < freq.length; i++) {
            if (freq[i] >= 0.0) {
                sum += freq[i];
                norm[i] = sum;
            }
            else {
                throw new IllegalArgumentException("Bad frequency value at index " +
                    i + " (value = " + freq[i] + ")");
            }
        }
        if (sum > 0.0) {
            for (int i = 0; i < norm.length; i++) {
                norm[i] = norm[i] / sum;
            }
        }
        else {
            throw new IllegalArgumentException("Frequency sum not positive: " + sum);
        }
        return norm;
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

/**
* Sets the array of values at which the cdf changes value.
* Warning: This array must be the same length as the probability and cdf arrays, however
* no checking is done by this method.
**/
    public void setValues(double[] values) { this.value = (double[]) values.clone(); }
    
/**
* Sets the cdf of this RandomVariate based on the contents of the given array.
* Warning: This array must be the same length as the value array, however
* no checking is done by this method.
* @param prob An array containing either the probabilities or frequecies at the
* points contained in the value array.
**/
    public void setProbabilities(double[] prob) { cdf = normalize(prob); }
    
/**
* Returns a copy of the value array.
**/
    public double[] getValues() { return (double[]) value.clone(); }

/**
* Returns a copy of the cdf array.
**/
    public double[] getCDF() { return (double[]) cdf.clone(); }

/**
* Returns an array containing the probability at each point in the value array.
**/
    public double[] getProbabilities() {
        double[] freq = new double[cdf.length];
        for (int i = 0; i < cdf.length; i++) {
            freq[i] = (i == 0) ? cdf[i] :  cdf[i] - cdf[i-1];
        }
        return freq;
    }
}

