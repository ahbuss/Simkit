package simkit.test;

import simkit.Schedule;
import simkit.SimEntityBase;
import simkit.util.SimplePropertyDumper;

/**
 *
 * @author ahbuss
 */
public class TestPostRep extends SimEntityBase {

    private double step;

    public void doRun() {
        waitDelay("Ping", 0.0);
    }

    public void doPing() {
        waitDelay("Ping", 1.0);
    }

    public void doPostReplication() {
        firePropertyChange("postReplication", true);
    }

    /**
     * @return the step
     */
    public double getStep() {
        return step;
    }

    /**
     * @param step the step to set
     */
    public void setStep(double step) {
        this.step = step;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TestPostRep testPostRep = new TestPostRep();
        testPostRep.setStep(1.3);

        SimplePropertyDumper simplePropertyDumper = new SimplePropertyDumper();
        testPostRep.addPropertyChangeListener(simplePropertyDumper);

//        Schedule.stopAtTime(10.0);
        Schedule.stopOnEvent(20, "Ping");

        Schedule.setVerbose(true);

        Schedule.reset();
        Schedule.startSimulation();
    }

}
