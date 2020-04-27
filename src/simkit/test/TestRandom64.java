
package simkit.test;

import simkit.random.RandomNumberFactory;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;

/**
 * 
 * @author ahbuss
 */
public class TestRandom64 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        RandomVariate rv = RandomVariateFactory.getInstance("Exponential", 8.9);
        RandomVariate rv_64 = RandomVariateFactory.getInstance("Exponential_64", 8.9);

        rv_64.setRandomNumber(RandomNumberFactory.getInstance(
                RandomVariateFactory.getDefaultRandomNumber()));

        System.out.println(rv + "\t" + rv_64);

        for (int i = 0; i < 10; ++i) {
            double x = rv.generate();
            double y = rv_64.generate();

            System.out.println(x + "\t" + y + "\t" + (x - y));
        }

        rv = RandomVariateFactory.getInstance("Weibull",
                rv.getRandomNumber(), 8.9, 2.3);
        rv_64 = RandomVariateFactory.getInstance("Weibull_64",
                rv_64.getRandomNumber() ,8.9, 2.3);

        System.out.println();

        System.out.println(rv + "\t" + rv_64);

        for (int i = 0; i < 10; ++i) {
            double x = rv.generate();
            double y = rv_64.generate();

            System.out.println(x + "\t" + y + "\t" + (x - y));
        }
    }

}
