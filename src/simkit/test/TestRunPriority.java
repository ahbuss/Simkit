/*
 * TestRunPriority.java
 *
 * Created on January 11, 2002, 9:56 AM
 */

package simkit.test;
import simkit.*;
import simkit.util.*;

/**
 *
 * @author  ahbuss
 * @version 
 */
public class TestRunPriority {

    /** Creates new TestRunPriority */
    public TestRunPriority() {
    }

    /**
    * @param args the command line arguments
    */
    public static void main (String args[]) {
        
    }

}

class FirstSimEntity extends SimEntityBase {
    
    public FirstSimEntity() {
        this.setPriority(1.0);
    }
    
    public void doRun() {
        this.waitDelay("This", 0.0);
        
    }
    
    public void doThis(int i) {
        firePropertyChange("FirstProp", i);
    }
}

class SecondSimEntity extends SimEntityBase {
    public SecondSimEntity() {
        this.setPriority(2.0);
    }
    public void doRun() {
        this.waitDelay("That", 0.0, new Double(this.getPriority()));
    }
    
    public void doThat(int i) {
        firePropertyChange("SecondProp", i);
    }
}
