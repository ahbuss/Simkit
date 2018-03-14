package simkit.examples;

import java.util.SortedSet;
import java.util.TreeSet;
import simkit.Entity;
import simkit.Priority;

import simkit.SimEntityBase;
import simkit.random.RandomVariate;

/**
 * Implements a Multi-server single queue server for Customers. After
 * instantiating a EntityServer, register it as a SimEventListener with a
 * CustomerCreator.
 *
 * @author Arnold Buss
 */
public class EntityServer extends SimEntityBase {

    /**
     * The total number of servers in the system
     */
    private int totalNumberServers;

    /**
     * The RandomVariate used to generate service times.
     */
    private RandomVariate serviceTimeGenerator;

    /**
     * The queue of waiting Customers.
     */
    protected SortedSet<Entity> queue;

    /**
     * The number of available servers.
     */
    protected int numberAvailableServers;

    public EntityServer() {
        queue = new TreeSet<>();
    }

    /**
     * Creates a new EntityServer with the given number of servers, and service
     * time distribution.
     *
     * @param totalNumberServers total number of servers
     * @param serviceTimeGenerator Generates service times
     */
    public EntityServer(int totalNumberServers, RandomVariate serviceTimeGenerator) {
        this();
        setTotalNumberServers(totalNumberServers);
        setServiceTimeGenerator(serviceTimeGenerator);
    }

    /**
     * Clear queue, set numberAvailableServers to total number of servers.
     */
    @Override
    public void reset() {
        super.reset();
        numberAvailableServers = totalNumberServers;
        queue.clear();
    }

    /**
     * Fire property changes for the number in the queue (numberInQueue) and the
     * number of available servers (numberAvailableServers).
     */
    public void doRun() {
        firePropertyChange("queue", getQueue());
        firePropertyChange("numberInQueue", getQueue().size());
        firePropertyChange("numberAvailableServers", numberAvailableServers);
    }

    /**
     * Adds arriving customer to queue. If there is an available server,
     * schedules StartService event. Fires a property change for the number in
     * the queue. (numberInQueue)
     *
     * @param customer Arriving Customer
     */
    public void doArrival(Entity customer) {
        customer.stampTime();
        SortedSet<Entity> oldQueue = getQueue();
        queue.add(customer);
        firePropertyChange("queue", oldQueue, getQueue());
        firePropertyChange("numberInQueue", oldQueue.size(), getQueue().size());

        if (getNumberAvailableServers() > 0) {
            waitDelay("StartService", 0.0, Priority.HIGH);
        }
    }

    /**
     * Removes first Customer from queue; decrements number of available
     * servers. Schedules EndService event with delay of service time. Fires
     * property changes for delayInQueue, numberInQueue, and
     * numberAvailableServers.
     */
    public void doStartService() {
        SortedSet<Entity> oldQueue = getQueue();
        Entity customer = queue.first();
        queue.remove(customer);

        firePropertyChange("queue", oldQueue, getQueue());
        firePropertyChange("numberInQueue", oldQueue.size(), getQueue().size());
        firePropertyChange("delayInQueue", customer.getElapsedTime());

        int OldNumberAvailableServers = getNumberAvailableServers();
        numberAvailableServers -= 1;
        firePropertyChange("numberAvailableServers", OldNumberAvailableServers,
                getNumberAvailableServers());

        waitDelay("EndService", serviceTimeGenerator.generate(), customer);
    }

    /**
     * Increments number of available servers. If there are still customer(s) in
     * the queue, schedules a StartService event. Fires property changes for
     * timeInSystem (The total time since the Customer's arrival) and
     * numberAvailableServers.
     *
     * @param customer Customer finishing service
     */
    public void doEndService(Entity customer) {
        firePropertyChange("timeInSystem", customer.getElapsedTime());

        int OldNumberAvailableServers = getNumberAvailableServers();
        numberAvailableServers += 1;
        firePropertyChange("numberAvailableServers", OldNumberAvailableServers,
                getNumberAvailableServers());

        if (getNumberInQueue() > 0) {
            waitDelay("StartService", 0.0, Priority.HIGH);
        }
    }

    /**
     * Sets the RandomVariate used to generate the service times.
     *
     * @param st Service time RandomVariate
     */
    public void setServiceTimeGenerator(RandomVariate st) {
        serviceTimeGenerator = st;
    }

    /**
     * Returns the RandomVariate used to generate the service times.
     *
     * @return Service time RandomVariate instance
     */
    public RandomVariate getServiceTimeGenerator() {
        return serviceTimeGenerator;
    }

    /**
     * Sets the total number of servers.
     *
     * @param totalNumberServers Total number of servers for this instance
     */
    public void setTotalNumberServers(int totalNumberServers) {
        if (totalNumberServers <= 0) {
            throw new IllegalArgumentException("totalNumberServers must be > 0: " + totalNumberServers);
        }
        this.totalNumberServers = totalNumberServers;
    }

    /**
     * Returns the total number of servers.
     *
     * @return Total number of servers for this instance
     */
    public int getTotalNumberServers() {
        return this.totalNumberServers;
    }

    /**
     * Returns the number of Customers in the queue.
     *
     * @return Number in queue
     */
    public int getNumberInQueue() {
        return queue.size();
    }

    /**
     * Returns a copy of the queue.
     *
     * @return Shallow copy of queue
     */
    public SortedSet<Entity> getQueue() {
        return new TreeSet<>(this.queue);
    }

    /**
     * Returns the number of servers that are not busy.
     *
     * @return Number available servers at this point in time
     */
    public int getNumberAvailableServers() {
        return this.numberAvailableServers;
    }
}
