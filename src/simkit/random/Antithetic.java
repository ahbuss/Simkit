package simkit.random;

public class Antithetic implements RandomNumber {
    
    protected static final String DEFAULT_DELEGATE = "simkit.random.Congruential";
    
    protected RandomNumber delegate;
    
    public Antithetic() {
        this(RandomNumberFactory.getInstance(DEFAULT_DELEGATE));
    }
    
    public Antithetic(RandomNumber delegate) {
        setRandomNumber(delegate);
    }
    
    public void setRandomNumber(RandomNumber rng) { delegate = rng; }
    
    public RandomNumber getRandomNumber() { return delegate; }
    
    public void setSeed(long seed) {delegate.setSeed(seed);}
    
    public long getSeed() {return delegate.getSeed();}
    
    public void resetSeed() {delegate.resetSeed();}
    
    public double draw() {return 1.0 - delegate.draw();}
    
    public void setSeeds(long[] seeds) { delegate.setSeeds(seeds); }
    
    public long[] getSeeds() { return delegate.getSeeds(); }
    
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {}
        finally{}
        return null;
    }
    
    public String toString() { return delegate.toString() + " [Antithetic]"; }
    
    public double getMultiplier() { return delegate.getMultiplier(); }
    
    public long drawLong() {
        return ((long) (1.0 / delegate.getMultiplier())) - delegate.drawLong();
    }
    
}