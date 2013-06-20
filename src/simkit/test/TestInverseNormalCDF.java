package simkit.test;

import simkit.stat.NormalQuantile;

/**
 * @version $Id$
 * @author ahbuss
 */
public class TestInverseNormalCDF {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        NormalQuantile quantile = new NormalQuantile(2.0, 1.5);
        for (double p = 0.01; p <= 0.51; p +=0.01) {
            System.out.println(p + "\t" + NormalQuantile.getQuantile(p));
        }
    }

}
