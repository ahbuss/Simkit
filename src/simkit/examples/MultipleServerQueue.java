package simkit.examples;

import simkit.SimEntityBase;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;

/**
* A standalone implementation of a multi-server, single queue system. This Class
* models both the arrival process and the server process.
* @version $Id$
**/
public class MultipleServerQueue extends SimEntityBase {

/**
* The total number of servers in the system.
**/
    private int totalNumberServers;

/**
* The RandomVariate used to generate the inter-arrival times.
**/
    private RandomVariate interArrivalTime;

/**
* The RandomVariate used to generate the service times.
**/
    private RandomVariate serviceTime;

/**
* The total number of arrivals.
**/
    protected int numberArrivals;

/**
* The current number waiting in the queue.
**/
    protected int numberInQueue;

/**
* The number of servers that are not busy.
**/
    protected int numberAvailableServers;

/**
* The total number served.
**/
    protected int numberServed;

/**
* Makes a new MultipleServerQueue with the given number of servers and distributions.
* Note: Copies are made of the given RandomVariates.
* @param numberServers The total number of servers in the system.
* @param iat The RandomVariate used to generate the interarrival times.
* @param st The RandomVariate used to generate the service times.
**/
    public MultipleServerQueue(int numberServers, RandomVariate iat, RandomVariate st) {
        totalNumberServers = numberServers;
        this.setInterArrivalTime(iat);
        this.setServiceTime(st);
    }

/**
* Resets the system to its initial state.
**/
    public void reset() {
        super.reset();
        numberArrivals = 0;
        numberInQueue = 0;
        numberAvailableServers = totalNumberServers;
        numberServed = 0;
    }

/**
* Schedules the first arrival. Fires property changes for numberInQueue and 
* numberAvailableServers.
**/
    public void doRun() {
        firePropertyChange("numberInQueue", numberInQueue);
        firePropertyChange("numberAvailableServers", numberAvailableServers);
        waitDelay("Arrival", interArrivalTime.generate());
    }

/**
* Schedules the next arrival and if there is an available server, schedules
* StartService for now. Fires a property change for numberInQueue.
**/
    public void doArrival() {
        firePropertyChange("numberInQueue", numberInQueue, ++numberInQueue);
        waitDelay("Arrival", interArrivalTime.generate());
        if (numberAvailableServers > 0) {
            waitDelay("StartService", 0.0);
        }
    }

/**
* Schedules EndServer. Fires property changes for numberAvailableServers and
* numberInQueue.
**/
    public void doStartService() {
        firePropertyChange("numberAvailableServers", numberAvailableServers,
            --numberAvailableServers);
        firePropertyChange("numberInQueue", numberInQueue, --numberInQueue);
        waitDelay("EndService", serviceTime.generate());
    }

/**
* If the queue is not empty, schedules start service for now. Fires property
* changes for numberAvailableServers and numberServed.
**/
    public void doEndService() {
        firePropertyChange("numberAvailableServers", numberAvailableServers,
            ++numberAvailableServers);
        firePropertyChange("numberServed", ++numberServed);
        if (numberInQueue > 0) {
            waitDelay("StartService", 0.0);
        }
    }

/**
 * Sets the RandomVariate used to generate interarrival times to a copy
 * of the given RandomVariate.
 *  @param iat Interarrival distribution (parameter)
**/
    public void setInterArrivalTime(RandomVariate iat) {
        interArrivalTime = RandomVariateFactory.getInstance(iat);
    }
/**
 * Returns a copy of the RandomVariate used to generate interarrival times. 
 *  @return Interarrival distribution (parameter)
**/
    public RandomVariate getInterArrivalTime() {
        return RandomVariateFactory.getInstance(interArrivalTime);
    }
/**
 * Sets the RandomVariate used to generate service times to a copy
 * of the given RandomVariate.
 *  @param iat Service time distribution (parameter)
**/
    public void setServiceTime(RandomVariate st) {
        serviceTime = RandomVariateFactory.getInstance(st);
    }
/**
 * Returns a copy of the RandomVariate used to generate service times. 
 *  @return Service time distribution (parameter)
**/
    public RandomVariate getServiceTime() {
        return RandomVariateFactory.getInstance(serviceTime);
    }
/**
 * Returns the total number of servers in the system.
 *  @return Total number of servers (parameter)
**/
    public int getTotalNumberServers() { return totalNumberServers; }

/**
 * Returns the number of arrivals to the system.
 * @return Number of arrivals (state variable)
**/
    public int getNumberArrivals() { return numberArrivals; }

/**
 * Returns the current length of the queue.
 * @return Number in Queue (state variable)
**/
    public int getNumberInQueue() { return numberInQueue; }

/**
 * Returns The number of servers that are not busy.
 * @return Number of available servers (state variable)
**/
    public int getNumberAvailableServers() { return numberAvailableServers; }
/**
 * Returns the total number served by the system.
 * @return Number served (state variable)
**/
    public int getNumberServed() { return numberServed; }

/**
 * Returns a String containing a short description of this Class.
 *  @return A short description of this class
**/
    public static String description() {
        return "Model of Multiple Server Queue";
    }
}
