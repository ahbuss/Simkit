/*
 * TestBinomialVariate.java
 *
 * Created on March 29, 2002, 4:22 PM
 */

package simkit.test;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URL;

import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;
/**
 *
 * @author  Arnold Buss
 * @version $Id$
 */
public class TestNormal02Variate {

    /** Creates new TestBinomialVariate */
    public TestNormal02Variate() {
    }

    /**
    * @param args the command line arguments
     * @throws java.io.FileNotFoundException if output file not found
    */
    public static void main (String args[]) throws FileNotFoundException  {
        String dist = "Normal03Variate";
        Object params[] = new Object[2];
        params[0] = new Integer(0);
        params[1] = new Integer(1);
        RandomVariate rv = 
            (RandomVariate) RandomVariateFactory.getInstance(dist, params);
        
        int number = args.length > 0 ? Integer.parseInt(args[0]) : 100000;
        
        URL dirURL = ClassLoader.getSystemResource("simkit");
        File dir = new File(dirURL.getFile()).getParentFile();
        PrintWriter out = new PrintWriter(new File(dir, "norm02.txt"));
        
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < number; i++) {
            buf.append(rv.generate());
            buf.append(' ');
            if ( (i + 1) % 100 == 0 ) {
                out.println(buf.toString());
                buf = new StringBuilder();
            }
        }
        out.close();        
        System.out.println(rv);
    }

}
