/*
 * TestNewMediator.java
 *
 * Created on March 6, 2002, 8:49 PM
 */

package simkit.test;

import simkit.smdx.*;
import java.awt.geom.*;
/**
 *
 * @author  Arnold Buss
 * @version 
 */
public class TestNewMediator {

    /** Creates new TestNewMediator */
    public TestNewMediator() {
    }

    /**
    * @param args the command line arguments
    */
    public static void main (String args[]) {
        MediatorFactory mediatorFactory = SensorTargetMediatorFactory.getInstance();
        mediatorFactory.addMediatorFor(simkit.smdx.CookieCutterSensor.class,
            simkit.smdx.UniformLinearMover.class, simkit.smdx.CookieCutterMediator.class);
        System.out.println(mediatorFactory.getMediators());
        
        Mover target = new UniformLinearMover();
        Sensor sensor = new CookieCutterSensor();
        
        Mediator mediator = mediatorFactory.getMediatorFor(sensor.getClass(), target.getClass());
        System.out.println(mediator);
        
        SensorTargetReferee referee = new SensorTargetReferee();
        referee.register(target);
        referee.register(sensor);
        System.out.println(referee);

        mediatorFactory.clear();
        System.out.println(mediatorFactory.getMediators());
        try {
            mediatorFactory.addMediatorFor("simkit.smdx.CookieCutterSensor",
                "simkit.smdx.UniformLinearMover", "simkit.smdx.CookieCutterMediator");
        }
        catch (ClassNotFoundException e) {System.err.println(e);}
        System.out.println(mediatorFactory.getMediators());
        
    }

}
