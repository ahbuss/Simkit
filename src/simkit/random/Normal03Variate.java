/*
 * Normal03Variate.java
 *
 * Created on March 31, 2002, 9:00 PM
 */

package simkit.random;

/**
 *
 * @author  Arnold Buss
 * @version 
 */
public class Normal03Variate extends Normal02Variate {

    private static final double ONE_THIRD = 1.0 / 3.0;
    private static final double TWO_THIRDS = 2.0 / 3.0;
    
    /** Creates new Normal03Variate */
    public Normal03Variate() {
    }

    public double generate() {
        double y = Double.NaN;
        do {
            double v = rng.draw();
            double w = rng.draw();
            if (w < ONE_THIRD) {
                y = -0.5 + Math.log(v);
            }
            else if (w < TWO_THIRDS) {
                y = v - 0.5;
            }
            else {
                y = 0.5 - Math.log(v);
            }
        } while (
            (Math.abs(y) < 0.5 && rng.draw() > Math.exp(0.5 * y * y)) ||
            (Math.abs(y) >= 0.5 && rng.draw() > Math.exp(-0.5 * Math.pow(Math.abs(y) - 1.0, 2)))
        );
        return getMean() + y * getStandardDeviation();
    }
    
}
