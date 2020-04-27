/*
 * MoverManagerFactory.java
 *
 * Created on July 29, 2002, 12:00 PM
 */
package simkit.smdx;

import java.beans.Beans;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * A factory used to construct MoverManagers.
 *
 * @see MoverManager
 * @author Arnold Buss
 * 
 */
public class MoverManagerFactory {

    /**
     * Should never be constructed.
     */
    private MoverManagerFactory() {
    }

    /**
     * Constructs a MoverManager. Note that if for any reason the MoverManager
     * cannot be constructed, <CODE>getInstance</CODE> will return
     * <CODE>null</CODE>.
     *
     * @param className The name of the MoverManager to construct.
     * @param parameters An Object array containing the desired constructor
     * parameters
     * @return MoverManager with given class name and parameters
     */
    @SuppressWarnings("unchecked")
    public static MoverManager getInstance(String className, Object[] parameters) {
        MoverManager manager = null;
        try {
            Class<? extends MoverManager>  managerClass = 
                    (Class<? extends MoverManager> )Thread.currentThread().getContextClassLoader().loadClass(className);
            Constructor[] constructor = managerClass.getConstructors();
            for (int i = 0; i < constructor.length; i++) {
                Class<?>[] signature = constructor[i].getParameterTypes();
                if (signature.length == parameters.length) {
                    for (int j = 0; j < signature.length; j++) {
                        if (!Beans.isInstanceOf(parameters[j], signature[j])) {
                            break;
                        }
                    }
                    manager = (MoverManager) constructor[i].newInstance(parameters);
                }
            }
        } catch (ClassNotFoundException e) {
            System.err.println(e);
            throw (new RuntimeException(e));
        } catch (IllegalAccessException e) {
            System.err.println(e);
            throw (new RuntimeException(e));
        } catch (InstantiationException e) {
            System.err.println(e);
            throw (new RuntimeException(e));
        } catch (InvocationTargetException e) {
            System.err.println(e.getTargetException());
            throw (new RuntimeException(e));
        }
        return manager;
    }

}
