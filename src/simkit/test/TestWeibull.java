package simkit.test;
import simkit.random.*;
/**
 *
 * @author  Arnold Buss
 * @version 
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
            params, 12345L);
        
        System.out.println(rv);
        System.out.println(rv.generate());
        rv = RandomVariateFactory.getInstance(simkit.random.WeibullVariate.class,
            params, 12345L);
        
        System.out.println(rv);
        System.out.println(rv.generate());
    }

}