package simkit.examples;

import java.util.LinkedList;
import java.util.List;

import simkit.SimEntityBase;
import simkit.random.RandomVariate;

/**
 * A multiple-server queue with impatient customers.  Each arriving
 * is willing to wait only a given amount of time in the queue,
 * represented by the random sequence renegeTime.  If a customer's
 * time has elapsed without receiving service, that customer
 * exits the queue ("reneges") without receiving service.
 *
 * @author  Arnold Buss
 */
public class ServerWithReneges extends SimEntityBase {
    
    private int numberServers;
    private RandomVariate serviceTime;
    private RandomVariate renegeTime;
    
    protected int numberAvailableServers;
    protected LinkedList queue;
    
    protected int numberServed;
    protected int numberReneges;
    
    /** Creates a new instance of Simkit */
    public ServerWithReneges(int servers, RandomVariate service, RandomVariate renege) {
        setNumberServers(servers);
        setServiceTime(service);
        setRenegeTime(renege);
        queue = new LinkedList();
    }
    
    /** Set initial values of all state variables. */    
    public void reset() {
        super.reset();
        numberAvailableServers = getNumberServers();
        queue.clear();
        numberServed = 0;
        numberReneges = 0;
    }
    
    /** Just fires PropertyChangeEvents for time-varying states
     * and for counts.
     */    
    public void doRun() {
        firePropertyChange("numberAvailableServers", numberAvailableServers);
        firePropertyChange("numberInQueue", queue.size());
        firePropertyChange("numberServed", numberServed);
        firePropertyChange("numberReneges", numberReneges);
    }
    
    /** Add arriving customer to queue.
     * Schedule Renege event for this customer after a renegeTime delay.
     * If there is an available server, schedule StartService event
     * @param customer Arriving Customer index
     */    
    public void doArrival(Integer customer) {
        queue.add(customer);
        firePropertyChange("queue", getQueue());
        
        waitDelay("Renege", renegeTime.generate(), customer);
        if (getNumberAvailableServers() > 0) {
            waitDelay("StartService", 0.0, 1.0);
        }
    }
    
    /** Remove first customer from queue.
     * Decrement number of available servers.
     * Cancel Renege event for this customer.
     * Schedule EndService after a delay of a service time.
     */    
    public void doStartService() {
        Integer customer = (Integer) queue.getFirst();
        firePropertyChange("customer", customer);
        
        queue.removeFirst();
        firePropertyChange("queue", getQueue());
        
        numberAvailableServers = numberAvailableServers - 1;
        firePropertyChange("numberAvailableServers", numberAvailableServers);
        
        interrupt("Renege", customer);
        
        waitDelay("EndService", serviceTime.generate(), customer);
    }
    
    /** Incremenet number of availabe servers.
     * If queue is not empty, schedule StartService with 0.0 delay.
     * @param customer Index of customer ending service
     */    
    public void doEndService(Integer customer) {
        numberAvailableServers = numberAvailableServers + 1;
        firePropertyChange("numberAvailableServers", numberAvailableServers);
        
        numberServed = numberServed + 1;
        firePropertyChange("numberServed", numberServed);
//        Needs higher priority than Arrival event in case of time ties
        if (getNumberInQueue() > 0) {
            waitDelay("StartService", 0.0, 1.0);
        }
    }
    
    /** A customer has reached the "limit" of their patience
     * and leaves the queue without receiving service ("renege).
     *
     * Remove the given customer from the queue.
     * Increment the total number of reneges.
     * @param customer Index of customer leaving queue
     */    
    public void doRenege(Integer customer) {
        queue.remove(customer);
        firePropertyChange("queue", getQueue());
        
        numberReneges = numberReneges + 1;
        firePropertyChange("numberReneges", numberReneges);
    }
    
    public void setNumberServers(int num) { numberServers = num; }
    
    public int getNumberServers() { return numberServers; }
    
    public void setServiceTime(RandomVariate st) { serviceTime = st; }
    
    public RandomVariate getServiceTime() { return serviceTime; }
    
    public void setRenegeTime(RandomVariate rt) { renegeTime = rt; }
    
    public RandomVariate getRenegeTime() { return renegeTime; }
    
    public int getNumberAvailableServers() { return numberAvailableServers; }
    
    public int getNumberInQueue() { return queue.size(); }
    
    public List getQueue() { return (List) queue.clone(); }
    
    public int getNumberReneges() { return numberReneges; }
    
    public int getNumberServed() { return  numberServed; }
    
}
