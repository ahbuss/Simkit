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
    
    public void reset() {
        super.reset();
        numberAvailableServers = numberServers;
        queue.clear();
    }
    
    public void doRun() {
        firePropertyChange("numberInQueue", queue.size());
        firePropertyChange("numberAvailableServers", numberAvailableServers);
    }
    
    public void doArrival(Customer customer) {
        customer.stampTime();
        queue.add(customer);
        firePropertyChange("numberInQueue", queue.size() - 1, queue.size());
        if (getNumberAvailableServers() > 0) {
            waitDelay("StartService", 0.0, 1.0);
        }
    }
    
    public void doStartService() {
        Customer customer = (Customer) queue.removeFirst();
        firePropertyChange("delayInQueue", customer.getTimeSinceStamp());
        firePropertyChange("numberInQueue", queue.size() + 1, queue.size());
        firePropertyChange("numberAvailableServers", numberAvailableServers,
            --numberAvailableServers);
        
        waitDelay("EndService", serviceTime.generate(), customer);
    }
    
    public void doEndService(Customer customer) {
        firePropertyChange("timeInSystem", customer.getTimeSinceStamp());
        firePropertyChange("numberAvailableServers", numberAvailableServers,
            ++numberAvailableServers);
        if (getNumberInQueue() > 0) {
            waitDelay("StartService", 0.0, -1.0);
        }
    }
    
    public void setServiceTime(RandomVariate st) { serviceTime = st; }
    
    public RandomVariate getServiceTime() { return serviceTime; }
    
    public void setNumberServers(int ns) { numberServers = ns; }
    
    public int getNumberServers() { return numberServers; }
    
    public int getNumberInQueue() { return queue.size(); }
    
    public List getQueue() { return new LinkedList(queue); }
    
    public int getNumberAvailableServers() { return numberAvailableServers; }
    
    public String toString() {
        return "Multiple Server Queue" +
            "\n\t" + getNumberServers() + " servers" +
            "\n\t" + getServiceTime() + " service time";
    }
    
}
