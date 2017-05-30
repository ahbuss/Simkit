package simkit;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ahbuss
 */
public class SimEntityFactory {

    private static final Logger LOGGER = Logger.getLogger(SimEntityFactory.class.getName());

    protected static Map<Class<?>, BeanInfo> cache = new HashMap<>();

    public static List<SimEntity> createCopies(SimEntity original, int quantity) {
        if (quantity < 0) {
            String message = "quantity must be \u2265 0: " + quantity;
            LOGGER.severe(message);
            throw new IllegalArgumentException(message);
        }
        List<SimEntity> copies = new ArrayList<>();
        for (int i = 0; i < quantity; ++i) {
            copies.add(createCopy(original));
        }
        return copies;
    }

    public static SimEntity createCopy(SimEntity original) {
        SimEntity copy = null;
        try {
            try {
                Constructor<?> constructor = original.getClass().getConstructor();
                if (constructor != null) {
                    copy = original.getClass().newInstance();
                } else {
                    return copy;
                }
            } catch (NoSuchMethodException | SecurityException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
            try {
                BeanInfo beanInfo = cache.get(original.getClass());
                if (beanInfo == null) {
                    beanInfo = Introspector.getBeanInfo(original.getClass(),
                            BasicSimEntity.class);
                    cache.put(original.getClass(), beanInfo);
                }
                PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
                for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                    if (isProperty(propertyDescriptor)) {
                        try {
                            Object value = propertyDescriptor.getReadMethod().invoke(original);
                            propertyDescriptor.getWriteMethod().invoke(copy, value);
                        } catch (IllegalArgumentException | InvocationTargetException ex) {
                            Logger.getLogger(SimEntityFactory.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            } catch (IntrospectionException ex) {
                Logger.getLogger(SimEntityFactory.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (InstantiationException | IllegalAccessException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return copy;
    }

    public static boolean isProperty(PropertyDescriptor propertyDescriptor) {
        if (propertyDescriptor == null) {
            return false;
        }
        return propertyDescriptor.getReadMethod() != null && propertyDescriptor.getWriteMethod() != null;
    }

    public static boolean isExactCopyOf(SimEntity original, SimEntity copy) {
        boolean exactCopy = true;

        if (original.getClass() != copy.getClass()) {
            exactCopy = false;
        } else {

        }

        return exactCopy;
    }

    public static Map<String, Object> getParameters(SimEntity simEntity) {
        Map<String, Object> parameters = new HashMap<>();

        BeanInfo beanInfo = cache.get(simEntity.getClass());
        if (beanInfo == null) {
            try {
                beanInfo = Introspector.getBeanInfo(simEntity.getClass(), BasicSimEntity.class);
                cache.put(simEntity.getClass(), beanInfo);
            } catch (IntrospectionException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }
        for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
            if (isProperty(propertyDescriptor)) {
                try {
                    parameters.put(propertyDescriptor.getName(), propertyDescriptor.getReadMethod().invoke(simEntity));
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    LOGGER.log(Level.SEVERE, null, ex);
                }
            }
        }
        return parameters;
    }

}
