package simkit;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>
 * Contains static methods that support making exact copies of SimEntity
 * instances. Each copy should have parameters that are identical to the ones in
 * the original. It is up to each SimEntity to ensure that appropriate copies
 * are made by the respective setter methods, if needed.
 * <p>
 * It is crucial that a SimEntity to be copied in this way adhere to the Simkit
 * conventions (which are essentially Javabeans). Specifically, it should
 * <ol><li>Have a zero-argument constructor.
 * <li>Have setter/getter pairs for each parameter
 * </ol>
 *
 * @author ahbuss
 */
public class SimEntityFactory {

    private static final Logger LOGGER = Logger.getLogger(SimEntityFactory.class.getName());

    protected static final Map<Class<?>, BeanInfo> cache = new HashMap<>();

    /**
     * Creates multiple copies in a List.
     *
     * @param original Given SimEntity to make copies
     * @param quantity Number of copies to create
     * @return List of copies
     * @throws IllegalArgumentException if quantity &le; 0
     */
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

    /**
     * Creates a copy of the given SimEntity object.As noted, it should conform
     * to Simkit's conventions (zero-argument constructor, setter/getter pairs
     * for each parameter, but not for state variables)
     *
     * @param original Given SimEntity
     * @return Copy of SimEntity - a new object with identical parameters
     */
    public static SimEntity createCopy(SimEntity original) {
        SimEntity copy = null;
        try {
            try {
                Constructor<? extends SimEntity> constructor = original.getClass().getConstructor();
                if (constructor != null) {
                    try {
                        copy = constructor.newInstance();
                    } catch (IllegalArgumentException | InvocationTargetException ex) {
                        Logger.getLogger(SimEntityFactory.class.getName()).log(Level.SEVERE, null, ex);
                    }
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
                            Object.class);
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

    /**
     *
     * @param propertyDescriptor Given PropertyDescriptor
     * @return true if given PropertyDescriptor is for a "property" - i.e. has
     * both a setter and a getter method
     */
    public static boolean isProperty(PropertyDescriptor propertyDescriptor) {
        if (propertyDescriptor == null) {
            return false;
        }
        return propertyDescriptor.getReadMethod() != null && propertyDescriptor.getWriteMethod() != null;
    }

    /**
     *
     * @param simEntity Given SimEntity
     * @return Map of parameters with (name, parameterValue)
     */
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
