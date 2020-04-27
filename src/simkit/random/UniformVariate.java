package simkit.random;

/**
 * Generates continuous uniform random variates.
 * 
* 
*
 */
public class UniformVariate extends RandomVariateBase {

    /**
     * The lowest possible value.
     */
    private double minimum;

    /**
     * The highest possible value.
     */
    private double maximum;

    /**
     * The difference between the maximum and minimum values. (Precalculated)
     */
    private double range;

    /**
     * Creates a new UniformVariate, parameters must be set prior to use.
*
     */
    public UniformVariate() {
        this.minimum = 0.0;
        this.maximum = 1.0;
    }

    /**
     * Sets the minimum and maximum values.
     *
     * @param params A 2 element array containing the minimum and the maximum as
     * Numbers.
     * @throws IllegalArgumentException If the array doesn't contain exactly 2
     * elements, if the elements are not Numbers, or if the minimum is larger
     * than the maximum.
*
     */
    @Override
    public void setParameters(Object... params) {
        double newLow = 0.0;
        double newHigh = 1.0;
        if (params.length != 2) {
            throw new IllegalArgumentException("Requires 2 elements, supplied " + params.length);
        }
        if (params[0] instanceof Number && params[1] instanceof Number) {
            this.setMinimum(((Number) params[0]).doubleValue());
            this.setMaximum(((Number) params[1]).doubleValue());
        } else {
            throw new IllegalArgumentException("The array elements must be Numbers ("
                    + params[0].getClass().getName() + ", "
                    + params[1].getClass().getName() + ") supplied. ");
        }

        if (getMinimum() > getMaximum()) {
            throw new IllegalArgumentException("Uniform parameters must have low < high: ("
                    + newLow + ", " + newHigh + ")");
        }
        range = maximum - minimum;
    }

    /**
     *
     * @return a 2 element array containing the minimum and maximum limits.
     */
    @Override
    public Object[] getParameters() {
        return new Object[]{getMinimum(), getMaximum()};
    }

    @Override
    public double generate() {
        return minimum + range * rng.draw();
    }

    /**
     * @param minimum the minimum value. Causes internal precalculated values to be
     * updated.
     */
    public void setMinimum(double minimum) {
        this.minimum = minimum;
        range = maximum - minimum;
    }

    /**
     * @return the minimum value
     */
    public double getMinimum() {
        return minimum;
    }

    /**
     * @param maximum the maximum value. Causes internal precalculated values to be
     * updated.
     */
    public void setMaximum(double maximum) {
        this.maximum = maximum;
        range = maximum - minimum;
    }

    public double getMaximum() {
        return maximum;
    }

    /**
     * @return  a String containing the name of this distribution with the
     * minimum and maximum parameters.
     */
    @Override
    public String toString() {
        return String.format("Uniform (%.3f, %.3f)", getMinimum(), getMaximum());
    }
}
