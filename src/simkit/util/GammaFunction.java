package simkit.util;

import static java.lang.Double.POSITIVE_INFINITY;
import static java.lang.Math.PI;
import static java.lang.Math.exp;
import static java.lang.Math.log;

/**
 * The log gamma implementation is  based on the Lanczos approximation 
 * (Lanczos, "A Precision Approximation of the Gamma Function", Journal of the 
 * Society for Industrial and Applied Mathematics: Series B, Numerical Analysis, 
 * Vol. 1 (1964), pp. 86-96) and code from this site:
 * <a href="http://blog.javia.org/implementing-the-lgamma-function-in-java/">
 * Implementing the lgamma() function in Java</a> (using g = 607/128 and 15
 * terms)
 *
 * @author ahbuss
 */
public class GammaFunction {

    /**
     * log(2&pi;)/2
     */
    public static final double LOG_2PI_OVER_2 = log(2.0 * PI) * 0.5;

    /**
     * Coefficients for Lanczos approximation to log(&Gamma;(x)).
     */
    public static final double[] L15 = {
        0.99999999999999709182,
        57.156235665862923517,
        -59.597960355475491248,
        14.136097974741747174,
        -0.49191381609762019978,
        0.33994649984811888699e-4,
        0.46523628927048575665e-4,
        -0.98374475304879564677e-4,
        0.15808870322491248884e-3,
        -0.21026444172410488319e-3,
        0.21743961811521264320e-3,
        -0.16431810653676389022e-3,
        0.84418223983852743293e-4,
        -0.26190838401581408670e-4,
        0.36899182659531622704e-5};

    /**
     * TODO: tests fail for non-integer values, for some reason
     *
     * @param x argument for which to compute gamma function. Must be &lt; 21
     * @return &Gamma;(x) or &infin; if x &ge; 21
     * @throws IllegalArgumentException if x &le; 0
     */
    public static double gamma(double x) {
        double y = 0.0;

        if (x <= 0.0) {
            throw new IllegalArgumentException(
                    "Gamma function requires positive argument: " + x);
        }
        if (x < 21.0) {
            y = exp(logGamma(x));
        } else {
            return POSITIVE_INFINITY;
        }

        return y;
    }

    /**
     * Uses Lanczos approximation with g = 607/128 and 15 terms
     *
     * @param x argument for finding log(&Gamma;(x))
     * @return log(&Gamma;(x))
     */
    public static double logGamma(double x) {
        if (x <= -1) {
            return Double.NaN;
        } else {
            x = x - 1.0;
        }
        double a = L15[0];
        for (int i = 1; i < 15; ++i) {
            a += L15[i] / (x + i);
        }

        double tmp = x + (607.0 / 128.0 + 0.5);
        return (LOG_2PI_OVER_2 + log(a)) + (x + 0.5) * log(tmp) - tmp;
    }

}
