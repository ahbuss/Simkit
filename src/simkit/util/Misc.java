package simkit.util;

import java.lang.reflect.*;
import java.beans.*;
import simkit.*;

public class Misc {
    /**
     *  Given an <CODE>Object[]</CODE> array, return the corresponding <CODE>Class[]</CODE>
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
    
    public static String getFullMethodName(Class theClass, String name, Object[] args) {
        String fullName = null;
        try {
            Method m = theClass.getMethod(name, getSignatureFromArguments(args));
            fullName = m.toString().substring(12);
        }
        catch (NoSuchMethodException e) {System.err.println(e);}
        return fullName;
    }
    
    public static void removeAllSimEventListeners(SimEventSource source) {
        SimEventListener[] listeners = source.getSimEventListeners();
        for (int i = 0; i < listeners.length; i++) {
            source.removeSimEventListener(listeners[i]);
        }
    }
    
    public static void removeAllPropertyChangeListeners(PropertyChangeSource source) {
        PropertyChangeListener[] listeners = source.getPropertyChangeListeners();
        for (int i = 0; i < listeners.length; i++) {
            source.removePropertyChangeListener(listeners[i]);
        }
    }
}
