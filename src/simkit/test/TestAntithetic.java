/*
 * TestAntithetic.java
 *
 * Created on March 15, 2002, 4:41 PM
 */

package simkit.test;
import simkit.random.*;

/**
 *
 * @author  Arnold Buss
 * @version $Id$
 */
public class TestAntithetic {

    /** Creates new TestAntithetic */
    public TestAntithetic() {
    }

    /**
    * @param args the command line arguments
    */
    public static void main (String args[]) {
        RandomVariate rv = RandomVariateFactory.getInstance("Uniform",
            new Object[] { new Double(0.0), new Double(1.0) });
        RandomVariate copy = RandomVariateFactory.getInstance(rv);
        RandomNumber anti = RandomNumberFactory.getAntithetic(copy.getRandomNumber());
        copy.setRandomNumber(anti);
        
        for (int i = 0; i < 10; i++) {
            double x = copy.generate();
            System.out.println(rv.generate() + "\t" + x + "\t" + (1.0 - x));
        }
        
        
    }

}
