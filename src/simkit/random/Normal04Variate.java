package simkit.random;

import static simkit.stat.NormalQuantile.getQuantile;

/**
 * Generates Normal random variates using the Inverse Transform method
 * 
 * @see simkit.stat.NormalQuantile
 * @author Arnold Buss
 * 
 */
public class Normal04Variate extends Normal02Variate {

    @Override
    public double generate() {
        double u = rng.draw();
        return getQuantile(u);
    }

}
