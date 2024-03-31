package simkit.random;

import java.text.DecimalFormat;

/**
 * "Generates" numbers specified by the parameters. The parameters are an array
 * of <code>doubles</code> and a default value. Unless the supporting
 * RandomNumber is replaced, <code>generate</code> will return the contents of
 * the array in order. After all values of the array have been used, the default
 * value is returned until reset.
 *
 * @author ahbuss
 *
 */
public class TraceVariate extends RandomVariateBase {

    /**
     * The default value for the default is Double.NaN
     *
     */
    private static final double DEFAULT_DEFAULT_VALUE = Double.NaN;

    private static final DecimalFormat DF = new DecimalFormat("0.000;-0.000");

    /**
     * An array of the values to be generated.
     *
     */
    private double[] traceValues;

    /**
     * The value to be generated after all of the trace values are used.
     *
     */
    private double defaultValue;

    /**
     * True if all of the trace values will be included in the result of
     * toString. If false, only a limited number of values will be output.
     *
     */
    private boolean allDataInToString;

    /**
     * The number of values to limit the output of toString to if
     * allDataInToString is false.
     *
     */
    public static final int SHORT_NUMBER = 5;

    /**
     * Constructs a new TraceVariate. The default RandomNumber will cause the
     * values to be output in order. The default value is NaN.
     *
     */
    public TraceVariate() {
        rng = new Sequential();
        this.setDefaultValue(DEFAULT_DEFAULT_VALUE);
        this.setAllDataInToString(false);
    }

    /**
     *
     * @param traceValues the traceValues to be returned by generate().
     */
    public void setTraceValues(double[] traceValues) {
        this.traceValues = traceValues.clone();
    }

    /**
     *
     * @return a copy of the value to be returned by generate.
     */
    public double[] getTraceValues() {
        return this.traceValues.clone();
    }

    /**
     *
     * @param defaultValue the defaultValue that will be generated after all of
     * the trace values have been used.
     */
    public void setDefaultValue(double defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     *
     * @return the value that will be generated after all of the trace values
     * have been used.
     */
    public double getDefaultValue() {
        return defaultValue;
    }

    /**
     *
     * @param allDataInToString True if all of the trace values will be included
     * in the result of toString. If false, only a limited number of values will
     * be output.
     */
    public void setAllDataInToString(boolean allDataInToString) {
        this.allDataInToString = allDataInToString;
    }

    /**
     * Sets the contents of the trace array and (optionally) the default value.
     *
     * @param params A 1 or 2 element array. The first element is an array of
     * doubles containing the values to be returned by <code>generate</code>.
     * The second (optional) element contains the default value to be returned
     * after all of the trace values have been used.
     * @throws IllegalArgumentException If the array is not 1 or 2 elements, if
     * the first element is not an array of <code>doubles</code>, or if the 2nd
     * element (if present) is not a Number.
     *
     */
    @Override
    public void setParameters(Object... params) {
        boolean badDefault = false;
        boolean badTraceValues = false;
        switch (params.length) {
            case 2:
                if (params[1] instanceof Number) {
                    setDefaultValue(((Number) params[1]).doubleValue());
                } else {
                    badDefault = true;
                }
                break;
            case 1:
                if (params[0] instanceof double[]) {
                    setTraceValues(((double[]) params[0]));
                    setDefaultValue(DEFAULT_DEFAULT_VALUE);
                } else if (params[0] instanceof Double[]) {
                    
                }
                
                else {
                    badTraceValues = true;
                }
                break;
            default:
                throw new IllegalArgumentException(
                        "TraceVariate requires 1 or 2 arguments: " + params.length);
        }
        if (badDefault || badTraceValues) {
            StringBuilder builder = new StringBuilder();
            if (badTraceValues) {
                builder.append("Trace values must be double[]: ").append(params[0].getClass().getSimpleName());
            }
            if (badDefault) {
                builder.append(System.lineSeparator()).append("Default value must be a Number: ").append(params[1].getClass().getSimpleName());
            }
            throw new IllegalArgumentException(builder.toString());
        }
    }

    /**
     * Returns a 1 or 2 element array containing the trace value array and the
     * default value (if not NaN).
     *
     * @return a 1 or 2 element array containing the trace value array and the
     * default value (if not NaN).
     */
    @Override
    public Object[] getParameters() {
        if (Double.isNaN(defaultValue)) {
            return new Object[]{getTraceValues()};
        } else {
            return new Object[]{getTraceValues(), getDefaultValue()};
        }
    }

    @Override
    public double generate() {
        long theIndex = rng.getSeed();
        double value = getDefaultValue();
        if (theIndex < traceValues.length) {
            value = traceValues[(int) theIndex];
            rng.draw();
        }
        return value;
    }

    /**
     * Returns a String containing the trace values. If the allDataInToString
     * flag is false only the first 5 and last 5 values will be included.
     *
     * @return a String containing the trace values.
     */
    @Override
    public String toString() {

        StringBuilder buf = new StringBuilder("Trace: [");
        if (allDataInToString || traceValues.length <= 2 * SHORT_NUMBER) {
            for (int i = 0; i < traceValues.length; i++) {
                buf.append(DF.format(traceValues[i]));
                if (i < traceValues.length - 1) {
                    buf.append(',');
                }
            }
        } else {
            for (int i = 0; i < SHORT_NUMBER; i++) {
                buf.append(DF.format(traceValues[i]));
                buf.append(',');
            }
            buf.append("...,");
            for (int i = traceValues.length - SHORT_NUMBER; i < traceValues.length; i++) {
                buf.append(DF.format(traceValues[i]));
                if (i < traceValues.length - 1) {
                    buf.append(',');
                }
            }
        }
        buf.append("] (");
        buf.append(traceValues.length);
        buf.append(" values)");
        return buf.toString();
    }

    /**
     * This is a no-op because the TraceVariate must use a Sequential instance
     * to successively generate values
     *
     * @param rng The RandomNumber instance
     */
    @Override
    public void setRandomNumber(RandomNumber rng) {
    }
}
