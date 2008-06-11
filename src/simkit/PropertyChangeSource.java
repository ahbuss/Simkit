package simkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
/**
 *  Basic interface for property sources. (Since java.beans does not do
 *  it.)  The usual stuff, inspired by Arent Arntzen's Modkit ModPropertySource.
 *  For Simkit's purposes, however, we can probably reuse the rest
 *  from java.beans -- that is, fire PropertyChangeEvents on PropertyChangeListeners
 *
 *  <P>The listeners will be java.beans.PropertyChangeListeners, who will have
 *  their propertyChanged(PropertyChangeEvent) method invoked (not enforced).
 *
 *  @author Arnold Buss
 *  @version $Id: PropertyChangeSource.java 773 2005-05-12 19:49:06Z ahbuss $
 **/

public interface PropertyChangeSource {
    
    /**
     * Set the given property to the given value.
     *  @param name The name of the property to be set
     *  @param value The new value of the property
     **/
    public void setProperty(String name, Object value);
    
    /**
     * Get the value of a property
     *  @param name The name of the property to be retrieved
     *  @return The value of the property
     **/
    public Object getProperty(String name);
    
    /**
     * Get the value of a property if able; if not able return the 
     * provided default value.
     *  @param name The name of the property to be retrieved.
     *  @param defaultValue The default value -- returned if property's value cannot
     *         be returned or is null.
     *  @return The value of the property.
     **/
    public Object getProperty(String name, Object defaultValue);
    
    /**
     *  Notify all PropertyChangeListeners of the PropertyChangeEvent.
     *  @param event The event with all the information about what property has changed
     *  and to what value.
     **/
    public void firePropertyChange(PropertyChangeEvent event);
    
    /**
     * Adds a PropertyChangeListener to this PropertyChangeSource. The listener will
     * be notified of all property changes by calling firePropertyChange.
     *  @param listener The new listener to all my property changes.
     **/
    public void addPropertyChangeListener(PropertyChangeListener listener);
    
    /**
     * Adds a PropertyChangeListener to this PropertyChangeSource for a specific property.
     * The listener will be notified when the specified property changes by calling firePropertyChange.
     *  @param propertyName The name of the property the listener is interested in.
     *  @param listener The new listener to all my property changes.
     **/
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);

    /**
     * Causes the PropertyChangeListener to stop listening. Note that if this listener
     * was also listening to specific properties, it will continue to listen to those properties
     *  @param listener The listener that will stop listening to my property changes.
     **/
    public void removePropertyChangeListener(PropertyChangeListener listener);
    
    /**
     * Causes the PropertyChangeListener to stop listening. 
     *  @param propertyName The name of the property to stop listening to.
     *  @param listener The listener that will stop listening to my property changes.
     **/
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener);

/**
 * Get an Array containing all of the PropertyChangeListeners. This includes
 * listeners registered for all properties and specific properties.
 * The array may contain a mixture of PropertyChangeListeners (for the listeners
 * listening to all properties) and PropertyChangeListenerProxies (for the listeners
 * listening to a specific property.) See 
 * @link PropertyChangeSupport#getPropertyChangeListeners()
 * for more information.
 *  @return An array of PropertyChangeListeners.
 */
    public PropertyChangeListener[] getPropertyChangeListeners();
    
    /**
     * @return an array of the names of all added properties
     */ 
    public String[] getAddedProperties();
    
}
