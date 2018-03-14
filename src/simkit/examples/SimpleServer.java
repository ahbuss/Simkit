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
    private RandomVariate serviceTimeGenerator;

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
     * Creates a new Server with the given number of totalNumberServers and the
     * given service serviceTimeGenerator distribution.
     *
     * @param totalNumberServers total # servers
     * @param serviceTimeGenerator generates service times
     */
    public SimpleServer(int totalNumberServers, RandomVariate serviceTimeGenerator) {
        this();
        setTotalNumberServers(totalNumberServers);
        setServiceTimeGenerator(serviceTimeGenerator);
    }

    /**
     * Resets the system to its initial state:<ul>
     * <li> numberInQueue = 0
     * <li>numberAvailableServers = totalNumberServers
     * <li> numberServed = 0
     * </ul>
     */
    @Override
    public void reset() {
        super.reset();
        numberInQueue = 0;
        numberAvailableServers = getTotalNumberServers();
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

        waitDelay("EndService", serviceTimeGenerator.generate());

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
     *
     * @return the number of servers that are not busy.
     */
    public int getNumberAvailableServers() {
        return numberAvailableServers;
    }

    /**
     *
     * @return the current length of the queue.
     */
    public int getNumberInQueue() {
        return numberInQueue;
    }

    /**
     *
     * @return the number currently in the system. That is the number in the
     * queue plus the number currently being served.
     */
    public int getNumberInSystem() {
        return getNumberInQueue() + getTotalNumberServers() - getNumberAvailableServers();
    }

    /**
     * Sets the total number of totalNumberServers.
     *
     * @param totalNumberServers Given total number of servers
     * @throws IllegalArgumentException if the number of totalNumberServers is
     * not positive.
     *
     */
    public void setTotalNumberServers(int totalNumberServers) {
        if (totalNumberServers <= 0) {
            throw new IllegalArgumentException("Need positive number of servers: " + totalNumberServers);
        }
        this.totalNumberServers = totalNumberServers;
    }

    /**
     *
     * @return the total number of servers in the system.
     */
    public int getTotalNumberServers() {
        return this.totalNumberServers;
    }

    /**
     *
     * @param serviceTimeGenerator the RandomVariate used to generate
     * serviceTimeGenerator times.
     */
    public void setServiceTimeGenerator(RandomVariate serviceTimeGenerator) {
        this.serviceTimeGenerator = serviceTimeGenerator;
    }

    /**
     *
     * @return the RandomVariate used to generate service times.
     */
    public RandomVariate getServiceTimeGenerator() {
        return serviceTimeGenerator;
    }

    /**
     *
     * @return the total number served.
     */
    public int getNumberServed() {
        return numberServed;
    }

}
