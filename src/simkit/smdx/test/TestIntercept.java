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
    
    private Mover pursuer;
    private Mover target;
    
    static {
        SensorTargetMediatorFactory.getInstance().addMediatorFor(
            simkit.smdx.CookieCutterSensor.class,
            simkit.smdx.UniformLinearMover.class,
            simkit.smdx.CookieCutterMediator.class
        );
    }
    
    public void doRun() {
        
        SensorTargetReferee ref = new SensorTargetReferee();
        
        target = new UniformLinearMover("Fred", new Point2D.Double(30.0, 50.0), 25.0);
        target.moveTo(new Point2D.Double(200.0 + 30.0, 100.0 + 50.0), Math2D.norm(new Point2D.Double(20.0, 10.0)));
        
        pursuer = new UniformLinearMover("Barney", new Point2D.Double(-20.0, 40.0), 40.0);
        Sensor sensor = new CookieCutterSensor(20.0, pursuer);
        ref.register(sensor);
        ref.register(target);
        
        Point2D intercept = Math2D.getIntercept(pursuer, pursuer.getMaxSpeed(), target);
        System.out.println("Intercept: " + intercept);
        System.out.println("Velocity: " + Math2D.getInterceptVelocity(pursuer, target));
        System.out.println("Enter Range at: " + Math2D.
        if (intercept != null) {
            pursuer.moveTo(intercept);
        }
    }
    
    public static void main(String[] args) {
        TestIntercept te = new TestIntercept();
        Schedule.setVerbose(true);
        Schedule.reset();
        Schedule.startSimulation();
        
        System.out.println(te.pursuer);
        System.out.println(te.target);
    }
    
}
