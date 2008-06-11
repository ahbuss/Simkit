/*
 * TestGenerate.java
 *
 * Created on September 4, 2001, 6:07 PM
 */

package simkit.test;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;

import simkit.random.CongruentialSeeds;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;
import simkit.stat.GraphStat;
import simkit.util.CloseableDataWindow;

/**
 *
 * @author  ahbuss
 * @version $Id: TestRightWedge.java 1051 2008-02-27 00:14:47Z ahbuss $
 */
public class TestRightWedge {
    
    /** Creates new TestGenerate */
    public TestRightWedge() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main (String args[]) {
/*        
        String name = "oa3302.TriangleVariate";
        Object[] params = new Object[] {
            new Double(1.0),
            new Double(2.5),
            new Double(1.5)
        };
 */
        double left = 0;
        double right = 1;
        double little =.5/(right-left);
       String name = "simkit.random.RightWedge";
        
        RandomVariate rv = RandomVariateFactory.getInstance(
        name, left, right, little);
        
        System.out.println(rv);
/*        
        for (int i = 0; i < 5; i++) {
            System.out.println(rv.generate());
        }
*/        
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle window = new Rectangle(400, 300);
        CloseableDataWindow cdw =
        new CloseableDataWindow("Right Wedge Variate");
        cdw.setBounds((screen.width - window.width) / 2, (screen.height - window.height) / 2,
        window.width, window.height);
        GraphStat gs = new GraphStat("RightWedge", 0.0);
        cdw.add(gs.initHistogram(true, left, right, 100));
        cdw.setVisible(true);
        
        int numberToGenerate = 100000;  //10^6
        for (int i = 0; i < numberToGenerate; i++) {
            gs.sample(0.0, rv.generate());
            cdw.repaint();
        }
        System.out.println("finished!");
    }
    
}
