package simkit.test;

import java.text.DecimalFormat;
import simkit.random.DiscreteRandomVariate;
import simkit.random.RandomVariateFactory;
import simkit.stat.SimpleStatsTally;

/**
 * @version $Id$
 * @author ahbuss
 */
public class TestBNegativeBinomial {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        double r = 3.8;
        double p = 0.2;
        DiscreteRandomVariate drv = RandomVariateFactory.getDiscreteRandomVariateInstance(
                "NegativeBinomial", r, p);

        int numberObs = 100000;
        SimpleStatsTally sst = new SimpleStatsTally(drv.toString());
        for (int i = 0; i < numberObs; ++i) {
            sst.newObservation(drv.generateInt());
        }
        System.out.println(sst);

        DecimalFormat df = new DecimalFormat("0.0000");
        System.out.println("Exact mean: " + df.format(r *(1.0 - p)/p));
        System.out.println("Exact variance: " + df.format(r *(1.0 - p)/(p * p)));
    }
}
