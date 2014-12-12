package simkit.random;

/**
 * Based on Devroye (1986), pp. 488-9.
 *
 * @version $Id$
 * @author ahbuss
 */
public class NegativeBinomialVariate extends RandomVariateBase implements DiscreteRandomVariate {

    private double r;
    private double p;
    private GammaVariate gammaVariate;
    private PoissonVariate poissonVariate;

    public NegativeBinomialVariate() {
        super();
        poissonVariate = (PoissonVariate) RandomVariateFactory.getDiscreteRandomVariateInstance("Poisson", 1.0);
        gammaVariate = (GammaVariate) RandomVariateFactory.getInstance("Gamma", 1.0, 1.0);
        this.p = 0.5;
        this.r = 1;
    }

    @Override
    public int generateInt() {
        double x = gammaVariate.generate();
        poissonVariate.setMean(x);
        return poissonVariate.generateInt();
    }

    @Override
    public double generate() {
        return this.generateInt();
    }

    @Override
    public void setParameters(Object... params) {
        if (params.length != 2) {
            throw new IllegalArgumentException(
                    "Negative Binomial needs 2 parameters: " + params.length);
        }
        if (params[0] instanceof Number && params[1] instanceof Number) {
            this.setR(((Number) params[0]).doubleValue());
            this.setP(((Number) params[1]).doubleValue());
        } else {
            throw new IllegalArgumentException(
                    "Need (Number, Number): ("
                    + params[0].getClass().getName() + ", "
                    + params[1].getClass().getName() + ")");
        }
    }

    @Override
    public Object[] getParameters() {
        return new Object[]{getR(), getP()};
    }

    /**
     * @return the r
     */
    public double getR() {
        return r;
    }

    /**
     * @param r the r to set
     */
    public void setR(double r) {
        if (r <= 0) {
            throw new IllegalArgumentException("r must be > 0: " + r);
        }
        this.r = r;
        gammaVariate.setParameters(getR(), getP() / (1.0 - getP()));
    }

    /**
     * @return the p
     */
    public double getP() {
        return p;
    }

    /**
     * @param p the p to set
     */
    public void setP(double p) {
        if (p <= 0.0 || p >= 1.0) {
            throw new IllegalArgumentException(
                    "p must be in (0.0, 1.0): " + p);
        }
        this.p = p;
        gammaVariate.setParameters(getR(),  getP()/(1.0 - getP()));
    }
    
    public String toString() {
        return "Negative Binomial (" + getR() + ", " + getP() + ")";
    }

    public GammaVariate getGammaVariate() {
        return gammaVariate;
    }

    public PoissonVariate getPoissonVariate() {
        return poissonVariate;
    }
}
