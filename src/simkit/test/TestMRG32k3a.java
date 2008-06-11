package simkit.test;

import simkit.random.MRG32k3a;
import simkit.random.RandomNumber;
import simkit.random.RandomNumberFactory;
import simkit.stat.SimpleStatsTally;

/**
 * @version $Id$
 * @author ahbuss
 */
public class TestMRG32k3a {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        RandomNumber rng = RandomNumberFactory.getInstance("MRG32k3a");
        RandomNumber rng2 = RandomNumberFactory.getInstance("MRG32k3a");
        System.out.println(rng);
        
        SimpleStatsTally stat = new SimpleStatsTally("RNG Test");
        SimpleStatsTally stat2 = new SimpleStatsTally("RNG Test - Mine");
        for (int i = 0; i < Math.pow(10, 7); ++i) {
            double x = rng.draw();
            stat.newObservation(x);
            stat2.newObservation(rng2.drawLong() * MRG32k3a.norm);
        }
        System.out.println(stat);
        System.out.println(stat.getMean() * stat.getCount());
        System.out.println(stat2);
        System.out.println(stat2.getMean() * stat2.getCount());
    }

}
