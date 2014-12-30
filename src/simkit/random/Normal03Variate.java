/*
 * Normal03Variate.java
 *
 * Created on March 31, 2002, 9:00 PM
 */
package simkit.random;

/**
 * Generates Normal random variates using Acceptance/Rejection method. The
 * majorizing function is uniform in the center and exponential in the tails.
 *
 * @author Arnold Buss
 * @version $Id$
 */
public class Normal03Variate extends Normal02Variate {

    private static final double ONE_THIRD = 1.0 / 3.0;
    private static final double TWO_THIRDS = 2.0 / 3.0;

    /**
     * Creates new Normal03Variate
     */
    public Normal03Variate() {
    }
//Javadoc inherited

    public double generate() {
        double y = Double.NaN;
        double u = Double.NaN;
        do {
            u = rng.draw();
            double v = rng.draw();
            double w = rng.draw();
            if (w < ONE_THIRD) {
                y = -0.5 + Math.log(v);
            } else if (w < TWO_THIRDS) {
                y = v - 0.5;
            } else {
                y = 0.5 - Math.log(v);
            }
        } while ((Math.abs(y) < 0.5 && u > Math.exp(-0.5 * y * y))
                || (Math.abs(y) >= 0.5 && u > Math.exp(-0.5 * Math.pow(Math.abs(y) - 1.0, 2))));
        return getMean() + y * getStandardDeviation();
    }

}
