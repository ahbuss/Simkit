package simkit.random;

public class GammaVariate extends RandomVariateBase {

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
        if (params.length != 2) {
            throw new IllegalArgumentException("Must have 2 parameters for GammaVariate");
        }
        else if (params[0] instanceof Number && params[1] instanceof Number) {
             this.setAlpha(((Number) params[0]).doubleValue());
            
             this.setBeta(((Number) params[1]).doubleValue());
            this.setConvenienceParameters();
        }
        else {
            throw new IllegalArgumentException("Parameters for GammaVariate must be of type Number");
        }
    }
    
    public Object[] getParameters() { return new Object[] { new Double(alpha), new Double(beta) }; }
    
    public void setbeta(double beta) {
        if (beta <= 0.0) {
            throw new IllegalArgumentException("Parameter beta must be positive");
        }
        else {
            this.beta = beta;
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
    public double getAlpha() {return alpha;}
    public double getBeta() {return beta;}

    public String toString() { return "Gamma (" + getAlpha() + ", " + getBeta() + ")"; }
}
