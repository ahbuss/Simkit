package simkit.smdx.test;

import java.awt.Color;
import java.awt.geom.Point2D;
import simkit.Schedule;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;
import simkit.smdx.CookieCutterSensor;
import simkit.smdx.Mover;
import simkit.smdx.RandomLocationMoverManager;
import simkit.smdx.Sensor;
import simkit.smdx.SensorTargetMediatorFactory;
import simkit.smdx.SensorTargetReferee;
import simkit.smdx.UniformLinearMover;
import simkit.smdx.animate.SandboxFrame;

/**
 * @version $Id$
 * @author  ahbuss
 */
public class TestSandboxFrame {
    
    static {
        SensorTargetMediatorFactory.getInstance().addMediatorFor(
            simkit.smdx.CookieCutterSensor.class,
            simkit.smdx.UniformLinearMover.class, 
            simkit.smdx.CookieCutterMediator.class
        );
    }
    
    public static void main(String[] args) {
        
        SensorTargetReferee ref = new SensorTargetReferee();
        
        Mover[] mover = new Mover[] {
            new UniformLinearMover("Fred", new Point2D.Double(200, 250), 30.0),
            new UniformLinearMover("Barney", new Point2D.Double(250, 250), 50.0)
        };
        
        for (int i = 0; i < mover.length; ++i) {
            ref.register(mover[i]);
        }
        
        RandomVariate[] rv = new RandomVariate[] {
            RandomVariateFactory.getInstance("Uniform", new Object[] { new Double(0.0), new Double(500.0) }),
            RandomVariateFactory.getInstance("Uniform", new Object[] { new Double(0.0), new Double(400.0) })
        };
        rv[1].setRandomNumber(rv[0].getRandomNumber());
        
        RandomLocationMoverManager[] rmm = new RandomLocationMoverManager[mover.length];
        for (int i = 0; i < rmm.length; ++i) {
            rmm[i] = new RandomLocationMoverManager(mover[i], rv);
            rmm[i].setStartOnReset(true);
        }
        
        Sensor[] sensor = new Sensor[mover.length];
        sensor[0] = new CookieCutterSensor(mover[0], 30.0);
        sensor[1] = new CookieCutterSensor(mover[1], 40.0);
        
        Schedule.reset();
        
        SandboxFrame frame = new SandboxFrame("Test");
        frame.getSandbox().setOrigin(new Point2D.Double(10.0, 500.0));
        frame.getSandbox().setDrawAxes(true);
        frame.setSize(700, 700);
        
        frame.getControlPanel().getVcrController().setDeltaT(0.1);
        frame.getControlPanel().getVcrController().setMillisPerSimtime(100L);
        
        frame.addMover(mover[0], Color.blue);
        frame.addMover(mover[1], Color.red);
        
        for (int i = 0; i < sensor.length; ++i) {
            frame.addSensor(sensor[i], Color.GREEN);
            ref.register(sensor[i]);
        }
        
        System.out.println(ref.paramString());
        
        frame.setVisible(true);
        
    }
    
}
