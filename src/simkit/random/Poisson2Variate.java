package simkit.random;

import static java.lang.Double.NaN;
import static java.lang.Math.PI;
import static java.lang.Math.exp;
import static java.lang.Math.floor;
import static java.lang.Math.log;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static simkit.util.GammaFunction.logGamma;

/**
 *
 * @author ahbuss
 */
public class Poisson2Variate extends PoissonVariate {
    
    private double alpha;

    private double beta;

    private double k;

    private double c;

    @Override
    public int generateInt() {
        double x = NaN;
        double u1 = NaN;
        double u2 = NaN;
        int n;

        if (mean > 30.0) {
            do {
                do {
                    u1 = rng.draw();
                    x = (alpha - log((1.0 - u1) / u1)) / beta;
                } while (x < -0.5);
                n = (int) floor(x + 0.5);
                u2 = rng.draw();
            } while (alpha - beta * x + log(u2 / (1 + exp(pow(alpha - beta * x, 2))))
                    > k + n * log(mean) - logGamma(n + 1));
        } else {
            n = super.generateInt();
        }
        return n;
    }
    


    @Override
    public void setMean(double mean) {
        
        super.setMean(mean);
        beta = PI / sqrt(3.0 * mean);
        alpha = beta * mean;
        c = 0.767 - 3.36 / mean;
        k = log(c) - mean - log(beta);

    }

}
