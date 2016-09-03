package simkit.examples;

import simkit.Priority;
import simkit.SimEntityBase;
import simkit.random.RandomVariate;

/**
 * An implementation of a simple single queue, multi-server process. After
 * instantiating, should be registered as a SimEventListener for a source of
 * arrival events (like {@link ArrivalProcess2}.
 *
 * @author Arnold Buss
 * @version $Id$
 */
public class SimpleServer extends SimEntityBase {

    /**
     * The total number of servers in the system.
     *
     */
    private int totalNumberServers;

    /**
     * The RandomVariate used to generate service times.
     *
     */
    private RandomVariate serviceTime;

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

    public SimpleServer() {
    }

    /**
     * Creates a new Server with the given number of servers and the given
     * service time distribution.
     */
    public SimpleServer(int servers, RandomVariate time) {
        totalNumberServers = servers;
        serviceTime = time;
    }

    /**
     * Resets the system to its initial state.
     *
     */
    public void reset() {
        super.reset();
        numberInQueue = 0;
        numberAvailableServers = totalNumberServers;
        numberServed = 0;
    }

    /**
     * Fires property changes for numberInQueue and numberAvailableServers.
     *
     */
    public void doRun() {
        firePropertyChange("numberInQueue", numberInQueue);
        firePropertyChange("numberAvailableServers", numberAvailableServers);
        firePropertyChange("numberServed", getNumberServed());
    }

    /**
     * If there is an available server, schedules StartService now. Fires a
     * property change for numberInQueue.
     *
     */
    public void doArrival() {
        firePropertyChange("numberInQueue", numberInQueue, ++numberInQueue);
        if (getNumberAvailableServers() > 0) {
            waitDelay("StartService", 0.0, Priority.HIGH);
        }
    }

    /**
     * Schedules EndService. Fires property changes for numberInQueue and
     * numberAvailable Servers.
     *
     */
    public void doStartService() {
        firePropertyChange("numberInQueue", numberInQueue, --numberInQueue);
        firePropertyChange("numberAvailableServers", numberAvailableServers, --numberAvailableServers);

        waitDelay("EndService", serviceTime.generate());

    }

    /**
     * If the queue is not empty, schedules StartService for now. Fires property
     * changes for numberAvailableServers and numberServed.
     *
     */
    public void doEndService() {
        firePropertyChange("numberAvailableServers", numberAvailableServers, ++numberAvailableServers);
        firePropertyChange("numberServed", numberServed, ++numberServed);
        if (getNumberInQueue() > 0) {
            waitDelay("StartService", 0.0, Priority.HIGH);
        }
    }

    /**
     * Returns the number of servers that are not busy.
     *
     */
    public int getNumberAvailableServers() {
        return numberAvailableServers;
    }

    /**
     * Returns the current length of the queue.
     *
     */
    public int getNumberInQueue() {
        return numberInQueue;
    }

    /**
     * Returns the number currently in the system. That is the number in the
     * queue plus the number currently being served.
     *
     */
    public int getNumberInSystem() {
        return getNumberInQueue() + getNumberServers() - getNumberAvailableServers();
    }

    /**
     * Sets the total number of servers.
     *
     * @throws IllegalArgumentException if the number of servers is not
     * positive.
     *
     */
    public void setNumberServers(int servers) {
        if (totalNumberServers <= 0) {
            throw new IllegalArgumentException("Need positive number of servers: " + servers);
        }
        totalNumberServers = servers;
    }

    /**
     * Returns the total number of servers in the system.
     *
     */
    public int getNumberServers() {
        return totalNumberServers;
    }

    /**
     * Sets the RandomVariate used to generate service times.
     *
     */
    public void setServiceTime(RandomVariate service) {
        serviceTime = service;
    }

    /**
     * Returns the RandomVariate used to generate service times.
     *
     */
    public RandomVariate getServiceTime() {
        return serviceTime;
    }

    /**
     * Returns the total number served.
     *
     */
    public int getNumberServed() {
        return numberServed;
    }

}
