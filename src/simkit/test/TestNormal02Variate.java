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
    */
    public static void main (String args[]) throws Throwable {
        String dist = "Normal03Variate";
        Object params[] = new Object[2];
        params[0] = new Integer(0);
        params[1] = new Integer(1);
        RandomVariate rv = 
            (RandomVariate) RandomVariateFactory.getInstance(dist, params);
        
        int number = args.length > 0 ? Integer.parseInt(args[0]) : 100000;
        
        URL dirURL = Thread.currentThread().getContextClassLoader().getSystemResource("simkit");
        File dir = new File(dirURL.getFile()).getParentFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(new File(dir, "norm03.txt")));
        
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < number; i++) {
            buf.append(rv.generate());
            buf.append(' ');
            if ( (i + 1) % 100 == 0 ) {
                out.write(buf.toString());
                out.newLine(); 
                buf = new StringBuffer();
            }
        }
        out.close();        
        System.out.println(rv);
    }

}
