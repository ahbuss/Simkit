package simkit.random;

public class GammaVariate extends RandomVariateBase {

    private static final double DEFAULT_ALPHA = 1.0;
    private static final double DEFAULT_BETA = 1.0;

    private double alpha;
    private double beta;

    private double b;
    private double p;
    private double a;
    private double q;
    private double theta;
    private double d;
    private double z;
    private double u1;
    private double v;
    private double w;
    private double alphaInv;


    public GammaVariate() {
    }

    public void setParameters(Object[] params) {
        if (params == null) {
            super.setParameters(new Object[]{new Double(DEFAULT_ALPHA), new Double(DEFAULT_BETA)});
        }
        else {
        switch(params.length) {
            case 1:
                super.setParameters(new Object[]{params[0], new Double(DEFAULT_BETA)});
                break;
            case 2:
                super.setParameters(params);
                break;
            default:
                throw new IllegalArgumentException(this.getClass().getName() + " must have two arguments: " + params.length +
                                             " passed");
        }
        setConvenienceParameters();
        }
    }

    public double generate() {
        double y = 0.0;
        if (alpha < 1.0) {
            while ( true ) {
                p = b * rng.draw();
                if ( p <= 1) {
                    y = Math.pow(p, alphaInv);
                    if ( rng.draw() <= Math.exp(-y) ) {
                        break;
                    }
                } else {
                    y = - Math.log( (b - p)/alpha);
                    if (rng.draw() <= Math.pow( y, alpha - 1.0) ) {
                        break;
                    }
                }
            }
        } else {
            while (true) {
                u1 = rng.draw();
                v = a * Math.log( u1 / (1.0 - u1) );
                y = alpha * Math.exp(v);
                z = u1 * u1 * rng.draw();
                w = b + q * v - y;
                if (w + d - theta * z >= 0) {
                    break;
                } else {
                    if (w >= Math.log(z) ) {
                        break ;
                    }
                }
            }
        }
        return y * beta;
    }


    protected void setConvenienceParameters() {
        alpha = getAlpha();
        beta = getBeta();
        if (alpha < 1.0) {
        b = 1.0 + alpha / Math.E;
        alphaInv = 1.0 / alpha;
        }
        else {
        a = 1.0 / Math.sqrt(2.0 * alpha - 1.0);
        b = alpha - 1.38629436111989061883;    // Number is Math.log(4)
        q = alpha + 1.0 / a;
        theta = 4.5;
        d = 2.504077396776274;        // Math.log(1 + theta)
        }
    }

    public double getAlpha() {return ((Number)getParameters()[0]).doubleValue();}
    public double getBeta() {return ((Number)getParameters()[1]).doubleValue();}

    public String toString() { return "Gamma (" + getAlpha() + ", " + getBeta() + ")"; }
}
