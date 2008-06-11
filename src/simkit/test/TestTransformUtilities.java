/*
 * Main.java
 *
 * Created on March 29, 2002, 8:47 AM
 */

package simkit.test;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;

import javax.swing.JFrame;
import javax.swing.JPanel;

import simkit.smdx.TransformUtilities;
/**
 *
 * @author  Arnold Buss
 * @version $Id: TestTransformUtilities.java 643 2004-04-25 22:59:33Z kastork $
 */
public class TestTransformUtilities extends JPanel{
    
    protected RectangularShape[] original;
    protected RectangularShape[] transformed;
    protected AffineTransform trans;
    
    /** Creates new Main */
    public TestTransformUtilities() {
        
        setBackground(Color.white);
        
        trans = AffineTransform.getTranslateInstance(50.0, 30.0);
        
        original = new RectangularShape[] {
            new Rectangle2D.Double(10, 20, 30, 40),
            new Ellipse2D.Double(10, 100, 30, 40),
            new Arc2D.Double(10, 170, 30, 30, 45, 90, Arc2D.PIE),
            null
        };
        original[3] = original[2].getBounds2D();
        original[3] = TransformUtilities.createTransformedShape(original[3],
        AffineTransform.getTranslateInstance(0.0, 60.0));
        
        transformed = new RectangularShape[original.length];
        for (int i = 0; i < original.length; i++) {
            transformed[i] = TransformUtilities.createTransformedShape(original[i], trans);
        }
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        for (int i = 0; i < original.length; i++) {
            g2d.setColor(Color.blue);
            g2d.fill(original[i]);
            g2d.setColor(Color.black);
            g2d.draw(original[i].getBounds2D());
        }
        for (int i = 0; i < original.length; i++) {
            g2d.setColor(Color.yellow);
            g2d.fill(transformed[i]);
            g2d.setColor(Color.black);
            g2d.draw(transformed[i].getBounds2D());
        }
    }
    
    public String toString() {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < original.length; i++) {
            buf.append(original[i]);
            buf.append('\t');
            buf.append(transformed[i]);
            buf.append('\n');
        }
        return buf.toString();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        JPanel panel = new TestTransformUtilities();
        System.out.println(panel);
        JFrame frame = new JFrame("Test Transform Utilities");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(panel);
        frame.setSize(400, 500);
        frame.setVisible(true);
        
        double[] coord = new double[6];
        Ellipse2D ellipse = new Ellipse2D.Double(100.0, 200.0, 300.0, 400.0);
        Shape segment = null;
        Point2D lastPoint = new Point2D.Double();
        for (PathIterator i = ellipse.getPathIterator(null); !i.isDone(); i.next()) {
            int type = i.currentSegment(coord);
            String message = "";
            switch(type) {
                case PathIterator.SEG_CLOSE:
                    segment = null;
                    message = "Close";
                    break;
                case PathIterator.SEG_CUBICTO:
                    segment = new CubicCurve2D.Double(lastPoint.getX(), lastPoint.getY(), coord[0], coord[1], coord[2], coord[3], coord[4], coord[5]);
                    lastPoint.setLocation(coord[4], coord[5]);
                    message = "Cubic";
                    break;
                case PathIterator.SEG_LINETO:
                    segment = new Line2D.Double(coord[0], coord[1], coord[2], coord[3]);
                    lastPoint.setLocation(coord[2], coord[3]);
                    message = "Line";
                    break;
                case PathIterator.SEG_MOVETO:
                    segment = new Line2D.Double(coord[0], coord[1], coord[0], coord[1]);
                    lastPoint.setLocation(coord[0], coord[1]);
                    message = "Move";
                    break;
                case PathIterator.SEG_QUADTO:
                    segment = new QuadCurve2D.Double(lastPoint.getX(), lastPoint.getY(), coord[0], coord[1], coord[2], coord[3]);
                    lastPoint.setLocation(coord[2], coord[3]);
                    message = "Quadratic";
                    break;
                default:
                    message = "Huh??";
                    break;
            }
            
            System.out.print(message + "\t");
            if (segment instanceof CubicCurve2D) {
                System.out.println(cubicToString((CubicCurve2D)segment));
            }
            else if (segment instanceof Line2D) {
                System.out.println( ((Line2D) segment).getP1());
            }
            else{
                System.out.println(segment);
            }
        }
        
        CubicCurve2D[] seg = getSegments(ellipse);
        for (int i = 0; i < seg.length; i++) {
            System.out.println(cubicToString(seg[i]));
        }
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
    
}
