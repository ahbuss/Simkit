/*
 * TestGeometricRV.java
 *
 * Created on March 29, 2002, 12:14 AM
 */

package simkit.test;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URL;

import simkit.random.DiscreteRandomVariate;
import simkit.random.RandomVariateFactory;
import simkit.stat.SimpleStatsTally;
/**
 *
 * @author  Arnold Buss
 * 
 */
public class TestGeometricRV {

    /** Creates new TestGeometricRV */
    public TestGeometricRV() {
    }

    /**
    * @param args the command line arguments
     * @throws java.io.FileNotFoundException if output file not found
    */
    public static void main (String args[]) throws FileNotFoundException {
        int number = args.length > 0 ? Integer.parseInt(args[0]) : 100000;
        DiscreteRandomVariate rv = (DiscreteRandomVariate) RandomVariateFactory.getInstance(
            "simkit.random.GeometricVariate", 0.3);
        for (int i = 0; i < 5; i++) {
            System.out.println(rv.generateInt());
        }
            
        URL url = ClassLoader.getSystemResource("simkit");
        File dir = new File(url.getFile()).getParentFile();
        File outputFile = new File(dir, "geom.txt");
        PrintWriter out = new PrintWriter(outputFile);
            
        SimpleStatsTally sst = new SimpleStatsTally();
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < number; i++) {
            int obs = rv.generateInt();
            buf.append(obs);
            buf.append(' ');
            if ((i + 1) % 20 == 0) {
                out.println(buf.toString());
                buf = new StringBuilder();
            }
            sst.newObservation((double)obs);
        }
        System.out.println(sst);
        out.close();
        
        double p = ((Number) rv.getParameters()[0]).doubleValue();
        double mean = mean = (1.0 - p) / p;
        double var = mean / p;
        System.out.println(mean + " " + var);
    }

}
