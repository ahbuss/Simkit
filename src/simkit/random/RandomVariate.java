package simkit.random;

/**
 * <P> 22 October 1998 Started.
 * <P> 26 January 2001 Moved to <CODE>simkit.random</CODE> package
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

public interface RandomVariate extends java.io.Serializable, Cloneable {
    
    /**
     * Generate a random variate having this class's distribution.
     * @return The generated random variate
     */
    public double generate();
    
    /**
     *  Sets the random variate's parameters.
     *  Alternatively, the parameters could be set in the constructor or
     *  in additional methods provided by the programmer.
     * @param params the array of parameters, wrapped in objects.
     **/
    public void setParameters(Object[] params);
    
    /**
     * Returns the array of parameters as an Object[].
     * @return the array of parameters as an Object[].
     */
    public Object[] getParameters();
    
    /** Sets the supporting RandomNumber object
     * @param rng The RandomNumber instance supporting the generating algorithm
     */
    public void setRandomNumber(RandomNumber rng);
    
    /**
     * @return The underlying RandomNumber instance (should be a copy)
     */
    public RandomNumber getRandomNumber();
    
    /** Note: copy should be typically shallow in that the same RandomNumber instance is supporting the copy.
     * @return Copy of this RandomVariate instance.
     */
    public Object clone();
}
