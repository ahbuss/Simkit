/*
 * TestBinomialVariate.java
 *
 * Created on March 29, 2002, 4:22 PM
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
 * @author  Arnold Buss
 * @version $Id$
 */
public class TestBinomialVariate {
    /**
    * @param args the command line arguments
     * @throws java.lang.Throwable
    */
    public static void main (String args[]) throws Throwable {
        String dist = "Binomial";
        int n = 7;
        double p = 0.4;
        Object[] params = new Object[] { n, p};
        DiscreteRandomVariate binomial = RandomVariateFactory.getDiscreteRandomVariateInstance(dist, params);
        System.out.println(binomial);
        for (int i = 0; i < 10; i++) {
            System.out.print(binomial.generateInt() + " ");
        }
        System.out.printf("%n\u03BC = %.3f, \u03C3^2 = %.3f%n",
                n * p, n * p * (1.0 - p));
        
        SimpleStatsTally sst = new SimpleStatsTally(binomial.toString());
        
        int number = args.length > 0 ? Integer.parseInt(args[0]) : 1000000;
        for (int i = 0; i < number; ++i) {
            sst.newObservation(binomial.generateInt());
        }
        System.out.println(sst);
        
    }

}
