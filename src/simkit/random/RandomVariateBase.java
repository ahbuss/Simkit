package simkit.random;
/*
 *  <p>The set/get parameters really should be implemented in the subclass, so these have been
 *  removed. 31 Aug 2001.
*/
/**
 *  <p>The base class for Simkit's random variate generator classes.  It is
 *  abstract because the <CODE>generate()</CODE> method of <CODE>RandomVariate</CODE>
 *  is not implemented.  The easiest way to write a <CODE>RandomVariate</CODE> is to
 *  subclass and implement <CODE>generate()</CODE>.  It is also advised that subclasses
 *  add accessor methods for their parameters using the set/get template.
 *
 *
 *  @author Arnold Buss
 *  @version $Id: RandomVariateBase.java 1051 2008-02-27 00:14:47Z ahbuss $ 
 **/

public abstract class RandomVariateBase implements RandomVariate {
    
/**
* The supporting RandomNumber.
**/
    protected RandomNumber rng;
    //    private Object[] parameters ;
    
/**
* Creates a new RandomVariateBase with the default RandomNumber.
**/
    public RandomVariateBase() {
        setRandomNumber(RandomVariateFactory.getDefaultRandomNumber());
    }
    
    /**
     * Sets the supporting RandomNumber for this RandomVariate.
     *  Use this if another random number generator besides the default is desired
     *  @param rng The <CODE>RandomNumber</CODE> instance to use for Un(0, 1) random numbers.
     **/
    public void setRandomNumber(RandomNumber rng) { this.rng = rng; }
    
    /**
     * Returns the instance of the supporting RandomVariate.
     *  @return The <CODE>RandomNumber</CODE> instance currently being used.
     **/
    public RandomNumber getRandomNumber() { return rng; }

}
