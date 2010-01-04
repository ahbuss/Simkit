package simkit.smd.test;

import java.awt.Color;
import java.awt.geom.Point2D;
import simkit.smd.BasicLinearMover;
import simkit.smd.CookieCutterMediator;
import simkit.smd.CookieCutterSensor;
import simkit.smd.Mover;
import simkit.smd.RandomMoverManager;
import simkit.smd.Sensor;
import simkit.smd.SensorMoverReferee;
import simkit.smd.animate.SandboxFrame;
import simkit.Schedule;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;
import simkit.smd.ConstantTimeMediator;
import simkit.smd.ConstantTimeSensor;
import simkit.util.PropertyChangeFrame;


/**
 * @version $Id: TestSandboxFrame.java 84 2009-11-30 23:01:35Z ahbuss $
 * @author  ahbuss
 */
public class TestSandboxFrame {
    
    public static void main(String[] args) {
        
        SensorMoverReferee ref = new SensorMoverReferee();
        ref.addMediator(CookieCutterSensor.class, BasicLinearMover.class,
                new CookieCutterMediator());
        ref.addMediator(ConstantTimeSensor.class, BasicLinearMover.class,
                new ConstantTimeMediator());
        
        Mover[] movers = new Mover[] {
            new BasicLinearMover("Fred", new Point2D.Double(200, 250), 30.0),
            new BasicLinearMover("Barney", new Point2D.Double(250, 250), 50.0)
        };
        
        for (Mover mover : movers) {
            mover.addSimEventListener(ref);
        }
        
        RandomVariate[] rv = new RandomVariate[] {
            RandomVariateFactory.getInstance("Uniform", new Object[] { new Double(0.0), new Double(500.0) }),
            RandomVariateFactory.getInstance("Uniform", new Object[] { new Double(0.0), new Double(400.0) })
        };
        rv[1].setRandomNumber(rv[0].getRandomNumber());
        
        RandomMoverManager[] rmm = new RandomMoverManager[movers.length];
        for (int i = 0; i < rmm.length; ++i) {
            rmm[i] = new RandomMoverManager(movers[i], rv, true);
        }
        
        Sensor[] sensors = new Sensor[movers.length];
        sensors[0] = new CookieCutterSensor(movers[0], 30.0);
        sensors[1] = new ConstantTimeSensor(movers[1], 40.0, 0.5);
        
        Schedule.reset();
        
        SandboxFrame frame = new SandboxFrame("Test");
        frame.getSandbox().setOrigin(new Point2D.Double(10.0, 500.0));
        frame.getSandbox().setDrawAxes(true);
        frame.setSize(700, 700);
        
        frame.addMover(movers[0], Color.blue);
        frame.addMover(movers[1], Color.red);
        
        for (int i = 0; i < sensors.length; ++i) {
            frame.addSensor(sensors[i], Color.GREEN);
            sensors[i].addSimEventListener(ref);
        }

        PropertyChangeFrame propertyChangeFrame = new PropertyChangeFrame();
        for (Sensor sensor: sensors) {
            sensor.addPropertyChangeListener("detection", propertyChangeFrame);
            sensor.addPropertyChangeListener("undetection", propertyChangeFrame);
        }
        propertyChangeFrame.setLocation((int) (frame.getLocation().getX() + frame.getWidth()),
                (int) frame.getLocation().getY());

        propertyChangeFrame.setVisible(true);
        
        System.out.println(ref);
        
        frame.setVisible(true);
        
    }
    
}
