package simkit.data;

/**
 *  The base class for Simkit's random variate generator classes.  It is
 *  abstract because the <CODE>generate()</CODE> method of <CODE>RandomVariate</CODE>
 *  is not implemented, but must be subclassed.  For performance reasons, the
 *  subclasses may also keep primitive copies of the parameters.
 *  @author Arnold Buss
 *  @deprecated This class has been replaced by <CODE>simkit.random.RandomVariateBase<CODE>
 *  @see simkit.random.RandomVariateBase
**/

public abstract class RandomVariateBase implements RandomVariate {

  protected RandomNumber rng;
  private Object[] parameters ;
  private Object[] paramsCopy;

  public RandomVariateBase(Object[] params) {
    this(params, RandomFactory.getRandomNumber());
  }

  public RandomVariateBase(Object[] params, long seed) {
    this(params, RandomFactory.getRandomNumber(), seed);
  }
  
  public RandomVariateBase(Object[] params, RandomNumber rng) {
    super();
    this.setRandomNumberGenerator(rng);
    setParameters(params);
  }

  public RandomVariateBase(Object[] params, RandomNumber rng, long seed) {
    this(params, rng);
    rng.setSeed(seed);
  }
/**
 *  Sets the random variate's parameters.
 *  Alternatively, the parameters could be set in the constructor or
 *  in additional methods provided by the programmer.
 * @param params the array of parameters, wrapped in objects.
**/
  public void setParameters(Object[] params) {
    if (params != null) {
      parameters = (Object[]) params.clone();
    }
  }

/**
 *  Returns the array of parameters as an Object[].
**/
  public Object[] getParameters() {
     return (parameters == null) ? new Object[]{} : (Object[]) parameters.clone();
  }

/**
 *  Resets the seed to the original value
**/
  public void resetSeed() {rng.resetSeed();}

/**
 *  Sets the seed to the value passed
 *  @param newSeed the new seed for the underlying RandomNumber
**/
  public void setSeed(long newSeed) {rng.setSeed(newSeed);}

    public long getSeed() { return rng.getSeed(); }

  public void setRandomNumberGenerator(RandomNumber gen) {this.rng = gen;}

}