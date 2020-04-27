package simkit.util;

import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.util.List;
import java.util.ArrayList;
import simkit.PropertyChangeSource;
import simkit.SimEventListener;
import simkit.SimEventSource;

/**
 * A location for a number of useful static methods.
 * <p>
 * Unit test JavaReflectionTest covers the reflection oriented methods in this
 * class.
 *
 * 
 *
 */
public class Misc {

    /**
     * Given an <CODE>Object[]</CODE> array, returns the corresponding
     * <CODE>Class[]</CODE> array.
     *
     * @since Simkit 1.0
     * @param args The <CODE>Object[]</CODE> array we want the signature for.
     * @return The <CODE>Class[]</CODE> representing the signature of the
     * argument array.
     *
     */
    @SuppressWarnings("unchecked")
    public static Class<?>[] getSignatureFromArguments(Object[] args) {
        List<Class<?>> classList = new ArrayList<>();
        for (Object arg: args) {
            classList.add(arg.getClass());
        }
        Class<?>[] signature = classList.toArray(new Class<?>[0]);
        return signature;
    }

    /**
     *
     * @param methodName Given method name
     * @param arguments Given array of arguments
     * @return the method name with the class names of the arguments appended.
     */
    // covered by unit test JavaReflectionTest
    public static String getFullMethodName(String methodName, Object[] arguments) {
        StringBuilder buf = new StringBuilder(methodName);
        buf.append('(');
        for (int i = 0; i < arguments.length; i++) {
            buf.append(arguments[i].getClass());
            if (i < arguments.length - 1) {
                buf.append(',');
            }
        }
        buf.append(')');
        return buf.toString();
    }

    /**
     * Returns the method name followed by the class names of the arguments.
     * Only works for <CODE>public void</CODE> methods.
     *
     * @param theClass The Class that contains the method.
     * @param name The name of the method.
     * @param args An array containing the arguments for the method.
     * @return The method name followed by the classes of the arguments.
     */
    // covered by unit test JavaReflectionTest
    public static String getFullMethodName(Class<?> theClass, String name, Object[] args) {
        String fullName = null;
        try {
            Method m = theClass.getMethod(name, getSignatureFromArguments(args));
            fullName = m.toString().substring(12);
        } catch (NoSuchMethodException e) {
//            don't need this, rethrowning RuntimException will produce a stack trace
//            System.err.println(e);
            throw (new RuntimeException(e));
        }
        return fullName;
    }

    /**
     * Removes all of the SimEventListners currently registers with the given
     * SimEventSource.
     *
     * @param source Given SimEventSource
     */
    // covered in test case simkit.BasicSimEventSourceTest
    public static void removeAllSimEventListeners(SimEventSource source) {
        SimEventListener[] listeners = source.getSimEventListeners();
        for (int i = 0; i < listeners.length; i++) {
            source.removeSimEventListener(listeners[i]);
        }
    }

    /**
     * Removes all of the PropertyChangeListners currently registers with the
     * given PropertChangeSource.
     *
     * @param source given PropertChangeSource
     */
    // covered in test case simkit.PropertyChangeDispatcherTest.
    public static void removeAllPropertyChangeListeners(PropertyChangeSource source) {
        PropertyChangeListener[] listeners = source.getPropertyChangeListeners();
        for (int i = 0; i < listeners.length; i++) {
            source.removePropertyChangeListener(listeners[i]);
        }
    }
}
