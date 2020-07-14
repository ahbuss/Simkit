package simkit.random;

import java.util.List;
import java.util.Map;

/**
 *
 * <p>
 Generates random variates having an arbitrary discrete distribution with
 integer val. The input is given by a set of val and a set of either
 frequencies or frequencies.

 <p>
 The parameters should have signature (int[], double[]), where the int[] array
 contains the possible val and the double[] array contains the frequencies,
 which may or may not sum to 1, but which must at least be non-negative. These
 are normalized to be a cdf inside the class.
 *
 * 
 * @author ahbuss
 */
public class DiscreteIntegerVariate extends RandomVariateBase
        implements DiscreteRandomVariate {

    private int[] values;

    private double[] frequencies;

    private double[] cdf;

    /**
     * Creates a new instance of DiscreteIntegerVariate
     */
    public DiscreteIntegerVariate() {
    }

    @Override
    public double generate() {
        return (double) generateInt();
    }

    /**
     * Sets the cdf for this DiscreteRandomVariate. First argument is an array
 of doubles that define where the cdf changes. The second argument is an
 array of doubles that specify either frequencies or frequencies at the
 points given.

 If (val, frequencies) are given, then the frequencies are normalized
 to sum to 1.
     *
     * @param params (val, prob) as (double[], double[])
     * @throws IllegalArgumentException If the given array does not have 2
     * elements, if the elements are not arrays of doubles, or if the two double
     * arrays are not the same length.
     * @throws IllegalArgumentException If any of the frequencies/frequencies
     * are negative or they're sums are zero.
     */
    @SuppressWarnings("unchecked")
    @Override
    public void setParameters(Object... params) {
        boolean paramsOK = true;
        String message = "";
        switch (params.length) {
            case 2: // params[0]: int[] or Integer[]; params[1]: double[] or Double[]
                if (params[0] instanceof int[]) {
                    int[] val = (int[]) params[0];
                    this.setValues(val);
                } else if (params[0] instanceof Integer[]) {
                    Integer[] values = (Integer[]) params[0];
                    this.setValues(values);
                } else {
                    paramsOK = false;
                    message = "params[0] must be int[] or Integer[]; ";
                }
                if (params[1] instanceof double[]) {
                    double[] freq = (double[]) params[1];
                    this.setFrequencies(freq);
                } else if (params[1] instanceof Double[]) {
                    this.setFrequencies((Double[]) params[1]);
                } else {
                    paramsOK = false;
                    message += "params[1] must be double[] or Double[];";
                }
                break;
            case 1: // params[0]: Map<Integer, Double> - not implemented
                if (params[0] instanceof Map) {
                    Map<Integer,Double> paramsMap = (Map<Integer,Double>) params[0];
                    this.setValues(paramsMap.keySet().toArray(new Integer[0]));
                    this.setFrequencies(paramsMap.values().toArray(new Double[0]));
                }
                break;
            default: // Error
                paramsOK = false;
                message = "DiscreteIntegerVariate requires 1 or 2 parameters;";
                break;
        }

        if (this.frequencies.length != this.values.length) {
            paramsOK = false;
            message = String.format("values (,%d) & frequencies (,%d) not of same length:", 
                    this.values.length, this.frequencies.length);
        }
        if (!paramsOK) {
            throw new IllegalArgumentException(message);
        }

    }

    @Override
    public Object[] getParameters() {
        return new Object[]{getValues(), getFrequencies()};
    }

    @Override
    public int generateInt() {
        int index = 0;
        double uniform = this.rng.draw();
        while (index < cdf.length && uniform > cdf[index]) {
            index += 1;
        }
//        for (index = 0; (uniform > cdf[index]) && (index < cdf.length - 1); index++) ;
        return this.values[index];
    }

    /**
     * Convert the given array of frequencies/frequencies to a cdf.
     *
     * @throws IllegalArgumentException If any of the frequencies/frequencies
     * are negative or they sum to zero.
     */
    private void toCDF() {
        this.normalize();
        cdf = new double[getFrequencies().length];
        cdf[0] = this.frequencies[0];
        for (int i = 1; i < frequencies.length; i++) {
            cdf[i] += cdf[i - 1] + frequencies[i];
        }
    }

    private void normalize() {
        double sum = 0.0;
        for (int i = 0; i < frequencies.length; ++i) {
            if (frequencies[i] < 0.0) {
                throw new IllegalArgumentException(
                        String.format("Bad frequency value at index %d (value = %.3f)", i, getFrequencies()[i]));
            }
            sum += frequencies[i];
        }
        if (sum > 0.0) {
            for (int i = 0; i < frequencies.length; ++i) {
                frequencies[i] /= sum;
            }
        } else {
            throw new IllegalArgumentException(
                    String.format("Frequency sum not positive: %.3f", sum));
        }
    }

    public int[] getValues() {
        return values.clone();
    }

    public void setValues(int[] values) {
        this.values = values.clone();
    }

    protected void setValues(Integer[] values) {
        this.values = new int[values.length];
        for (int i = 0; i < this.values.length; ++i) {
            this.values[i] = values[i];
        }
    }

    protected void setValues(Object[] values) {
        boolean paramsOK = true;
        StringBuilder builder = new StringBuilder();
        this.values = new int[values.length];
        for (int i = 0; i < this.values.length; ++i) {
            if (values[i] instanceof Number) {
                this.values[i] = ((Number) values[i]).intValue();
            } else {
                paramsOK = false;
                builder.append(String.format("index %,d not a number: %s; ", i, values[i]));
            }
        }
        if (!paramsOK) {
            this.setValues(this.values);
        } else {
            throw new IllegalArgumentException(builder.toString());
        }
    }

    public double[] getCdf() {
        return cdf.clone();
    }

    /**
     * Returns a String containing a table representation of the pdf and cdf.
     *
     */
    @Override
    public String toString() {
        java.text.DecimalFormat df = new java.text.DecimalFormat("0.000");
        StringBuilder buf = new StringBuilder("Discrete Integer");
        buf.append("\nx    \tf(x)   \tF(x)\n");
        for (int i = 0; i < cdf.length; i++) {
            buf.append(getValues()[i]);
            buf.append('\t');
            buf.append(df.format(i == 0 ? cdf[i] : cdf[i] - cdf[i - 1]));
            buf.append('\t');
            buf.append(df.format(cdf[i]));
            buf.append('\n');
        }
        return buf.toString();
    }

    public double[] getFrequencies() {
        return frequencies.clone();
    }

    protected void setFrequencies(Double[] frequencies) {
        this.frequencies = new double[frequencies.length];
        for (int i = 0; i < this.frequencies.length; ++i) {
            this.frequencies[i] = frequencies[i];
        }
        this.normalize();
        this.toCDF();
    }

    protected void setFrequencies(List<Double> frequencies) {
        this.setFrequencies(frequencies.toArray(new Double[0]));
    }

    protected void setFrequencies(Object[] frequencies) {
        boolean paramsOK = true;
        StringBuilder builder = new StringBuilder();
        this.frequencies = new double[values.length];
        for (int i = 0; i < this.values.length; ++i) {
            if (frequencies[i] instanceof Number) {
                this.frequencies[i] = ((Number) values[i]).doubleValue();
            } else {
                paramsOK = false;
                builder.append(String.format("index %,d not a Number: %s; ", i, values[i]));
            }
        }
        if (!paramsOK) {
            this.setFrequencies(this.frequencies);
        } else {
            throw new IllegalArgumentException(builder.toString());
        }
    }

    public void setFrequencies(double[] frequencies) {
        this.frequencies = frequencies.clone();
        this.normalize();
        this.toCDF();
    }

}
