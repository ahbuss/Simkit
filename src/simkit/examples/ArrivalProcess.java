package simkit.examples;

import simkit.SimEntityBase;
import simkit.random.RandomVariate;

/**
 * <P>
 * Simplest non-trivial Event Graph. A basic arrival process that can have any
 * interarrival probability distribution.
 *
 * @author Arnold Buss
 *
 */
public class ArrivalProcess extends SimEntityBase {

    /**
     * The interarrival distribution (parameter)
     *
     */
    private RandomVariate interarrivalTimeGenerator;
    /**
     * The number of arrivals (state variable)
     *
     */
    protected int numberArrivals;

    public ArrivalProcess() {
    }

    /**
     * Construct an <CODE>ArrivalProcess</CODE> instance with given interarrival
     * distribution. The distribution must generate values that are &gt;= 0.
     * This is the preferred way to construct and ArrivalProcess instance.
     *
     * @param interarrivalTimeGenerator The interarrival distribution
     * RandomVariate
     *
     */
    public ArrivalProcess(RandomVariate interarrivalTimeGenerator) {
        this.setInterarrivalTimeGenerator(interarrivalTimeGenerator);
    }

    /**
     * Resets the number of arrivals to 0
     *
     */
    @Override
    public void reset() {
        super.reset();
        numberArrivals = 0;
    }

    /**
     * Schedules the first arrival. (Event Method)
     *
     */
    public void doRun() {
        firePropertyChange("numberArrivals", numberArrivals);
        waitDelay("Arrival", interarrivalTimeGenerator.generate());
    }

    /**
     * Arrival event. Increments number of arrivals and schedules next arrival.
     * (Event Method)
     *
     */
    public void doArrival() {
        firePropertyChange("numberArrivals", numberArrivals, ++numberArrivals);
        waitDelay("Arrival", interarrivalTimeGenerator.generate());
    }

    /**
     * Cancels the pending Arrival event, stopping the arrival process.
     *
     */
    public void doStopArrivals() {
        interrupt("Arrival");
    }

    public RandomVariate getInterarrivalTimeGenerator() {
        return interarrivalTimeGenerator;
    }

    public void setInterarrivalTimeGenerator(RandomVariate interarrivalTimeGenerator) {
        this.interarrivalTimeGenerator = interarrivalTimeGenerator;
    }

    /**
     * Returns the current number of arrivals.
     *
     * @return number of arrivals (state variable)
     *
     */
    public int getNumberArrivals() {
        return numberArrivals;
    }

    /**
     * @return a String containing information about this ArrivalProcess.
     */
    public String paramString() {
        return toString();
    }

    /**
     * Returns a String containing usage information for the main method.
     *
     * @return Usage String (uses Exponential interarrival times)
     *
     */
    public static String usage() {
        return "Usage: java simkit.examples.ArrivalProcess <mean Interarrival Time> "
                + " <stop time> [single-step (true|false)]";
    }

    /**
     *
     * @return A short description of this class
     *
     */
    public static String description() {
        return "Models an arrival process.  The interarrival time distribution is "
                + "passed to the constructor via a RandomVariate object.";
    }

}
