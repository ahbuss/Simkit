/*
 * TestAR1Variate.java
 *
 * Created on December 7, 2001, 2:52 PM
 */

package simkit.test;

import simkit.random.*;

/**
 *
 * @author  ahbuss
 * @version $Id$
 */
public class TestAR1Variate {
    
    public static void main(String[] args) {
        
        RandomNumber rng = RandomNumberFactory.getInstance();
        
        RandomVariate rv1 =
        RandomVariateFactory.getInstance("simkit.random.AR1Variate",
        new Object[] { new Double(0.0), new Double(2.0), new Double(0.0) },
        rng);
        RandomVariate rv2 =
        RandomVariateFactory.getInstance("simkit.random.NormalVariate",
        new Object[] { new Double(0.0), new Double(2.0) },
        rng);
        
        System.out.println(rv1);
        System.out.println(rv2);
        for (int i = 0; i < 10; i++) {
            System.out.println(rv1.generate() + "\t" + rv2.generate());
        }
        
        
        
    }
    
}
