package simkit.test;

import simkit.*;
import simkit.util.*;

/**
 *
 * @author  ahbuss
 */
public class TestSimEntityClass extends SimEntityBase {
    
    public TestSimEntityClass() {
    }

    public void doRun() {
        waitDelay("This", 0.0);
        waitDelay("That", 0.0);
        waitDelay("TheOther", 0.0);
    }
    
    public void doThis() {
        waitDelay("This", 0.9);
    }
    
    public void doThat() {
        waitDelay("That", 1.1);
    }
    
    public void doTheOther() {
        waitDelay("TheOther", 1.3);
    }
    

    public static void main(String[] args) {
        SimEntity se = new TestSimEntityClass();
        simkit.SimEventFilter sef = 
//            new simkit.SimEventFilter(new String[] {"This", "TheOther"});
//        sef.setAllButTheseEvents(true);
        new simkit.SimEventFilter("That", true);
        System.out.println(sef);
        
        
//        sef.addFilteredEvent("This");
//        sef.addFilteredEvent("TheOther");
        MyListener l = new MyListener();
        l.addPropertyChangeListener(new SimplePropertyDumper());
        
        se.addSimEventListener(sef);
        sef.addSimEventListener(l);
        
        Schedule.setSingleStep(true);
        Schedule.reset();
        Schedule.startSimulation();
        
    }
    
}
