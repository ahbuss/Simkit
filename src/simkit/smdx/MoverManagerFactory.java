/*
 * MoverManagerFactory.java
 *
 * Created on July 29, 2002, 12:00 PM
 */

package simkit.smdx;
import java.beans.*;
import java.lang.reflect.*;
/**
 *
 * @author  Arnold Buss
 */
public class MoverManagerFactory {
    
    public static MoverManager getInstance(String className, Object[] parameters) {
        MoverManager manager = null;
        try {
            Class managerClass = Thread.currentThread().getContextClassLoader().loadClass(className);
            Constructor[] constructor = managerClass.getConstructors();
            for (int i = 0; i < constructor.length; i++) {
                Class[] signature = constructor[i].getParameterTypes();
                if (signature.length == parameters.length) {
                    for (int j = 0; j < signature.length; j++) {
                        if (!Beans.isInstanceOf(parameters[j], signature[j] )) {
                            break;
                        }
                    }
                    manager = (MoverManager) constructor[i].newInstance(parameters);
                }
            }
        }
        catch (ClassNotFoundException e) { System.err.println(e); }
        catch (IllegalAccessException e) { System.err.println(e); }
        catch (InstantiationException e) { System.err.println(e); }
        catch (InvocationTargetException e) { System.err.println(e.getTargetException()); }
        return manager;
    }
    
}
