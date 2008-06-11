package simkit.test;

import simkit.random.RandomNumber;
import simkit.random.RandomNumberFactory;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;

/**
 * @version $Id$
 * @author ahbuss
 */
public class TestTraceVariate {
    public static void main(String[] args) {
        
        System.out.println("Testing Trace Variate");
        
        String rvName = "Trace";
        double[] param = new double[] {1.2, 2.5, 3.4, 1.0, 7.4};
            RandomVariate rv = RandomVariateFactory.getInstance(
                    rvName, param
                    );
            
            System.out.println(rv);
            System.out.println(rv.getRandomNumber());
            
            RandomNumber rng = RandomNumberFactory.getInstance("Sequential");
            System.out.println(rng);
            
            double[] traceValues = new double[] { 1, 3, 5, 7 };
            
            for (int i = 0; i < traceValues.length; ++i) {
//            System.out.println(rng.drawLong());
                double value = (rng.getSeed() < traceValues.length) ? traceValues[(int) rng.getSeed() ] : Double.NaN;
                rng.draw();
                System.out.println( value );
            }
            
            
            for (int i = 0; i < 10; ++i) {
                System.out.println(rv.generate());
            }
            
            
    }
}