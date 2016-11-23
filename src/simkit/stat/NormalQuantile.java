package simkit.stat;

/**
 * @version $Id$
 * @author ahbuss
 */
public class NormalQuantile {

    public static final double ONE_OVER_ROOT_TWO_PI = 1.0 / Math.sqrt(2.0 * Math.PI);
    public static final double SQRT_EIGHT_OVER_PI = Math.sqrt(8.0 / Math.PI);
    public static final double SQRT_PI_OVER_EIGHT = Math.sqrt(Math.PI / 8.0);
    private static final double[] c
            = new double[]{2.515517, 0.802853, 0.010328};
    private static final double[] d
            = new double[]{1.0, 1.432788, 0.189269, 0.001308};
    public static final double A0 = 2.50662823884;
    public static final double A1 = -18.61500062529;
    public static final double A2 = 41.39119773534;
    public static final double A3 = -25.44106049637;
    public static final double B1 = -8.47351093090;
    public static final double B2 = 23.08336743743;
    public static final double B3 = -21.06224101826;
    public static final double B4 = 3.13082909833;
    public static final double C0 = -2.78718931138;
    public static final double C1 = -2.29796479134;
    public static final double C2 = 4.85014127135;
    public static final double C3 = 2.32121276858;
    public static final double D1 = 3.54388924762;
    public static final double D2 = 1.63706781897;
    public static final double SPLIT = 0.42;

    public static double getDensity(double x) {
        return ONE_OVER_ROOT_TWO_PI * Math.exp(-0.5 * x * x);
    }

    public static double getDensity(double x, double mean, double stdDev) {
        return getDensity((x - mean) / stdDev) / stdDev;
    }

    public static double getDistribution(double x) {
        double cdf = 0.5 + 0.5 * Math.sqrt(1.0 - Math.exp(-SQRT_PI_OVER_EIGHT * x * x));
        if (x < 0.0) {
            cdf = 1.0 - cdf;
        }
        return cdf;
    }

    public static double getDistribution(double x, double mean, double stdDev) {
        return getDistribution((x - mean) / stdDev);
    }

    /**
     * Based on Beasley &amp; Springer, "Algorithm AS 111: The Percentage Points
     * of the Normal Distribution," <i>Journal of the Royal Statistical Society.
     * Series C (Applied Statistics)</i>. Vol. 26, No 1.
     *
     * @param p Desired probability
     * @return x such that Pr{Z &lt; x} = p
     */
    public static double getQuantile(double p) {
        double quantile;
        if (p == 0.0) {
            quantile = Double.NEGATIVE_INFINITY;
        } else if (p == 1.0) {
            quantile = Double.POSITIVE_INFINITY;
        } else if (p == 0.5) {
            quantile = 0.0;
        } else {
            double q = p - 0.5;

            if (Math.abs(q) < SPLIT) {
                double r = q * q;
                quantile = q * (((A3 * r + A2) * r + A1) * r + A0)
                        / ((((B4 * r + B3) * r + B2) * r + B1) * r + 1.0);
            } else {
                double r = q <= 0.0 ? p : 1.0 - p;
                r = Math.sqrt(-Math.log(r));
                quantile = (((C3 * r + C2) * r + C1) * r + C0)
                        / ((D2 * r + D1) * r + 1.0);
                quantile = q >= 0.0 ? quantile : -quantile;
            }
        }
        return quantile;
    }

    /**
     * Based on Abramawitz &amp; Stegun, <i>Handbook of Mathematical
     * Functions</i>, 26.2.23, p. 933.
     *
     * Not as accurate as getQuantile() using Algorithm AS 111.
     *
     * @param p Desired probability
     * @return x such that Pr{Z &lt; x} = p
     */
    public static double getQuantile2(double p) {
        double quantile;
        double multiplier = -1.0;
        if (p == 0.0) {
            quantile = Double.NEGATIVE_INFINITY;
        } else if (p == 1.0) {
            quantile = Double.POSITIVE_INFINITY;
        } else if (p == 0.5) {
            quantile = 0.0;
        } else {
            multiplier = -1.0;
            if (p > 0.5) {
                p = 1.0 - p;
                multiplier = 1.0;
            }
            double t = Math.sqrt(-2.0 * Math.log(p));
            quantile = t - (c[0] + c[1] * t + c[2] * t * t)
                    / (1.0 + d[1] * t + d[2] * t * t + d[3] * t * t * t);
        }
        return multiplier * quantile;
    }
}
