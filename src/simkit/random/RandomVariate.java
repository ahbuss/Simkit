package simkit.random;

/*
 * <P> 22 October 1998 Started.
 * <P> 26 January 2001 Moved to <CODE>simkit.random</CODE> package
*/
/**
 * <P> The common interface for all random variate generation classes and the
 * parent interface for specialized random variate interfaces.  Typically an
 * instance is retrieved using an Abstract Factory, handing the factory a String
 * with the name of the algorithm and (optionally) either the name of the RandomNumber
 * instance or a reference to one. <p>The generate method returns a <CODE>double</CODE>
 * since a random variate can always be cast to one.  More specialized classes
 * may choose to generate an int or some other thing.</p>
 *
 * @author Arnold Buss
 * @version $Id$
 **/

public interface RandomVariate extends java.io.Serializable {
    
    /**
     * Generate a random variate having this class's distribution.
     * @return The generated random variate
     */
    public double generate();
    
    /**
     *  Sets the random variate's parameters.
     *  Alternatively, the parameters could be set in the constructor or
     *  in additional methods provided in the implementation.
     * @param params the array of parameters, wrapped in objects.
     **/
    public void setParameters(Object[] params);
    
    /**
     * Returns the array of parameters as an Object[].
     * @return The array of parameters as an Object[].
     */
    public Object[] getParameters();
    
    /** Sets the supporting RandomNumber object
     * @param rng The RandomNumber instance supporting the generating algorithm
     */
    public void setRandomNumber(RandomNumber rng);
    
    /**
     * Returns the instance of the supporting RandomNumber
     * @return The underlying RandomNumber instance.
     */
    public RandomNumber getRandomNumber();

}
