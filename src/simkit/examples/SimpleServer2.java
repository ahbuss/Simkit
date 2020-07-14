package simkit.examples;

import simkit.BasicSimEntity;
import simkit.Priority;
import simkit.SimEvent;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;

/**
 * Multiple server queue using BasicSimEntity as superclass. This implementation
 * does not generate its own arrivals. It listens to another SimEntity's arrival
 * and endService events which cause an arrival in this SimpleServer2.
 * <p>
 * This approach does not use reflection, but implicitly relies on the moral
 * equivalent of a "switch" statement to decide which event to execute when it
 * is called back from Schedule (the handleSimEvent() method).</p>
 *
 * @author Arnold Buss
 *
 */
public class SimpleServer2 extends BasicSimEntity {

    /**
     * The RandomVariate used to generate service times.
     *
     */
    private RandomVariate serviceTimeGenerator;

    /**
     * The total number of servers in the system.
     *
     */
    private int totalNumberServers;

    /**
     * The number of servers that are not busy.
     *
     */
    protected int numberAvailableServers;

    /**
     * The current length of the queue.
     *
     */
    protected int numberInQueue;

    /**
     * The total number served by the system.
     *
     */
    protected int numberServed;

    public SimpleServer2() {
    }

    /**
     * Creates a new SimpleServer2 with the given number of servers and the
     * given service time distribution.
     *
     * @param totalNumberServers total # servers
     * @param serviceTimeGenerator generates service times
     */
    public SimpleServer2(int totalNumberServers, RandomVariate serviceTimeGenerator) {
        this();
        this.setTotalNumberServers(totalNumberServers);
        this.setServiceTimeGenerator(serviceTimeGenerator);
    }

    /**
     * Resets state to empty and idle with no customers processed.
     */
    @Override
    public void reset() {
        super.reset();
        numberAvailableServers = getTotalNumberServers();
        numberInQueue = 0;
        numberServed = 0;
    }

    /**
     * Simply fires the PropertyChangeEvents for numberInQueue and
     * numberAvailableServers.
     */
    public void doRun() {
        firePropertyChange("numberInQueue", getNumberInQueue());
        firePropertyChange("numberAvailableServers", getNumberAvailableServers());
    }

    /**
     * Arrival of a customer to queue. After incrementing the number in queue,
     * if a server is available, a StartService event is scheduled. Fires a
     * property change for numberInQueue.
     */
    public void doArrival() {
        int oldNumberInQueue = getNumberInQueue();
        numberInQueue += 1;
        firePropertyChange("numberInQueue", oldNumberInQueue, getNumberInQueue());

        if (numberAvailableServers > 0) {
            waitDelay("StartService", 0.0, Priority.HIGH);
        }
    }

    /**
     * Schedules EndService event after delay of a service time. Decrements
     * number in queue and number of available servers. Fires property changes
     * for numberInQueue and numberAvailable servers.
     */
    public void doStartService() {
        int oldNumberInQueue = getNumberInQueue();
        numberInQueue -= 1;
        firePropertyChange("numberInQueue", oldNumberInQueue, getNumberInQueue());

        int oldNumberAvailableServers = getNumberAvailableServers();
        numberAvailableServers -= 1;
        firePropertyChange("numberAvailableServers", oldNumberAvailableServers, getNumberAvailableServers());

        waitDelay("EndService", getServiceTimeGenerator());
    }

    /**
     * Increments number of available servers. If a customer is waiting in the
     * queue, schedules a StartService event immediately. Fires property changes
     * for numberAvailableServers and numberServed.
     */
    public void doEndService() {
        int oldNumberAvailableServers = getNumberAvailableServers();
        numberAvailableServers += 1;
        firePropertyChange("numberAvailableServers", oldNumberAvailableServers, getNumberAvailableServers());

        int oldNumberServed = getNumberServed();
        numberServed += 1;
        firePropertyChange("numberServed", oldNumberServed, getNumberServed());

        if (numberInQueue > 0) {
            waitDelay("StartService", 0.0, Priority.HIGH);
        }
    }

    /**
     * Returns the total number of servers.
     *
     * @return Total number of servers (parameter)
     */
    public int getTotalNumberServers() {
        return this.totalNumberServers;
    }

    /**
     *
     * @return current number in queue (state)
     */
    public int getNumberInQueue() {
        return this.numberInQueue;
    }

    /**
     *
     * @return Current number of available servers (state)
     */
    public int getNumberAvailableServers() {
        return this.numberAvailableServers;
    }

    /**
     *
     * @return Total number of customers who have completed service (state)
     */
    public int getNumberServed() {
        return this.numberServed;
    }

    /**
     *
     * @return Service time RandomVariate
     */
    public RandomVariate getServiceTimeGenerator() {
        return this.serviceTimeGenerator;
    }

    /**
     * Returns a short description of this Class.
     *
     * @return A short description of this class
     *
     */
    public static String description() {
        return "Model of Multiple Server Queue";
    }

    /**
     * Handles the Run, Arrival, StartService, and EndService events by calling
     * the corresponding event ("do") method.
     *
     * @param event The SimEvent to be handled.
     */
    @Override
    public void handleSimEvent(SimEvent event) {
        String thisEventName = event.getEventName();
        switch (thisEventName) {
            case "Run":
                doRun();
                break;
            case "Arrival":
                doArrival();
                break;
            case "StartService":
                doStartService();
                break;
            case "EndService":
                doEndService();
                break;
            default:
                break;

        }
    }

    /**
     * Processes events for which this SimpleServer2 is a listener. If the event
     * is Arrival or EndService, schedules Arrival for now. This allows this
     * SimpleServer2 to get Arrival events either from a SimEntity which
     * generates arrivals or by "chaining" to a preceeding SimEntity.
     *
     * @param event "Listened-to" event.
     */
    @Override
    public void processSimEvent(SimEvent event) {
        String thisEvent = event.getEventName();
        switch (thisEvent) {
            case "Arrival":
            case "EndService":
                waitDelay("Arrival", 0.0);
                break;
            default:
                break;
        }
    }

    /**
     * @param serviceTimeGenerator the serviceTimeGenerator to set
     */
    public void setServiceTimeGenerator(RandomVariate serviceTimeGenerator) {
        this.serviceTimeGenerator = serviceTimeGenerator;
    }

    /**
     * @param totalNumberServers the totalNumberServers to set
     */
    public void setTotalNumberServers(int totalNumberServers) {
        if (totalNumberServers <= 0) {
            throw new IllegalArgumentException("totalNumberServers must be > 0: "
                    + totalNumberServers);
        }
        this.totalNumberServers = totalNumberServers;
    }

}
