package simkit.examples;
import simkit.*;
import simkit.random.*;

/**
 * Multiple server queue using BasicSimEntity as superclass.  This approach
 * does not use reflection, but implicitly relies on the moral equivalent of a "switch"
 * statement to decide which event to execute when it is called back from Schedule (the
 * handleSimEvent() method).
 * @author Arnold Buss
 */
public class Server extends BasicSimEntity {

    private RandomVariate serviceTime;
    private int totalNumberServers;

    protected int numberAvailableServers;
    protected int numberInQueue;
    protected int numberServed;

    public Server(int numServ, RandomVariate rv) {
        serviceTime = RandomVariateFactory.getInstance(rv);
        totalNumberServers = numServ;
    }

    /** Resets state to empty and idle with no customers processed.
     */    
    public void reset() {
        super.reset();
        numberAvailableServers = totalNumberServers;
        numberInQueue = 0;
        numberServed = 0;
    }

    /** Simply fires the PropertyChangeEvents for time-varying state variables.
     */    
    public void doRun() {
        firePropertyChange("numberInQueue", numberInQueue);
        firePropertyChange("numberAvailableServers", numberAvailableServers);
    }

    /** Arrivale of a customer to queue.  After incrementing the number in queue, 
     *  if a server is available, a StartService event is scheduled.
     */    
    public void doArrival() {
        firePropertyChange("numberInQueue", numberInQueue, ++numberInQueue);

        if (numberAvailableServers > 0) {
            waitDelay("StartService", 0.0);
        }
    }

    /** Decrement number in queue and number of available servers.
     * Schedule EndService event after delay of a service time.
     */    
    public void doStartService() {
        firePropertyChange("numberInQueue", numberInQueue, --numberInQueue);
        firePropertyChange("numberAvailableServers", numberAvailableServers, --numberAvailableServers);

        waitDelay("EndService", serviceTime.generate());
    }

    /** Increment number of available servers.  If a customer is
     * waiting in the queue, schedule a StartService event immediately.
     */    
    public void doEndService() {
        firePropertyChange("numberAvailableServers", numberAvailableServers, ++numberAvailableServers);
        firePropertyChange("numberServed", 0, ++numberServed);

        if (numberInQueue > 0 ) {
            waitDelay("StartService", 0.0);
        }
    }

    /**
     * @return Total number of servers (parameter)
     */    
    public int getTotalNumberServers() { return totalNumberServers; }

    /**
     * @return current number in queue (state)
     */    
    public int getNumberInQueue() { return numberInQueue; }

    /**
     * @return Current number of available servers (state)
     */    
    public int getNumberAvailableServers() { return numberAvailableServers; }

    /**
     * @return Total number of customers who have completed service (state)
     */    
    public int getNumberServed() { return numberServed; }

    /**
     * @return Service time RandomVariate
     */    
    public RandomVariate getServiceTime() { return RandomVariateFactory.getInstance(serviceTime); }
    
    /**
     * @param st  RandomVariate instance to generate service times from.
     */    
    public void setServiceTime(RandomVariate st) {serviceTime = st;}

    public String paramString() {
        return toString();
    }
    
    /**
     * @return Short description of class
     */    
    public String toString() {
        return "Multiple Server Queue with " + totalNumberServers + " servers" +
            "\nService time distribution is " + serviceTime;
    }/**
 *  @return A short description of this class
**/
    public static String description() {
        return "Model of Multiple Server Queue";
    }
    
    /**
     * Typically an Event is handled (as opposed to processed, as in SimEventListener)
     * by actually executing a method.
     * @param event The SimEvent to be handled.
     */
    public void handleSimEvent(SimEvent event) {
        String thisEvent = event.getEventName();
        if (thisEvent.equals("Run")) {
            doRun();
        }
        
        else if(thisEvent.equals("Arrival")) {
            doArrival();
        }
        
        else if(thisEvent.equals("StartService")) {
            doStartService();
        }
        else if (thisEvent.equals("EndService")) {
            doEndService();
        }
    }
    
    /** Callback method when a SimEvent is "heard" via the SimEventListener pattern
     * @param event "Listened-to" event.
     */    
    public void processSimEvent(SimEvent event) {
        String thisEvent = event.getEventName();
        if (thisEvent.equals("Arrival") || thisEvent.equals("EndService")) {
            waitDelay("Arrival", 0.0);;
        }
    }
    
}
