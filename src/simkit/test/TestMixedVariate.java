/*
 * TestMixedVariate.java
 *
 * Created on August 31, 2001, 11:55 AM
 */

package simkit.test;

import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;
/**
 *
 * @author  Arnold Buss
 * @version $Id$
 */
public class TestMixedVariate extends Object {

    /** Creates new TestMixedVariate */
    public TestMixedVariate() {
    }

    /**
    * @param args the command line arguments
    */
    public static void main (String args[]) {
        RandomVariate[] rv = 
            new RandomVariate[] {
                RandomVariateFactory.getInstance(
                        "simkit.random.ConstantVariate", 0.0),
                RandomVariateFactory.getInstance(
                        "simkit.random.GammaVariate", 2.0, 1.5),
                RandomVariateFactory.getInstance(
                        "Normal", -3.1, 4.2)
            };
            double[] prob = new double[] { 4.0, 6.0, 2.0 };
            
        RandomVariate mixed = RandomVariateFactory.getInstance(
                "simkit.random.MixedVariate", prob, rv  );
        System.out.println(mixed);
        for (int i = 0; i < 10; i++) {
            System.out.println(mixed.generate());
        }
    }

}
