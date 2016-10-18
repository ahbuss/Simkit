package simkit.test;

import simkit.Schedule;
import simkit.examples.ArrivalProcess;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;
import simkit.stat.SimpleStatsTimeVarying;
import simkit.util.Resetter;
/**
 * @version $Id$
 * @author  ahbuss
 */
public class TestResetter {
    
    public void myResetMethod() {
        System.out.println("In myResetMethod()!");
    }
    
    public void reset() {
        System.out.println("In reset()!");
    }
    
    public static void main(String[] args) {
        RandomVariate iat = RandomVariateFactory.getInstance(
        "Uniform", new Object[] { new Double(1.2), new Double(3.4) }
        );
        ArrivalProcess arrival = new ArrivalProcess(iat);
        
        SimpleStatsTimeVarying sstv = new SimpleStatsTimeVarying("numberArrivals");
        arrival.addPropertyChangeListener(sstv);
        
        Resetter resetter = new Resetter();
        resetter.addResetter(sstv);
        
        Object me = new TestResetter();
        resetter.addResetter(me, "myResetMethod");
        
        System.out.println(resetter.getResetters());
        
        Schedule.stopAtTime(10000.0);
        
        for (int i = 0; i < 3; ++i) {
            System.out.println("Starting iteration " + i);
            Schedule.reset();
            Schedule.startSimulation();
            System.out.println("\tSimulation ended at time " + Schedule.getSimTime());
            System.out.println("\tArrivalProcess reports " + arrival.getNumberArrivals() +
            " arrivals");
            System.out.println(sstv);
        }
        
        resetter.removeResetter(me);
        System.out.println(resetter.getResetters());

        Schedule.reset();
        Schedule.startSimulation();
        System.out.println("\tSimulation ended at time " + Schedule.getSimTime());
        System.out.println("\tArrivalProcess reports " + arrival.getNumberArrivals() +
        " arrivals");
        System.out.println(sstv);
    }
    
}
