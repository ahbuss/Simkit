package simkit.random;

/**
 * <P> 26 January 2001 started
 * <P> The common interface for all random vector classes and the
 * parent interface for specialized random variate interfaces.  Typically an
 * instance is retrieved using an Abstract Factory, handing the factory a String
 * with the name of the algorithm and (optionally) either the name of the RandomNumber
 * instance or a reference to one.  This method returns a <CODE>double</CODE>
 * since a random variate can always be cast to one.  More specialized classes
 * may choose to generate an int or some other thing.
 *
 * @author Arnold Buss
**/

public interface RandomVector extends java.io.Serializable, Cloneable {

/**
 *  @return a random variate having this class's distribution.
**/
    public double[] generate();

/**
 *  Sets the random variate's parameters.
 *  Alternatively, the parameters could be set in the constructor or
 *  in additional methods provided by the programmer.
 * @param params the array of parameters, wrapped in objects.
**/
    public void setParameters(Object[] params);

/**
 *  @return the array of parameters as an Object[].
**/
    public Object[] getParameters();
/**
 *  @param rng The RandomNumber object to be used to generate the underlying Un(0,1)
 *          random numbers. 
**/
    public void setRandomNumber(RandomNumber rng);
/**
 *  @return the RandomNumber used for this RandomVector instance  
**/
    public RandomNumber getRandomNumber();
    
    public Object clone();
} 
