package simkit.examples;

import java.util.LinkedList;
import java.util.List;

import simkit.SimEntityBase;
import simkit.random.RandomVariate;

/**
 * Implements a Multi-server single queue server for Customers.
 * After instantiating a CustomerServer, register it as a SimEventListener
 * with a {@link CustomerCreator}.
 * @author  Arnold Buss
 * @version $Id$
 */
public class CustomerServer extends SimEntityBase {
    
/**
* The total number of servers in the system
**/
    private int numberServers;

/**
* The RandomVariate used to generate service times.
**/
    private RandomVariate serviceTime;
    
/**
* The queue of waiting Customers.
**/
    protected LinkedList queue;

/**
* The number of available servers.
**/
    protected int numberAvailableServers;
    
/**
* Creates a new CustomerServer with the given number of servers, and service
* time distribution.
**/
    public CustomerServer(int numberServers, RandomVariate serviceTime) {
        queue = new LinkedList();
        setNumberServers(numberServers);
        setServiceTime(serviceTime);
    }
    
    /** Clear queue, set numberAvailableServers to total number
     * of servers.
     */    
    public void reset() {
        super.reset();
        numberAvailableServers = numberServers;
        queue.clear();
    }
    
/**
* Fire property changes for the number in the queue (numberInQueue) and the number of available
* servers (numberAvailableServers).
**/
    public void doRun() {
        firePropertyChange("numberInQueue", queue.size());
        firePropertyChange("numberAvailableServers", numberAvailableServers);
    }
    
    /** Adds arriving customer to queue.  If there is an available
     * server, schedules StartService event.
     * Fires a property change for the number in the queue. (numberInQueue)
     * @param customer Arriving Customer
     */    
    public void doArrival(Customer customer) {
        customer.stampTime();
        queue.add(customer);
        firePropertyChange("numberInQueue", queue.size() - 1, queue.size());
        if (getNumberAvailableServers() > 0) {
            waitDelay("StartService", 0.0, 1.0);
        }
    }
    
    /** Removes first Customer from queue; decrements number of
     * available servers.
     * Schedules EndService event with delay of service time.
     * Fires property changes for delayInQueue, numberInQueue, and numberAvailableServers.
     */    
    public void doStartService() {
        Customer customer = (Customer) queue.removeFirst();
        firePropertyChange("delayInQueue", customer.getTimeSinceStamp());
        firePropertyChange("numberInQueue", queue.size() + 1, queue.size());
        firePropertyChange("numberAvailableServers", numberAvailableServers,
            --numberAvailableServers);
        
        waitDelay("EndService", serviceTime.generate(), customer);
    }
    
    /** Increments number of available servers.  If there are
     * still customer(s) in the queue, schedules a StartService
     * event.
     * Fires property changes for timeInSystem (The total time since the Customer's
     * arrival) and numberAvailableServers.
     * @param customer Customer finishing service
     */    
    public void doEndService(Customer customer) {
        firePropertyChange("timeInSystem", customer.getTimeSinceStamp());
        firePropertyChange("numberAvailableServers", numberAvailableServers,
            ++numberAvailableServers);
        if (getNumberInQueue() > 0) {
            waitDelay("StartService", 0.0, -1.0);
        }
    }
    
    /**
     * Sets the RandomVariate used to generate the service times.
     * @param st Service time RandomVariate
     */    
    public void setServiceTime(RandomVariate st) { serviceTime = st; }
    
    /**
     * Returns the RandomVariate used to generate the service times.
     * @return Service time RandomVariate instance
     */    
    public RandomVariate getServiceTime() { return serviceTime; }
    
    /**
     * Sets the total number of servers.
     * @param ns Total number of servers for this instance
     */    
    public void setNumberServers(int ns) { numberServers = ns; }
    
    /**
     * Returns the total number of servers.
     * @return Total number of servers for this instance
     */    
    public int getNumberServers() { return numberServers; }
    
    /**
     * Returns the number of Customers in the queue.
     * @return Number in queue
     */    
    public int getNumberInQueue() { return queue.size(); }
    
    /**
     * Returns a copy of the queue.
     * @return Shallow copy of queue
     */    
    public List getQueue() { return new LinkedList(queue); }
    
    /**
     * Returns the number of servers that are not busy.
     * @return Number available servers at this point in time
     */    
    public int getNumberAvailableServers() { return numberAvailableServers; }
}
