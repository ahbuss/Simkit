package simkit.test;

import simkit.Schedule;
import simkit.SimEntityBase;
import simkit.SimEntityBaseRetainedProperties;

/**
 * @version $Id$
 * @author ahbuss
 */
public class TestSimEntityBaseRetainedProperties extends SimEntityBaseRetainedProperties{
    
    /**
     * Creates a new instance of TestSimEntityBaseRetainedProperties
     */
    public TestSimEntityBaseRetainedProperties() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SimEntityBaseRetainedProperties simEntity = new TestSimEntityBaseRetainedProperties();
        simEntity.setProperty("pi", 3.14159);
        simEntity.setProperty("lifeTheUniverseAndEverything", 42);
        
        System.out.println("Properties set: " + simEntity);
        
        Schedule.reset();
        System.out.println("After Schedule.reset(): " +  simEntity);
        
        simEntity.setRetainAddedProperties(false);
        
        Schedule.reset();
        System.out.println("After turning off retaining Saved Properties and " +
                " invoking Schedule.reset(): " +  simEntity);
        
    }
    
}
