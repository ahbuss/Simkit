package simkit.test;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import simkit.Schedule;
import simkit.animate.SandboxFrame;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;
import simkit.smd.BasicLinearMover;
import simkit.smd.PathMoverManager;
import simkit.smd.PatrolMoverManager;
import simkit.smd.RandomMoverManager;
import simkit.smd.WayPoint;
import simkit.util.SimplePropertyDumper;

/**
 * 
 * @author ahbuss
 */
public class TestMovers1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BasicLinearMover boris = new BasicLinearMover("Boris",
                new Point2D.Double(100.0, 200.0), 50.0);

        LinkedList<WayPoint> path = new LinkedList<WayPoint>();
        path.add(new WayPoint(new Point2D.Double(-50.0, 50.0)));
        path.add(new WayPoint(new Point2D.Double(75.0, 100.0)));
        path.add(new WayPoint(new Point2D.Double(10.0, 300.0)));

        PathMoverManager borisPathMoverManager = new PathMoverManager(boris, path, true);

        BasicLinearMover natasha = new BasicLinearMover("Natasha", new Point2D.Double(50.0, 75.0), 40.0);

        BasicLinearMover rocky = new BasicLinearMover("Rocky", new Point2D.Double(0.0, 0.0), 60.0);

        RandomVariate[] rv = new RandomVariate[2];
        rv[0] = RandomVariateFactory.getInstance("Uniform", -100.0, 200.0);
        rv[1] = RandomVariateFactory.getInstance("Uniform", 0.0, 300.0);

        RandomMoverManager rockyMoverManager = new RandomMoverManager(rocky, rv, true);

        path.clear();
        path.add(new WayPoint(new Point2D.Double(50.0, 150.0)));
        path.add(new WayPoint(new Point2D.Double(50.0, -100.0)));
        path.add(new WayPoint(new Point2D.Double(-50.0, -100.0)));
        path.add(new WayPoint(new Point2D.Double(-50.0, 150.0)));

        PatrolMoverManager natashaMoverManager = new PatrolMoverManager(natasha, path, true);

        SimplePropertyDumper simplePropertyDumper = new SimplePropertyDumper(true);
//        boris.addPropertyChangeListener(simplePropertyDumper);
//        borisPathMoverManager.addPropertyChangeListener(simplePropertyDumper);

        System.out.println(boris);
        System.out.println(borisPathMoverManager);

        Schedule.addIgnoreOnDump("Ping");;

//        Schedule.setVerbose(true);
        Schedule.reset();
//        Schedule.startSimulation();

        SandboxFrame frame = new SandboxFrame("Boris Test");
        frame.setSize(500, 700);
        frame.getSandbox().setOrigin(new Point2D.Double(100.0, 400.0));
        frame.getSandbox().setDrawAxes(true);
        frame.getSandbox().setBackground(Color.WHITE);
        frame.getSandbox().setOpaque(true);

        frame.addMover(boris, Color.BLUE);
        frame.addMover(natasha, Color.RED);
        frame.addMover(rocky, Color.ORANGE);
//        File file = new File("img/images.jpg");
//        ImageIcon imageIcon = new ImageIcon(file.getAbsolutePath());
//        MoverIcon rockyIcon = new MoverIcon(rocky, imageIcon);
//        frame.getSandbox().add(rockyIcon);

        frame.setVisible(true);

    }
}
