/*
 * Main.java
 *
 * Created on March 8, 2002, 3:38 PM
 */

package simkit.test;
import simkit.*;
import simkit.random.*;
import simkit.stat.*;
/**
 *
 * @author  ahbuss
 * @version $Id$
 */
public class TestBetaVariate {

    public static void main (String args[]) {
        double alpha = 2.0;
        double beta = 3.0;
        RandomVariate rv = RandomVariateFactory.getInstance("simkit.random.BetaVariate",
            new Double[] { new Double(alpha), new Double(beta) } );
            
        int numberReps = args.length > 0 ? Integer.parseInt(args[0]) : 10000;
        SimpleStatsTally sst = new SimpleStatsTally("Beta Variate Test");
        
        for (int i = 0; i < numberReps; i++) {
            double next = rv.generate();
            sst.newObservation(next);
        }
        
        System.out.println(sst);
        System.out.println("true mean = " + alpha/(alpha + beta));
        System.out.println("true variance = " + alpha * beta/(Math.pow((alpha + beta), 2) * (alpha + beta + 1)));
    }

}
