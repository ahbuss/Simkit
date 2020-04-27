package simkit.util;

/**
 * Slightly modified version obtained from Mr. Phillip Wright of
 * TRAC-WSMR. This reliably gives the same results on 64-bit platforms,
 * unlike the ones shipped with the JDK. It is noticibly slower, however.
 *
 * 
 * @author Phillip Wright
 * @author ahbuss
 */
public class Math64 {

    public static final double LN2 = 0.69314718055994530941723212145818;

    public static final double log(double x) {

        if (x == 0.0) {
            return - Double.NEGATIVE_INFINITY;
        }
        if (x < 0.5) {
            return - log(1.0 / x);
        }
        // Because the Mclaurin series doesn't work very well
        // for values of x too far from 1 we'll just leverage
        // log(xy) = log(x) + log(y) and divide by 2 until we
        // get a value of x2 that is small enough to work.
        double sum2 = 0.0;
        double x2 = x;

        // collect the ln(2) in each iteration.
        while (x2 > 2.0) {
            sum2 += LN2;
            x2 = x2 / 2.0;
        }

        double z = (x2 - 1.0) / (x2 + 1.0);
        double sum = z;
        double value = 0;
        double pow = z;
        double z2 = z * z;
        // todo: don't calculate the z^i each time, carry
        // the previous result forward.  Should be faster.
        // Also could check the value of each term and stop
        // when it gets close enough to zero.
        for (int i = 3; i < 61; i += 2) {
            pow *= z2;
            sum = sum + pow / i;
        }

        value = sum2 + sum * 2;
        return value;
    }
}
