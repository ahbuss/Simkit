/*
 * TestRandomVariateCloneable.java
 *
 * Created on September 20, 2001, 9:48 AM
 */

package simkit.test;
import simkit.random.*;
/**
 *
 * @author  Arnold Buss
 * @version
 */
public class TestRandomVariateCloneable {
    
    /** Creates new TestRandomVariateCloneable */
    public TestRandomVariateCloneable() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        RandomVariate live = RandomVariateFactory.getInstance(
        "simkit.random.ExponentialVariate",
        new Object[] { new Double(2.5) },
        12345L);
        RandomVariate memorex1 = RandomVariateFactory.getInstance(live);
        
        RandomVariate memorex2 = (RandomVariate) live.clone();
        
        System.out.println("Live: " +live + "\tDeep : " + memorex1 + "\tShallow" + memorex2);
        
        for (int i = 0; i < 10; i++) {
            System.out.println(live.generate() + "\t" + memorex1.generate() + "\t" + memorex2.generate());
        }
        
        RandomNumber live2 = RandomNumberFactory.getInstance(12345L);
        RandomNumber mem1 = (RandomNumber) live2.clone();
        System.out.println("live: " + live2 + "\tMemorex: " + mem1);
        for (int i = 0; i < 10; i++) {
            System.out.println(live2.draw() + "\t" + mem1.draw() );
        }
        System.out.println(live2 + "\t" + mem1);
        
        live2 = RandomNumberFactory.getInstance("simkit.random.PooledGenerator",
        new long[] {123454L, 54321L} );
        mem1 = (RandomNumber) live2.clone();
        RandomNumber mem2 = RandomNumberFactory.getInstance(live2); 
        System.out.println("live: " + live2 + "\tMemorex (factory): " + mem2 + "\tMemorex (clone): " + mem1);
        for (int i = 0; i < 10; i++) {
            System.out.println(live2.draw() + "\t" + mem2.draw() + "\t" + mem1.draw() );
        }
        
        System.out.println(live2 + "\t" + mem2 + "\t" + mem1);
    }
    
}
