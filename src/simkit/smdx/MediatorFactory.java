/*
 * Mediator.java
 *
 * Created on March 6, 2002, 9:09 PM
 */
package simkit.smdx;

import java.util.Map;

/**
 * Holds instances of Mediators indexed by the two Classes that the Mediator is
 * used for.
 *
 * @author Arnold Buss
 * 
 */
public interface MediatorFactory<T1, T2, T3> {

    /**
     * 
     * @return a Map containing the Mediators currently held by this factory.
     */
    @SuppressWarnings("unchecked")
    public Map getMediators();

    /**
     * Construct and adds a Mediator to this factory.
     *
     * @param first One of the Classes that the Mediator is used for.
     * @param second The other Class that the Mediator is used for.
     * @param mediator The Class of the Mediator to construct and add to this
     * factory.
     *
     */
    public void addMediatorFor(Class<? extends T1> first, Class<? extends T2> second, Class<? extends T3> mediator);

    /**
     * Construct and adds a Mediator to this factory.
     *
     * @param first The name of one of the Classes that the Mediator is used
     * for.
     * @param second The name of the other Class that the Mediator is used for.
     * @param mediator The name of the Class of the Mediator to construct and
     * add to this factory.
     * @throws ClassNotFoundException If any of the names are not names of valid
     * Classes.
     *
     */
    public void addMediatorFor(String first, String second, String mediator)
            throws ClassNotFoundException;

    public <S extends T3> void addMediatorFor(Class<? extends T1> first, Class<? extends T2> second, S mediatorInstance);

    public <S1 extends T1, S2 extends T2, S3 extends T3> void
            addMediatorFor(S1 first, S2 second, S3 mediatorInstance);

    /**
     * Gets the Mediator for the given Objects.
     *
     * @param <S1> generic for first object
     * @param <S2> generic for second object
     * @param first Given first object
     * @param second Given second object
     * @return Mediator for first &amp; second objects
     */
    public <S1 extends T1, S2 extends T2> Mediator
            getMeditorFor(S1 first, S2 second);

    /**
     *
     * @param firstClass Given first class
     * @param secondClass Given second class
     * @return the Mediator for the given Classes
     */
    public Mediator getMediatorFor(Class<? extends T1> firstClass, Class<? extends T2> secondClass);

    /**
     * Removes all of the Mediators from this factory.
     *
     */
    public void clear();

}
