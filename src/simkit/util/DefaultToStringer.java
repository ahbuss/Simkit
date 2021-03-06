package simkit.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 * @author ahbuss
 */
public class DefaultToStringer {

    /**
     * Returns all of the known properties of the given Object.
     *
     * @param obj The Object about which to get information.
     * @return all of the known properties of the given Object in a String.
     */
    public static String getToString(Object obj) {
        StringBuilder buf = new StringBuilder();
        if (obj instanceof simkit.Named) {
            buf.append(((simkit.Named) obj).getName());
        } else {
            buf.append(obj.getClass().getName());
        }
        try {
            BeanInfo info = Introspector.getBeanInfo(obj.getClass(),
                    Introspector.IGNORE_ALL_BEANINFO);
            PropertyDescriptor[] descriptors = info.getPropertyDescriptors();
            for (int i = 0; i < descriptors.length; ++i) {
                Method readMethod = descriptors[i].getReadMethod();
                if (readMethod != null) {
                    buf.append(System.getProperty("line.separator"));
                    buf.append('\t');
                    buf.append(descriptors[i].getName());
                    buf.append(" = ");
                    buf.append(readMethod.invoke(obj, (Object[]) null));
                }
            }
        } catch (IntrospectionException | IllegalAccessException e) {
            e.printStackTrace(System.err);
            throw (new RuntimeException(e));
        } catch (InvocationTargetException e) {
            e.getTargetException().printStackTrace(System.err);
            throw (new RuntimeException(e));
        }
        return buf.toString();
    }

}
