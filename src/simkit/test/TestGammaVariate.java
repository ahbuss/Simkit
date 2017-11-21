package simkit.test;

import static java.lang.Math.sqrt;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;
import simkit.stat.SimpleStatsTally;

/**
 *
 * @author ahbuss
 */
public class TestGammaVariate {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        double alpha = 1.5;
        double beta = 2.1;
        
        double mean = alpha * beta;
        double stdDev = sqrt(alpha) * beta;
        
        RandomVariate rv = RandomVariateFactory.getInstance("Gamma", 
                alpha, beta);
        System.out.println(rv);
        
        SimpleStatsTally stats = new SimpleStatsTally(rv.toString());
        int n = 1000000;
        for (int i = 0; i < n; ++i) {
            stats.newObservation(rv.generate());
        }
        
        System.out.printf("\u03BC=%.3f (desired=%.3f)" +
                " \u03C3=%.3f (desired=%.3f)%n", stats.getMean(), mean,
                    stats.getStandardDeviation(), stdDev);
     
    }
    
}
