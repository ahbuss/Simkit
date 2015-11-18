package simkit.animate.test;

import java.awt.Color;
import java.awt.geom.Point2D;
import simkit.Schedule;
import simkit.animate.SandboxFrame;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;
import simkit.smd.BasicLinearMover;
import simkit.smd.CookieCutterSensor;
import simkit.smd.Mover;
import simkit.smd.RandomMoverManager;
import simkit.smd.Sensor;
import simkit.smd.SensorMoverReferee;


/**
 * @version $Id$
 * @author  ahbuss
 */
public class TestSandboxFrame {
        
    public static void main(String[] args) {
        
        SensorMoverReferee ref = new SensorMoverReferee();
        
        Mover[] mover = new Mover[] {
            new BasicLinearMover("Fred", new Point2D.Double(200, 250), 30.0),
            new BasicLinearMover("Barney", new Point2D.Double(250, 250), 50.0)
        };
        
        for (int i = 0; i < mover.length; ++i) {
            mover[i].addSimEventListener(ref);
        }
        
        RandomVariate[] rv = new RandomVariate[] {
            RandomVariateFactory.getInstance("Uniform", new Object[] { new Double(0.0), new Double(500.0) }),
            RandomVariateFactory.getInstance("Uniform", new Object[] { new Double(0.0), new Double(400.0) })
        };
        rv[1].setRandomNumber(rv[0].getRandomNumber());
        
        RandomMoverManager[] rmm = new RandomMoverManager[mover.length];
        for (int i = 0; i < rmm.length; ++i) {
            rmm[i] = new RandomMoverManager(mover[i], rv, true);
            rmm[i].setStartOnRun(true);
        }
        
        Sensor[] sensor = new Sensor[mover.length];
        sensor[0] = new CookieCutterSensor(mover[0], 30.0);
        sensor[1] = new CookieCutterSensor(mover[1], 40.0);
        
        Schedule.addIgnoreOnDump("Ping");
        Schedule.reset();
        
        SandboxFrame frame = new SandboxFrame("Test");
        frame.getSandbox().setOrigin(new Point2D.Double(10.0, 500.0));
        frame.getSandbox().setDrawAxes(true);
        frame.setSize(700, 700);
        
        frame.addMover(mover[0], Color.blue);
        frame.addMover(mover[1], Color.red);
        
        for (int i = 0; i < sensor.length; ++i) {
            frame.addSensor(sensor[i], Color.GREEN);
            sensor[i].addSimEventListener(ref);
        }
                
        frame.setVisible(true);
        
    }
    
}
