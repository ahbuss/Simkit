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

import simkit.util.*;

/**
 *  <P> A delegate for handling properties for an entity. 
 *  Uses reflection to set/get properties by name (String).  This will also
 *  fire a PropertyChangeEvent whenever a property is set/gotten using the
 *  setProperty() or getProperty() methods here.
 *  @version 1.1.1
**/
public class PropertyChangeDispatcher extends PropertyChangeSupport implements PropertyChangeSource {

/**
* Used internally to hold an instance of java.lang.Object.class.
**/
    private static final Class STOP_CLASS;

/**
* A two dimensional Hash Table to cache information about the properties
* of classes indexed by class and property name. The information is stored 
* as a PropertyDescriptor which includes
* information like the name of the setter and getter of a property.
**/
    private static simkit.util.Hashtable2 properties;
    static {
        STOP_CLASS = java.lang.Object.class;
        properties = new simkit.util.Hashtable2();
    }

/**
* The entity for which this PropertyChangeDispatcher manages properties.
**/
    private Object source;
    private PropertyChangeListener pcl; //appears unused

/**
* Create a new PropertyChangeDispatcher to manage properties for the given
* Object. The properties of the Object are automatically discovered
* using introspection.
**/
    public PropertyChangeDispatcher(Object bean) {
        super(bean);
        source = bean;
        Class beanClass = source.getClass();
        if (!properties.containsKey(beanClass)) {
            try {
                BeanInfo bi = Introspector.getBeanInfo(bean.getClass(), STOP_CLASS);
                PropertyDescriptor[] pd = bi.getPropertyDescriptors();
                for (int i = 0; i < pd.length; i++) {
                    properties.put(beanClass, pd[i].getName(), pd[i]);
                }
            }
            catch (IntrospectionException e) {System.err.println(e);}
        }
    }

/**
* Set the specified property to the given value, firing a PropertyChangeEvent
* to inform any registered listeners of the change.
* @throws NullPointerException If the property doesn't exist.
**/
    public void setProperty(String propertyName, Object propertyValue) {
        PropertyDescriptor pd = (PropertyDescriptor) properties.get(source.getClass(), propertyName);
        Method setter = pd.getWriteMethod();
        if (setter == null) { return; }
        try {
             setter.invoke(source, new Object[] {propertyValue});
             this.firePropertyChange(propertyName, null, propertyValue);
        }
        catch (InvocationTargetException e) {
            System.err.println(e.getTargetException());
        }
        catch (IllegalAccessException e) {System.err.println(e);}
    }

/**
* Get the value of the given property, firing a PropertyChangeEvent
* to inform any registered listeners of the current value of the 
* property.
* @throws NullPointerException If the property doesn't exist.
**/
    public Object getProperty(String propertyName) {
        PropertyDescriptor pd = (PropertyDescriptor) properties.get(source.getClass(), propertyName);
        Method getter = pd.getReadMethod();
        if (getter == null) { return null; }
        Object result = null;
        try {
             result = getter.invoke(source, null);
             firePropertyChange(propertyName, null, result);
        }
        catch (InvocationTargetException e) {
            System.err.println(e.getTargetException());
        }
        catch (IllegalAccessException e) {System.err.println(e);}
        return result;
    }

/**
* Get the value of the given property or return the given default.
* @return The value of the property if found and not null, otherwise the 
* default value.
* @throws NullPointerException If the property doesn't exist.
**/
    public Object getProperty(String propertyName, Object defaultValue) {
        Object prop = getProperty(propertyName);
        return (prop != null) ? prop : defaultValue;
    }

} 
