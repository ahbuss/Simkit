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
 * @author  Arnold Buss
 * @version $Id$
 */
public class TestPoissonVariate {

    /** Creates new TestPoissonVariate */
    public TestPoissonVariate() {
    }

    /**
    * @param args the command line arguments
    */
    public static void main (String args[]) throws Throwable {
        String dist = "Poisson";
        double lambda = 1.5;
        Object[] params = new Object[] { new Double(lambda) };
        DiscreteRandomVariate poiss = 
                (DiscreteRandomVariate) RandomVariateFactory.getInstance(
                dist, lambda);

        int number = args.length > 0 ? Integer.parseInt(args[0]) : 1000000;
        SimpleStatsTally stat = new SimpleStatsTally(poiss.toString());
        URL dirURL = Thread.currentThread().getContextClassLoader().getSystemResource("simkit");
        File dir = new File(dirURL.getFile()).getParentFile();
        File outFile = new File(dir, "poisson.txt");
        BufferedWriter out = new BufferedWriter(new FileWriter(outFile));
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < number; i++) {
            double x = poiss.generateInt();
            stat.newObservation(x);
            buf.append(x);
            buf.append(' ');
            if ( (i + 1) % 100 == 0) {
                out.write(buf.toString());
                out.newLine();
                buf = new StringBuilder();
            }
        }
        System.out.println(stat);
        out.flush();
        out.close();
    }

}
