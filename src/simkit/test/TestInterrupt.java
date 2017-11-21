package simkit.test;

import simkit.Schedule;
import simkit.SimEntityBase;

/**
 *
 * @author ahbuss
 */
public class TestInterrupt extends SimEntityBase {

    protected int nextArg;

    public void doRun() {
        waitDelay("Ping1", 0.0, 0);
        waitDelay("Ping2", 0.0);
        waitDelay("Interrupt", 10.0);
    }

    public void doPing1(int i) {
        nextArg = i + 1;
        waitDelay("Ping1", 1.1, nextArg);
    }

    public void doPing2() {
        waitDelay("Ping2", 1.2);
    }

    public void doInterrupt() {
        interrupt("Ping1");
        interrupt("Ping2");
        waitDelay("Interrupt2", 4.0);
    }

    public void doInterrupt2() {
        interrupt("Ping1", nextArg);
    }

    /**
     * @return the nextArg
     */
    public int getNextArg() {
        return nextArg;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TestInterrupt testInterrupt = new TestInterrupt();
        Schedule.setVerbose(true);
        Schedule.reset();
        Schedule.startSimulation();
    }

}
