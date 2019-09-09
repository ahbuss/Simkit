package simkit.test;

import simkit.random.ExponentialVariate;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;

/**
 *
 * @author ahbuss
 */
public class TestSciRegex {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String x = "Exponential (0.345E-10)";
        RandomVariate rv = RandomVariateFactory.getInstance(x);
        System.out.println(((ExponentialVariate)rv).getMean());
        x = "Constant(0.12345E+3)";
        rv = RandomVariateFactory.getInstance(x);
        System.out.println(rv);
        x = "Exponential(0.123E3)";
        rv = RandomVariateFactory.getInstance(x);
        System.out.println(rv);

    }
    
}
