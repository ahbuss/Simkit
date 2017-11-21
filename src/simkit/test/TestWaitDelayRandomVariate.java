package simkit.test;

import simkit.Priority;
import simkit.Schedule;
import simkit.SimEntityBase;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;

/**
 * Test of waitDelay(String, RandomVariate);
 * 
 * @version $Id$
 * @author abuss
 */
public class TestWaitDelayRandomVariate extends SimEntityBase {

    private RandomVariate timeToFoo;
    
    public TestWaitDelayRandomVariate(RandomVariate timeToFoo) {
        this.setTimeToFoo(timeToFoo);
    }
    
    public void doRun() {
        waitDelay("Foo", 0.0);
    }
    
    public void doFoo() {
        waitDelay("Bar", timeToFoo);
    }
    
    public void doBar() {
        waitDelay("Foo", timeToFoo, Priority.HIGH);
    }

    public RandomVariate getTimeToFoo() {
        return timeToFoo;
    }

    public void setTimeToFoo(RandomVariate timeToFoo) {
        this.timeToFoo = timeToFoo;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        RandomVariate timeToFoo = RandomVariateFactory.getInstance(
                "Beta", 1.2, 3.4);
        TestWaitDelayRandomVariate simEntity = new
                TestWaitDelayRandomVariate(timeToFoo);
        System.out.println(simEntity);
        Schedule.setVerbose(true);
        Schedule.stopAtTime(5.0);
        Schedule.reset();
        Schedule.startSimulation();
    }

}
