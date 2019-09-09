package simkit.stat;

import simkit.Priority;
import simkit.SimEntityBase;

/**
 * This computes tally statistics but resets after a given time rather than a
 * given number of observations.
 *
 * @author ahbuss
 */
public class TimeTruncatedTallyStat extends SimpleStatsTally {

    private double truncationTime;

    protected boolean truncated;

    private Truncate truncator;

    protected SimpleStatsTally statsAtTruncation;

    /**
     * Instantiate a TimeTruncatedTallyStat with given stateName and truncation
     * time
     *
     * @param stateName Name of state (property) to listen for
     * @param truncationTime Time to truncate (reset statistics)
     */
    public TimeTruncatedTallyStat(String stateName, double truncationTime) {
        super(stateName);
        this.setTruncationTime(truncationTime);
        this.truncator = new Truncate();
    }

    /**
     * Instantiate a TimeTruncatedTallyStat with given truncation time and
     * default state name
     *
     * @param truncationTime Time to truncate (reset statistics)
     */
    public TimeTruncatedTallyStat(double truncationTime) {
        this(DEFAULT_NAME, truncationTime);
    }

    @Override
    public void newObservation(double x) {
        super.newObservation(x);
        if (!truncated) {
            statsAtTruncation.newObservation(x);
        }
    }

    @Override
    public void reset() {
        super.reset();
        if (statsAtTruncation == null) {
            this.statsAtTruncation = new SimpleStatsTally(this);
        }
        statsAtTruncation.reset();
    }

    public void transientReset() {
        super.reset();
    }
    
    /**
     * @return the truncationTime
     */
    public double getTruncationTime() {
        return truncationTime;
    }

    /**
     * @param truncationTime the truncationTime to set
     * @throws IllegalArgumentException if truncationTime &le; 0.0
     */
    public final void setTruncationTime(double truncationTime) {
        if (truncationTime < 0.0) {
            throw new IllegalArgumentException(
                    "truncationTime must be \u2265 0.0: " + truncationTime);
        }
        this.truncationTime = truncationTime;
    }

    /**
     * @return true if truncation point has been passed; false otherwise
     */
    public boolean isTruncated() {
        return truncated;
    }

    @Override
    public String toString() {
        return super.toString() + " " + isTruncated();
    }

    /**
     * This class schedules a Truncate event at time given by truncationTime
     */
    public class Truncate extends SimEntityBase {

        /**
         * set truncated to false
         */
        @Override
        public void reset() {
            super.reset();
            truncated = false;
        }

        /**
         * Schedule Truncate event with delay of truncationTime
         */
        public void doRun() {
            waitDelay("Truncate", getTruncationTime(), Priority.LOWER);
        }

        /**
         * Reset statistics; set truncated to true
         */
        public void doTruncate() {
            TimeTruncatedTallyStat.this.transientReset();
            truncated = true;
        }
    }

    /**
     * @return the statsAtTruncation
     */
    public SimpleStatsTally getStatsAtTruncation() {
        return statsAtTruncation;
    }

}
