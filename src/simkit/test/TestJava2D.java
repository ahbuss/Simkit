/*
 * TestJava2D.java
 *
 * Created on March 19, 2002, 7:42 PM
 */

package simkit.test;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import simkit.smdx.Math2D;
import simkit.util.GenericAction;
/**
 *
 * @author  Arnold Buss
 * @version $Id$
 */
public class TestJava2D extends JPanel{
    
    private Point2D[] point;
    private Line2D[] line;
    private Rectangle2D rect;
    private Shape[] shape;
    private Point2D[] intersect;
    private Shape[] circle;
    private QuadCurve2D[] curve;
    private JTextField mouseLocation;
    private double size;   // size of intersection dots and control points
    private Point2D sizePt;
    private boolean showControlPoints;
    
    private Point2D[] cubicIntersections;
    private boolean verbose;
    
    /** Creates new TestJava2D */
    public TestJava2D() {
        
        setBackground(Color.white);
        setOpaque(true);
        size = 9;
        sizePt = new Point2D.Double(size/2, size/2);
        shape = new Shape[2];
        shape[0] = new Ellipse2D.Double(100.0, 100.0, 300.0, 300.0);
        
        CubicCurve2D[] seg = getSegments((Ellipse2D) shape[0]);
        for (int i = 0; i < seg.length; i++) {
            print(cubicToString(seg[i]));
        }
        
        rect = shape[0].getBounds2D();
        shape[1] = new Arc2D.Double();
        point = new Point2D[] {
            new Point2D.Double(0.0, 300.0),
            new Point2D.Double(450.0, 100.0),
            new Point2D.Double(250.0, 400.0),
            new Point2D.Double(500.0, 300.0),
            new Point2D.Double(450.0, 600.0),
        };
        
        print(Math2D.add(point));
        
        line = new Line2D[point.length];
        for (int i = 0; i < line.length; i++) {
            int j = (i + 1) % line.length;
            line[i] = new Line2D.Double(point[i], point[j]);
        }
        
        Line2D[] face = new Line2D[4];
        face[0] = new Line2D.Double(rect.getX(), rect.getY(), rect.getX(), rect.getY() + rect.getHeight());
        face[1] = new Line2D.Double(rect.getX() , rect.getY() + rect.getHeight(), rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight());
        face[2] = new Line2D.Double(rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight(), rect.getX() + rect.getWidth(), rect.getY() );
        face[3] = new Line2D.Double(rect.getX() + rect.getWidth(), rect.getY(), rect.getX(), rect.getY());
        
        curve = new QuadCurve2D[] {
            new QuadCurve2D.Double(400.0, 100.0, 100.0, 300.0, 400.0, 500.0),
            new QuadCurve2D.Double(100.0, 100.0, 300.0, 300.0, 400.0, 100.0)
        };
        
        Ellipse2D ellipse = (Ellipse2D) shape[0];
        
        CubicCurve2D segment  = (CubicCurve2D) Math2D.getSegments(ellipse, null)[0];
        print("Cubic segment: " + cubicToString(segment));
        
        ArrayList<Shape> intersections = new ArrayList<Shape>();
        ArrayList<Point2D> cubicInts = new ArrayList<Point2D>();
        
        for (int j = 0; j < point.length; j++) {
            
            intersect = new Point2D[face.length];
            circle = new Shape[intersect.length];
            
            for (int i = 0; i < face.length; i++) {
                int outCode = rect.outcode(point[j]);
                print(point[j] + ": " + outCode);
                Point2D[] inter = Math2D.findIntersection(point[j], Math2D.subtract(point[(j+1) % point.length], point[j]), face[i]);
                if (inter.length > 0) {
                    intersect[i] = inter[0];
                }
                print(intersect[i]);
                if (intersect[i] != null) {
                    intersections.add(new Ellipse2D.Double(intersect[i].getX() - size/2, intersect[i].getY() - size/2, size, size));
                }
            }
            
            for (int i = 0; i < curve.length; i++) {
                Point2D[] inters = Math2D.findIntersection(point[j], Math2D.subtract(point[(j+1) % point.length], point[j]), curve[i]);
                if (inters == null) { continue; }
                for (int k = 0; k < inters.length; k++) {
                    intersections.add(new Ellipse2D.Double(inters[k].getX() - size/2, inters[k].getY() - size/2, size, size));
                }
            }
            for (int m = 0; m < seg.length; m++) {
                Point2D velocity = Math2D.subtract(point[(j+1) % point.length], point[j]);
                Point2D[] cubicIntersect = Math2D.findIntersection(point[j], velocity, seg[m]);
                for (int k = 0; k < cubicIntersect.length; k++) {
                    double t = Math2D.innerProduct(velocity, Math2D.subtract(cubicIntersect[k], point[j]))/Math2D.normSq(velocity);
                    if (t >= 0.0 && t <= 1.0) {
                        cubicInts.add(cubicIntersect[k]);
                    }
                }
            }
        }
        circle = (Shape[]) intersections.toArray(new Shape[0]);
        cubicIntersections = cubicInts.toArray(new Point2D[cubicInts.size()]);
        
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        for (int i = 0; i < shape.length; i++) {
            g2d.setColor(Color.blue);
            g2d.draw(shape[i]);
            g2d.setColor(Color.red);
            g2d.draw(shape[i].getBounds2D());
            if (showControlPoints) {
                if (shape[i] instanceof Ellipse2D) {
                    Ellipse2D ellipse = (Ellipse2D) shape[i];
                    CubicCurve2D[] seg = getSegments(ellipse);
                    for (int j = 0; j < seg.length; j++) {
                        Point2D c1 = seg[j].getCtrlP1();
                        Point2D c2 = seg[j].getCtrlP2();
                        g2d.setColor(Color.black);
                        g2d.draw(new Rectangle2D.Double(c1.getX() - size/2, c1.getY() - size/2, size, size));
                        g2d.draw(new Rectangle2D.Double(c2.getX() - size/2, c2.getY() - size/2, size, size));
                        g2d.setColor(Color.lightGray);
                        Point2D p1 = seg[j].getP1();
                        Point2D p2 = seg[j].getP2();
                        g2d.draw(new Line2D.Double(c1.getX(), c1.getY(), p1.getX(), p1.getY()));
                        g2d.draw(new Line2D.Double(c2.getX(), c2.getY(), p2.getX(), p2.getY()));
                    }
                }
            }
        }
        
        for (int i = 0; i < point.length; i++) {
            if (shape[0].getBounds2D().intersectsLine(line[i])) {
                g2d.setColor(Color.green);
            }
            else {
                g2d.setColor(Color.orange);
            }
            g2d.draw(line[i]);
            Point2D[] intersect = Math2D.findIntersection(point[i], Math2D.subtract(point[(i + 1) % point.length], point[i]), (Ellipse2D) shape[0]);
            g2d.setColor(Color.blue);
            for (int k = 0; k < intersect.length; k++) {
                if (point[i].distance(intersect[k]) <= point[i].distance(point[ (i + 1) % point.length])) {
                    g2d.fill(new Ellipse2D.Double(intersect[k].getX() - size/2, intersect[k].getY() - size/2, size, size));
                }
            }
            
            g2d.setColor(Color.red);
            for (int k = 0; k < cubicIntersections.length; k++) {
                g2d.draw(new Rectangle2D.Double(cubicIntersections[k].getX() - size/2,
                cubicIntersections[k].getY() - size/2, size, size));
            }
        }
        
        for (int i = 0; i < curve.length; i++) {
            g2d.setColor(Color.magenta);
            g2d.draw(curve[i]);
            if (showControlPoints) {
                g2d.setColor(Color.black);
                g2d.draw(new Rectangle2D.Double(curve[i].getCtrlX() - size/2,
                curve[i].getCtrlY() - size/2, size, size));
                g2d.setColor(Color.lightGray);
                g2d.draw(new Line2D.Double(curve[i].getCtrlPt(), curve[i].getP1()));
                g2d.draw(new Line2D.Double(curve[i].getCtrlPt(), curve[i].getP2()));
            }
        }
        
        g2d.setColor(Color.black);
        for (int i = 0; i < circle.length; i++) {
            if (circle[i] == null) { continue; }
            g2d.fill(circle[i]);
        }
    }
    
