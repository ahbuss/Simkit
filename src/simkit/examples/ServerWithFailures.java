package simkit.examples;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;

public class ServerWithFailures extends MultipleServerQueue {

    private RandomVariate timeToFailure;
    private RandomVariate repairTime;

    protected int numberFailedMachines;

    public ServerWithFailures(RandomVariate iat, RandomVariate st,
            RandomVariate ft, RandomVariate rt) {
        super(1, iat, st);
        timeToFailure = RandomVariateFactory.getInstance(ft);
        repairTime = RandomVariateFactory.getInstance(rt);
    }

    public void reset() {
        super.reset();
        numberFailedMachines = 0;
    }

    public void doRun() {
        super.doRun();
        waitDelay("Failure", timeToFailure.generate());
    }

    public void doFailure() {
        firePropertyChange("numberFailedMachines", numberFailedMachines, ++numberFailedMachines);
        int oldNIQ = numberInQueue;
        numberInQueue += 1 - numberAvailableServers;
        firePropertyChange("numberInQueue", oldNIQ, numberInQueue);
        int oldNAS = numberAvailableServers;
        firePropertyChange("numberAvailableServers", oldNAS, numberAvailableServers);

        interrupt("EndService");

        waitDelay("EndRepair", repairTime.generate());
   }

    public void doEndRepair() {
        firePropertyChange("numberFailedMachines", numberFailedMachines, --numberFailedMachines);
        numberAvailableServers = 1;
        firePropertyChange("numberAvailableServers", 0, numberAvailableServers);
        waitDelay("Failure", repairTime.generate());
        if (numberInQueue > 0) {
            waitDelay("StartService", 0.0);
        }
    }

    public String paramString() {
        return super.paramString() + "\nFailure time distribution " + timeToFailure +
            "\nRepair time distribution " + repairTime;
    }

    public static String description() {
        return "Server With Failures";
    }
}