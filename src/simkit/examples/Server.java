package simkit.examples;

import simkit.*;
import simkit.random.*;

/**
 *
 * @author  Arnold Buss
 */
public class Server extends SimEntityBase {
    
    private int totalNumberServers;
    private RandomVariate serviceTime;
    
    protected int numberAvailableServers;
    protected int numberInQueue;
    protected int numberServed;
    
    /** Creates a new instance of Simkit */
    public Server(int servers, RandomVariate time) {
        totalNumberServers = servers;
        serviceTime = time;
    }
    
    public void reset() {
        super.reset();
        numberInQueue = 0;
        numberAvailableServers = totalNumberServers;
        numberServed = 0;
    }
    
    public void doRun() {
        firePropertyChange("numberInQueue", numberInQueue);
        firePropertyChange("numberAvailableServers", numberAvailableServers);
    }
    
    public void doArrival() {
        firePropertyChange("numberInQueue", numberInQueue, ++numberInQueue);
        if (getNumberAvailableServers() > 0) {
            waitDelay("StartService", 0.0);
        }
    }
    
    public void doStartService() {
        firePropertyChange("numberInQueue", numberInQueue, --numberInQueue);
        firePropertyChange("numberAvailableServers", numberAvailableServers, --numberAvailableServers);
        
        waitDelay("EndService", serviceTime.generate());
        
    }
    
    public void doEndService() {
        firePropertyChange("numberAvailableServers", numberAvailableServers, ++numberAvailableServers);
        firePropertyChange("numberServed", numberServed, ++numberServed);
        if (getNumberInQueue() > 0) {
            waitDelay("StartService", 0.0);
        }
    }
    
    public int getNumberAvailableServers() { return numberAvailableServers; }
    
    public int getNumberInQueue() { return numberInQueue; }
    
    public int getNumberInSystem() {
        return getNumberInQueue() + getNumberServers() - getNumberAvailableServers();
    }
    
    public void setNumberServers(int servers) { 
        if (totalNumberServers <= 0) {
            throw new IllegalArgumentException("Need positive number of servers: " + servers);
        }
        totalNumberServers = servers; 
    }
    
    public int getNumberServers() { return totalNumberServers; }
    
    public void setServiceTime(RandomVariate service) { serviceTime = service; }
    
    public RandomVariate getServiceTime() { return serviceTime; }
    
    public int getNumberServed() { return numberServed; }
    
    public String toString() { 
        return "Multiple Server Queue\n\tNumber Servers:\t" + getNumberServers() +
            "\n\tService Time Distribution:\t" + getServiceTime();
    }
    
    public String paramString() { return toString(); }
}
