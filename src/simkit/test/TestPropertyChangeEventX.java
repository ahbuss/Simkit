package simkit.test;

import simkit.Priority;
import simkit.Schedule;

/**
 *
 * @author ahbuss
 */
public class TestPropertyChangeEventX {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SimEntityWithExtraData simEntityWithExtraData = new SimEntityWithExtraData();
        SimplePropertyDumperX simplePropertyDumper = new SimplePropertyDumperX();
        simEntityWithExtraData.setExtraData(new Object[] { "Hi Mom!", 3, Priority.HIGH});
        simEntityWithExtraData.addPropertyChangeListener(simplePropertyDumper);
        
        System.out.println(simEntityWithExtraData);
        
        Schedule.setVerbose(true);
        Schedule.reset();
        Schedule.startSimulation();
    }
    
}
