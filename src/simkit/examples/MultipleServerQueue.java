package simkit.examples;

import simkit.SimEntityBase;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;

public class MultipleServerQueue extends SimEntityBase {

    private int totalNumberServers;
    private RandomVariate interArrivalTime;
    private RandomVariate serviceTime;

    protected int numberArrivals;
    protected int numberInQueue;
    protected int numberAvailableServers;
    protected int numberServed;

    public MultipleServerQueue(int numberServers, RandomVariate iat, RandomVariate st) {
        totalNumberServers = numberServers;
        this.setInterArrivalTime(iat);
        this.setServiceTime(st);
    }

    public void reset() {
        super.reset();
        numberArrivals = 0;
        numberInQueue = 0;
        numberAvailableServers = totalNumberServers;
        numberServed = 0;
    }

    public void doRun() {
        firePropertyChange("numberInQueue", numberInQueue);
        firePropertyChange("numberAvailableServers", numberAvailableServers);
        waitDelay("Arrival", interArrivalTime.generate());
    }

    public void doArrival() {
        firePropertyChange("numberInQueue", numberInQueue, ++numberInQueue);
        waitDelay("Arrival", interArrivalTime.generate());
        if (numberAvailableServers > 0) {
            waitDelay("StartService", 0.0);
        }
    }

    public void doStartService() {
        firePropertyChange("numberAvailableServers", numberAvailableServers,
            --numberAvailableServers);
        firePropertyChange("numberInQueue", numberInQueue, --numberInQueue);
        waitDelay("EndService", serviceTime.generate());
    }

    public void doEndService() {
        firePropertyChange("numberAvailableServers", numberAvailableServers,
            ++numberAvailableServers);
        firePropertyChange("numberServed", ++numberServed);
        if (numberInQueue > 0) {
            waitDelay("StartService", 0.0);
        }
    }

/**
 *  @param iat Interarrival distribution (parameter)
**/
    public void setInterArrivalTime(RandomVariate iat) {
        interArrivalTime = RandomVariateFactory.getInstance(iat);
    }
/**
 *  @return Interarrival distribution (parameter)
**/
    public RandomVariate getInterArrivalTime() {
        return RandomVariateFactory.getInstance(interArrivalTime);
    }
/**
 *  @param iat Service time distribution (parameter)
**/
    public void setServiceTime(RandomVariate st) {
        serviceTime = RandomVariateFactory.getInstance(st);
    }
/**
 *  @return Service time distribution (parameter)
**/
    public RandomVariate getServiceTime() {
        return RandomVariateFactory.getInstance(serviceTime);
    }
/**
 *  @return Total number of servers (parameter)
**/
    public int getTotalNumberServers() { return totalNumberServers; }
/**
 * @return Number of arrivals (state variable)
**/
    public int getNumberArrivals() { return numberArrivals; }
/**
 * @return Number in Queue (state variable)
**/
    public int getNumberInQueue() { return numberInQueue; }
/**
 * @return Number of available servers (state variable)
**/
    public int getNumberAvailableServers() { return numberAvailableServers; }
/**
 * @return Number served (state variable)
**/
    public int getNumberServed() { return numberServed; }

    public String paramString() {
        return "Multiple Server Queue\n\nNumber of servers: " + getTotalNumberServers() +
                "\n\nInterarrival Times are " + getInterArrivalTime() +
                "\n\nService times are " + getServiceTime();
    }
/**
 *  @return A short description of this class
**/
    public static String description() {
        return "Model of Multiple Server Queue";
    }
}
