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

/**
 *  <P> Use reflection to set/get properties by name (String).  This will also
 *  fires a PropertyChangeEvent whenever a property is set/gotten using the
 "  setProperty() or getProperty() methods here.
 *  @version 1.1.1
**/
import java.beans.*;
import java.lang.reflect.*;
import java.util.*;

import simkit.util.*;

public class PropertyChangeDispatcher extends PropertyChangeSupport {

    private static final Class STOP_CLASS;
    private static simkit.util.Hashtable2 properties;
    static {
        STOP_CLASS = java.lang.Object.class;
        properties = new simkit.util.Hashtable2();
    }

    private Object source;
    private PropertyChangeListener pcl;

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

    public Object getProperty(String propertyName, Object defaultValue) {
        Object prop = getProperty(propertyName);
        return (prop != null) ? prop : defaultValue;
    }

} 
