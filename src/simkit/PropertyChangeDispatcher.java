package simkit;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * Overrode methods for firing PropertyChangeEvents to fire the property even
 * when the old and the new values were the same. This required creating our own
 * Map of listeners, keyed by the propertyName (if specific) and by null for the
 * unnamed listeners (i.e. those listening for all PropertyChangeEvents).</p>
 *
 * <p>
 * Unlike the superclass PropertyChangeSupport, multiple registrations still
 * receive only one PropertyChangeEvent per fire. This is accomplished by making
 * the listeners a Set rather than a List, as in PropertyChangeSupport. </p>
 *
 * <p>
 * A delegate for handling properties for an entity. Uses reflection to set/get
 * properties by name (String). This will also fire a PropertyChangeEvent
 * whenever a property is set/gotten using the setProperty() or getProperty()
 * methods here.</p>
 *
 * <p>
 * Static caches keyed by class are kept for setters and getters, since only one
 * set is needed per class. They are created on-the-fly as required and re-used
 * when an instance of a previous class is created.</p>
 *
 * @version $Id: PropertyChangeDispatcher.java 1341 2014-12-15 20:07:00Z ahbuss
 * $
 *
 */
public class PropertyChangeDispatcher extends PropertyChangeSupport implements PropertyChangeSource {

    /**
     * Used internally to hold an instance of java.lang.Object.class.
     *
     */
    private static final Class STOP_CLASS = java.lang.Object.class;

    /**
     * Holds references to setter methods keyed by property name, each setter
     * map keyed by a Class reference Signature of map is &lt;Class,
     * HashMap&lt;String, Method&gt;&gt;
     */
    private static Map<Class, Map<String, Method>> allSetters;

    /**
     * Holds references to getter methods keyed by property name, each setter
     * map keyed by a Class reference Signature of map is &lt;Class,
     * HashMap&lt;String, Method&gt;&gt;
     */
    private static Map<Class, Map<String, Method>> allGetters;

    static {
        allSetters = new LinkedHashMap<>();
        allGetters = new LinkedHashMap<>();
    }

    /**
     * The entity for which this PropertyChangeDispatcher manages properties.
     *
     */
    private Object source;

    /**
     * Contains properties to be added on-the-fly. Signature of Map is
     * &lt;String, Object&gt;
     */
    private Map<String, Object> addedProperties;

    /**
     * Local reference to setters for the class of this instance. Signature is
     * &lt;String, Method&gt;
     */
    private Map<String, Method> setters;

    /**
     * Local reference to getters for the class of this instance. Signature is
     * &lt;String, Method&gt;
     */
    private Map<String, Method> getters;

    /**
     * Holds the named listeners with the key as the propertyName, and the
     * unnamed listeners keyed by null.
     */
    private Map<String, Set<PropertyChangeListener>> listeners;

    /**
     * Create a new PropertyChangeDispatcher to manage properties for the given
     * Object. The properties of the Object are automatically discovered using
     * introspection.
     *
     * @param bean The Object this dispatcher will manage properties for
     * @param stopClass Class to stop introspecting - normally SimEntityBase or
     * BasicSimEntity
     *
     */
    public PropertyChangeDispatcher(Object bean, Class stopClass) {
        super(bean);
        source = bean;

        setters = allSetters.get(bean.getClass());
        getters = allGetters.get(bean.getClass());
        if (setters == null || getters == null) {
            setters = new LinkedHashMap<>();
            allSetters.put(bean.getClass(), setters);
            getters = new LinkedHashMap<>();
            allGetters.put(bean.getClass(), getters);
            for (Class clazz = source.getClass(); clazz != stopClass; clazz = clazz.getSuperclass()) {
                Method[] method = clazz.getDeclaredMethods();
                for (int i = 0; i < method.length; ++i) {
                    if (!Modifier.isPublic(method[i].getModifiers())) {
                        continue;
                    }
                    String name = method[i].getName();
                    if (name.startsWith("get") && isGetterSignature(method[i])) {
                        name = getPropertyName(name, 3);
                        getters.put(name, method[i]);
                    } else if (name.startsWith("is") && isGetterSignature(method[i])
                            && method[i].getReturnType() == boolean.class) {
                        name = getPropertyName(name, 2);
                        getters.put(name, method[i]);
                    } else if (name.startsWith("set") && isSetterSignature(method[i])) {
                        name = getPropertyName(name, 3);
                        setters.put(name, method[i]);
                    }
                }
            }
        }
        addedProperties = new LinkedHashMap<>();
        listeners = new HashMap<>();
        listeners.put(null, new LinkedHashSet<>());
    }

