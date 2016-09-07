package simkit.test;

import simkit.random.RandomNumber;
import simkit.random.RandomVariateFactory;
import simkit.stat.NormalQuantile;
import simkit.stat.SimpleStatsTally;

/**
 *
 * @author ahbuss
 */
public class TestNormalQuantile {

    public static double getQuantile(double p, double mu, double sigma) {
        return NormalQuantile.getQuantile(p) * sigma + mu;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        double mu = 2.0;
        double sigma = 1.5;
        SimpleStatsTally sst = new SimpleStatsTally();
        int number = 10000000;
        
        RandomNumber rng = RandomVariateFactory.getDefaultRandomNumber();
        
        for (int i = 0; i < number; ++i) {
            sst.newObservation(getQuantile(rng.draw(), mu, sigma));
        }
        System.out.println(sst);
    }

}
