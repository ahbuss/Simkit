/*
 * TestArcIntersection.java
 *
 * Created on April 5, 2002, 10:40 AM
 */

package simkit.smdx.test;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;

import simkit.test.*;
import simkit.smdx.*;
/**
 *
 * @author  Arnold Buss
 * @version
 */
public class TestArcIntersection extends JPanel {
    
    protected Shape[] arc;
    
    protected Line2D line[];
    protected ArrayList intersections;
    protected static final int SIZE = 9;
    
    /** Creates new TestArcIntersection */
    public TestArcIntersection() {
        setOpaque(true);
        setBackground(Color.white);
        arc = new Shape[] {
            new Arc2D.Double(50.0, 50.0, 300.0, 300.0, 30.0, 120.0, Arc2D.PIE),
            null};
        
        AffineTransform trans = AffineTransform.getTranslateInstance(200, 200);
        Shape transArc = trans.createTransformedShape(arc[0]);
        Area area = new Area(transArc);
        Area obstacle = new Area(new Rectangle2D.Double(360, 230, 100, 70));
        area.subtract(obstacle);
        arc[1] = new GeneralPath(area);
        intersections = new ArrayList();
        
        line = new Line2D[] {
            new Line2D.Double(80, 30, 300, 180),
            new Line2D.Double(70, 160, 300, 140),
            new Line2D.Double(40, 90, 225, 225),
            new Line2D.Double(175, 30, 50, 330),
            new Line2D.Double(275, 260, 530, 310)
        };
        for (int k = 0; k < arc.length; k++) {
            for (int i = 0; i < line.length; i++) {
                Point2D velocity = Math2D.subtract(line[i].getP2(), line[i].getP1());
                Point2D start = line[i].getP1();
                Point2D[] inter = Math2D.findIntersection(start, velocity, arc[k], null);
                for (int j = 0; j < inter.length; j++) {
                    double t = Math2D.innerProduct(velocity, Math2D.subtract(inter[j], start))/Math2D.normSq(velocity);
                    if (t >= 0.0 && t <= 1.0) {
                        intersections.add(inter[j]);
                    }
                }
            }
        }
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;
        Stroke stroke = g2d.getStroke();
        g2d.setColor(Color.blue);
        g2d.setStroke(new BasicStroke(2.0f));
        for (int i = 0; i < arc.length; i++) {
            g2d.draw(arc[i]);
        }
        
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
        TestArcIntersection taa = new TestArcIntersection();
        frame.getContentPane().add(taa);
        
        MouseDisplay md = new MouseDisplay();
        taa.addMouseMotionListener(md);
        frame.getContentPane().add(md, BorderLayout.SOUTH);
        
        frame.setSize(600, 500);
        frame.setVisible(true);
        
    }
}