    public void toggleControlPoints() {
        showControlPoints = !showControlPoints;
        revalidate();
        repaint();
    }
    
    public static String cubicToString(CubicCurve2D curve) {
        StringBuffer buf = new StringBuffer();
        buf.append(curve.getP1());
        buf.append(' ');
        buf.append(curve.getCtrlP1());
        buf.append(' ');
        buf.append(curve.getCtrlP2());
        buf.append(' ');
        buf.append(curve.getP2());
        return buf.toString();
    }
    
    public static CubicCurve2D[] getSegments(Ellipse2D ellipse) {
        CubicCurve2D[] segments = new CubicCurve2D[4];
        Point2D lastPoint = null;
        double[] coord = new double[6];
        PathIterator iter = ellipse.getPathIterator(null);
        iter.currentSegment(coord);
        iter.next();
        lastPoint = new Point2D.Double(coord[0], coord[1]);
        for (int i = 0; i < 4; i++) {
            iter.currentSegment(coord);
            segments[i] = new CubicCurve2D.Double(lastPoint.getX(), lastPoint.getY(),
            coord[0], coord[1], coord[2], coord[3], coord[4], coord[5]);
            lastPoint = new Point2D.Double(coord[4], coord[5]);
            iter.next();
        }
        return segments;
    }
    
    public void setVerbose(boolean v) { verbose = v; }
    
    public boolean isVerbose() { return verbose; }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        
        JFrame frame = new JFrame("Testing");
        JPanel panel = new TestJava2D();
        frame.getContentPane().add(new JScrollPane(panel), BorderLayout.CENTER);
        
        MouseDisplay md = new MouseDisplay();
        panel.addMouseMotionListener(md);
        frame.getContentPane().add(md, BorderLayout.SOUTH);
        
        JToolBar toolBar = new JToolBar();
        Action action = new GenericAction(panel, "toggleControlPoints");
        action.putValue(Action.NAME, "Show/Hide Control Points");
        toolBar.add(action);
        frame.getContentPane().add(toolBar, BorderLayout.NORTH);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(550, 700);
        frame.setVisible(true);
    }
    
    public void print(Object msg) {
        if (isVerbose()) {
            System.out.println(msg);
        }
    }
    
}
