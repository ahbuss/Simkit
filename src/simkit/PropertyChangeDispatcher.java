package simkit;

import java.beans.*;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;


/**
 *  <P> A delegate for handling properties for an entity.
 *  Uses reflection to set/get properties by name (String).  This will also
 *  fire a PropertyChangeEvent whenever a property is set/gotten using the
 *  setProperty() or getProperty() methods here.
 *  @version $Id$
 **/
public class PropertyChangeDispatcher extends PropertyChangeSupport implements PropertyChangeSource {
    
    /**
     * Used internally to hold an instance of java.lang.Object.class.
     **/
    private static final Class STOP_CLASS = java.lang.Object.class;
    
    /**
     * The entity for which this PropertyChangeDispatcher manages properties.
     **/
    private Object source;
    
    private HashMap addedProperties;
    
    private HashMap setters;
    
    private HashMap getters;
    
    /**
     * Create a new PropertyChangeDispatcher to manage properties for the given
     * Object. The properties of the Object are automatically discovered
     * using introspection.
     **/
    public PropertyChangeDispatcher(Object bean, Class stopClass) {
        super(bean);
        source = bean;
        Class beanClass = source.getClass();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass(), stopClass);
            PropertyDescriptor[] pd = beanInfo.getPropertyDescriptors();
            setters = new HashMap(pd.length);
            getters = new HashMap(pd.length);
            for (int i = 0; i < pd.length; i++) {
                Method setter = pd[i].getWriteMethod();
                if (setter != null) {
                    setters.put(pd[i].getName(), setter);
                }
                Method getter = pd[i].getReadMethod();
                if (getter != null) {
                    getters.put(pd[i].getName(), getter);
                }
            }
        }
        catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
        addedProperties = new HashMap();
    }
    
    /**
     * Set the specified property to the given value, firing a PropertyChangeEvent
     * to inform any registered listeners of the change.
     * If property is not in the class, it is added to the addedProperties
     * HashMap.
     **/
    public void setProperty(String propertyName, Object propertyValue) {
        Method setter = (Method) setters.get(propertyName);
        if (setter != null ) {
            try {
                setter.invoke(source, new Object[] {propertyValue});
                this.firePropertyChange(propertyName, null, propertyValue);
            }
            catch (InvocationTargetException e) {
                throw new RuntimeException(e.getTargetException());
            }
            catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            addedProperties.put(propertyName, propertyValue);
        }
    }
    
    public PropertyChangeDispatcher(Object bean) {
        this(bean, STOP_CLASS);
    }
    
    /**
     * Get the value of the given property,  First the class's
     * properties are tried, then the addedProperties.
     * @return property value (or null if property doesn't exist)
     **/
    public Object getProperty(String propertyName) {
        Object result = null;
        Method getter = (Method) getters.get(propertyName);
        if (getter != null) {
            try {
                result = getter.invoke(source, (Object[]) null);
            }
            catch (InvocationTargetException e) {
                throw new RuntimeException(e.getTargetException());
            }
            catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            result = addedProperties.get(propertyName);
        }
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
    
    /**
     * Removes all added properties
     */
    public void clearAddedProperties() {
        addedProperties.clear();
    }
    
    /**
     * @return a String listing of all 
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        for (Iterator i = getters.keySet().iterator(); i.hasNext(); ) {
            Object property = i.next();
            
            Object value = getProperty(property.toString());
            if (value == null || !value.getClass().isArray() ) {
                buf.append(System.getProperty("line.separator"));
                buf.append('\t');
                buf.append(property);
                buf.append(" = ");
                buf.append(value);
            }
            else {
                for (int j = 0; j < Array.getLength(value); ++j) {
                    buf.append(System.getProperty("line.separator"));
                    buf.append('\t');
                    buf.append(property);
                    buf.append('[');
                    buf.append(j);
                    buf.append("] = ");
                    buf.append(Array.get(value, j));
                }
            }
        }
        for (Iterator i = addedProperties.keySet().iterator(); i.hasNext(); ) {
            Object property = i.next();
            Object value = addedProperties.get(property);
            if (value == null || !value.getClass().isArray() ) {
                buf.append(System.getProperty("line.separator"));
                buf.append('\t');
                buf.append(property);
                buf.append(" = ");
                buf.append(value);
            }
            else {
                for (int j = 0; j < Array.getLength(value); ++j) {
                    buf.append(System.getProperty("line.separator"));
                    buf.append('\t');
                    buf.append(property);
                    buf.append('[');
                    buf.append(j);
                    buf.append("] = ");
                    buf.append(Array.get(value, j));
                }
            }
        }
        return buf.toString();
    }
    
    public String[] getAddedProperties() {
        return (String[]) addedProperties.keySet().toArray(new String[0]);
    }
    
}
