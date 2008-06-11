
package simkit.smdx;

import simkit.Named;
import simkit.PropertyChangeSource;
/**
* A SimEntity implementing this interface will be able to be displayed in the
* Simkit Viewer. 
* <br/>This interface is intended to allow for additional optional properties
* in additional to those specified by the setters and getters. An example of
* these properties would be damage state and other things that might be useful
* for determining the correct symbol to display for the entity, but that
* don't make sense for all entities.
*
* @version $Id: Displayable.java 782 2005-05-23 16:58:54Z ahbuss $
* @author John Ruck (Rolands and Associates Corporation 4/12/05)
**/
public interface Displayable extends Named, PropertyChangeSource, Moveable, Sided {

/**
* Set the property to the given value. If the property does not exist add it as
* a new property. 
* (What to do when the property is not found is not secified in PropertyChangeSource.
* The implementation in PropertyChangeDispatcher throws an Exception when the 
* property is not found.)
**/
    public void setProperty(String name, Object value);

/**
* Get the value of the property. If the propert is not found, return null.
* (What to do when the property is not found is not secified in PropertyChangeSource.
* The implementation in PropertyChangeDispatcher throws an Exception when the 
* property is not found.)
**/ 
    public Object getProperty(String name);

/**
* The battle dimension or operating medium of the entity.
**/
    public BattleDimension getBattleDimension();

/**
* An unique id for this Displayable.
**/
    public int getSerial();
    
    /**
     * @return the properties that have been dynamically added.
     */
    public String[] getAddedProperties();
}

