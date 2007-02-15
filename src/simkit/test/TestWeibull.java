package simkit.test;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;
/**
 *
 * @author  Arnold Buss
 * @version $Id$
 */
public class TestWeibull {

    /** Creates new TestWeibull */
    public TestWeibull() {
    }

    /**
    * @param args the command line arguments
    */
    public static void main (String args[]) {
        Object[] params = new Object[2];
        params[0] = new Double(2.0);
        params[1] = new Double(3.0);
        
        RandomVariate rv = RandomVariateFactory.getInstance("simkit.random.WeibullVariate",
            params);
        
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
