package simkit.random;

/**
 * The common interface for all random vector classes.
 * Typically an
 * instance is retrieved using an Abstract Factory, handing the factory a String
 * with the name of the algorithm and (optionally) either the name of the RandomNumber
 * instance or a reference to one.  This method returns an array of <CODE>doubles</CODE>
 * since a random variate can always be cast to one.
 *
 * @author Arnold Buss
 * @version $Id$
**/

public interface RandomVector extends java.io.Serializable, Cloneable {

/**
 * Returns an array containing a vector having this class's distribution.
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
 * Returns an array containing the parameters of this RandomVector.
 *  @return the array of parameters as an Object[].
**/
    public Object[] getParameters();
/**
 * Sets the supporting RandomNumber.
 *  @param rng The RandomNumber object to be used to generate the underlying Un(0,1)
 *          random numbers. 
**/
    public void setRandomNumber(RandomNumber rng);
/**
 * Gets the instance of the supporting RandomNumber.
 *  @return the RandomNumber used for this RandomVector instance  
**/
    public RandomNumber getRandomNumber();
    
/**
* Makes a copy of this RandomNumber.
**/
    public Object clone();
} 
