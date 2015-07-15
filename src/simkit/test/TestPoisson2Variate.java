package simkit.test;

import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;
import simkit.stat.SimpleStatsTally;

/**
 *
 * @author ahbuss
 */
public class TestPoisson2Variate {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String dist = "Poisson2";
        double mean = 1035.0;
        
        RandomVariate rv = RandomVariateFactory.getInstance(dist, mean);
        System.out.println(rv);
        
        SimpleStatsTally sst = new SimpleStatsTally(rv.toString());
        
        int number = 1000000;
        for (int i = 1; i <= number; ++i) {
            sst.newObservation(rv.generate());
        }
        System.out.println(sst);
    }

}
