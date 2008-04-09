package simkit.util;
import java.beans.*;
import java.lang.reflect.*;
/**
 *
 * @author  ahbuss
 */
public class DefaultToStringer {
    
/**
* Returns all of the known properties of the given Object.
* @param obj The Object about which to get information.
* @param stopClass Ignored.
* @deprecated Use getToString(Object) instead.
**/
    public static String getToString(Object obj, Class stopClass) {
        StringBuffer buf = new StringBuffer();
        if (obj instanceof simkit.Named) {
            buf.append( ((simkit.Named) obj).getName());
        }
        else {
            buf.append(obj.getClass().getName());
        }
        try {
            BeanInfo info = Introspector.getBeanInfo(obj.getClass(), 
                                        Introspector.IGNORE_ALL_BEANINFO );
            PropertyDescriptor[] descriptors = info.getPropertyDescriptors();
            for (int i = 0; i < descriptors.length; ++i) {
                Method readMethod = descriptors[i].getReadMethod();
                if (readMethod != null) {
                    buf.append(System.getProperty("line.separator"));
                    buf.append('\t');
                    buf.append( descriptors[i].getName() );
                    buf.append(" = ");
                    buf.append( readMethod.invoke(obj, (Object[]) null));
                }
            }
        } 
        catch (IntrospectionException e) {
            e.printStackTrace(System.err);
            throw(new RuntimeException(e));
        }
        catch (IllegalAccessException e) {
            e.printStackTrace(System.err);
            throw(new RuntimeException(e));
        }
        catch (InvocationTargetException e) {
            e.getTargetException().printStackTrace(System.err);
            throw(new RuntimeException(e));
        }
        return buf.toString();
    }
    
/**
* Returns all of the known properties of the given Object.
* @param obj The Object about which to get information.
**/
    public static String getToString(Object obj) {
        return getToString(obj, null);
    }
}
