/*
 * Main.java
 *
 * Created on March 29, 2002, 8:47 AM
 */

package simkit.test;
import simkit.smdx.*;
import java.awt.geom.*;
import java.awt.*;
import javax.swing.*;
/**
 *
 * @author  Arnold Buss
 * @version
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
    }
    
}
