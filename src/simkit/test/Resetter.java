/*
 * Resetter.java
 *
 * Created on August 25, 2001, 12:57 PM
 */

package simkit.test;

import simkit.Schedule;
import simkit.SimEntityBase;
import simkit.examples.ArrivalProcess;
import simkit.random.RandomVariateFactory;
import simkit.stat.SimpleStatsTimeVarying;

/**
 *
 * @author  ahbuss
 * 
 */
public class Resetter extends SimEntityBase {
    
    private int state;
    private final double resetTime;
    
    /**
     * Creates new Resetter
     * @param resetTime Given reset time
     */
    public Resetter(double resetTime) {
        this.resetTime = resetTime;
    }
    
    @Override
    public void reset() {
        super.reset();
        state = 0;
    }
    
    public void doRun() {
        firePropertyChange("state", state);
        waitDelay("Reset", resetTime);
    }
    
    public void doArrival() {
        firePropertyChange("state", state, ++state);
    }
    
    public void doReset() {
        firePropertyChange("reset", "state");
    }
    
    public static void main(String[] args) {
        ArrivalProcess arrival = new ArrivalProcess(
            RandomVariateFactory.getInstance("simkit.random.ConstantVariate", 1.0));
        Resetter r = new Resetter(5.0);
        SimpleStatsTimeVarying sstv = new SimpleStatsTimeVarying("state");
        SimpleStatsTimeVarying sstv2 = new SimpleStatsTimeVarying("numberArrivals");
        
        arrival.addSimEventListener(r);
        r.addPropertyChangeListener(sstv);
        arrival.addPropertyChangeListener(sstv2);
        
        Schedule.setSingleStep(true);
        Schedule.stopAtTime(10.0);
        Schedule.reset();
        Schedule.startSimulation();
        
        System.out.println(sstv);
        System.out.println(sstv2);
    }
    
}
