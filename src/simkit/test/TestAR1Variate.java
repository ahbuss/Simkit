/*
 * TestAR1Variate.java
 *
 * Created on December 7, 2001, 2:52 PM
 */

package simkit.test;

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
            RandomVariateFactory.getInstance("AR1", 0.8, .1, 0.2);
        RandomVariate rv2 =
            RandomVariateFactory.getInstance("Normal", 0.0, 2.0);
        
        System.out.println(rv1 + "\t" + rv2);
        for (int i = 0; i < 10; i++) {
            System.out.println(rv1.generate() + "\t\t\t" + rv2.generate());
        }
        
        rv1 = RandomVariateFactory.getInstance("AR1", 0.8, .1);
        System.out.println(rv1);
        
        
        
    }
    
}
