package simkit.test;

import simkit.random.DiscreteRandomVariate;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;

/**
 *
 * @author ahbuss
 */
public class TestParseRandomVariate {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String exponentialString = "Exponential (1.8)";
        RandomVariate rv = RandomVariateFactory.getInstance(exponentialString);
        System.out.println(rv);

        rv = RandomVariateFactory.getInstance("Discrete Distribution\n"
                + "x    	f(x)   	F(x)\n"
                + "2.0	0.100	0.100\n"
                + "4.0	0.200	0.300\n"
                + "6.0	0.300	0.600\n"
                + "8.0	0.400	1.000");
        System.out.println(rv);

        DiscreteRandomVariate drv = RandomVariateFactory.getDiscreteRandomVariateInstance("Discrete Integer\n"
                + "x    	f(x)   	F(x)\n"
                + "0	0.250	0.250\n"
                + "3    0.250   0.500\n"
                + "5	0.500	1.000");

        System.out.println(drv);

        drv = RandomVariateFactory.getDiscreteRandomVariateInstance("Geometric (0.3)");
        System.out.println(drv);
        drv = RandomVariateFactory.getDiscreteRandomVariateInstance("Poisson (2.3)");
        System.out.println(drv);
        drv = RandomVariateFactory.getDiscreteRandomVariateInstance("Binomial (5, 0.8)");
        System.out.println(drv);
        drv = RandomVariateFactory.getDiscreteRandomVariateInstance("Geometric (0.3)");
        System.out.println(drv);

    }

}
