package simkit.stat;

import simkit.BasicEventList;
import simkit.SimEntityBase;

/**
 * This is a fix of the class simkit.stat.TruncatingSimpleStatsTimeVarying,
 * which was not resetting properly when the truncation point was reached.
 * <p>
 * This version adds an inner class, Truncate, which schedules a Truncate event,
 * ensuring that the truncation occurs at exactly the correct time.
 *
 * @version $Id: TruncatingSimpleStatsTimeVarying.java 1396 2016-11-23 23:43:38Z
 * ahbuss $
 * @author ahbuss
 */
public class TruncatingSimpleStatsTimeVarying extends SimpleStatsTimeVarying {

    protected boolean truncated;

    private double truncationPoint;

    private Truncate truncate;

    protected double lastObservation;

    /**
     * Instantiate a TruncatingSimpleStatsTimeVarying with the given name and
     * given truncationPoint
     *
     * @param name Name of property to listen for
     * @param truncationPoint Time when statistics will be reset
     */
    public TruncatingSimpleStatsTimeVarying(String name, double truncationPoint) {
        super(name);
        this.setTruncationPoint(truncationPoint);
        this.truncated = false;
        this.truncate = new Truncate();

    }

    /**
     * Instantiate a TruncatingSimpleStatsTimeVarying with the given
     * truncationPoint and the default name. This should only be used if
     * newObservation is to be called directly.
     *
     * @param truncationPoint given truncation point
     */
    public TruncatingSimpleStatsTimeVarying(double truncationPoint) {
        this(DEFAULT_NAME, truncationPoint);
    }

    /**
     * sets truncated to false
     */
    @Override
    public void reset() {
        super.reset();
        truncated = false;
    }

    /**
     * Saves last value; if truncation point has been reached, clear the
     * statistics, set truncated to "true", set the startTime to the
     * truncationPoint, and call super.newObservation(x).
     *
     * @param x Given observation
     */
    @Override
    public void newObservation(double x) {
        this.lastObservation = x;
        if (!isTruncated() && eventList.getSimTime() >= getTruncationPoint()) {
            reset();
            truncated = true;
            startTime = getTruncationPoint();
        }
        super.newObservation(x);
    }

    /**
     *
     * @return true if truncation point has been reached
     */
    public boolean isTruncated() {
        return truncated;
    }

    /**
     *
     * @return truncation point (time at which statistics are reset)
     */
    public double getTruncationPoint() {
        return truncationPoint;
    }

    /**
     *
     * @param truncationPoint Given truncation point
     * @throws IllegalArgumentException if given truncationPoint &lt; 0.0
     */
    public void setTruncationPoint(double truncationPoint) {
        if (truncationPoint < 0.0) {
            throw new IllegalArgumentException(
                    "truncationPoint must be \u2265 0: " + truncationPoint);
        }
        this.truncationPoint = truncationPoint;
    }
    
    public void setEventList(BasicEventList eventList) {
        super.setEventList(eventList);
        truncate.setEventList(eventList);
        setEventListID(eventList.getID());
    }
    
    /**
     * This class is responsible for scheduling truncation at exactly the
     * correct time.
     */
    public class Truncate extends SimEntityBase {

        /**
         * Schedules the Truncate event that resets the stats.
         */
        public void doRun() {
            waitDelay("Truncate", getTruncationPoint());
        }

        /**
         * calls newObservation(lastObservation).
         */
        public void doTruncate() {
            newObservation(lastObservation);
        }
    }

}
