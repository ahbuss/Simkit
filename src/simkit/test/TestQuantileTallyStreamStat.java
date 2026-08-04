package simkit.test;

import java.util.ArrayList;
import java.util.List;
import simkit.stat.QuantileTallyStreamStat;

/**
 *
 * @author arnoldbuss
 */
public class TestQuantileTallyStreamStat {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        QuantileTallyStreamStat stat = new QuantileTallyStreamStat("test", 80);
        
        double[] data = {2, 4, 5, 8, 11, 13, 17, 20, 24, 30};
        List<Double> observed = new ArrayList<>();
        for (double x: data) {
            stat.newObservation(x);
            observed.add(x);
            System.out.printf("%dQ: %,.2f - %s%n", stat.getPercentile(), stat.getQuantile(), observed);
        }
    }

}
