/*
 * TestLogNormal.java
 *
 * Created on November 8, 2001, 9:30 AM
 */

package simkit.test;
import simkit.random.ExponentialTransform;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;
public class TestLogNormal {
    public static void main (String args[]) {
        
        double mean = 0.0;
        double stdDev = 1.0;
        
        RandomVariate norm = RandomVariateFactory.getInstance("simkit.random.NormalVariate",
            new Object[] { new Double(mean), new Double(stdDev) } );
         RandomVariate logNormal = RandomVariateFactory.getInstance("ExponentialTransform",
            new Object[] { norm });
         System.out.println(logNormal);
         for (int i = 0 ; i <100; i++) {
             System.out.println(logNormal.generate());
         }         
    }
}