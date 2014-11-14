package simkit.test;

import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;

/**
 * @version $Id$
 * @author ahbuss
 */
public class TestSequenceVariate {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        RandomVariate rv = RandomVariateFactory.getInstance("Sequence");
        System.out.println(rv);
        
        for (int i = 0; i < 100; ++i) {
            System.out.println(rv.generate());
        }
    }
    
}
