/*
 * TestMixedVariate.java
 *
 * Created on August 31, 2001, 11:55 AM
 */

package simkit.test;

import simkit.random.*;
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
                RandomVariateFactory.getInstance("simkit.random.ConstantVariate",
                new Object[] { new Double(0.0) } ),
                RandomVariateFactory.getInstance("simkit.random.GammaVariate",
                new Object[] { new Double(2.0), new Double(1.5) } )
            };
            double[] prob = new double[] { 0.3, 0.7 };
            
        RandomVariate mixed = RandomVariateFactory.getInstance("simkit.random.MixedVariate",
        new Object[] { prob, rv } );
        System.out.println(mixed);
        for (int i = 0; i < 10; i++) {
            System.out.println(mixed.generate());
        }
    }

}
