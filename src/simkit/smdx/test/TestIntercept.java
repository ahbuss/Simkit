package simkit.smdx.test;
import simkit.*;
import simkit.smdx.*;
import java.awt.geom.*;
import java.util.*;
/**
 *
 * @author  Arnold Buss
 */
public class TestIntercept extends SimEntityBase {
    
    public void doRun() {
        Mover target = new UniformLinearMover("Fred", new Point2D.Double(30.0, 50.0), 25.0);
        target.moveTo(new Point2D.Double(200.0 + 30.0, 100.0 + 50.0), Math2D.norm(new Point2D.Double(20.0, 10.0)));
        
        Mover pursuer = new UniformLinearMover("Barney", new Point2D.Double(-20.0, 40.0), 40.0);
        Point2D intercept = Math2D.getIntercept(pursuer, target);
        if (intercept != null) {
            pursuer.moveTo(intercept);
        }
    }
    
    public static void main(String[] args) {
        new TestIntercept();
        Schedule.setVerbose(true);
        Schedule.reset();
        Schedule.startSimulation();
    }
    
}
