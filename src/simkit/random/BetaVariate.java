package simkit.random;

/** Generates Beta(alpha, beta) random variates.
 *  mean =  alpha/(alpha + beta)
 *
 * It uses the ratio of two Gamma variates to generate a Beta -
 * perhaps not the most efficient way, but gets the job done.
 *
 * @author Arnold Buss
 */
public class BetaVariate extends RandomVariateBase {

    private double alpha;
    private double beta;

    private RandomVariate gammaVariate1;
    private RandomVariate gammaVariate2;

    public BetaVariate() {
    }

    public double generate() {
        double u1 = gammaVariate1.generate();
        double u2 = gammaVariate2.generate();
        return u1 / (u1 + u2);
    }

    /**
     * @param params (alpha, beta)
     */    
    public void setParameters(Object[] params) {
        if (params.length != 2) {
            throw new IllegalArgumentException(this.getClass().getName() + " must have two arguments: " + params.length +
                                             " passed");
        }
        else if (params[0] instanceof Number && params[1] instanceof Number) {
            this.setAlpha(((Number) params[0]).doubleValue());
            this.setBeta(((Number) params[1]).doubleValue());
            this.setGammas(rng);
        }
        else {
            throw new IllegalArgumentException("Parameters both must be of type Number");
        }
    }
    
    public Object[] getParameters() { return new Object[] { new Double(alpha), new Double(beta) }; }

    private void setGammas(RandomNumber rng) {
        gammaVariate1 = RandomVariateFactory.getInstance("simkit.random.GammaVariate", new Object[] {new Double(alpha), new Double(1.0)}, rng);
        gammaVariate2 = RandomVariateFactory.getInstance("simkit.random.GammaVariate", new Object[] {new Double(beta), new Double(1.0)}, rng);
    }

    public double getAlpha() {return alpha; }
    public double getBeta() { return beta; }
    
    public void setAlpha(double a) {
        if ( a > 0.0) {
            alpha = a;
        }
        else {
            throw new IllegalArgumentException("Alpha parameter must be > 0.0");
        }
    }

    public void setBeta(double b) {
        if ( b > 0.0) {
            beta = b;
        }
        else {
            throw new IllegalArgumentException("Beta parameter must be > 0.0");
        }
    }

    public void setRandomNumber(RandomNumber rng) {
        super.setRandomNumber(rng);
        setGammas(rng);
    }
    
    public String toString() { return "Beta (" + getAlpha() + ", " + getBeta() + ")"; }

} 