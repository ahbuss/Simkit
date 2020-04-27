package simkit.random;

/**
 * Generates random variates by resampling from a given data array. A generated
 * value is equally likely to be from any value in the data array.
 *
 * @author Arnold Buss
 * 
 */
public class ResampleVariate extends RandomVariateBase {

    /**
     * The array of data from which to sample.
*
     */
    private double[] data;

    /**
     * Generates the next value by sampling with equal likelihood from the data
     * array.
     *
     * @return resampled value, equally likely from data array
     */
    public double generate() {
        return data[(int) (rng.draw() * data.length)];
    }

    /**
     * Returns a single element array that contains a copy of the data array.
     *
     * @return copy of data array as first (and only) element
     */
    public Object[] getParameters() {
        return new Object[]{getData()};
    }

    /**
     * Sets the contents of the array from which to sample.
     *
     * @param params array with one element containing the double[] data values
     * to be resampled.
     * @throws IllegalArgumentException If the array does not have exactly 1
     * element or if the element is not an array of <code>doubles</code>.
     */
    public void setParameters(Object... params) {
        if (params.length != 1) {
            throw new IllegalArgumentException("Need 1 element: " + params.length);
        }
        double[] values;
        switch (params[0].getClass().getSimpleName()) {
            case "double[]":
                setData((double[]) params[0]);
                break;
            case "int[]":
                int[] intValues = (int[]) params[0];
                values = new double[intValues.length];
                for (int i = 0; i < values.length; ++i) {
                    values[i] = intValues[i];
                }
                setData(values);
                break;
            case "float[]":
                float[] floatValues = (float[]) params[0];
                values = new double[floatValues.length];
                for (int i = 0; i < values.length; ++i) {
                    values[i] = floatValues[i];
                }
                setData(values);
                break;
            case "long[]":
                long[] longValues = (long[]) params[0];
                values = new double[longValues.length];
                for (int i = 0; i < values.length; ++i) {
                    values[i] = longValues[i];
                }
                setData(values);
                break;
            case "Number[]":
            case "Double[]":
            case "Float[]":
            case "Integer[]":
            case "Long[]":
                Number[] numValues = (Number[]) params[0];
                values = new double[numValues.length];
                for (int i = 0; i < values.length; ++i) {
                    values[i] = numValues[i].doubleValue();
                }
                setData(values);
                break;
            default:
                throw new IllegalArgumentException(
                        "params[0] not a numebrical array: " +
                                params[0].getClass().getName()
                );
        }

    }

    public void setData(double[] data) {
        this.data = data.clone();
    }

    public double[] getData() {
        return data.clone();
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder("Resample Variate [");
        int count = 0;
        for (int i = 0; i < data.length; ++i) {
            buf.append(data[i]);
            if (++count % 10 == 0) {
                buf.append('\n');
            } else {
                buf.append(", ");
            }
        }
        buf.deleteCharAt(buf.length() - 1);
        buf.deleteCharAt(buf.length() - 1);
        buf.append(']');
        return buf.toString();
    }
}
