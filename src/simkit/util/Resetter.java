package simkit.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import simkit.BasicSimEntity;
import simkit.SimEvent;

/**
 * 
 * @version $Id$
 * @author  ahbuss
 */
public class Resetter extends BasicSimEntity {
    
    /**
     *
     */    
    public static final String DEFAULT_RESETTER_NAME = "reset";
    
    private HashMap resetters;
    
    public Resetter() {
        resetters = new HashMap();
    }
    
    /**
     * For all reigstered "resetters", invoke their declared
     * "reset" method (typically the "reset()" method)
     */    
    public void reset() {
        super.reset();
        for (Iterator i = resetters.keySet().iterator(); i.hasNext(); ) {
            Object resetter = i.next();
            Method resetMethod = (Method) resetters.get(resetter);
            try {
                resetMethod.invoke(resetter, (Object[]) null);
            }
            catch (InvocationTargetException e) {
                throw new RuntimeException(e.getTargetException().toString());
            }
            catch (Throwable t) {
                throw new RuntimeException(t.toString());
            }
        }
    }
    
    /**
     * Add a resetter using the default reset method name
     * ("reset").
     * @param resetter Object to be reset
     */    
    public void addResetter(Object resetter) {
        addResetter(resetter, DEFAULT_RESETTER_NAME);
    }
    
    /**
     * The reset method should be public and have no arguments.
     * @param resetter Object to be reset
     * @param resetMethodName Name of reset method
     */    
    public void addResetter(Object resetter, String resetMethodName) {
        try {
            Method resetMethod = resetter.getClass().getMethod(
                resetMethodName, (Class[]) null
            );
            
            resetters.put(resetter, resetMethod);
        }
        catch (NoSuchMethodException e) {
            throw new RuntimeException(e.toString());
        }
    }
    
    /**
     * Removes given object.  If the object has not been
     * previously added, there is no error.
     * @param resetter Objcet to be removed
     */    
    public void removeResetter(Object resetter) {
        resetters.remove(resetter);
    }
    
    /**
     * Remove all resetters.
     */    
    public void clear() {
        resetters.clear();
    }
    
    /**
     * Shallow copy of keyset for resetters Map.
     * @return All registered resetters in a Set
     */    
    public Set getResetters() {
        return new HashSet(resetters.keySet());
    }
    
    /**
     *
     * @return All registered resetters in an Object[] array
     */    
    public Object[] getResettersAsArray() {
        return resetters.keySet().toArray();
    }
    
    /**
     * Does nothing
     * @param event Heard SimEvent
     */    
    public void handleSimEvent(SimEvent event) {
    }    
    
    /**
     * Does nothing.
     * @param event Previosuly scheduled SimEvent
     */    
    public void processSimEvent(SimEvent event) {
    }
    
}
