package simkit.random;

import java.io.Serializable;

/*
 *  <P> Started 22 October 1998
 *  <P> Moved to <CODE>simkit.random</CODE> 26 January 2001
 *  <P> Added a <CODE>long[]</CODE> property called <CODE>seeds</CODE> for
 *      generators that reuire more than one seed.
*/
/**  <P> The common interface for all classes that generate random numbers.
 * RandomNumbers can output the generated number as an integer (long) or
 * a U(0,1).
 *
 * @author Arnold Buss
 * @version $Id$
**/
public interface RandomNumber extends java.io.Serializable, Cloneable {

/**
  * Set the random number seed for this RandomNumber.
  * @param seed The new random number seed
**/
    public void setSeed(long seed);

/**
  * Returns the current (not original) seed for this RandomNumber.
  * @return  The current random number seed
**/
    public long getSeed();
/**
 *  Resets seed to last setSeed() value
**/
    public void resetSeed();
/**
  * Sets the seeds of this RandomNumber to the given values.
  * @param seed The new array of seeds
**/
    public void setSeeds(long[] seed);
/**
  * Gets the current (not original) value of the seeds for this RandomNumber.
  * @return  The current array of random number seed s
**/
    public long[] getSeeds();

/**
  * Draws a random number and returns it as U(0,1)
  * @return  The next Uniform(0, 1) random number
**/
    public double draw();
    
    /**
     * Makes a deep copy.
     * @return Deep copy of this instance
     */    
    public Object clone();
    
    /**
     * Draws a random number and returns it as an integer.
     * @return Next integer value as a long
     */    
    public long drawLong();
    
    /**
     * Returns the value for this RandomNumber needed to scale
     * a number produced by drawLong to result in U(0,1).
     * @return Number that gives Un(0,1) when multiplied by return from drawLong()
     */    
    public double getMultiplier();
}
