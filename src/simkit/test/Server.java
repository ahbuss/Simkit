package simkit.test;

import simkit.SimEntityBase;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;

public class Server extends SimEntityBase {

    private RandomVariate serviceTime;
    private int totalNumberServers;

    protected int numberAvailableServers;
    protected int numberInQueue;
    protected int numberServed;

    public Server(int numServ, String className, Object[] params, long seed) {
        serviceTime = RandomVariateFactory.getInstance(className, params, seed);
        totalNumberServers = numServ;
    }

    public Server(int numServ, RandomVariate rv) {
        serviceTime = RandomVariateFactory.getInstance(rv);
        totalNumberServers = numServ;
    }

    public void reset() {
        super.reset();
        numberAvailableServers = totalNumberServers;
        numberInQueue = 0;
        numberServed = 0;
    }

    public void doRun() {
        firePropertyChange("numberInQueue", numberInQueue);
        firePropertyChange("numberAvailableServers", numberAvailableServers);
    }

    public void doArrival() {
        firePropertyChange("numberInQueue", numberInQueue, ++numberInQueue);

        if (numberAvailableServers > 0) {
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

        if (numberInQueue > 0 ) {
            waitDelay("StartService", 0.0);
        }
    }

    public int getTotalNumberServers() { return totalNumberServers; }

    public int getNumberInQueue() { return numberInQueue; }

    public int getNumberAvailableServers() { return numberAvailableServers; }

    public int getNumberServed() { return numberServed; }

    public RandomVariate getServiceTime() { return serviceTime; }
    
    public void setServiceTime(RandomVariate st) {serviceTime = st;}

    public String paramString() {
        return "Multiple Server Queue with " + totalNumberServers + " servers" +
            "\n\nService time distribution is " + serviceTime;
    }
} 