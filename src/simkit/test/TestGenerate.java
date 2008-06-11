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
 * @version $Id$
 */
public class TestGenerate {
    
    /** Creates new TestGenerate */
    public TestGenerate() {
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
        
       String name = "simkit.random.TriangleVariate";
       Object[] params = new Object[] {
           new Double(1.0), new Double(2.5), new Double(1.5)
       };
        
        RandomVariate rv = RandomVariateFactory.getInstance(name, params);
        
        System.out.println(rv);
/*        
        for (int i = 0; i < 5; i++) {
            System.out.println(rv.generate());
        }
*/        
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle window = new Rectangle(400, 300);
        CloseableDataWindow cdw =
        new CloseableDataWindow("Triangle Variate");
        cdw.setBounds((screen.width - window.width) / 2, (screen.height - window.height) / 2,
        window.width, window.height);
        GraphStat gs = new GraphStat("Triangle", 0.0);
        cdw.add(gs.initHistogram(true, 1.0, 2.5, 100));
        cdw.setVisible(true);
        
        int numberToGenerate = 100000;
        for (int i = 0; i < numberToGenerate; i++) {
            gs.sample(0.0, rv.generate());
            cdw.repaint();
        }
        System.out.println("finished!");
    }
    
}
