/*
 * TestPoissonVariate.java
 *
 * Created on March 30, 2002, 3:05 PM
 */
package simkit.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;

import simkit.random.DiscreteRandomVariate;
import simkit.random.RandomVariateFactory;
import simkit.stat.SimpleStatsTally;

/**
 *
 * @author Arnold Buss
 * @version $Id$
 */
public class TestPoissonVariate {

    /**
     * Creates new TestPoissonVariate
     */
    public TestPoissonVariate() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws Throwable {
        int number = args.length > 0 ? Integer.parseInt(args[0]) : 10000000;

        String dist = "Poisson";
        double[] lambda = {1.5, 20.0, 50.0, 100.0, 500.0};
        for (double mean : lambda) {
            DiscreteRandomVariate poiss
                    = (DiscreteRandomVariate) RandomVariateFactory.getInstance(
                            dist, mean);

            SimpleStatsTally stat = new SimpleStatsTally(poiss.toString());

            for (int i = 0; i < number; i++) {
                double x = poiss.generateInt();
                stat.newObservation(x);
            }
            System.out.println(stat);
        }
    }

}
