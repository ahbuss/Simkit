package simkit;
/////////////////////////// Copyright Notice //////////////////////////
//                                                                   //
// This simkit package or sub-package and this file is Copyright (c) //
// 1997, 1998, 1999 by Kirk A. Stork and Arnold H. Buss.             //
//                                                                   //
// Please forward any changes, comments or suggestions to:           //
//   abuss@nps.navy.mil                                              //
//                                                                   //
///////////////////////////////////////////////////////////////////////

import java.beans.*;
import java.lang.reflect.*;
import java.util.*;

public abstract class PropertyChangeSourceImpl implements PropertyChangeSource {

  private Hashtable addedProperties;
  private Hashtable definedProperties;
  private Vector listeners;
  private String[] definedPropertyNames;

  public PropertyChangeSourceImpl() {
    listeners = new Vector();
    addedProperties = new Hashtable();
    definedProperties = new Hashtable();
    try {
      BeanInfo bi = Introspector.getBeanInfo(this.getClass());
      PropertyDescriptor[] pd = bi.getPropertyDescriptors();
      for (int i = 0; i < pd.length; i++ ) {
         definedProperties.put(capitalizePropertyName(pd[i].getName()), pd[i]);
      }
    }
    catch (IntrospectionException e) {System.err.println(e); e.printStackTrace(System.err);}
System.out.println(definedProperties);
  }
/**
 *  @param name The name of the property to be set
 *  @param value The new value of the property 
**/
  public void setProperty(String name, Object value) {
      if (value != null && name != null) {
        Object oldValue = getProperty(name);
        PropertyDescriptor pd =  (PropertyDescriptor) definedProperties.get(name);
        if (pd != null) {
          Method m = pd.getWriteMethod();
          if (m != null) {
            try {
              m.invoke(this, new Object[]{value});
            }
            catch (IllegalAccessException e) {System.err.println(e); e.printStackTrace(System.err);}
            catch(InvocationTargetException e) {
              System.err.println(e.getTargetException()); e.printStackTrace(System.err);
            }
          }
        }
        else {
          addedProperties.put(name, value);
        }
        notifyPropertyChangeListeners(new PropertyChangeEvent(this, name, oldValue, value));
      }
  }

/**
 *  @param name The name of the property to be retrieved
 *  @return The value of the property
**/
  public Object getProperty(String name) {
    if (name != null) {
      PropertyDescriptor pd = (PropertyDescriptor) definedProperties.get(name);
      if (pd != null) {
        Method m = pd.getReadMethod();
        try {
          return (m != null) ? m.invoke(this, null) : null;
        }
        catch(IllegalAccessException e) {System.err.println(e);e.printStackTrace(System.err);}
        catch(InvocationTargetException e) {
            System.err.println(e.getTargetException()); e.printStackTrace(System.err);
        }
      }
      else {
        return addedProperties.get(name);
      }
    }
    return null;
  }

/**
 *  @param name The name of the property to be retrieved.
 *  @param defaultValue The default value -- returned if property's value cannot
 *         be returned or is null. 
 *  @return The value of the property.
**/
  public Object getProperty(String name, Object defaultValue) {
    Object value = getProperty(name);
    return (value != null) ? value : defaultValue;
  }

/**
 *  Notify all PropertyChangeListeners of the PropertyChangeEvent.
 *  @param e The event with all the information about what property has changed
 *  and to what value.
**/
  public void notifyPropertyChangeListeners(PropertyChangeEvent event) {
     Vector copy = null;
     synchronized (listeners) {
       copy = (Vector) listeners.clone();
     }
     for (Iterator e = copy.iterator(); e.hasNext();) {
       ((PropertyChangeListener)e.next()).propertyChange(event);
     }
  }

/**
 *  @param listener The new listener to all my property changes.
**/
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    if (!listeners.contains(listener)) {
      synchronized(listeners) {
        listeners.addElement(listener);
      }
    }
  }

/**
 *  @param listener The listener that will stop lietening to my property changes.
**/
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    synchronized(listeners) {
      listeners.removeElement(listener);
    }
  }

/**
 *  @return Properties defined by this class
**/
  public String[] getDefinedPropertyNames() {
    String[] definedPropertyNames = new String[definedProperties.size()];
    int i = 0;
    for (Iterator e = definedProperties.keySet().iterator(); e.hasNext();) {
      definedPropertyNames[i++] = (String) e.next();
    }
    return definedPropertyNames;
  }

/**
 *  @return Properties that have been added to this object
**/
  public String[] getAddedPropertyNames() {
    String[] addedPropertyNames = new String[addedProperties.size()];
    int i = 0;
    for (Iterator e = addedProperties.keySet().iterator(); e.hasNext();) {
      addedPropertyNames[i++] = (String) e.next();
    }
    return addedPropertyNames;
  }

/**
 *  @return all Properties for this object
**/
  public String[] getPropertyNames() {
    int numberDefinedProperties = definedProperties.size();
    int numberAddedProperties = addedProperties.size();

    String[] propertyNames = new String[numberDefinedProperties + numberAddedProperties];
    System.arraycopy(getDefinedPropertyNames(), 0, propertyNames, 0, numberDefinedProperties );
    System.arraycopy(getAddedPropertyNames(), 0, propertyNames, numberDefinedProperties, numberAddedProperties);
    return propertyNames;
  }

/**
 *  Helper method to capitalize the first letter of a property's name
 *  @param name The property name needing capitalization.
**/
  protected static String capitalizePropertyName(String name) {
     char[] letters = name.toCharArray();
     letters[0] = Character.toUpperCase(letters[0]);
     return new String(letters);
  }


} 