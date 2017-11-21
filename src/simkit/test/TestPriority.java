package simkit.test;

import simkit.Priority;
import simkit.Schedule;
import simkit.SimEntityBase;

/**
 * Verifies Priority objects working.
 * @version $Id$
 * @author ahbuss
 */
public class TestPriority extends SimEntityBase {
    
    public TestPriority() {
    }
    
    public void doRun() {
        waitDelay("FooDefault", 1.0);
        waitDelay("FooHigh", 1.0, Priority.HIGH);
        waitDelay("FooLow", 1.0, Priority.LOW);
        waitDelay("FooLower", 1.0, Priority.LOWER);
        waitDelay("FooHighest", 1.0, Priority.HIGHEST);
        waitDelay("FooLowest", 1.0, Priority.LOWEST);
        waitDelay("FooHumongous", 1.0, MorePriority.HUMONGOUS);
        waitDelay("FooMiniscule", 1.0, MorePriority.MINISCULE);
    }
    
    public static void main(String[] args) {
        new TestPriority();
        Schedule.setVerbose(true);
        Schedule.reset();
        Schedule.startSimulation();
    }
}