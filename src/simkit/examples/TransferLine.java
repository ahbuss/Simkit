package simkit.examples;

import simkit.SimEntityBase;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;

public class TransferLine extends SimEntityBase {

    private RandomVariate interarrivalTime;
    private RandomVariate[] serviceTime;
    private int[] numberServersAtStation;

    protected int[] numberInQueue;
    protected int[] numberAvailableServers;
    protected int numberArrivals;
    protected int numberServed;

    public TransferLine(RandomVariate iat, int[] ns, RandomVariate[] st) {
        if (ns.length != st.length) {
            throw new IllegalArgumentException("Different number of stations "+
                        "defined in # servers (" + ns.length + ") than "+
                        "service times (" + st.length + ")");
        }
        numberServersAtStation = (int[]) ns.clone();
        interarrivalTime = RandomVariateFactory.getInstance(iat);
        serviceTime = new RandomVariate[st.length];
        for (int i = 0; i < st.length; i++) {
            serviceTime[i] = RandomVariateFactory.getInstance(st[i]);
        }
    }

    public void reset() {
        super.reset();
        numberInQueue = new int[numberServersAtStation.length];
        numberAvailableServers = (int[]) numberServersAtStation;
        numberArrivals = 0;
        numberServed = 0;
    }

    public void doRun() {
        for (int i = 0; i < numberInQueue.length; i++) {
            fireIndexedPropertyChange(i, "numberInQueue", numberInQueue[i]);
            fireIndexedPropertyChange(i, "numberAvailableServers", numberInQueue[i]);
        }

        waitDelay("Arrival", interarrivalTime.generate());
    }

    public void doArrival() {
        firePropertyChange("numberArrivals", ++numberArrivals);
        waitDelay("Arrival", 0.0,  new Object[] { new Integer(0) } );
        waitDelay("Arrival", interarrivalTime.generate());
    }

    public void doArrival(int station) {
        fireIndexedPropertyChange(station, "numberInQueue", numberInQueue[station],
            ++numberInQueue[station]);
        if (numberAvailableServers[station] > 0 ) {
            waitDelay("StartService", 0.0, new Object[] { new Integer(station) } );
        }
    }

    public void doStartService(int station) {
        fireIndexedPropertyChange(station, "numberInQueue", numberInQueue[station],
            --numberInQueue[station]);
        fireIndexedPropertyChange(station, "numberAvailableServers", numberAvailableServers[station],
            --numberAvailableServers[station]);
        waitDelay("EndService", serviceTime[station].generate(), new Object[] { new Integer(station) });
    }

    public void doEndService(int station) {
        fireIndexedPropertyChange(station, "numberAvailableServers", numberAvailableServers[station],
            ++numberAvailableServers[station]);

        if (numberInQueue[station] > 0) {
            waitDelay("StartService", 0.0, new Object[] { new Integer(station) },  1.0);
        }

        if (station < numberServersAtStation.length - 1) {
            waitDelay("Arrival", 0.0, new Object[] { new Integer(station + 1) });
        }
    }

    public String paramString() {
        StringBuffer buf = new StringBuffer("Transfer Line with ");
        buf.append(numberServersAtStation.length);
        buf.append(" Stations\n");
        buf.append("Interarrival Times are ");
        buf.append(interarrivalTime);
        buf.append('\n');
        buf.append("Station\t# Mach\tService Time Distribution\n");
        for (int i =0; i < numberServersAtStation.length; i++) {
            buf.append(i);
            buf.append('\t');
            buf.append(numberServersAtStation[i]);
            buf.append('\t');
            buf.append(serviceTime[i]);
            buf.append('\n');
        }
        return buf.toString();
    }
} 