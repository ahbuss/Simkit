package simkit.stat;

/**
 * Basic class for collecting tally statistics as produced by a typical
 * simulation model, that is, one observation at a time. The algorithm is not
 * the naive one, keeping the sum, sum of squares, etc., but rather keeps the
 * mean and variance themselves. They are updated by a more stable algorithm
 * that is less prone to overflow, particularly for the sum of squares. All
 * counters have approximately the same order of magnitude as the quantities
 * they are estimating.
 *
 * <P>
 * A frequent use is as a PropertyChangeListener. In this capacity, an instance
 * listens for a single property name and adds a new observation if the newValue
 * is of type <CODE>Number</CODE>
 *
 * @author ahbuss
 * 
 *
 */
public class SimpleStatsTally extends AbstractSimpleStats {

    /**
     * The mean of the statistic.
*
     */
    private double mean;

    /**
     * The variance of the statistic.
*
     */
    private double variance;

    /**
     * The difference between the current observation and the mean.
*
     */
    private double diff;

    /**
     * Construct a SimpleStatsTally with the default name. Note: The name can be
     * set after instantiation using setName().
 *
     */
    public SimpleStatsTally() {
        this(DEFAULT_NAME);
    }

    /**
     * Construct a SimpleStatsTally with the given name.
     *
     * @param name The property name that will be listened to.
 *
     */
    public SimpleStatsTally(String name) {
        super(name);
        reset();
    }

    public SimpleStatsTally(SimpleStatsTally original) {
        this.mean = original.getMean();
        this.variance = original.getVariance();
        this.count = original.getCount();
        this.diff = original.diff;
        this.minObs = original.getMinObs();
        this.maxObs = original.getMaxObs();
        this.name = original.getName();
    }

    /**
     * Update counters with a new observation.
     *
     * @param x The new (primitive) observation.
 *
     */
    @Override
    public void newObservation(double x) {
        if (!Double.isNaN(x)) {
            super.newObservation(x);

            diff = x - mean;
            variance += (count == 1) ? 0.0 : diff * diff / count - 1.0 / (count - 1) * variance;
            mean += diff / count;
        }
    }

    /**
     * Gets the current mean of the statistic
     *
     * @return Current mean
     */
    @Override
    public double getMean() {
        return mean;
    }

    /**
     * Gets the current variance of the statistic
     *
     * @return Current variance of observations
     */
    @Override
    public double getVariance() {
        return variance;
    }

    /**
     * Gets the current standard deviation of the statistic
     *
     * @return Current Standard Deviation of observations
     */
    @Override
    public double getStandardDeviation() {
        return Math.sqrt(getVariance());
    }

    /**
     * Returns the SamplingType which is always <CODE>TALLY</CODE>.
     *
     * @return TALLY
*
     */
    @Override
    public SamplingType getSamplingType() {
        return SamplingType.TALLY;
    }

// Javadoc inherited.
    @Override
    public void reset() {
        super.reset();
        diff = 0.0;
        mean = 0.0;
        variance = 0.0;
    }

}
