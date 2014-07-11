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
/**
 *
 * @author  Arnold Buss
 * @version $Id$
 */
public class TestBinomialVariate {

    /** Creates new TestBinomialVariate */
    public TestBinomialVariate() {
    }

    /**
    * @param args the command line arguments
    */
    public static void main (String args[]) throws Throwable {
        String dist = "Binomial";
        int n = 7;
        double p = 0.4;
        Object[] params = new Object[] { new Integer(n), new Double(p) };
        DiscreteRandomVariate binomial = (DiscreteRandomVariate) RandomVariateFactory.getInstance(dist, params);
        
        for (int i = 0; i < 10; i++) {
            System.out.print(binomial.generateInt() + " ");
        }
        System.out.println();
        
        int number = args.length > 0 ? Integer.parseInt(args[0]) : 100000;
        
        URL dirURL = Thread.currentThread().getContextClassLoader().getSystemResource("simkit");
        File dir = new File(dirURL.getFile()).getParentFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(new File(dir, "binomial.txt")));
        
        
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < number; i++) {
            buf.append(binomial.generateInt());
            buf.append(' ');
            if ( (i + 1) % 100 == 0 ) {
                System.out.print(".");
                out.write(buf.toString());
                out.newLine(); 
                buf = new StringBuilder();
            }
        }
        System.out.println();
        out.close();        
    }

}
