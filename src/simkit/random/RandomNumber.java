package simkit.random;

import java.io.Serializable;

/**
 *  <P> Started 22 October 1998
 *  <P> Moved to <CODE>simkit.random</CODE> 26 January 2001
 *  <P> The common interface for all classes that generate random numbers.
 *      These are Un(0,1) numbers only. Presumably an instance is either gotten
 *      from an Abstract Factory or supplied by the user.
 *  <P> Added a <CODE>long[]</CODE> property called <CODE>seeds</CODE> for
 *      generators that reuire more than one seed.
 *
 * @author Arnold Buss
**/
public interface RandomNumber extends java.io.Serializable, Cloneable {

/**
  * @param seed The new random number seed
**/
    public void setSeed(long seed);

/**
  * @return  The current random number seed
**/
    public long getSeed();
/**
 *  Resets seed to last setSeed() value
**/
    public void resetSeed();
/**
  * @param seed The new array of seeds
**/
    public void setSeeds(long[] seed);
/**
  * @return  The current array of random number seed s
**/
    public long[] getSeeds();

/**
  * @return  The next Uniform(0, 1) random number
**/
    public double draw();
    
    public Object clone();
    
    public long drawLong();
}
