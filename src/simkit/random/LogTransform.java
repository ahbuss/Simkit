package simkit.random;

/**
* Generates random variates that are the log transform of another random variate.
* The underlying RandomVariate is specified in the constructor and cannot be
* changed. The <code>generate</code> method generates the underlying RandomVariate
* and if a positive number returns the natural log of it, if negative or zero
* it returns <code>Double.NaN</code>.
*
* @version $Id$
**/
public class LogTransform  implements RandomVariate {

/**
* The instance of the underlying RandomVariate.
**/
  private RandomVariate rv;

/**
* Constructs a new LogTransform based on the given RandomVariate with the given seed.
**/
  public LogTransform(RandomVariate rv, long seed) {
    this(rv);
    rv.getRandomNumber().setSeed(seed);
  }

/**
* Constructs a new LogTransform based on the given RandomVariate with the default
* seed(s).
**/
  public LogTransform(RandomVariate rv) {
    this.rv = rv;
  }

/**
* Generates the next LogTransform variate. If the underlying RandomVariate
* generates zero or a negative number, returns <code>Double.NaN</code>.
**/
  public double generate() {
    double original = rv.generate();
    if (original > 0) {
      return Math.log(original);
    }
    else {
      return Double.NaN;
    }
  }

/**
* Sets the seed of the supporting RandomNumber of the underlying RandomVariate.
**/
  public void setSeed(long seed) {rv.getRandomNumber().setSeed(seed);}

/**
* Resets the seed of the supporting RandomNumber of the underlying RandomVariate
* the the value last set.
**/
  public void resetSeed() {rv.getRandomNumber().resetSeed();}

/**
* Returns the current seed of the supporting RandomNumber of the underlying 
* RandomVariate.
**/
  public long getSeed() { return rv.getRandomNumber().getSeed(); }

/**
* Returns an array containing the parameters of the underlying RandomVariate.
**/
  public Object[] getParameters() {return rv.getParameters();}

/**
* Sets the parameters of the underlying RandomVariate.
**/
  public void setParameters(Object[] params) {rv.setParameters(params);}

/**
* Returns the instance of RandomNumber that supports the underlying
* RandomVariate.
**/
  public RandomNumber getRandomNumber() { return rv.getRandomNumber(); }

/**
* Sets the instance of RandomNumber that supports the underlying
* RandomVariate.
**/
  public void setRandomNumber(RandomNumber rng) { rv.setRandomNumber(rng); }

}
