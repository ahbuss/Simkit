/*
 * WeaponAdjudicatorFactory.java
 *
 * Created on February 20, 2002, 1:21 AM
 */

package simkit.smdx;
import simkit.smd.Mover;
import java.util.*;
import java.lang.reflect.*;

/**
 *
 * @author  Arnold Buss
 * @version
 */
public class WeaponAdjudicatorFactory {
    
    protected static final WeaponAdjudicatorFactory factory;
    
    static {
        factory = new WeaponAdjudicatorFactory();
    }
    
    public static WeaponAdjudicatorFactory getFactoryInstance() {
        return factory;
    }
    
    protected Map mediators;
    protected Map mediatorTypes;
    
    /** Creates new WeaponAdjudicatorFactory */
    protected WeaponAdjudicatorFactory() {
        mediators = (Map) Collections.synchronizedMap(new WeakHashMap());
        mediatorTypes = (Map) Collections.synchronizedMap(new HashMap());
    }
    
    protected WeaponAdjudicator getFromCache(Weapon weapon, Mover target) {
        WeaponAdjudicator mediator = null;
        Map map = (Map) mediators.get(weapon);
        if (map != null) {
            mediator = (WeaponAdjudicator) map.get(target);
        }
        return mediator;
    }
    
    protected Class getMediatorClass(Class weaponClass, Class targetClass) {
        Class mediatorClass = null;
        Map map = (Map) mediatorTypes.get(weaponClass);
        if (map != null) {
            mediatorClass =  (Class) map.get(targetClass);
        }
        return mediatorClass;
    }
    
    public WeaponAdjudicator getInstance(Weapon weapon, Mover target) {
        WeaponAdjudicator mediator = getFromCache(weapon, target);
        if (mediator == null) {
            Class mediatorClass = getMediatorClass(weapon.getClass(), target.getClass());
            if (mediatorClass != null) {
                try {
                    mediator = (WeaponAdjudicator) mediatorClass.newInstance();
                }
                catch (InstantiationException e) {System.err.println(e);}
                catch (IllegalAccessException e) {System.err.println(e);}
            }
        }
        return mediator;
    }
    
    public void addMediatorType(String weapon, String target, String mediator) {
        try {
        addMediatorType(Class.forName(weapon), Class.forName(target), Class.forName(mediator));
        }
        catch (ClassNotFoundException e) {System.err.println(e);}
    }
    
    public void addMediatorType(Class weaponClass, Class targetClass,
    Class mediatorClass) {
        Map map = (Map) mediatorTypes.get(weaponClass);
        if (map == null) {
            map = new HashMap();
            mediatorTypes.put(weaponClass, map);
        }
        map.put(targetClass, mediatorClass);
    }
    
    public void flushInstanceCache() {
        synchronized(mediators) {
            this.mediators.clear();
        }
    }
    
    public void flushMediatorTypes() {
        synchronized (mediatorTypes) {
            mediatorTypes.clear();
        }
    }
    
}
