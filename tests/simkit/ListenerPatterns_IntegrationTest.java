package simkit;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import junit.framework.TestCase;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;
import simkit.stat.PercentageInStateStat;
import simkit.stat.SimpleStatsTally;
import simkit.stat.SimpleStatsTimeVarying;

/**
 * This is an integration test case that verifies the operation
 * of the two main listener patterns in simkit.  Namely the SimEventListener
 * and the PropertyChangeListener.
 * 
 * Adapted from the example simkit.examples.TestStateStats
 *
 * @author Kirk Stork (The MOVES Instutute)
 */
public class ListenerPatterns_IntegrationTest extends TestCase {

    public class ArrivalProcess extends SimEntityBase {

        private RandomVariate interArrivalTime;
        protected int numberArrivals;

        public ArrivalProcess(RandomVariate iat) {
            this.setInterArrivalTime(iat);
        }

        public void reset() {
            super.reset();
            numberArrivals = 0;
        }

        public void doRun() {
            firePropertyChange("numberArrivals", numberArrivals);
            waitDelay("Arrival", interArrivalTime.generate());
        }

        public void doArrival() {
            firePropertyChange("numberArrivals", numberArrivals, ++numberArrivals);
            waitDelay("Arrival", interArrivalTime.generate());
        }

        public void doStopArrivals() {
            interrupt("Arrival");
        }

        public int getNumberArrivals() {
            return numberArrivals;
        }

        public void setInterArrivalTime(RandomVariate iat) {
            interArrivalTime = RandomVariateFactory.getInstance(iat);
        }

        public RandomVariate getInterArrivalTime() {
            return RandomVariateFactory.getInstance(interArrivalTime);
        }

        public String paramString() {
            return toString();
        }
    }

    public class EntityCreator extends SimEntityBase {

        public void doArrival() {
            waitDelay("Arrival", 0.0, new Entity("Customer"));
        }
    }

    public class EntityServer extends SimEntityBase {

        private int numberServers;
        private RandomVariate serviceTime;
        protected LinkedList<Entity> queue;
        protected int numberAvailableServers;

        public EntityServer(int numberServers, RandomVariate serviceTime) {
            queue = new LinkedList<Entity>();
            setNumberServers(numberServers);
            setServiceTime(serviceTime);
        }

        public void reset() {
            super.reset();
            numberAvailableServers = numberServers;
            queue.clear();
        }

        public void doRun() {
            firePropertyChange("queue", getQueue());
            firePropertyChange("numberInQueue", getQueue().size());
            firePropertyChange("numberAvailableServers", numberAvailableServers);
        }

        public void doArrival(Entity customer) {
            customer.stampTime();
            List<Entity> oldQueue = getQueue();
            queue.add(customer);
            firePropertyChange("queue", oldQueue, getQueue());
            firePropertyChange("numberInQueue", oldQueue.size(), getQueue().size());

            if (getNumberAvailableServers() > 0) {
                waitDelay("StartService", 0.0, Priority.HIGH);
            }
        }

        public void doStartService() {
            List<Entity> oldQueue = getQueue();
            Entity customer = queue.removeFirst();
            firePropertyChange("queue", oldQueue, getQueue());
            firePropertyChange("numberInQueue", oldQueue.size(), getQueue().size());

            firePropertyChange("delayInQueue", customer.getElapsedTime());

            int OldNumberAvailableServers = getNumberAvailableServers();
            numberAvailableServers -= 1;
            firePropertyChange("numberAvailableServers", OldNumberAvailableServers,
                    getNumberAvailableServers());

            waitDelay("EndService", serviceTime.generate(), customer);
        }

        public void doEndService(Entity customer) {
            firePropertyChange("timeInSystem", customer.getElapsedTime());

            int OldNumberAvailableServers = getNumberAvailableServers();
            numberAvailableServers += 1;
            firePropertyChange("numberAvailableServers", OldNumberAvailableServers,
                    getNumberAvailableServers());

            if (getNumberInQueue() > 0) {
                waitDelay("StartService", 0.0, Priority.HIGH);
            }
        }

        public void setServiceTime(RandomVariate st) {
            serviceTime = st;
        }

        public RandomVariate getServiceTime() {
            return serviceTime;
        }

        public void setNumberServers(int ns) {
            numberServers = ns;
        }

        public int getNumberServers() {
            return numberServers;
        }

        public int getNumberInQueue() {
            return queue.size();
        }

        public LinkedList<Entity> getQueue() {
            return new LinkedList<Entity>(queue);
        }

        public int getNumberAvailableServers() {
            return numberAvailableServers;
        }
    }

