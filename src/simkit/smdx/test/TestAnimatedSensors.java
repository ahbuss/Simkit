package simkit.smdx.test;

import java.awt.Color;
import java.awt.geom.Point2D;
import simkit.Schedule;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;
import simkit.smdx.CookieCutterMediator;
import simkit.smdx.CookieCutterSensor;
import simkit.smdx.Mover;
import simkit.smdx.MoverManager;
import simkit.smdx.RandomLocationMoverManager;
import simkit.smdx.Sensor;
import simkit.smdx.SensorTargetMediatorFactory;
import simkit.smdx.SensorTargetReferee;
import simkit.smdx.UniformLinearMover;
import simkit.smdx.animate.SandboxFrame;
import simkit.util.PropertyChangeFrame;

/**
 * 
 * @author ahbuss
 */
public class TestAnimatedSensors {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        SensorTargetMediatorFactory.addMediator(
                CookieCutterSensor.class,
                UniformLinearMover.class,
                CookieCutterMediator.class
        );

        Mover[] mover = new Mover[]{
            new UniformLinearMover("Fred", new Point2D.Double(0.0, 0.0), 30.0),
            new UniformLinearMover("Barney", new Point2D.Double(100.0, 50.0), 20.0),
            new UniformLinearMover("Wilma", new Point2D.Double(-100.0, 50.0), 40.0)
        };

        Sensor[] sensor = new Sensor[]{
            new CookieCutterSensor(70.0, mover[0]),
            new CookieCutterSensor(80.0, mover[1]),
            new CookieCutterSensor(60.0, mover[2])
        };

        RandomVariate[] rv = new RandomVariate[]{
            RandomVariateFactory.getInstance("Uniform", -150.0, 150.0),
            RandomVariateFactory.getInstance("Uniform", -150.0, 200.0)};
        rv[1].setRandomNumber(rv[0].getRandomNumber());

        MoverManager[] manager = new MoverManager[]{
            new RandomLocationMoverManager(mover[0], rv),
            new RandomLocationMoverManager(mover[1], rv),
            new RandomLocationMoverManager(mover[2], rv)
        };

        for (int i = 0; i < manager.length; ++i) {
            manager[i].setStartOnReset(true);
        }
        SensorTargetReferee ref = new SensorTargetReferee();
        for (int i = 0; i < mover.length; ++i) {
            ref.register(mover[i]);
        }
        for (int i = 0; i < sensor.length; ++i) {
            ref.register(sensor[i]);
        }

        System.out.println(ref);

        SandboxFrame frame = new SandboxFrame("Sensor Test 1.0");
        frame.setSize(700, 650);
        frame.getSandbox().setOrigin(new Point2D.Double(300, 350));
        frame.getSandbox().setDrawAxes(true);
        frame.getSandbox().setBackground(Color.WHITE);
        frame.getSandbox().setOpaque(true);

        for (int i = 0; i < mover.length; ++i) {
            frame.addMover(mover[i], Color.blue);
        }

        Color[] colors = new Color[]{
            Color.red, Color.green, Color.orange
        };

        for (int i = 0; i < sensor.length; ++i) {
            frame.addSensor(sensor[i], colors[i % colors.length]);
        }

        PropertyChangeFrame pcf = new PropertyChangeFrame();
        pcf.setLocation((int) (frame.getLocation().getX() + frame.getWidth()), (int) frame.getLocation().getY());

        for (int i = 0; i < sensor.length; ++i) {
            sensor[i].addPropertyChangeListener("detection", pcf);
            sensor[i].addPropertyChangeListener("undetection", pcf);
        }
        pcf.setVisible(true);

        Schedule.reset();
        frame.setVisible(true);
    }

}
