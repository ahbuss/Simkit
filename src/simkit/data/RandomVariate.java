package simkit.data;
/**
 *  22 October 1998 Started.
 * <P> The common interface for all random variate generation classes and the
 * parent interface for specialized random variate interfaces.  Typically an
 * instance is retrieved using an Abstract Factory, handing the factory a String
 * with the name of the algorithm and (optionally) either the name of the RandomNumber
 * instance or a reference to one.  This method returns a <CODE>double</CODE>
 * since a random variate can always be cast to one.  More specialized classes
 * may choose to generate an int or some other thing.
 *
 * @author Arnold Buss
**/
public interface RandomVariate {

/**
 *  Generate a random variate having this class's distribution.
**/
  public double generate();

/**
 *  Sets the random variate's parameters.
 *  Alternatively, the parameters could be set in the constructor or
 *  in additional methods provided by the programmer.
 * @param params the array of parameters, wrapped in objects.
**/
  public void setParameters(Object[] params);

/**
 *  Returns the array of parameters as an Object[].
**/
  public Object[] getParameters();

/**
 *  Resets the seed to the original value
**/
  public void resetSeed();

/**
 *  Sets the seed to the value passed
 *  @param newSeed the new seed for the underlying RandomNumber
**/
  public void setSeed(long newSeed);

/**
 *  Returns the seed to the value passed
 *  @return the current seed for the underlying RandomNumber
**/
  public long getSeed();

}
