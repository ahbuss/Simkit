package simkit.examples;

import simkit.SimEntityBase;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;

/**
* Implements a transfer line with a user determined number of stations and a user
* determined number of servers at each station.
* @version $Id$
*/
public class TransferLine extends SimEntityBase {

/**
* The RandomVariate used to generate the interarrival times.
**/
    private RandomVariate interarrivalTime;

/**
* The RandomVariates used to generate the service times for servers at each
* station.
**/
    private RandomVariate[] serviceTime;

/**
* The number of servers at each station.
**/
    private int[] numberServersAtStation;

/**
* The length of the queue at each station.
**/
    protected int[] numberInQueue;

/**
* The number of servers that are not busy at each station.
**/
    protected int[] numberAvailableServers;

/**
* The total number of arrivals into the system.
**/
    protected int numberArrivals;

/**
* The total number served by the system. (The number served by the
* last station.)
**/
    protected int numberServed;

/**
* Constructs a new TransferLine. The number of stations is determined by the
* length of numberOfServers and serviceTime, which must be the same.
* Note: Copies are made of the RandomVariates.
* @param arrivalTime The RandomVariate used to generate interarrival times.
* @param numberOfServers The number of servers at each station.
* @param serviceTime The RandomVariates used to generate the service times at
* each station.
* @throws IllegalArgumentException If the two arrays are not the same length.
**/
    public TransferLine(RandomVariate arrivalTime, 
                        int[] numberOfServers, 
                        RandomVariate[] serviceTime) {
        if (numberOfServers.length != serviceTime.length) {
            throw new IllegalArgumentException("Different number of stations "+
                        "defined in # servers (" + numberOfServers.length + ") than "+
                        "service times (" + serviceTime.length + ")");
        }
        numberServersAtStation = (int[]) numberOfServers.clone();
        interarrivalTime = RandomVariateFactory.getInstance(arrivalTime);
        this.serviceTime = new RandomVariate[serviceTime.length];
        for (int i = 0; i < serviceTime.length; i++) {
            this.serviceTime[i] = RandomVariateFactory.getInstance(serviceTime[i]);
        }
        numberInQueue = new int[numberServersAtStation.length];
        numberAvailableServers = (int[]) numberServersAtStation;
        numberArrivals = 0;
        numberServed = 0;
    }

/**
* Resets the system to its initial state.
**/
    public void reset() {
        super.reset();
        numberInQueue = new int[numberServersAtStation.length];
        numberAvailableServers = (int[]) numberServersAtStation;
        numberArrivals = 0;
        numberServed = 0;
    }

/**
* Schedules first Arrival event. Fires property changes for numberInQueue and
* numberAvailableServers.
**/
    public void doRun() {
        for (int i = 0; i < numberInQueue.length; i++) {
            fireIndexedPropertyChange(i, "numberInQueue", numberInQueue[i]);
            fireIndexedPropertyChange(i, "numberAvailableServers", numberInQueue[i]);
        }

        waitDelay("Arrival", interarrivalTime.generate());
    }

/**
* An arrival into the system. Schedules the next arrival into the system. Schedules
* an Arrival for now to the first station. Fires a property change for numberArrivals.
**/
    public void doArrival() {
        firePropertyChange("numberArrivals", ++numberArrivals);
        waitDelay("Arrival", 0.0,  new Object[] { new Integer(0) } );
        waitDelay("Arrival", interarrivalTime.generate());
    }

/**
* An arrival at the given station. If a server is available at this station, schedules
* an StartService for now. Fires a property change for numberInQueue.
**/
    public void doArrival(int station) {
        fireIndexedPropertyChange(station, "numberInQueue", numberInQueue[station],
            ++numberInQueue[station]);
        if (numberAvailableServers[station] > 0 ) {
            waitDelay("StartService", 0.0, new Object[] { new Integer(station) } );
        }
    }

/**
* StartService at the given station. Schedules EndService at this station. Fires
* property changes for the numberInQueue and numberAvailableServers at this station.
**/
    public void doStartService(int station) {
        fireIndexedPropertyChange(station, "numberInQueue", numberInQueue[station],
            --numberInQueue[station]);
        fireIndexedPropertyChange(station, "numberAvailableServers", numberAvailableServers[station],
            --numberAvailableServers[station]);
        waitDelay("EndService", serviceTime[station].generate(), new Object[] { new Integer(station) });
    }

/**
* EndService event at the given station. If this station's queue is not empty, schedule
* an Arrival at this station for now. If this is not the last station, schedule an Arrival
* at the next station for now. Fire property change for numberAvailableServers for this
* station. If this is the last station, fire a property change for numberServed.
**/
    public void doEndService(int station) {
        fireIndexedPropertyChange(station, "numberAvailableServers", numberAvailableServers[station],
            ++numberAvailableServers[station]);

        if (numberInQueue[station] > 0) {
            waitDelay("StartService", 0.0, new Object[] { new Integer(station) },  1.0);
        }

        if (station < numberServersAtStation.length - 1) {
            waitDelay("Arrival", 0.0, new Object[] { new Integer(station + 1) });
        } else {
            firePropertyChange("numberServed", ++numberServed);
        }
    }

/**
* Returns the number of arrivals into the system.
**/
   public int getNumberArrivals() {
      return numberArrivals;
   }

/**
* Returns the number served by the system. (The number served by the last server.)
*/
   public int getNumberServed() {
      return numberServed;
   }

/**
* Returns a String with information about this TransferLine.
**/
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
