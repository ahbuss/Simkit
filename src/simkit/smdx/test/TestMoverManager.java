/*
 * TestMoverManager.java
 *
 * Created on July 29, 2002, 11:39 AM
 */

package simkit.smdx.test;
import simkit.smdx.*;
import simkit.*;
import simkit.random.*;
import java.awt.geom.*;
/**
 *
 * @author  Arnold Buss
 */
public class TestMoverManager {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        RandomNumber rng = RandomNumberFactory.getInstance(12345L);
        RandomVariate[] location = new RandomVariate[2];
        location[0] = RandomVariateFactory.getInstance("Uniform",
        new Object[] { new Double(0.0), new Double(100.0) }, rng);
        location[1] = RandomVariateFactory.getInstance("Uniform",   
            new Object[] { new Double(0.0), new Double(100.0) }, rng);
        
        Point2D start = new Point2D.Double(0.0, 0.0);            
        Mover mover = new UniformLinearMover("Fred", start, 20.0);
            
        MoverManager manager = new RandomLocationMoverManager(mover, location);
        manager.setStartOnReset(true);
        Schedule.setSingleStep(true);
        Schedule.reset();
        Schedule.startSimulation();
    }
    
}
