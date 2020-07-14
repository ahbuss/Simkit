package simkit.random;

import java.util.Map;

/**
 * Generates random variates having an arbitrary discrete distribution. The
 * distribution is given by a set of values and a set of either probabilities or
 * frequencies. The values are (now) sorted regardless of how they are set
 * originally.
 *
 * @author Arnold Buss
 */
public class DiscreteVariate extends RandomVariateBase {

    /**
     * Holds the cdf defined for this RandomVariate.
     *
     */
    private double[] cdf;

    /**
     * Holds the values at which the cdf changes for this RandomVariate.
     *
     */
    private double[] values;
    
    private double[] frequencies;

    /**
     * Constructs a new DiscreteVariate with an undefined cdf. Use setParameters
     * to define this RandomVarite prior to use.
     *
     */
    public DiscreteVariate() {
        this.cdf = new double[0];
        this.values = new double[0];
        this.frequencies = new double[0];
    }

    /**
     * Defines the cdf for this RandomVariate. First argument is an array of
     * doubles that define where the cdf changes. The second argument is an
     * array of doubles that specify either frequencies or probabilities at the
     * points given.
     *
     * If (values, frequencies) are given, then the frequencies are normalized
     * to sum to 1.
     *
     * @param params (values, prob) as (double[], double[])
     * @throws IllegalArgumentException If the given array does not have 1 or 2
     * elements, if the elements are not arrays of doubles, or if the two double
     * arrays are not the same length.
     * @throws IllegalArgumentException If any of the probabilities/frequencies
     * are negative or their sums are zero.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void setParameters(Object... params) {
        boolean paramsOK = true;
        String message = "";
        switch (params.length) {
            case 2:
                if (params[0] instanceof double[]) {
                    this.setValues((double[]) params[0]);
                } else if (params[0] instanceof Double[]) {
                    this.setValues((Double[]) params[0]);
                } else {
                    paramsOK = false;
                    message += "params[0] must be double[] of Double[]; ";
                }
                if (params[1] instanceof double[]) {
                    this.setFrequencies((double[]) params[1]);
                } else if (params[1] instanceof Double[]) {
                    this.setFrequencies((Double[]) params[1]);
                } else {
                    paramsOK = false;
                    message += "params[1] must be Double[]; ";
                }
                break;
            case 1:
                if (params[0] instanceof Map) {
                    Map<Double, Double> paramsMap = (Map<Double, Double>) params[0];
                    this.setValues(paramsMap.keySet().toArray(new Double[0]));
                    this.setFrequencies(paramsMap.values().toArray(new Double[0]));
                }
                break;
            default:
                paramsOK = false;
                message += String.format("Must have 1 or 2 parameters: %,d provided", params.length);
                break;
        }
        if (!paramsOK) {
            throw new IllegalArgumentException(message);
        }
//        if (params.length != 2) {
//            throw new IllegalArgumentException("Must have Object[] {double[], double[]}");
//        } else if (params[0] instanceof double[] && params[1] instanceof double[]) {
//            setValues((double[]) params[0]);
//            setFrequencies((double[]) params[1]);
//        } else {
//            throw new IllegalArgumentException("Parameters not of type {double[], double[]}");
//        }
//        if (values.length != cdf.length) {
//            throw new IllegalArgumentException("The 2 double arrays are not the same length.");
//        }
    }

    /**
     * Returns the values and probabilities that define this RandomVariate.
     * Returns probabilities as second element in array regardless of how it was
     * instantiated or set.
     *
     * @return (values, probabilities)
     */
    @Override
    public Object[] getParameters() {
        return new Object[]{getValues(), getProbabilities()};
    }

//javadoc inherited
    @Override
    public double generate() {
        int index = 0;
        double uniform = this.rng.draw();
        while (index < cdf.length && uniform > cdf[index]) {
            index += 1;
        }
//        for (index = 0; (uniform > cdf[index]) && (index < cdf.length - 1); index++) ;
        return values[index];
    }

    /**
     * Convert the given array of probabilities/frequencies to a cdf.
     *
     * @param freq array of frequencies or probabilities
     * @return cdf values from the given array of probabilities/frequencies
     * @throws IllegalArgumentException If any of the probabilities/frequencies
     * are negative or they sum to zero.
     */
    protected double[] normalize(double[] freq) {
        double sum = 0.0;
        double[] normalized = new double[freq.length];
        for (int i = 0; i < freq.length; i++) {
            if (freq[i] >= 0.0) {
                sum += freq[i];
                normalized[i] = sum;
            } else {
                throw new IllegalArgumentException("Bad frequency value at index "
                        + i + " (value = " + freq[i] + ")");
            }
        }
        if (sum > 0.0) {
            for (int i = 0; i < normalized.length; i++) {
                normalized[i] = normalized[i] / sum;
            }
        } else {
            throw new IllegalArgumentException("Frequency sum not positive: " + sum);
        }
        return normalized;
    }

    /**
     * Returns a String containing a table representation of the pdf and cdf.
     */
    @Override
    public String toString() {
        java.text.DecimalFormat df = new java.text.DecimalFormat("0.000");
        StringBuilder buf = new StringBuilder("Discrete Distribution\n");
        buf.append("x    \tf(x)   \tF(x)\n");
        for (int i = 0; i < cdf.length; i++) {
            buf.append(values[i]);
            buf.append('\t');
            buf.append(df.format(i == 0 ? cdf[i] : cdf[i] - cdf[i - 1]));
            buf.append('\t');
            buf.append(df.format(cdf[i]));
            buf.append('\n');
        }
        return buf.toString();
    }

    /**
     * Warning: This array must be the same length as the probability and cdf
     * arrays, however no checking is done by this method.
     *
     * @param values the array of values at which the cdf changes values.
     */
    public void setValues(double[] values) {
        this.values = values.clone();
//        Arrays.sort(this.values)f;
    }

    protected void setValues(Double[] values) {
        double[] val = new double[values.length];
        for (int i = 0; i < val.length; ++i) {
            val[i] = values[i];
        }
        this.setValues(val);
    }

    /**
     * Sets the cdf of this RandomVariate based on the contents of the given
     * array. Warning: This array must be the same length as the values array,
     * however no checking is done by this method.
     *
     * @param frequencies An array containing either the probabilities or frequecies at
     * the points contained in the values array.
     *
     */
    public void setFrequencies(double[] frequencies) {
        this.frequencies = frequencies.clone();
        cdf = normalize(frequencies);
    }

    protected void setFrequencies(Double[] frequencies) {
        double[] freq = new double[frequencies.length];
        for (int i = 0; i < freq.length; ++i) {
            freq[i] = frequencies[i];
        }
        this.setFrequencies(freq);
    }
    /**
     *
     * @return a copy of the values array.
     */
    public double[] getValues() {
        return values.clone();
    }

    /**
     *
     * @return a copy of the cdf array.
     */
    public double[] getCDF() {
        return cdf.clone();
    }

    /**
     *
     * @return an array containing the probability at each point in the values
     * array.
     */
    public double[] getProbabilities() {
        double[] freq = new double[cdf.length];
        for (int i = 0; i < cdf.length; i++) {
            freq[i] = (i == 0) ? cdf[i] : cdf[i] - cdf[i - 1];
        }
        return freq;
    }
}
