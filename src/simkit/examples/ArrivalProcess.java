package simkit.examples;

import simkit.SimEntityBase;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;

/**
 * <P>
 * Simplest non-trivial Event Graph. A basic arrival process that can have any
 * interarrival probability distribution.
 *
 * @author Arnold Buss
 * @version $Id$
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
     * Returns a String containing information about this ArrivalProcess.
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
     * Returns a String containing a description of this class.
     *
     * @return A short description of this class
     *
     */
    public static String description() {
        return "Models an arrival process.  The interarrival time distribution is "
                + "passed to the constructor via a RandomVariate object.";
    }

    /**
     * <P>
     * Test ArrivalProcess for simple, verbose scenario. Interarrival times are
     * Exponential. The user specifies the mean interarrival time and the
     * stopping time on the command line. The user can optionally specify
     * whether it is to be run in single-step mode or just verbose mode.
     *
     * <P>
     * Example:
     * <PRE>
     * java simkit.examples.ArrivalProcess 40.0 100.0
     *
     * Arrival Process with interarrival times Exponential (40.0)
     * Simulation will end at time 100.0
     *
     ** Event List -- Starting Simulation **
     * 0.000   Run
     * 100.000 Stop
     ** End  of Event List -- Starting Simulation **
     *
     * Time: 0.000     Current Event: Run      [1]
     ** Event List --  **
     * 3.139   Arrival
     * 100.000 Stop
     ** End  of Event List --  **
     *
     * Time: 3.139     Current Event: Arrival  [1]
     ** Event List --  **
     * 72.111  Arrival
     * 100.000 Stop
     ** End  of Event List --  **
     *
     * Time: 72.111    Current Event: Arrival  [2]
     ** Event List --  **
     * 100.000 Stop
     * 128.740 Arrival
     ** End  of Event List --  **
     *
     * Time: 100.000   Current Event: Stop     [1]
     ** Event List --  **
     * &lt;&lt; empty &gt;&gt;
     ** End  of Event List --  **
     *
     * At time 100.0 there have been 2 arrivals
     * </PRE>
     *
     */
    public static void main(String[] args) {
        if (args.length < 2 || args.length > 3) {
            args = new String[]{"40.0", "100.0"};
            System.out.println(usage());
        }
        double meanIAT = Double.parseDouble(args[0]);
        double stopTime = Double.parseDouble(args[1]);
        boolean singleStep = (args.length == 2) ? false : Boolean.valueOf(args[2]).booleanValue();

        RandomVariate interarrivalTime
                = RandomVariateFactory.getInstance("Exponential", meanIAT);
        ArrivalProcess arrivals
                = new ArrivalProcess(interarrivalTime);

        arrivals.setEventListID(11);

        System.out.println();
        System.out.println(arrivals);
        System.out.println("Simulation will end at time " + stopTime);
        System.out.println();

        simkit.BasicEventList eventList = simkit.Schedule.getEventList(11);

        eventList.setVerbose(true);
        eventList.setSingleStep(true);
//        simkit.Schedule.setSingleStep(singleStep);

        eventList.stopOnEvent(5, "Arrival");

        eventList.reset();
        eventList.startSimulation();

        System.out.println("At time " + eventList.getSimTime() + " there have been "
                + arrivals.getNumberArrivals() + " arrivals");

        int newID = simkit.Schedule.addNewEventList();
        System.out.println(newID);
        ArrivalProcess ap = new ArrivalProcess(arrivals.getInterarrivalTimeGenerator());
        ap.setEventListID(newID);
        ap.getInterarrivalTimeGenerator().getRandomNumber().resetSeed();

        simkit.BasicEventList eventList2 = simkit.Schedule.getEventList(newID);
        eventList2.stopAtTime(stopTime * 2);
        eventList2.setVerbose(true);
        eventList2.reset();
        eventList2.startSimulation();

        newID = 33;
        eventList2 = simkit.Schedule.getEventList(newID);
        ap.setEventListID(newID);
        ap.getInterarrivalTimeGenerator().getRandomNumber().resetSeed();

        eventList2.stopOnEvent(5, "Arrival");
        eventList2.setVerbose(true);
        eventList2.reset();
        eventList2.startSimulation();
    }

}
