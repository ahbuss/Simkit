package simkit.examples;

import java.util.SortedSet;
import java.util.TreeSet;
import simkit.Priority;

import simkit.SimEntityBase;
import simkit.random.RandomVariate;

/**
 * A multiple-server queue with impatient customers. Each arriving is willing to
 * wait only a given amount of time in the queue, represented by the random
 * sequence renegeTimeGenerator. If a customerID's time has elapsed without
 * receiving service, that customerID exits the queue ("reneges") without
 * receiving service.
 *
 * @author Arnold Buss
 */
public class ServerWithReneges extends SimEntityBase {

    private int totalNumberServers;
    private RandomVariate serviceTimeGenerator;
    private RandomVariate renegeTimeGenerator;

    protected int numberAvailableServers;
    protected SortedSet<Integer> queue;

    protected int numberServed;
    protected int numberReneges;

    public ServerWithReneges() {
        this.queue = new TreeSet<>();
    }

    /**
     *
     * @param totalNumberServers total number of servers
     * @param serviceTimeGenerator Generate service times
     * @param renegeTimeGenerator generates renege times
     */
    public ServerWithReneges(int totalNumberServers, RandomVariate serviceTimeGenerator, RandomVariate renegeTimeGenerator) {
        this();
        setTotalNumberServers(totalNumberServers);
        setServiceTimeGenerator(serviceTimeGenerator);
        setRenegeTimeGenerator(renegeTimeGenerator);
    }

    /**
     * Set initial values of all state variables.
     */
    @Override
    public void reset() {
        super.reset();
        numberAvailableServers = getTotalNumberServers();
        queue.clear();
        numberServed = 0;
        numberReneges = 0;
    }

    /**
     * Just fires PropertyChangeEvents for time-varying states and for counts.
     */
    public void doRun() {
        firePropertyChange("numberAvailableServers", numberAvailableServers);
        firePropertyChange("numberInQueue", queue.size());
        firePropertyChange("numberServed", numberServed);
        firePropertyChange("numberReneges", numberReneges);
    }

    /**
     * Add arriving customerID to queue. Schedule Renege event for this
     * customerID after a renegeTimeGenerator delay. If there is an available
     * server, schedule StartService event
     *
     * @param customerID Arriving Customer index
     */
    public void doArrival(Integer customerID) {
        queue.add(customerID);
        firePropertyChange("queue", getQueue());

        waitDelay("Renege", renegeTimeGenerator.generate(), customerID);
        if (getNumberAvailableServers() > 0) {
            waitDelay("StartService", 0.0, Priority.HIGH);
        }
    }

    /**
     * Remove first customerID from queue. Decrement number of available
     * servers. Cancel Renege event for this customerID. Schedule EndService
     * after a delay of a service time.
     */
    public void doStartService() {
        Integer customerID = queue.first();
        firePropertyChange("customer", customerID);

        queue.remove(customerID);
        firePropertyChange("queue", getQueue());

        numberAvailableServers = numberAvailableServers - 1;
        firePropertyChange("numberAvailableServers", numberAvailableServers);

        interrupt("Renege", customerID);

        waitDelay("EndService", serviceTimeGenerator.generate(), customerID);
    }

    /**
     * Increment number of available servers. If queue is not empty, schedule
     * StartService with 0.0 delay.
     *
     * @param customer Index of customerID ending service
     */
    public void doEndService(Integer customer) {
        numberAvailableServers = numberAvailableServers + 1;
        firePropertyChange("numberAvailableServers", numberAvailableServers);

        numberServed = numberServed + 1;
        firePropertyChange("numberServed", numberServed);
//        Needs higher priority than Arrival event in case of time ties
        if (getNumberInQueue() > 0) {
            waitDelay("StartService", 0.0, Priority.HIGH);
        }
    }

    /**
     * A customerID has reached the "limit" of their patience and leaves the
     * queue without receiving service ("renege).
     *
     * Remove the given customerID from the queue. Increment the total number of
     * reneges.
     *
     * @param customerID Index of customerID leaving queue
     */
    public void doRenege(Integer customerID) {
        queue.remove(customerID);
        firePropertyChange("queue", getQueue());

        numberReneges = numberReneges + 1;
        firePropertyChange("numberReneges", numberReneges);
    }

    public void setTotalNumberServers(int totalNumberServers) {
        this.totalNumberServers = totalNumberServers;
    }

    public int getTotalNumberServers() {
        return this.totalNumberServers;
    }

    public void setServiceTimeGenerator(RandomVariate serviceTimeGenerator) {
        this.serviceTimeGenerator = serviceTimeGenerator;
    }

    public RandomVariate getServiceTimeGenerator() {
        return this.serviceTimeGenerator;
    }

    public void setRenegeTimeGenerator(RandomVariate renegeTimeGenerator) {
        this.renegeTimeGenerator = renegeTimeGenerator;
    }

    public RandomVariate getRenegeTimeGenerator() {
        return renegeTimeGenerator;
    }

    public int getNumberAvailableServers() {
        return this.numberAvailableServers;
    }

    public int getNumberInQueue() {
        return queue.size();
    }

    public SortedSet<Integer> getQueue() {
        return new TreeSet<>(this.queue);
    }

    public int getNumberReneges() {
        return this.numberReneges;
    }

    public int getNumberServed() {
        return this.numberServed;
    }

}
