package simkit.random;

/**
* Generates antithetic random numbers. 
* <p>Given an instance of a RandomNumber, construct an instance of Antithetic
* using a <b>copy</b> of the given RandomNumber instance. Then a draw from the
* given RandomNumber and the Antithetic will be antithetic random numbers. (The
* draw from Antithetic will be 1- the draw from the original.) </p>
* <p>This relationship
* only remains true if the same number of draws are made from each. Therefore, if
* the original RandomNumber and the Antithetic are used as the supporting RandomNumbers
* for 2 RandomVariates in order to get negatively correlated streams, the
* implementation of the RandomVariate must use the same number of RandomNumbers
* for each RandomVariate draw (like an inverse transform implementation).
* If an implementation that uses a random number of RandomNumber draws is
* used (like accept/reject), then the synchronization will be lost causing the
* negative correlation to be lost.</p>
* 
* @version $Id: Antithetic.java 503 2003-12-19 21:53:06Z ahbuss $
**/

public class Antithetic implements RandomNumber {
    
/**
* The default is currently Congruential.
**/
    protected static final String DEFAULT_DELEGATE = "simkit.random.Congruential";
  
/**
* The supporting RandomNumber instance.
**/  
    protected RandomNumber delegate;
    
/**
* Creates a new Antithetic based on an new instance of the default RandomNumber.
**/
    public Antithetic() {
        this(RandomNumberFactory.getInstance(DEFAULT_DELEGATE));
    }
    
/**
* Creates a new Antithetic based on the given RandomNumber. If the desire
* is to create 2 antithetic random number streams, then the RandomNumber
* instance supplied should be a copy of the original.
**/
    public Antithetic(RandomNumber delegate) {
        setRandomNumber(delegate);
    }
    
/**
* Sets the instance of the supporting RandomNumber. If the desire
* is to create 2 antithetic random number streams, then the RandomNumber
* instance supplied should be a copy of the original.
**/
    public void setRandomNumber(RandomNumber rng) { delegate = rng; }
    
/**
* Returns the instance of the supporting RandomNumber.
**/
    public RandomNumber getRandomNumber() { return delegate; }
    
/**
* Sets the seed of the supporting RandomNumber.
**/
    public void setSeed(long seed) {delegate.setSeed(seed);}
    
/**
* Gets the current value of the seed of the supporting RandomNumber.
**/
    public long getSeed() {return delegate.getSeed();}
    
/**
* Sets the seed to the value of the last setSeed.
**/
    public void resetSeed() {delegate.resetSeed();}
    
//javadoc inherited.
    public double draw() {return 1.0 - delegate.draw();}
    
//javadoc inherited.
    public void setSeeds(long[] seeds) { delegate.setSeeds(seeds); }
    
//javadoc inherited.
    public long[] getSeeds() { return delegate.getSeeds(); }

/**
* Returns a String containing the name of this RandomNumber.
**/
    public String toString() { return delegate.toString() + " [Antithetic]"; }
    
//javadoc inherited.
    public double getMultiplier() { return delegate.getMultiplier(); }
    
//javadoc inherited.
    public long drawLong() {
        return ((long) (1.0 / delegate.getMultiplier())) - delegate.drawLong();
    }
    
}
