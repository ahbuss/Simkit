/*
 * Main.java
 *
 * Created on January 30, 2002, 6:03 PM
 */

package simkit.test;

import java.beans.PropertyChangeSupport;

import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;
import simkit.stat.Histogram;

/**
 *
 * @author  Arnold Buss
 * @version
 */
public class TestDiscreteVariate {
    
    /** Creates new Main */
    public TestDiscreteVariate() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        RandomVariate rv =
        RandomVariateFactory.getInstance(
        "simkit.random.DiscreteVariate",
        new Object[] { new double[] {0, 1}, new double[] { 20, 20}},
        12345L
        );
        
        System.out.println(rv);
        
        Histogram h = new Histogram("discrete", 1);
        PropertyChangeSupport pcs = new PropertyChangeSupport(rv);
        pcs.addPropertyChangeListener("discrete", h);
        for (int i = 0; i < 10; i++) {
            System.out.println(rv.generate());
        }
        
        for (int i = 0; i < 10000; i++) {
            pcs.firePropertyChange("discrete", null, new Double(rv.generate()));
        }
        
        System.out.println(h);
        
    }
    
}
