/*
 * TestArcIntersection.java
 *
 * Created on April 5, 2002, 10:40 AM
 */

package simkit.smdx.test;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.JPanel;

import simkit.smdx.Math2D;
import simkit.test.MouseDisplay;
/**
 * This test is also performed in the unit test suite, where it is verified
 * by numerical assertions rather than the visual/manual approach taken here.
 * We keep this class around as a debugging aid
 * in case the unit test starts failing. 
 * @see "simkit.smdx.Math2DTest in the tests"
 *
 * @author  Arnold Buss
 * 
 */
public class TestSwissCheeseIntersection extends JPanel {
    
    protected Area footprint;
    Shape compositeArea;
    protected Line2D line[];
    protected ArrayList<Point2D> intersections;
    protected static final int SIZE = 9;
    
    /** Creates new TestArcIntersection */
    public TestSwissCheeseIntersection() {
        setOpaque(true);
        setBackground(Color.white);

        // just a circle
        footprint = new Area(new Ellipse2D.Double(50.0, 50.0, 300.0, 300.0));
        // a tiny circle
        Area obstacle = new Area(new Ellipse2D.Double(180, 180, 40, 40));
        footprint.subtract(obstacle);
        compositeArea = new GeneralPath(footprint);

        intersections = new ArrayList<Point2D>();
        
        line = new Line2D[] {
            new Line2D.Double(50, 50, 350, 50),
            new Line2D.Double(50, 50, 50, 350),
            new Line2D.Double(350, 50, 350, 350),
            new Line2D.Double(50, 350, 350, 350),
            new Line2D.Double(40, 180, 360, 180),
            new Line2D.Double(40, 220, 360, 220),
            new Line2D.Double(180, 50, 180, 360),
            new Line2D.Double(220, 50, 220, 360),
        };
        for (int i = 0; i < line.length; i++) {
            Point2D velocity = Math2D.subtract(line[i].getP2(), line[i].getP1());
            Point2D start = line[i].getP1();
            Point2D[] inter = Math2D.findIntersection(start, velocity, compositeArea, null);
            for (int j = 0; j < inter.length; j++) {
                double t = Math2D.innerProduct(velocity, Math2D.subtract(inter[j], start)) / Math2D.normSq(velocity);
                if (t >= 0.0 && t <= 1.0) {
                    intersections.add(inter[j]);
                }
            }
            System.out.println(Arrays.toString(inter));
        }
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;
        Stroke stroke = g2d.getStroke();
        g2d.setColor(Color.blue);
        g2d.setStroke(new BasicStroke(2.0f));
        g2d.draw(compositeArea);
        
        g2d.setStroke(stroke);
        for (int i = 0; i < line.length; i++) {
            g2d.setColor(Color.red);
            g2d.draw(line[i]);
        }
        Rectangle2D start = new Rectangle2D.Double();
        Ellipse2D end = new Ellipse2D.Double();
        Point2D corner = new Point2D.Double(SIZE/2, SIZE/2);
        g2d.setColor(Color.black);
        for (int i = 0; i < line.length; i++) {
            start.setFrameFromCenter(line[i].getP1(), Math2D.subtract(line[i].getP1(), corner));
            g2d.fill(start);
            end.setFrameFromCenter(line[i].getP2(), Math2D.subtract(line[i].getP2(), corner));
            g2d.fill(end);
        }
        g2d.setColor(Color.blue);
        Point2D[] pts = (Point2D[]) intersections.toArray(new Point2D[intersections.size()]);
        for (int i = 0; i < pts.length; i++) {
            start.setFrameFromCenter(pts[i], Math2D.subtract(pts[i], corner));
            g2d.draw(start);
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        JFrame frame = new JFrame("Arc Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        TestSwissCheeseIntersection taa = new TestSwissCheeseIntersection();
        frame.getContentPane().add(taa);
        
        MouseDisplay md = new MouseDisplay();
        taa.addMouseMotionListener(md);
        frame.getContentPane().add(md, BorderLayout.SOUTH);
        
        frame.setSize(600, 500);
        frame.setVisible(true);
        
    }
}
