package simkit.random;

public class WeibullVariate extends RandomVariateBase{
    private double alpha;
    private double alphaInverse;
    private double beta;
    
    public WeibullVariate() {
    }
    public double generate() {
        return beta * Math.pow(- Math.log(rng.draw()), alphaInverse);
    }
    public void setParameters(Object[] params) {
        if (params.length != 2) {
            throw new IllegalArgumentException("Must bave two parameters for Weibull, "
            + params.length + " given");
        }
        if (params[0] instanceof Number && params[1] instanceof Number) {
            alpha = ((Number) params[0]).doubleValue();
            alphaInverse = 1.0 / alpha;
            beta = ((Number) params[1]).doubleValue();
        }
        else {            
            throw new IllegalArgumentException("For Weibull both parameters must "+
            " be numbers: (" + params[0].getClass().getName() + ", " +
            params[1].getClass().getName() + ") given" );
        }
    }
    public Object[] getParameters() {
        return new Object[] {new Double(alpha),  new Double(beta)};
    }
    public String toString() { return "Weibull (" + beta + ", " + alpha + ")"; }
}