package simkit.random;

/**
 *  The base class for Simkit's random variate generator classes.  It is
 *  abstract because the <CODE>generate()</CODE> method of <CODE>RandomVariate</CODE>
 *  is not implemented.  The easiest way to write a <CODE>RandomVariate</CODE> is to
 *  subclass and implement <CODE>generate()</CODE>.  It is also advised that subclasses
 *  add accessor methods for their parameters using the set/get template. 
 *
 *  @author Arnold Buss
 *  @version 1.2.1 
**/

public abstract class RandomVariateBase implements RandomVariate {

    protected RandomNumber rng;
    private Object[] parameters ;

    public RandomVariateBase() {
        rng = RandomNumberFactory.getInstance();
    }

/**
 *  Sets the random variate's parameters.
 *  Alternatively, the parameters could be set in the constructor or
 *  in additional methods provided by the programmer.
 *  @param params the array of parameters, wrapped in objects.
**/
    public void setParameters(Object[] params) {
        if (params != null) {
            parameters = (Object[]) params.clone();
        }
    }

/**
 *  @return the array of parameters as an Object[].
**/
    public Object[] getParameters() {
        return (parameters == null) ? new Object[]{} : (Object[]) parameters.clone();
    }

/**
 *  Use this if another random number generator besides the default is desired
 *  @param gen The <CODE>RandomNumber</CODE> instance to use for Un(0, 1) random numbers.
**/
    public void setRandomNumber(RandomNumber rng) { this.rng = rng; }

/**
 *  @return The <CODE>RandomNumber</CODE> instance currently being used.
**/
    public RandomNumber getRandomNumber() { return rng; }

}