    public ListenerPatterns_IntegrationTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /*
    This is the reference output of the original example program, it
    is assumed to be correct.
    
    simkit.examples.ArrivalProcess.1
    interArrivalTime = Exponential (1.7)
    simkit.examples.EntityServer.3
    numberServers = 2
    serviceTime = Gamma (2.5, 1.2)
    
    Simulation ended at	1000.000
    Number arrivals: 	613
    Number delays:   	613
    Number served:   	611
    Avg # in queue: 	9.589
    Avg delay in queue: 	15.643
    Avg time in system: 	18.762
    Avg Utilization:	0.939
    State	% In State
    0		0.910
    1		0.059
    2		0.031
    Total	1.000
    
     */
    public void testMulitpleQueueSystem() {
        
        RandomVariate[] rv = new RandomVariate[2];
        rv[0] = RandomVariateFactory.getInstance("Exponential", 1.7);
        rv[1] = RandomVariateFactory.getInstance("Gamma", 2.5, 1.2);
        
        rv[0].getRandomNumber().resetSeed();
        rv[1].getRandomNumber().resetSeed();
        int numberServers = 2;
        
        ArrivalProcess arrival = new ArrivalProcess(rv[0]);
        EntityCreator creator = new EntityCreator();
        EntityServer server = new EntityServer(numberServers, rv[1]);
        arrival.addSimEventListener(creator);
        creator.addSimEventListener(server);
        
        PercentageInStateStat percentageInStateStat = 
                new PercentageInStateStat(
                "numberAvailableServers", numberServers);
        server.addPropertyChangeListener(percentageInStateStat);

        SimpleStatsTimeVarying niqStat = new SimpleStatsTimeVarying(
                "numberInQueue");
        SimpleStatsTimeVarying nasStat = new SimpleStatsTimeVarying(
                "numberAvailableServers");
        SimpleStatsTally diqStat = new SimpleStatsTally(
                "delayInQueue");
        SimpleStatsTally tisStat = new SimpleStatsTally(
                "timeInSystem");

        int eventListID = 0;
        arrival.setEventListID(eventListID);
        creator.setEventListID(eventListID);
        server.setEventListID(eventListID);
        
        niqStat.setEventListID(eventListID);
        nasStat.setEventListID(eventListID);
        
        server.addPropertyChangeListener(niqStat);
        server.addPropertyChangeListener(nasStat);
        server.addPropertyChangeListener(diqStat);
        server.addPropertyChangeListener(tisStat);
        
//        server.addPropertyChangeListener(new SimplePropertyDumper());
        
        System.out.println(arrival);
        System.out.println(server);
        
        Schedule.getEventList(eventListID).stopAtTime(1000.0);
        
        Schedule.getEventList(eventListID).setVerbose(false);
        
        Schedule.getEventList(eventListID).reset();
        Schedule.getEventList(eventListID).startSimulation();
        
        DecimalFormat form = new DecimalFormat("0.000");

//        System.out.println();
//        System.out.println("Simulation ended at\t" + form.format(Schedule.getEventList(eventListID).getSimTime()));
//        System.out.println("Number arrivals: \t" + arrival.getNumberArrivals());
//        System.out.println("Number delays:   \t" + diqStat.getCount());
//        System.out.println("Number served:   \t" + tisStat.getCount());
//        
//        System.out.println("Avg # in queue: \t" + form.format(niqStat.getMean()));
//        System.out.println("Avg delay in queue: \t" + form.format(diqStat.getMean()));
//        System.out.println("Avg time in system: \t" + form.format(tisStat.getMean()));
//        
//        System.out.println("Avg Utilization:\t" + form.format(1.0 - 
//            nasStat.getMean() / server.getNumberServers()));
//        
//        System.out.println(percentageInStateStat.stateString());

        assertEquals(1000.0, Schedule.getEventList(eventListID).getSimTime());
        assertEquals(613, arrival.getNumberArrivals());
        assertEquals(613, diqStat.getCount());
        assertEquals(611, tisStat.getCount());
        assertEquals(9.589, niqStat.getMean(), 0.0009);
        assertEquals(15.643, diqStat.getMean(), 0.0009);
        assertEquals(18.762, tisStat.getMean(), 0.0009);
        assertEquals(0.939, 1.0 - nasStat.getMean() / server.getNumberServers(), 0.0009);
        assertEquals(0.910, percentageInStateStat.getPercentageFor(new Integer(0)), 0.009);
        assertEquals(0.059, percentageInStateStat.getPercentageFor(new Integer(1)), 0.009);
        assertEquals(0.031, percentageInStateStat.getPercentageFor(new Integer(2)), 0.009);
    }
}
