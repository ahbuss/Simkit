package simkit.examples;

import java.util.List;
import java.util.LinkedList;
import simkit.SimEntityBase;
import simkit.random.RandomVariate;

/**
 *
 * @author  Arnold Buss
 */
public class CustomerServer extends SimEntityBase {
    
    private int numberServers;
    private RandomVariate serviceTime;
    
    protected LinkedList queue;
    protected int numberAvailableServers;
    
    
    /** Creates a new instance of CustomerServer */
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
    
    public void doRun() {
        firePropertyChange("numberInQueue", queue.size());
        firePropertyChange("numberAvailableServers", numberAvailableServers);
    }
    
    /** Add arriving customer to queue.  If there is an available
     * server, schedule StartService event.
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
    
    /** Remove first Customer from queue; decrement number of
     * available servers.
     * Schedule EndService event with delay of service time.
     */    
    public void doStartService() {
        Customer customer = (Customer) queue.removeFirst();
        firePropertyChange("delayInQueue", customer.getTimeSinceStamp());
        firePropertyChange("numberInQueue", queue.size() + 1, queue.size());
        firePropertyChange("numberAvailableServers", numberAvailableServers,
            --numberAvailableServers);
        
        waitDelay("EndService", serviceTime.generate(), customer);
    }
    
    /** Increment number of available servers.  If there are
     * still customer(s) in the queue, schedule a StartService
     * event.
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
     * @param st Service time RandomVariate
     */    
    public void setServiceTime(RandomVariate st) { serviceTime = st; }
    
    /**
     * @return Service time RandomVariate instance
     */    
    public RandomVariate getServiceTime() { return serviceTime; }
    
    /**
     * @param ns Total number of servers for this instance
     */    
    public void setNumberServers(int ns) { numberServers = ns; }
    
    /**
     * @return Total number of servers for this instance
     */    
    public int getNumberServers() { return numberServers; }
    
    /**
     * @return Number in queue
     */    
    public int getNumberInQueue() { return queue.size(); }
    
    /**
     * @return Shallow copy of queue
     */    
    public List getQueue() { return new LinkedList(queue); }
    
    /**
     * @return Number available servers at this point in time
     */    
    public int getNumberAvailableServers() { return numberAvailableServers; }
    
    /**
     * @return Short description
     */    
    public String toString() {
        return "Multiple Server Queue" +
            "\n\t" + getNumberServers() + " servers" +
            "\n\t" + getServiceTime() + " service time";
    }
    
}
