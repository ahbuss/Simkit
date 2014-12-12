package simkit.test;

import static java.lang.Math.pow;
import static java.lang.Math.round;
import static java.lang.Math.sqrt;
import simkit.random.NegativeBinomialVariate;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;
import simkit.stat.SimpleStatsTally;

/**
 *
 * @author ahbuss
 */
public class TestNegativeBinomial {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        double mean = 7;
        double stdDev = 4.56;

        double p = 1.0 - mean / pow(stdDev, 2);
        int r = (int) round((pow(mean, 2) / (pow(stdDev, 2) - mean)));

        double mean2 = r * p / (1.0 - p);
        double var2 = r * p / (Math.pow(1.0 - p, 2));
        double stdDev2 = sqrt(var2);

        System.out.printf("(adjusted) mean = %.3f (adjusted) std dev = %.3f%n%n",
                mean, stdDev);
//        System.out.printf("mean2=%.3f, std2=%.3f%n", mean2, stdDev2);
        RandomVariate rv = RandomVariateFactory.getInstance("NegativeBinomial", r, p);
        SimpleStatsTally stats = new SimpleStatsTally(rv.toString());
        int n = 1000000;
        System.out.printf("Generating %,d variates:%n%n", n);

        System.out.println("With original Parameters:");
        System.out.println(rv);
        stats.reset();
        for (int i = 0; i < n; ++i) {
            stats.newObservation(rv.generate());
        }
        System.out.printf("mean = %.3f (desired = %.3f), std dev = %.3f"
                + " (desired = %.3f)%n",
                stats.getMean(), mean, stats.getStandardDeviation(), stdDev);
        System.out.printf("Exact mean =%.3f exact std dev = %.3f%n", 
                r * p / (1.0 - p), sqrt(r * p /pow(1.0 - p, 2)));
        System.out.println();
        p = mean / (mean + r);
        rv.setParameters(r, p);

        System.out.println("With integer r and 'fixed' p:");
        System.out.println(rv);

        stats.reset();
        for (int i = 0; i < n; ++i) {
            stats.newObservation(rv.generate());
        }
        System.out.printf("mean = %.3f (desired = %.3f), std dev = %.3f"
                + " (desired = %.3f)%n",
                stats.getMean(), mean, stats.getStandardDeviation(), stdDev);
        System.out.printf("Exact mean =%.3f exact std dev = %.3f%n", 
                r * p / (1.0 - p), sqrt(r * p /pow(1.0 - p, 2)));        System.out.println("\nWith floating point r:");

        p = 1.0 - mean / pow(stdDev, 2);
        double r2 = pow(mean, 2) / (pow(stdDev, 2) - mean);

        rv.setParameters(r2, p);
        System.out.println(rv);
        stats.reset();
        for (int i = 0; i < n; ++i) {
            stats.newObservation(rv.generate());
        }
        System.out.printf("mean = %.3f (desired = %.3f), std dev = %.3f"
                + " (desired = %.3f)%n",
                stats.getMean(), mean, stats.getStandardDeviation(), stdDev);

        rv.setParameters(round(r2), p);

        if (true) {
            return;
        }

        r = 2;
        p = 0.6637;
//        nbv.setP(p);
//        nbv.setR(r);
        rv.setParameters(r, p);

        System.out.println("for i_014925591:");
        System.out.println(rv);
        stats.reset();
        for (int i = 0; i < n; ++i) {
            stats.newObservation(rv.generate());
        }
        System.out.printf("mean = %.3f (desired = %.3f), std dev = %.3f"
                + " (desired = %.3f)%n",
                stats.getMean(), 7.0, stats.getStandardDeviation(), 4.56);
    }

}
