/*
 * TestJava2D.java
 *
 * Created on March 19, 2002, 7:42 PM
 */

package simkit.test;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import simkit.smdx.*;
import simkit.util.*;
/**
 *
 * @author  Arnold Buss
 * @version
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
    private boolean showControlPoints;
    
    /** Creates new TestJava2D */
    public TestJava2D() {
        
        setBackground(Color.white);
        setOpaque(true);
        size = 8;
        shape = new Shape[2];
        shape[0] = new Ellipse2D.Double(100.0, 100.0, 300.0, 400.0);
        
        CubicCurve2D[] seg = getSegments((Ellipse2D) shape[0]);
        for (int i = 0; i < seg.length; i++) {
            System.out.println(cubicToString(seg[i]));
        }
        
        rect = shape[0].getBounds2D();
        shape[1] = new Arc2D.Double();
        point = new Point2D[] {
            new Point2D.Double(0.0, 300.0),
            new Point2D.Double(450.0, 100.0),
            new Point2D.Double(500.0, 300.0),
            new Point2D.Double(450.0, 600.0),
        };
        
        System.out.println(Math2D.add(point));
        
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
        
        ArrayList intersections = new ArrayList();
        
        for (int j = 0; j < point.length; j++) {
            
            intersect = new Point2D[face.length];
            circle = new Shape[intersect.length];
            
            for (int i = 0; i < face.length; i++) {
                int outCode = rect.outcode(point[j]);
                System.out.println(point[j] + ": " + outCode);
                intersect[i] = Math2D.findIntersection(point[j], Math2D.subtract(point[(j+1) % point.length], point[j]), face[i]);
                System.out.println(intersect[i]);
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
        }
        Ellipse2D ellipse = (Ellipse2D) shape[0];
        circle = (Shape[]) intersections.toArray(new Shape[0]);
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
        
        for (int i = 0; i < line.length; i++) {
            if (shape[0].getBounds2D().intersectsLine(line[i])) {
                g2d.setColor(Color.green);
            }
            else {
                g2d.setColor(Color.orange);
            }
            
            g2d.draw(line[i]);
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
    
}
