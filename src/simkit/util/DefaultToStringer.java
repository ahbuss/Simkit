package simkit.util;
import java.beans.*;
import java.lang.reflect.*;
/**
 *
 * @author  ahbuss
 */
public class DefaultToStringer {
    
    public static String getToString(Object obj, Class stopClass) {
        StringBuffer buf = new StringBuffer();
        if (obj instanceof simkit.Named) {
            buf.append( ((simkit.Named) obj).getName());
        }
        else {
            buf.append(obj.getClass().getName());
        }
        try {
            BeanInfo info = Introspector.getBeanInfo( obj.getClass(), stopClass );
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
        catch (IntrospectionException e) { e.printStackTrace(System.err);  }
        catch (IllegalAccessException e) { e.printStackTrace(System.err);  }
        catch (InvocationTargetException e) { e.getTargetException().printStackTrace(System.err);  }
        return buf.toString();
    }
    
}
