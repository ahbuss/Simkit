package simkit.random;
/**
 *  <p>The base class for Simkit's random variate generator classes.  It is
 *  abstract because the <CODE>generate()</CODE> method of <CODE>RandomVariate</CODE>
 *  is not implemented.  The easiest way to write a <CODE>RandomVariate</CODE> is to
 *  subclass and implement <CODE>generate()</CODE>.  It is also advised that subclasses
 *  add accessor methods for their parameters using the set/get template.
 *
 *
 *  @author Arnold Buss
 *   
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
        // Change made due to Findbugs warning
        // UR_UNINIT_READ_CALLED_FROM_SUPER_CONSTRUCTOR which alerted on 
        // a subclass (ConvolutionVariate).
        //
        // One should not call overridable methods from the constructor
        // because the classes doing the overriding are not yet initialized.
        // For example, in ConvolutionVariate, setRandomNumber has to
        // apply the change to an array of objects, and that array
        // cannot yet be initialized.
        //
        //setRandomNumber(RandomVariateFactory.getDefaultRandomNumber());
        rng = RandomVariateFactory.getDefaultRandomNumber();
    }
    
    /**
     * Sets the supporting RandomNumber for this RandomVariate.
     *  Use this if another random number generator besides the default is desired
     *  @param rng The <CODE>RandomNumber</CODE> instance to use for Un(0, 1) random numbers.
     **/
    @Override
    public void setRandomNumber(RandomNumber rng) { this.rng = rng; }
    
    /**
     * Returns the instance of the supporting RandomVariate.
     *  @return The <CODE>RandomNumber</CODE> instance currently being used.
     **/
//    @Override
    public RandomNumber getRandomNumber() { return rng; }
}
