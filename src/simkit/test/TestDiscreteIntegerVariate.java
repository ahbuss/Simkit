package simkit.test;

import simkit.random.DiscreteRandomVariate;
import simkit.random.RandomVariateFactory;
import simkit.stat.Histogram;

/**
 * @version $Id$
 * @author ahbuss
 */
public class TestDiscreteIntegerVariate {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        RandomVariateFactory.addSearchPackage("random");
        DiscreteRandomVariate rv =
                RandomVariateFactory.getDiscreteRandomVariateInstance(
                "DiscreteInteger",  new int[] { 0, 5 },
                new double[] {1.0, 3.0 } );
        System.out.println(rv);
        
        Histogram histogram = new Histogram("Discrete Generated", 0, 5, 5);
        for (int i = 0; i < 100000; ++i) {
            histogram.newObservation(rv.generateInt());
        }
        System.out.println(histogram);
        
        rv = RandomVariateFactory.getDiscreteRandomVariateInstance(
                "DiscreteUniform", -50, 50);
        System.out.println(rv);
        histogram = new Histogram("Discrete Uniform", -50, 50, 100);
        for (int i = 0; i < 100000; ++i) {
            histogram.newObservation(rv.generateInt());
        }
        System.out.println(histogram);
    }
}