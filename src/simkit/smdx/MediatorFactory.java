/*
 * Mediator.java
 *
 * Created on March 6, 2002, 9:09 PM
 */

package simkit.smdx;
import java.util.*;
/**
 * Holds instances of Mediators indexed by the two Classes 
 * that this Mediator is used for.
 * @author  Arnold Buss
 * @version $Id$
 */
public interface MediatorFactory {
    
/**
* Returns a Map containing the Mediators currently held by this
* factory.
**/
    public Map getMediators();
    
/**
* Construct and adds a Mediator to this factory.
* @param first One of the Classes that the Mediator is used for.
* @param second The other Class that the Mediator is used for.
* @param mediator The Class of the Mediator to construct and add to this
* factory.
**/
    public void addMediatorFor(Class first, Class second, Class mediator);
    
/**
* Construct and adds a Mediator to this factory.
* @param first The name of one of the Classes that the Mediator is used for.
* @param second The name of the other Class that the Mediator is used for.
* @param mediator The name of the Class of the Mediator to construct and add to this
* factory.
* @throws ClassNotFoundException If any of the names are not names
* of valid Classes.
**/
    public void addMediatorFor(String first, String second, String mediator)
        throws ClassNotFoundException;
    
/**
* Gets the Mediator for the given Objects.
**/
    public Mediator getMeditorFor(Object first, Object second);
    
/**
* Gets the Mediator for the given Classes.
**/
    public Mediator getMediatorFor(Class firstClass, Class secondClass);
    
/**
* Removes all of the Mediators from this factory.
**/
    public void clear();

}

