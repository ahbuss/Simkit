package simkit.random;

public class ExponentialTransform implements RandomVariate {

    private RandomVariate rv;

    public ExponentialTransform(RandomVariate rvariate) {
        rv = RandomVariateFactory.getInstance(rvariate);
    }

    public double generate() {
        return Math.exp(rv.generate());
    }

    public void setSeed(long seed) {rv.getRandomNumber().setSeed(seed);}
    public void resetSeed() {rv.getRandomNumber().resetSeed();}

    public long getSeed() { return rv.getRandomNumber().getSeed(); }

     public Object[] getParameters() {return rv.getParameters();}

    public void setParameters(Object[] params) {rv.setParameters(params);}

    public RandomNumber getRandomNumber() { return rv.getRandomNumber(); }

    public void setRandomNumber(RandomNumber rng) { rv.setRandomNumber(rng); }
    
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {}
        finally{}
        return null;
    }
}