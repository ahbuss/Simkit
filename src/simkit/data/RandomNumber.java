package simkit.data;
/**
 *  Started 22 October 1998
 * <P> The common interface for all classes that generate random numbers.
 * These are Un(0,1) numbers only. Presumably an instance is either gotten
 * from an Abstract Factory or supplied by the user.
 *
 *  @author Arnold Buss
 *  @deprecated This interface has been replaced by <CODE>simkit.random.RandomNumber</CODE>
 *  @see simkit.random.RandomNumber
**/
public interface RandomNumber {

/**
  * @param seed The new random number seed
**/
  public void setSeed(long seed);
  public long getSeed();
  public void resetSeed();
  public double draw();
  
}
