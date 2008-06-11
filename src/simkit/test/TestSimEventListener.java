/*
 * TestSimEventListener.java
 *
 * Created on July 13, 2002, 1:59 PM
 */

package simkit.test;
import java.beans.PropertyChangeListener;

import simkit.SimEventListener;
import simkit.examples.ArrivalProcess;
import simkit.examples.SimpleServer;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;
import simkit.stat.SimpleStatsTally;
/**
 * @version $Id: TestSimEventListener.java 1049 2008-02-08 16:44:09Z ahbuss $
 * @author  ahbuss
 */
public class TestSimEventListener {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        RandomVariate rv = RandomVariateFactory.getInstance("Exponential",
        new Object[] { new Double(1.0) });
        ArrivalProcess arrival = new ArrivalProcess(rv);
        
        for (int i = 0; i < 5; i++) {
            arrival.addSimEventListener(new SimpleServer(1, rv));
        }
        
        SimEventListener[] listeners = arrival.getSimEventListeners();
        System.out.println("Listeners:");
        for (int i = 0; i < listeners.length; i++) {
            System.out.println(listeners[i]);
        }
        
        simkit.util.Misc.removeAllSimEventListeners(arrival);
        listeners = arrival.getSimEventListeners();
        System.out.println("Listeners after simkit.util.Misc.removeAllSimEventListeners():");
        for (int i = 0; i < listeners.length; i++) {
            System.out.println(listeners[i]);
        }
        
        for (int i = 0; i < 7; i++) {
            arrival.addPropertyChangeListener(new SimpleStatsTally("number-" +i));
        }
        
        PropertyChangeListener[] pcl = arrival.getPropertyChangeListeners();
        System.out.println("PropertyChangeListeners:");
        for (int i = 0; i < pcl.length; i++) {
            System.out.println(pcl[i]);
        }
        
        simkit.util.Misc.removeAllPropertyChangeListeners(arrival);
        System.out.println("Listeners after simkit.util.Misc.removeAllSimEventListeners():");
        pcl = arrival.getPropertyChangeListeners();
        for (int i = 0; i < pcl.length; i++) {
            System.out.println(pcl[i]);
        }
        
    }
    
}
