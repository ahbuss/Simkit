package simkit.data;

public class Antithetic implements RandomNumber {

    private RandomNumber rnDelegate;

    public Antithetic(RandomNumber delegate) {
        rnDelegate = delegate;
    }

    public void setSeed(long seed) {rnDelegate.setSeed(seed);}

    public long getSeed() {return rnDelegate.getSeed();}

    public void resetSeed() {rnDelegate.resetSeed();}

    public double draw() {return 1.0 - rnDelegate.draw();}


} 