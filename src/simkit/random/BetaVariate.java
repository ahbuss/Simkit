package simkit.random;

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

    public void setParameters(Object[] params) {
        if (params.length != 2) {
            throw new IllegalArgumentException(this.getClass().getName() + " must have two arguments: " + params.length +
                                             " passed");
        }
        else if (params[0] instanceof Number && params[1] instanceof Number) {
            alpha = ((Number) params[0]).doubleValue();
            beta = ((Number) params[1]).doubleValue();
        }
        else {
            throw new IllegalArgumentException("Parameters both must be of type Number");
        }
    }
    
    public Object[] getParameters() { return new Object[] { new Double(alpha), new Double(beta) }; }

    private void setGammas(RandomNumber rng) {
        gammaVariate1 = RandomVariateFactory.getInstance("simkit.random.GammaVariate", new Object[] {new Double(alpha)}, rng);
        gammaVariate2 = RandomVariateFactory.getInstance("simkit.random.GammaVariate", new Object[] {new Double(beta)}, rng);
    }

    public double getAlpha() {return alpha; }
    public double getBeta() { return beta; }

    public String toString() { return "Beta (" + getAlpha() + ", " + getBeta() + ")"; }

} 