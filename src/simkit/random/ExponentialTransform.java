package simkit.random;

/**
* Generates a random variate that is the exponential transform of another
* random variate. The underlying RandomVariate is specified in the constructor
* and cannot be changed. The generate method simple generates the underlying
* variate, then takes the exponential of it.
* <p><b>Note: This RandomVariate cannot be instantiated using the 
* RandomVariateFactory</b></p>
* 
* @version $Id$
**/
public class ExponentialTransform implements RandomVariate {

/**
* An instance of the underlying RandomVariate.
**/
    private RandomVariate rv;

/**
* Constructs a new ExponentialTransform with the given underlying
* RandomVariate.
**/
    public ExponentialTransform(RandomVariate rvariate) {
        rv = RandomVariateFactory.getInstance(rvariate);
    }

/**
* Generates the next exponentially transformed value.
**/
    public double generate() {
        return Math.exp(rv.generate());
    }

/**
* Sets the seed of the supporting RandomNumber of the underlying RandomVariate.
**/ 
    public void setSeed(long seed) {rv.getRandomNumber().setSeed(seed);}

/**
* Resets the seed of the supporting RandomNumber to the value of the
* last set seed.
**/
    public void resetSeed() {rv.getRandomNumber().resetSeed();}

/**
* Gets the current value of the seed of the supporting RandomNumber of
* the underlying RandomVariate.
**/
    public long getSeed() { return rv.getRandomNumber().getSeed(); }

/**
* Gets the parameters of the underlying RandomVariate.
**/
     public Object[] getParameters() {return rv.getParameters();}

/**
* Sets the parameters of the underlying RandomVariate.
**/
    public void setParameters(Object[] params) {rv.setParameters(params);}

/**
* Returns the instance of the supporting RandomNumber of the underlying
* RandomVariate.
**/
    public RandomNumber getRandomNumber() { return rv.getRandomNumber(); }

/**
* Sets the supporting RandomNumber of the underlying RandomVariate.
**/
    public void setRandomNumber(RandomNumber rng) { rv.setRandomNumber(rng); }
    
/**
* Returns a copy of this RandomVariate.
**/
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {}
        finally{}
        return null;
    }
}
