/*
 * Test.java
 *
 * Created on March 25, 2002, 12:25 AM
 */

package simkit.smdx.test;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.geom.*;

/**
 *
 * @author  Arnold Buss
 * @version $Id$
 */
public class TestPathIterator extends JComponent {

    /** Creates new Test */
    public TestPathIterator() {
    }

    /**
    * @param args the command line arguments
    */
    public static void main (String args[]) {
        Ellipse2D ellipse = new Ellipse2D.Double(10, 20, 30, 40);
        double[] coords = new double[6];
        StringBuffer buf = new StringBuffer();
        for (PathIterator path = ellipse.getPathIterator(null); !path.isDone(); path.next()) {
            switch(path.currentSegment(coords)) {
                case PathIterator.SEG_MOVETO:
                    buf.append("Move to ");
                    buf.append(coords[0]);
                    buf.append(' ');
                    buf.append(coords[1]);
                    buf.append('\n');
                    break;
                    
                case PathIterator.SEG_QUADTO:
                    buf.append("Quad to ");
                    buf.append(coords[0]);
                    buf.append(' ');
                    buf.append(coords[1]);
                    buf.append(' ');
                    buf.append(coords[2]);
                    buf.append(' ');
                    buf.append(coords[3]);
                    buf.append('\n');
                    break;
                    
                case PathIterator.SEG_CUBICTO:
                    buf.append("Cubic to ");
                    buf.append(coords[0]);
                    buf.append(' ');
                    buf.append(coords[1]);
                    buf.append(' ');
                    buf.append(coords[2]);
                    buf.append(' ');
                    buf.append(coords[3]);
                    buf.append(' ');
                    buf.append(coords[4]);
                    buf.append(' ');
                    buf.append(coords[5]);
                    buf.append('\n');
                    break;
                    
                case PathIterator.SEG_LINETO:
                buf.append("Line to ");
                    buf.append(coords[0]);
                    buf.append(' ');
                    buf.append(coords[1]);
                    buf.append('\n');
                    break;
                    
                case PathIterator.SEG_CLOSE:
                    buf.append("Close\n");
                    break;
                    
                default:
                    buf.append("Something Else: ");
                    buf.append(path.currentSegment(coords));                    
                    break;                    
            }
        }
        System.out.println(buf.toString());
        
        Shape[] segments = simkit.smdx.Math2D.getSegments(ellipse, null);
        for (int i = 0; i < segments.length; i++) {
            if (segments[i] instanceof CubicCurve2D) {
                System.out.println(simkit.test.TestJava2D.cubicToString((CubicCurve2D) segments[i]));
            }
            else {
                System.out.println(segments[i]);
            }
        }
    }

}
