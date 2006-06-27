/*
 * PropertyChangeNamespace.java
 *
 * Created on July 13, 2002, 12:25 PM
 */

package simkit;

/**
 * Used to listen for a PropertyChangeEvent, then prepend the namespace to the property name and 
 * refire the event. 
 * <P> This class is useful for collecting data on multiple SimEntities of
 * the same type. It allows you to distinguish between properties with the same name on 
 * different SimEntities. For example, to collect data on a number of identical servers,
 * A PropertyChangeNamespace is created with a unique namespace as a PropertyChangeListener
 * for each server. Then the class responsible for collecting the statistics is 
 * registered as a listener to each of the PropertyChangeNamespaces. Since the
 * namespace is prepended to the property name, the server on which the property
 * changed can be distinguished by the statistics collector.
 *
 * @author  Arnold Buss
 * @version $Id$
 */
public class PropertyChangeNamespace extends PropertyChangeDispatcher implements java.beans.PropertyChangeListener {
    
/**
* The String to prepend to the property name.
**/
    private String nameSpace;
    
/** 
* Creates a new instance of PropertyChangeNamespace.
* @param source The Object this PropertyChangeNamespace will manage properties for.
* @param name The String to prepend to the property name.
* @see PropertyChangeDispatcher
**/
    public PropertyChangeNamespace(Object source, String name) {
        super(source, Object.class);
        setNamespace(name);
    }
    
/**
* Re-fires the PropertyChangeEvent with the namespace prepended to the property name.
**/
    public void propertyChange(java.beans.PropertyChangeEvent e) {
        firePropertyChange(nameSpace + e.getPropertyName(), e.getOldValue(), e.getNewValue());
    }
    
/**
* The String to prepend to the property name.
**/
    public String getNameSpace() { return nameSpace; }
    
/**
* Set the String to prepend to the property name.
**/
    public void setNamespace(String name) {
        nameSpace = (name == null || name.equals("")) ? "" : name + ':';
    }
    
    public String toString() { return "PropertyChangeNamespace " + nameSpace; }
}
