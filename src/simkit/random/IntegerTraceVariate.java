package simkit.random;

/**
 *
 * @author ahbuss
 */
public class IntegerTraceVariate extends RandomVariateBase implements DiscreteRandomVariate {

    private static final int DEFAULT_DEFAULT_VALUE = Integer.MIN_VALUE;

    public static final int SHORT_NUMBER = 10;

    private int[] traceValues;

    private int defaultValue;

    private boolean allDataInToString;

    public IntegerTraceVariate() {
        this.rng = new Sequential();
        this.setDefaultValue(DEFAULT_DEFAULT_VALUE);
        this.setAllDataInToString(false);
    }

    /**
     *
     * @return What generateInt() returns as a double
     */
    @Override
    public double generate() {
        return generateInt();
    }

    @Override
    public void setParameters(Object... params) {
        boolean badDefault = false;
        boolean badTraceValues = false;
        switch (params.length) {
            case 2:
                if (params[1] instanceof Integer) {
                    setDefaultValue(((Integer) params[1]).intValue());
                } else {
                    badDefault = true;
                }
            case 1:
                if (params[0] instanceof int[]) {
                    setTraceValues((int[]) params[0]);
                } else if (params[0] instanceof Integer[]) {
                    setTraceValues(convert((Integer[])params[0]));
                } else {
                    badTraceValues = true;
                }
                break;
            default:
                throw new IllegalArgumentException(
                        "IntegerTraceVariate requires 1 or 2 arguments: " + params.length);
        }
        if (badDefault || badTraceValues) {
            StringBuilder builder = new StringBuilder();
            if (badTraceValues) {
                builder.append("Trace values must be int[]: ").append(params[0].getClass().getSimpleName());
            }
            if (badDefault) {
                builder.append(System.lineSeparator()).append("Default value must be int: ").append(params[1].getClass().getSimpleName());
            }
            throw new IllegalArgumentException(builder.toString());
        }
    }

    @Override
    public Object[] getParameters() {
        if (defaultValue != DEFAULT_DEFAULT_VALUE) {
            return new Object[]{getTraceValues(), getDefaultValue()};
        } else {
            return new Object[]{getTraceValues()};
        }
    }

    @Override
    public int generateInt() {
        long index = rng.getSeed();
        if (index < traceValues.length) {
            rng.draw();
            return traceValues[(int) index];
        } else {
            return defaultValue;
        }
    }

    /**
     * This is a no-op, since an instance of Sequential is used.
     *
     * @param rng Given RandomNumber instance (ignored)
     */
    @Override
    public void setRandomNumber(RandomNumber rng) {
    }

    /**
     * @return the traceValues
     */
    public int[] getTraceValues() {
        return traceValues.clone();
    }

    /**
     * @param traceValues the traceValues to set
     */
    public void setTraceValues(int[] traceValues) {
        this.traceValues = traceValues.clone();
    }

    /**
     * @return the defaultValue
     */
    public int getDefaultValue() {
        return defaultValue;
    }

    /**
     * @param defaultValue the defaultValue to set
     */
    public void setDefaultValue(int defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * @param allDataInToString the allDataInToString to set
     */
    public void setAllDataInToString(boolean allDataInToString) {
        this.allDataInToString = allDataInToString;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("IntegerTrace: [");
        if (allDataInToString || traceValues.length <= 2 * SHORT_NUMBER) {
            for (int i = 0; i < traceValues.length; ++i) {
                builder.append(traceValues[i]);
                if (i < traceValues.length - 1) {
                    builder.append(',');
                }
            }
        } else {
            for (int i = 0; i < SHORT_NUMBER; ++i) {
                builder.append(traceValues[i]).append(',');
            }
            builder.append("....,");
            for (int i = traceValues.length - SHORT_NUMBER; i < traceValues.length; ++i) {
                builder.append(traceValues[i]);
                if (i < traceValues.length - 1) {
                    builder.append(',');
                }
            }
        }

        builder.append(']').append(' ').append('(');
        builder.append(traceValues.length).append(" values)");
        return builder.toString();
    }

    public static int[] convert(Integer[] values) {
        int[] convert = new int[values.length];
        for (int i = 0; i < values.length; ++i) {
            convert[i] = values[i];
        }
        return convert;
    }

}
