/*
 * TestBooleanSimpleStats.java
 *
 * Created on August 10, 2001, 8:37 AM
 */

package simkit.test;

import simkit.Schedule;
import simkit.SimEntityBase;
import simkit.stat.SimpleStatsTally;
import simkit.stat.SimpleStatsTimeVarying;
import simkit.random.RandomNumber;
import simkit.random.RandomNumberFactory;
/**
 *
 * @author  Arnold Buss
 * @version $Id$
 */
public class TestBooleanSimpleStats extends SimEntityBase {

    private RandomNumber rand;
    
    public TestBooleanSimpleStats() {
        rand = RandomNumberFactory.getInstance();
    }
    
    public void doRun() {
        waitDelay("Arrival", 0.0);
    }
    
    public void doArrival() {
        boolean b = (rand.draw() < 0.2);
        firePropertyChange("obs", new Boolean(b));
        waitDelay("Arrival", 1.0);
    }
    
    /**
    * @param args the command line arguments
    */
    public static void main (String args[]) {
        SimpleStatsTally tally1 = new SimpleStatsTally();
        SimpleStatsTally tally2 = new SimpleStatsTally();
        SimpleStatsTally tally3 = new SimpleStatsTally();
        
        RandomNumber rng = RandomNumberFactory.getInstance();
        for (int i = 0; i < 100; i++) {
            double x = rng.draw();
            boolean b = (x < 0.2);
            x = (b ? 1.0 : 0.0);
            Boolean bObj = new Boolean(b);
            tally1.newObservation(x);
            tally2.newObservation(b);
            tally3.newObservation(bObj);
        }
        System.out.println(tally1);
        System.out.println(tally2);
        System.out.println(tally3);
        
        TestBooleanSimpleStats tbss = new TestBooleanSimpleStats();
        SimpleStatsTally tally4 = new SimpleStatsTally("obs");
        SimpleStatsTimeVarying sstv = new SimpleStatsTimeVarying("obs");
        tbss.addPropertyChangeListener(tally4);
        tbss.addPropertyChangeListener(sstv);
        Schedule.reset();
        Schedule.stopAtTime(100.0);
        Schedule.startSimulation();
        System.out.println(tally4);
        System.out.println(sstv);
    }

}
