package simkit.test;

import simkit.Schedule;
import simkit.SimEntityBase;
import simkit.util.SimplePropertyDumper;

/**
 *
 * @author ahbuss
 */
public class TestIgnoreOnDump extends SimEntityBase {

    
    public void doRun() {
        waitDelay("SeeMe", 0.0);
        waitDelay("DontSeeMe", 0.0);
    }
    
    public void doSeeMe() {
        firePropertyChange("seeMe", String.format("%.3f", Schedule.getSimTime()));
        waitDelay("SeeMe", 1.0);
    }
    
    public void doDontSeeMe() {
        firePropertyChange("dontSeeMe", String.format("%.3f", Schedule.getSimTime()));
        waitDelay("DontSeeMe", 1.2);
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TestIgnoreOnDump testIgnoreOnDump = new TestIgnoreOnDump();
        SimplePropertyDumper simplePropertyDumper =
                new SimplePropertyDumper();
        testIgnoreOnDump.addPropertyChangeListener(simplePropertyDumper);
        
        Schedule.setVerbose(true);
        Schedule.stopAtTime(10.0);
        
        System.out.println("All Events Dumped:");
        Schedule.reset();
        Schedule.startSimulation();
        
        Schedule.addIgnoreOnDump("DontSeeMe");
        Schedule.reset();
        Schedule.startSimulation();        
        
        
    }
    
}
