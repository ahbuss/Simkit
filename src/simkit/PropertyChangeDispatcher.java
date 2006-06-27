package simkit;

import java.beans.PropertyChangeSupport;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.Iterator;


/**
 *  <P> A delegate for handling properties for an entity.
 *  Uses reflection to set/get properties by name (String).  This will also
 *  fire a PropertyChangeEvent whenever a property is set/gotten using the
 *  setProperty() or getProperty() methods here.
 *
 * <p> Static caches keyed by class are kept for setters and getters, since 
 *  only one set is needed per class.  They are created on-the-fly as required and
 *  re-used when an instance of a previous class is created.
 *  @version $Id$
 **/
public class PropertyChangeDispatcher extends PropertyChangeSupport implements PropertyChangeSource {
    
    /**
     * Used internally to hold an instance of java.lang.Object.class.
     **/
    private static final Class STOP_CLASS = java.lang.Object.class;
    
    /** 
     * Holds references to setter methods keyed by property name, each
     * setter map keyed by a Class reference
     * Signature of map is <Class, HashMap<String, Method>>
     */
    private static HashMap allSetters;
    
    /** 
     * Holds references to getter methods keyed by property name, each
     * setter map keyed by a Class reference
     * Signature of map is <Class, HashMap<String, Method>>
     */
    private static HashMap allGetters;
    
    static {
        allSetters = new LinkedHashMap();
        allGetters = new LinkedHashMap();
    }
    
    /**
     * The entity for which this PropertyChangeDispatcher manages properties.
     **/
    private Object source;
    
    /**
     * Contains properties to be added on-the-fly.  Signature of Map is
     * <String, Object>
     */
    private HashMap addedProperties;
    
    /**
     * Local reference to setters for the class ofthis instance.  Signature
     * is <String, Method>
     */
    private HashMap setters;
    
    /**
     * Local reference to getters for the class ofthis instance.  Signature
     * is <String, Method>
     */
    private HashMap getters;
    
    /**
     * Create a new PropertyChangeDispatcher to manage properties for the given
     * Object. The properties of the Object are automatically discovered
     * using introspection.
     * @param bean The Object this dispatcher will manage properties for
     * @param stopClass Class to stop introspecting - normally SimEntityBase
     *          or BasicSimEntity
     **/
    public PropertyChangeDispatcher(Object bean, Class stopClass) {
        super(bean);
        source = bean;
        
        setters = (HashMap) allSetters.get(bean.getClass());
        getters = (HashMap) allGetters.get(bean.getClass());
        if (setters == null || getters == null) {
            setters = new LinkedHashMap();
            allSetters.put(bean.getClass(), setters);
            getters = new LinkedHashMap();
            allGetters.put(bean.getClass(), getters);
            for (Class clazz = source.getClass(); clazz != stopClass; clazz = clazz.getSuperclass()) {
                Method[] method = clazz.getDeclaredMethods();
                for (int i = 0; i < method.length; ++i) {
                    String name = method[i].getName();
                    if (name.startsWith("get") && isGetterSignature(method[i])) {
                        name = getPropertyName(name, 3);
                        getters.put(name, method[i]);
                    } else if (name.startsWith("is") && isGetterSignature(method[i]) &&
                            method[i].getReturnType() == boolean.class) {
                        name = getPropertyName(name, 2);
                        getters.put(name, method[i]);
                    } else if (name.startsWith("set") && isSetterSignature(method[i])) {
                        name = getPropertyName(name, 3);
                        setters.put(name, method[i]);
                    }
                }
            }
        }
        addedProperties = new LinkedHashMap();
    }
    
    /**
     * Set the specified property to the given value, firing a PropertyChangeEvent
     * to inform any registered listeners of the change.
     * If property is not in the class, it is added to the addedProperties
     * HashMap.
     * @param propertyName Name of the property, used as key to Method, if
     *     defined or Object, if not
     * @param propertyValue new value of the property
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
    
    /**
     * Get the value of the given property,  First the class's
     * properties are tried, then the addedProperties.
     * @param propertyName key to Method for built-in properties or
     *      HashMap of additional properties if not
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
     * @return Only parameter values
     */
    public String paramString() {
        StringBuffer buf = new StringBuffer();
        for (Iterator i = getters.keySet().iterator(); i.hasNext(); ) {
            Object property = i.next();
            Object setter = setters.get(property);
            if (setter == null) {
                continue;
            }
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
    
    /**
     * @return a String listing of all properties, including added ones
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
    
    /**
     * @return array of added properties
     */
    public String[] getAddedProperties() {
        return (String[]) addedProperties.keySet().toArray(new String[0]);
    }
    
    /**
     * Changes set/get/is method name to corresponding property name by
     * deleting the first <code>offset</code> characters and making the
     * first character of the result lower case.
     * @param name method name of setter/getter
     * @param offset normall y 3 (for "set"/"get") or 2 (for "is")
     * @return property name from method
     */
    public static String getPropertyName(String name, int offset) {
        char[] chars = name.toCharArray();
        chars[offset] = Character.toLowerCase(chars[offset]);
        return new String(chars, offset, chars.length - offset);
    }
    
    /**
     * Is the signature empty or has an int (for indexed getter)?
     * @param Method to be checked
     * @return true if the signature of the method is consistent with a getter
     */
    public static boolean isGetterSignature(Method method) {
        Class[] signature = method.getParameterTypes();
        return (signature.length == 0) || 
                (signature.length == 1 && signature[0] == int.class);
    }
    
    /**
     * Is the signature of length 1 (scaler setter) or 2 (for indexed getter)?
     * @param Method to be checked
     * @return true if the signature of the method is consistent with a setter
     */
    public static boolean isSetterSignature(Method method) {
        Class[] signature = method.getParameterTypes();
        return (signature.length == 1) || 
                (signature.length == 2 && signature[0] == int.class);
    }
}