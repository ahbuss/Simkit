package simkit.random;

public class BivariateNormal implements RandomVector {

    private RandomVariate[] rv;
    private double correlation;

    public BivariateNormal() {
        rv = new RandomVariate[2];
        RandomNumber rng = RandomNumberFactory.getInstance();
        rv[0] = RandomVariateFactory.getInstance("simkit.random.Normal",
            new Object[] { new Double(0.0), new Double(1.0) }, rng);
        rv[1] = RandomVariateFactory.getInstance("simkit.random.Normal",
            new Object[] { new Double(0.0), new Double(1.0) }, rng);
    }

    public void setParameters(Object[] params) {
        boolean goodData = true;
        if (params.length != 5) {
            throw new IllegalArgumentException("Need 5 elements; " +
                params.length + " provided");
        }
        double[] par = (double[]) params[0];
        rv[0].setParameters(new Object[] { new Double(par[0]), new Double(par[1])} );
        par = (double[]) params[1];
        rv[1].setParameters(new Object[] { new Double(par[0]), new Double(par[1])} );
        correlation = ((Number) params[2]).doubleValue();
    }

    public Object[] getParameters() {
        return new Object[] {};
    }

    public void setRandomNumber(RandomNumber rng) {
        for (int i = 0; i < 2; i++) {
            rv[i].setRandomNumber(rng);
        }
    }

    public RandomNumber getRandomNumber() { return rv[0].getRandomNumber(); }

    public double[] generate() {
        double[] val = new double[rv.length];
        for (int i = 0; i < val.length; i++) {
            val[i] = rv[i].generate();
        }
        return val;
    }
} 