package simkit.test;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;
/**
 *
 * @author  Arnold Buss
 * @version $Id: TestWeibull.java 1051 2008-02-27 00:14:47Z ahbuss $
 */
public class TestWeibull {

    /** Creates new TestWeibull */
    public TestWeibull() {
    }

    /**
    * @param args the command line arguments
    */
    public static void main (String args[]) {
        
        RandomVariate rv = RandomVariateFactory.getInstance(
                "simkit.random.WeibullVariate", 2.0, 3.0);
        
        System.out.println(rv);
        System.out.println(rv.generate());
        rv = RandomVariateFactory.getInstance(rv);
        
        System.out.println(rv);
        System.out.println(rv.generate());
        
        rv.getRandomNumber().resetSeed();
        System.out.println("After resetting RandomNumber:");
        System.out.println(rv.generate());
    }

}
