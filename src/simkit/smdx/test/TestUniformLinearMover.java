/*
 * TestUniformLinearMover.java
 *
 * Created on May 28, 2002, 4:41 PM
 */

package simkit.smdx.test;
import simkit.smdx.*;
import simkit.*;
import simkit.util.*;
import java.awt.geom.*;

/**
 *
 * @author  Arnold Buss
 */
public class TestUniformLinearMover {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        String[] name = new String[] {"Fred", "Barney"};
        Point2D[] initialLocation = new Point2D[] {
                new Point2D.Double(0.0, 0.0),
                new Point2D.Double(100.0, 100.0) };
        double[] maxSpeed = new double[] { 10.0, 15};
        Point2D[][] destination = 
            new Point2D[][] {
                new Point2D[] {new Point2D.Double(20.0, 30.0), 
                    new Point2D.Double(30.0, 50.0),
                    new Point2D.Double(50.0, 50.0)},
                new Point2D[] {
                    new Point2D.Double(80.0, 70.0),
                    new Point2D.Double(40.0, 70.0),
                    new Point2D.Double(40.0, 30.0),
                    new Point2D.Double(45.0, 50.0)}
                    };
        
        Mover[] mover = new Mover[2]; 
        for (int i = 0; i < mover.length; i++) {
            mover[i] = new UniformLinearMover(name[i], initialLocation[i], maxSpeed[i]);                
        }
        
        PathMoverManager[] pmm = new PathMoverManager[2];
        for (int i = 0; i < pmm.length; i++) {
            pmm[i] = new PathMoverManager(mover[i], destination[i]);
        }
        
        PropertyChangeFrame pcf = new PropertyChangeFrame();
        for (int i = 0; i < mover.length; i++ ){
            mover[i].addPropertyChangeListener("movementState", pcf);
        }
        pcf.setVisible(true);
        
        Schedule.reset();
        for (int i = 0; i < mover.length; i++ ) {
            pmm[i].start();
        }
        
        Schedule.setVerbose(true);
        Schedule.startSimulation();
        
    }
    
}
