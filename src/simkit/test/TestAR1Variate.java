/*
 * TestAR1Variate.java
 *
 * Created on December 7, 2001, 2:52 PM
 */

package simkit.test;

import simkit.random.RandomNumber;
import simkit.random.RandomNumberFactory;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;

/**
 *
 * @author  ahbuss
 * @version $Id$
 */
public class TestAR1Variate {
    
    public static void main(String[] args) {
        
        RandomVariate rv1 =
        RandomVariateFactory.getInstance("simkit.random.AR1Variate",
        2.0, 0.0);
        RandomVariate rv2 =
        RandomVariateFactory.getInstance("simkit.random.NormalVariate",
            0.0, 2.0);
        
        System.out.println(rv1);
        System.out.println(rv2);
        for (int i = 0; i < 10; i++) {
            System.out.println(rv1.generate() + "\t" + rv2.generate());
        }
        
        
        
    }
    
}
