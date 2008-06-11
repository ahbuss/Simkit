package simkit.util;

import java.lang.reflect.*;
import java.beans.*;
import simkit.*;

/**
* A location for a number of useful static methods.
* @version $Id: Misc.java 1057 2008-04-09 17:15:14Z kastork $
**/
public class Misc {
    /**
     *  Given an <CODE>Object[]</CODE> array, returns the corresponding <CODE>Class[]</CODE>
     *  array.
     *  @since Simkit 1.0
     *  @param args The <CODE>Object[]</CODE> array we want the signature for.
     *  @return The <CODE>Class[]</CODE> representing the signature of the argument array.
     **/
    public static Class[] getSignatureFromArguments(Object[] args) {
        Class[] signature = new Class[args.length];
        for (int i = 0; i < signature.length; i++) {
            signature[i] = args[i].getClass();
        }
        return signature;
    }
    
/**
* Returns the method name with the class names of the arguments appended.
**/
    public static String getFullMethodName(String methodName, Object[] arguments) {
        StringBuffer buf = new StringBuffer(methodName);
        buf.append('(');
        for (int i = 0; i < arguments.length; i++) {
            buf.append(arguments[i].getClass());
            if (i < arguments.length - 1) { buf.append(','); }
        }
        buf.append(')');
        return buf.toString();
    }

/**
* Returns the method name followed by the class names of the arguments. 
* Only works for <CODE>public void<CODE/> methods.
* @param theClass The Class that contains the method.
* @param name The name of the method.
* @param args An array containing the arguments for the method.
* @return The method name followed by the classes of the arguments.
* @throws NoSuchMethodException If the method doesn't exist or isn't public.
**/    
    public static String getFullMethodName(Class<?> theClass, String name, Object[] args) {
        String fullName = null;
        try {
            Method m = theClass.getMethod(name, getSignatureFromArguments(args));
            fullName = m.toString().substring(12);
        }
        catch (NoSuchMethodException e) {
            System.err.println(e);
            throw(new RuntimeException(e));
        }
        return fullName;
    }
    
/**
* Removes all of the SimEventListners currently registers with 
* the given SimEventSource.
**/
    public static void removeAllSimEventListeners(SimEventSource source) {
        SimEventListener[] listeners = source.getSimEventListeners();
        for (int i = 0; i < listeners.length; i++) {
            source.removeSimEventListener(listeners[i]);
        }
    }
    
/**
* Removes all of the PropertyChangeListners currently registers with 
* the given PropertChangeSource.
**/
    public static void removeAllPropertyChangeListeners(PropertyChangeSource source) {
        PropertyChangeListener[] listeners = source.getPropertyChangeListeners();
        for (int i = 0; i < listeners.length; i++) {
            source.removePropertyChangeListener(listeners[i]);
        }
    }
}
