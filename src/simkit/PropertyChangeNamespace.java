/*
 * PropertyChangeNamespace.java
 *
 * Created on July 13, 2002, 12:25 PM
 */

package simkit;

/**
 *
 * @author  Arnold Buss
 */
public class PropertyChangeNamespace extends PropertyChangeDispatcher implements java.beans.PropertyChangeListener {
    
    private String nameSpace;
    
    /** Creates a new instance of PropertyChangeNamespace */
    public PropertyChangeNamespace(Object source, String name) {
        super(source);
        setNamespace(name);
    }
    
    public void propertyChange(java.beans.PropertyChangeEvent e) {
        firePropertyChange(nameSpace + e.getPropertyName(), e.getOldValue(), e.getNewValue());
    }
    
    public String getNameSpace() { return nameSpace; }
    
    public void setNamespace(String name) {
        nameSpace = (name == null || name.equals("")) ? "" : name + ':';
    }
    
    public String toString() { return "PropertyChangeNamespace " + nameSpace; }
}