package simkit.examples;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;

/**
* An implementation of a single queue single-server processes with server
* failures. This Class models both the arrival process and the server process.
**/
public class ServerWithFailures extends MultipleServerQueue {
    
/**
* The RandomVariate used to generate the failure times.
*/
    private RandomVariate timeToFailure;

/**
* The RandomVariate used to generate the repair times.
**/
    private RandomVariate repairTime;
    
/**
* The number of servers that are currently failed.
**/
    protected int numberFailedMachines;
    
/**
* Creates a single server system with the given time distributions.
* Note: Copies are made of the RandomVariates.
* @param arrivalTime The RandomVariate used to generate the interarrival times.
* @param serviveTime The RandomVariate used to generate the service times.
* @param timeToFailure The RandomVariate used to generate the time to failure.
* @param repairTime The RandomVariate used to generate the time to repair.
**/
    public ServerWithFailures(RandomVariate arrivalTime,
                               RandomVariate serviceTime,
                               RandomVariate timeToFailure,
                               RandomVariate repairTime) {
        super(1, arrivalTime, serviceTime);
        this.timeToFailure = RandomVariateFactory.getInstance(timeToFailure);
        this.repairTime = RandomVariateFactory.getInstance(repairTime);
    }
    
/**
* Resets the system to its initial state.
**/
    public void reset() {
        super.reset();
        numberFailedMachines = 0;
    }
    
/**
* Schedules the first Arrival and first server Failure. Fires property changes
* for numberInQueue and numberAvailableServers.
**/
    public void doRun() {
        super.doRun();
        waitDelay("Failure", timeToFailure.generate());
    }
    
/**
* Schedules the EndRepair event and cancels the next EndService event. Fires property
* changes for numberFailedMachines, numberInQueue, numberAvailableServers. If
* the server is busy when it fails, pushes the object being served back to the queue.
**/
    public void doFailure() {
        firePropertyChange("numberFailedMachines", numberFailedMachines, ++numberFailedMachines);
        int oldNIQ = numberInQueue;
 //I think this next line limits us to a single server - JLR
        numberInQueue += 1 - numberAvailableServers;
        firePropertyChange("numberInQueue", oldNIQ, numberInQueue);
        int oldNAS = numberAvailableServers;
        firePropertyChange("numberAvailableServers", oldNAS, numberAvailableServers);
        
        interrupt("EndService");
        
        waitDelay("EndRepair", repairTime.generate());
    }
    
/**
* Schedules the next Failure event and if the queue is not empty, schedules StartService for
* now. Fires property changes for numberFailedMachines and numberAvailableServers.
**/
    public void doEndRepair() {
        firePropertyChange("numberFailedMachines", numberFailedMachines, --numberFailedMachines);
        numberAvailableServers = 1;
        firePropertyChange("numberAvailableServers", 0, numberAvailableServers);
        waitDelay("Failure", repairTime.generate());
        if (numberInQueue > 0) {
            waitDelay("StartService", 0.0);
        }
    }
    
/**
* Returns a String containing a brief description of this Class.
**/
    public static String description() {
        return "Server With Failures";
    }
}
