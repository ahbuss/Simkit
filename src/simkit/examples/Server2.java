package simkit.examples;
import simkit.*;
import simkit.random.*;

/**
 * Multiple server queue using BasicSimEntity as superclass.  This approach
 * does not use reflection, but implicitly relies on the moral equivalent of a "switch"
 * statement to decide which event to execute when it is called back from Schedule (the
 * handleSimEvent() method).
 * @author Arnold Buss
 * @version $Id$
 */
public class Server2 extends BasicSimEntity {

/**
* The RandomVariate used to generate service times.
**/
    private RandomVariate serviceTime;

/**
* The total number of servers in the system.
**/
    private int totalNumberServers;

/**
* The number of servers that are not busy.
**/
    protected int numberAvailableServers;

/**
* The current length of the queue.
**/
    protected int numberInQueue;

/**
* The total number served by the system.
**/
    protected int numberServed;

/**
* Creates a new Server2 with the given number of servers and the given service
* time distribution.
**/
    public Server2(int numServ, RandomVariate rv) {
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

    /** Simply fires the PropertyChangeEvents for numberInQueue and numberAvailableServers.
     */    
    public void doRun() {
        firePropertyChange("numberInQueue", numberInQueue);
        firePropertyChange("numberAvailableServers", numberAvailableServers);
    }

    /** Arrival of a customer to queue.  After incrementing the number in queue, 
     *  if a server is available, a StartService event is scheduled.
     * Fires a property change for numberInQueue.
     */    
    public void doArrival() {
        firePropertyChange("numberInQueue", numberInQueue, ++numberInQueue);

        if (numberAvailableServers > 0) {
            waitDelay("StartService", 0.0);
        }
    }

/** 
* Schedules EndService event after delay of a service time.
* Decrements number in queue and number of available servers.
* Fires property changes for numberInQueue and numberAvailable servers.
*/    
    public void doStartService() {
        firePropertyChange("numberInQueue", numberInQueue, --numberInQueue);
        firePropertyChange("numberAvailableServers", numberAvailableServers, --numberAvailableServers);

        waitDelay("EndService", serviceTime.generate());
    }

    /** Increments number of available servers.  If a customer is
     * waiting in the queue, schedules a StartService event immediately.
     * Fires property changes for numberAvailableServers and numberServed.
     */    
    public void doEndService() {
        firePropertyChange("numberAvailableServers", numberAvailableServers, ++numberAvailableServers);
        firePropertyChange("numberServed", 0, ++numberServed);

        if (numberInQueue > 0 ) {
            waitDelay("StartService", 0.0);
        }
    }

    /**
     * Returns the total number of servers.
     * @return Total number of servers (parameter)
     */    
    public int getTotalNumberServers() { return totalNumberServers; }

    /**
     * Returns the current length of the queue.
     * @return current number in queue (state)
     */    
    public int getNumberInQueue() { return numberInQueue; }

    /**
     * Returns the number of servers that are not currently busy.
     * @return Current number of available servers (state)
     */    
    public int getNumberAvailableServers() { return numberAvailableServers; }

    /**
     * Returns the total number served by the system.
     * @return Total number of customers who have completed service (state)
     */    
    public int getNumberServed() { return numberServed; }

    /**
     * Returns a copy of the RandomVariate used to generate service times.
     * @return Service time RandomVariate
     */    
    public RandomVariate getServiceTime() { return RandomVariateFactory.getInstance(serviceTime); }
    
    /**
     * Sets the RandomVariate used to generate service times.
     * @param st  RandomVariate instance to generate service times from.
     */    
    public void setServiceTime(RandomVariate st) {serviceTime = st;}

    /**
     * Returns a String containing information about the total number of servers and
     * the distribution of the service times.
     */
    public String paramString() {
        return toString();
    }
    
    /**
     * Returns a String containing information about the total number of servers and
     * the distribution of the service times.
     * @return Short description of class
     */    
    public String toString() {
          return "Multiple Server Queue\n\tNumber Servers:\t" + totalNumberServers +
            "\n\tService Time Distribution:\t" + serviceTime;
  }

/**
 * Returns a short description of this Class.
 *  @return A short description of this class
**/
    public static String description() {
        return "Model of Multiple Server Queue";
    }
    
    /**
     * Handles the Run, Arrival, StartService, and EndService events by calling
     * the corresponding event ("do") method.
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
    
/**
* Processes events for which this Server2 is a listener. If the event is Arrival
* or EndService, schedules Arrival for now.
* @param event "Listened-to" event.
  */    
    public void processSimEvent(SimEvent event) {
        String thisEvent = event.getEventName();
        if (thisEvent.equals("Arrival") || thisEvent.equals("EndService")) {
            waitDelay("Arrival", 0.0);;
        }
    }
    
}