    /**
     * Instantiate a PropertyChangeDispatcher with default STOP_CLASS (currently
     * Object).
     *
     * @param bean The Object this dispatcher will manage properties for
     */
    public PropertyChangeDispatcher(Object bean) {
        this(bean, STOP_CLASS);
    }

    /**
     * Adds listener to the unnamed listeners (keyed by 'null'). The Set of
     * listeners is instantiated if this is the first listeners to be added.
     *
     * @param listener Listener to be added.
     */
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        Set<PropertyChangeListener> currentUnnamedListeners
                = listeners.get(null);
        if (currentUnnamedListeners == null) {
            currentUnnamedListeners = new LinkedHashSet<>();
            listeners.put(null, currentUnnamedListeners);
        }
        currentUnnamedListeners.add(listener);
    }

    /**
     * Adds the listener for the specific named propertyName. If this is the
     * first to be listening for this property, the Set will be instantiated
     *
     * @param propertyName name of the property to be listened for
     * @param listener will listen for PropertyChangeEvents having this
     * propertyName
     */
    @Override
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        if (listener == null) {
            return;
        }
        Set<PropertyChangeListener> currentNamedListeners
                = listeners.get(propertyName);
        if (currentNamedListeners == null) {
            currentNamedListeners = new LinkedHashSet<>();
            listeners.put(propertyName, currentNamedListeners);
        }
        currentNamedListeners.add(listener);
    }

    /**
     * Removes the listener from the unnamed listeners. If it is not already
     * listening, then no error.
     *
     * @param listener Listener to be removed
     */
    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        if (listener != null) {
            Set<PropertyChangeListener> currentUnnamedListeners
                    = listeners.get(null);
            if (currentUnnamedListeners != null) {
                currentUnnamedListeners.remove(listener);
            }
        }
    }

    /**
     * Removes the listener for this given property. If it isn't listening, then
     * no error.
     *
     * @param propertyName name of property that listener will be removed for
     * @param listener stops listening for this property
     */
    @Override
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        if (listener != null) {
            Set<PropertyChangeListener> currentNamedListeners
                    = listeners.get(propertyName);
            if (currentNamedListeners != null) {
                currentNamedListeners.remove(listener);
            }
        }
    }

    @Override
    public PropertyChangeListener[] getPropertyChangeListeners() {
        Set<PropertyChangeListener> allListeners
                = new LinkedHashSet<>();
        if (listeners.keySet() != null) {
            for (String propertyName : listeners.keySet()) {
                allListeners.addAll(listeners.get(propertyName));
            }
        }
        return allListeners.toArray(new PropertyChangeListener[0]);
    }

    @Override
    public PropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
        PropertyChangeListener[] theseListeners
                = new PropertyChangeListener[0];
        if (propertyName != null) {
            Set<PropertyChangeListener> namedListeners =
                    listeners.get(propertyName);
            if (namedListeners != null) {
                theseListeners = namedListeners.toArray(new PropertyChangeListener[0]);
            }
        }
        return theseListeners;
    }

    @Override
    public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        if (propertyName != null) {
            firePropertyChange(new PropertyChangeEvent(this.source, propertyName, oldValue, newValue));
        }
    }

    @Override
    public void firePropertyChange(PropertyChangeEvent event) {
        Set<PropertyChangeListener> unnamedListeners
                = listeners.get(null);
        if (unnamedListeners != null) {
            for (PropertyChangeListener listener : unnamedListeners) {
                listener.propertyChange(event);
            }
        }
        Set<PropertyChangeListener> namedListeners
                = listeners.get(event.getPropertyName());
        if (namedListeners != null) {
            for (PropertyChangeListener listener : namedListeners) {
                if (!unnamedListeners.contains(listener)) {
                    listener.propertyChange(event);
                }
            }
        }
    }

    /**
     * Set the specified property to the given value, firing a
     * PropertyChangeEvent to inform any registered listeners of the change. If
     * property is not in the class, it is added to the addedProperties HashMap.
     *
     * @param propertyName Name of the property, used as key to Method, if
     * defined or Object, if not
     * @param propertyValue new value of the property
     *
     */
    @Override
    public void setProperty(String propertyName, Object propertyValue) {
        Method setter = setters.get(propertyName);
        if (setter != null) {
            try {
                setter.invoke(source, (Object) propertyValue);
                this.firePropertyChange(propertyName, null, propertyValue);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e.getTargetException());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        } else {
            addedProperties.put(propertyName, propertyValue);
        }
    }

    /**
     * Get the value of the given property, First the class's properties are
     * tried, then the addedProperties.
     *
     * @param propertyName key to Method for built-in properties or HashMap of
     * additional properties if not
     * @return property value (or null if property doesn't exist)
     *
     */
    @Override
    public Object getProperty(String propertyName) {
        Object result = null;
        Method getter = getters.get(propertyName);
        if (getter != null) {
            try {
                result = getter.invoke(source, (Object[]) null);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e.getTargetException());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        } else {
            result = addedProperties.get(propertyName);
        }
        return result;
    }

    /**
     * Get the value of the given property or return the given default.
     *
     * @param propertyName name of property
     * @return The value of the property if found and not null, otherwise the
     * default value.
     * @throws NullPointerException If the property doesn't exist.
     *
     */
    @Override
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
        StringBuilder buf = new StringBuilder();
        for (String propertyName : getters.keySet()) {
            Object setter = setters.get(propertyName);
            if (setter == null) {
                continue;
            }
            Object value = getProperty(propertyName);
            if (value == null || !value.getClass().isArray()) {
                buf.append(System.getProperty("line.separator"));
                buf.append('\t');
                buf.append(propertyName);
                buf.append(" = ");
                buf.append(value);
            } else {
                for (int j = 0; j < Array.getLength(value); ++j) {
                    buf.append(System.getProperty("line.separator"));
                    buf.append('\t');
                    buf.append(propertyName);
                    buf.append('[');
                    buf.append(j);
                    buf.append("] = ");
                    buf.append(Array.get(value, j));
                }
            }
        }
        for (String propertyName : addedProperties.keySet()) {
            Object value = addedProperties.get(propertyName);
            if (value == null || !value.getClass().isArray()) {
                buf.append(System.getProperty("line.separator"));
                buf.append('\t');
                buf.append(propertyName);
                buf.append(" = ");
                buf.append(value);
            } else {
                for (int j = 0; j < Array.getLength(value); ++j) {
                    buf.append(System.getProperty("line.separator"));
                    buf.append('\t');
                    buf.append(propertyName);
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
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        for (String propertyName : getters.keySet()) {
            Object value = getProperty(propertyName);
            if (value == null || !value.getClass().isArray()) {
                buf.append(System.getProperty("line.separator"));
                buf.append('\t');
                buf.append(propertyName);
                buf.append(" = ");
                buf.append(value);
            } else {
                for (int j = 0; j < Array.getLength(value); ++j) {
                    buf.append(System.getProperty("line.separator"));
                    buf.append('\t');
                    buf.append(propertyName);
                    buf.append('[');
                    buf.append(j);
                    buf.append("] = ");
                    buf.append(Array.get(value, j));
                }
            }
        }
        for (String propertyName : addedProperties.keySet()) {
            Object value = addedProperties.get(propertyName);
            if (value == null || !value.getClass().isArray()) {
                buf.append(System.getProperty("line.separator"));
                buf.append('\t');
                buf.append(propertyName);
                buf.append(" = ");
                buf.append(value);
            } else {
                for (int j = 0; j < Array.getLength(value); ++j) {
                    buf.append(System.getProperty("line.separator"));
                    buf.append('\t');
                    buf.append(propertyName);
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
    @Override
    public String[] getAddedProperties() {
        return addedProperties.keySet().toArray(new String[0]);
    }

    /**
     * Removes the added property of the given name. If no such property exists,
     * then there is no error or warning.
     *
     * @param propertyName The name of the property to be cleared.
     */
    public void clearAddedProperty(String propertyName) {
        addedProperties.remove(propertyName);
    }

    /**
     * Changes set/get/is method name to corresponding property name by deleting
     * the first <code>offset</code> characters and making the first character
     * of the result lower case.
     *
     * @param name method name of setter/getter
     * @param offset normally 3 (for "set"/"get") or 2 (for "is")
     * @return property name from method
     */
    public static String getPropertyName(String name, int offset) {
        char[] chars = name.toCharArray();
        chars[offset] = Character.toLowerCase(chars[offset]);
        return new String(chars, offset, chars.length - offset);
    }

    /**
     * Is the signature empty or has an int (for indexed getter)?
     *
     * @param method Method to be checked
     * @return true if the signature of the method is consistent with a getter
     */
    public static boolean isGetterSignature(Method method) {
        Class[] signature = method.getParameterTypes();
        return signature.length == 0;
    }

    /**
     * Is the signature of length 1 (scaler setter) or 2 (for indexed getter)?
     *
     * @param method Method to be checked
     * @return true if the signature of the method is consistent with a setter
     */
    public static boolean isSetterSignature(Method method) {
        Class[] signature = method.getParameterTypes();
        return signature.length == 1;
    }
}
