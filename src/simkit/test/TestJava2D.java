/*
 * TestJava2D.java
 *
 * Created on March 19, 2002, 7:42 PM
 */

package simkit.test;
import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import simkit.smdx.*;
import java.util.*;
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
    
    /** Creates new TestJava2D */
    public TestJava2D() {
        
        setBackground(Color.white);
        setOpaque(true);
        
        shape = new Shape[2];
        shape[0] = new Ellipse2D.Double(100.0, 200.0, 300.0, 400.0);
        rect = shape[0].getBounds2D();
        shape[1] = new Arc2D.Double();
        point = new Point2D[] {
            new Point2D.Double(0.0, 400.0),
            new Point2D.Double(450.0, 200.0),
            new Point2D.Double(500.0, 400.0),
            new Point2D.Double(450.0, 700.0),
        };
        
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
        
        ArrayList intersections = new ArrayList();
        
        for (int j = 0; j < point.length; j++) {
            
            intersect = new Point2D[face.length];
            circle = new Shape[intersect.length];
            
            for (int i = 0; i < face.length; i++) {
                int outCode = rect.outcode(point[j]);
                System.out.println(point[j] + ": " + outCode);
                intersect[i] = Math2D.findIntersection(point[j], Math2D.subtract(point[(j+1) % point.length], point[j]), face[i]);
                System.out.println(intersect[i]);
                double size = 8;
                if (intersect[i] != null) {
                    intersections.add(new Ellipse2D.Double(intersect[i].getX() - size/2, intersect[i].getY() - size/2, size, size));
                }
            }
        }
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
        }
        
        g2d.setColor(Color.orange);
        for (int i = 0; i < line.length; i++) {
            if (shape[0].getBounds2D().intersectsLine(line[i])) {
                g2d.setColor(Color.green);
            }
            else {
                g2d.setColor(Color.orange);
            }
            
            g2d.draw(line[i]);
        }
        
        g2d.setColor(Color.black);
        for (int i = 0; i < circle.length; i++) {
            if (circle[i] == null) { continue; }
            g2d.fill(circle[i]);
        }
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        
        JFrame frame = new JFrame("Testing");
        JPanel panel = new TestJava2D();
        frame.getContentPane().add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 800);
        frame.setVisible(true);
        
    }
    
}
