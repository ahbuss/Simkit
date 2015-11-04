package simkit;

/**
 * Allows verbose mode of the event lost to be active between the values
 * startVerboseTime (default = 0.0) and endVerboseTime (default = &infin;).
 * 
 * @version $Id$
 * @author ahbuss
 */
public class VerboseAfter extends SimEntityBase {

    private double startVerboseTime;

    private double endVerboseTime;

    /**
     * Construct a VerboseAfter with given start and end times
     * @param startVerboseTime Time to turn verbose on
     * @param endVerboseTime Time to turn verbose off
     */
    public VerboseAfter(double startVerboseTime, double endVerboseTime) {
        this.setStartVerboseTime(startVerboseTime);
        this.setEndVerboseTime(endVerboseTime);
    }

    /**
     * Construct a VerboseAfter with given start time and default end time (&infin;)
     * @param startVerboseTime Time to turn verbose on
     */
    public VerboseAfter(double startVerboseTime) {
        this(startVerboseTime, Double.POSITIVE_INFINITY);
    }

    /**
     * Construct a VerboseAfter with default start time (0.0) and end time
     * (&infin;)
     */
    public VerboseAfter() {
        this(0.0, Double.POSITIVE_INFINITY);
    }

    /**
     * Schedule StartVerbose with delay of startVerboseTime.
     */
    public void doRun() {
        waitDelay("StartVerbose", getStartVerboseTime(), Priority.HIGHEST);
    }

    /**
     * Turn on verbose mode for event list; schedule EndVerbose with delay of
     * endVerbose - startVerbose
     */
    public void doStartVerbose() {
        this.getEventList().setVerbose(true);
        waitDelay("EndVerbose", getEndVerboseTime() - getStartVerboseTime(),
                Priority.LOWEST);
    }

    /**
     * Schedule EndEverbose with 0.0 delay
     */
    public void doEndVerbose() {
        waitDelay("TurnVerboseOff", 0.0, Priority.LOWEST);
    }

    /**
     * Set event list verbose to false
     */
    public void doTurnVerboseOff() {
        this.getEventList().setVerbose(false);
    }

    /**
     * 
     * @return startVerboseTime
     */
    public double getStartVerboseTime() {
        return startVerboseTime;
    }

    /**
     * 
     * @param startVerboseTime given startVerboseTime
     * @throws IllegalArgumentException if startVerboseTime &lt; 0.0
     */
    public void setStartVerboseTime(double startVerboseTime) {
        if (startVerboseTime < 0.0) {
            throw new IllegalArgumentException("startVerboseTime must be \u2265 0.0: "
                    + startVerboseTime);
        }
        this.startVerboseTime = startVerboseTime;
    }

    /**
     * 
     * @return endVerboseTime
     */
    public double getEndVerboseTime() {
        return endVerboseTime;
    }

    /**
     * 
     * @param endVerboseTime Given endVerboseTime
     * @throws IllegalArgumentException if given endVerboseTime &gt; startVerboseTime
     */
    public void setEndVerboseTime(double endVerboseTime) {
        if (endVerboseTime < this.getStartVerboseTime()) {
            throw new IllegalArgumentException(
                    String.format("endVerboseTime must be \u2265 startVerboseTime: "
                            + "startVerboseTime=%f endVerboseTime=%f",
                            this.getStartVerboseTime(), endVerboseTime));
        }
        this.endVerboseTime = endVerboseTime;
    }

}
