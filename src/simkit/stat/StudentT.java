package simkit.stat;

/**
 * @version $Id$
 * @author ahbuss
 */
public class StudentT {

    /**
     * &pi;/2
     */
    public static final double HALF_PI = 0.5 * Math.PI;

    /**
     * Based on Hill, G.W., "Algorithm 396 Student T Quantiles,"
     * <i>Communications of the ACM</i>, Vol 13, No. 10, October 1970.
     *
     * @throws IllegalArgumentException if df &le; 0
     * @param p desired percentile
     * @param df degrees of freedom
     * @return x such that Pr{X &lt; x} = p for X ~ Student T(df)
     */
    public static double getQuantile(double p, int df) {
        double quantile;
        p = 2.0 * p;
        if (df <= 0) {
            throw new IllegalArgumentException(
                    "Degrees of freedom for Student T must be > 0: " + df);
        }

        if (p == 0.0) {
            quantile = Double.NEGATIVE_INFINITY;
        } else if (p == 2.0) {
            quantile = Double.POSITIVE_INFINITY;
        } else if (p == 1.0) {
            quantile = 0.0;
        } else if (df == 1) {
            quantile = 1.0 / Math.tan(p * HALF_PI);
        } else if (df == 2) {
            quantile = Math.sqrt(2.0 / (p * (2.0 - p)) - 2.0);
        } else {
            double a = 1.0 / (df - 0.5);
            double b = 48.0 / Math.pow(a, 2);
            double c = ((20700.0 * a / b - 98.0) * a - 16.0) * a + 96.36;
            double d = ((94.5 / (b + c) - 3.0) / b + 1.0) * Math.sqrt(a * HALF_PI) * df;
            double x = d * p;
            double y = Math.pow(x, 2.0 / df);

            if (y > 0.05 + a) {
                x = NormalQuantile.getQuantile(0.5 * p);
                y = x * x;
                if (df < 5) {
                    c = c + 0.3 * (df - 4.5) * (x + 0.6);
                }
                c = (((0.05 * d * x - 5.0) * x - 7.0) * x - 2.0) * x + b + c;
                y = (((((0.4 * y + 6.3) * y + 36.0) * y + 94.5) / c - y - 3.0) / b + 1.0) * x;
                y = a * y * y;
//                    This is the update from Hill, 1981
                y = (y > 0.1) ? Math.exp(y) - 1.0 : ((y + 4.0) * y + 112.0) * y * y / 24.0 + y;
            } else {
                y = ((1.0 / (((df + 6.0) / (df * y) - 0.089 * d - 8.222)
                        * (df + 2.0) * 3.0) + 0.5 / (df + 4.0)) * y - 1.0)
                        * (df + 1.0) / (df + 2.0) + 1.0 / y;
            }
            quantile = Math.sqrt(df * y);
        }
//        This is because Algorithm 396 returns the upper quantile!
        return p < 0.5 || df == 1 ? -quantile : quantile;
    }

    /**
     * Based on Abramawitz &amp; Stegun, <i>Handbook of Mathematical
     * Functions</i>, 26.7.5, p. 949.
     *
     * Not as accurate as getQuantile(), based on Hill's Algorithm 396.
     *
     * @throws IllegalArgumentException if df &le; 0
     * @param p desired percentile
     * @param df degrees of freedom
     * @return x such that Pr{X &lt; x} = p for X ~ Student T(df)
     */
    public static double getQuantile2(double p, int df) {
        double quantile;
        if (p == 0.0) {
            quantile = Double.NEGATIVE_INFINITY;
        } else if (p == 1.0) {
            quantile = Double.POSITIVE_INFINITY;
        } else if (p == 0.5) {
            return 0.0;
        } else {

            double x = NormalQuantile.getQuantile(p);

            quantile = x
                    + 0.25 * (x + Math.pow(x, 3)) / df
                    + (5.0 * Math.pow(x, 5) + 16.0 * Math.pow(x, 3) + 3.0 * x) / 96.0 / Math.pow(df, 2)
                    + (3.0 * Math.pow(x, 7) + 19.0 * Math.pow(x, 5) + 17.0 * Math.pow(x, 3) - 15.0 * x) / 384.0 / Math.pow(df, 3)
                    + (79.0 * Math.pow(x, 9) + 776.0 * Math.pow(x, 7) + 1482.0 * Math.pow(x, 5) - 1920.0 * Math.pow(x, 3) - 945.0 * x) / 92160.0 / Math.pow(df, 4);
        }
        return quantile;
    }
}
