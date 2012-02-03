package simkit.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.logging.Logger;

/**
 * Serve as a base class for 1.4 style enums. 1.5 style is not
 * used since we want to be able to define types at runtime.
 * <p>
 *
 * @version $Id$
 * @version Copied from NpsTracCommon at rev EnumBase.java 24 
 * 2007-12-04 22:16:48Z.
 * @author John Ruck (Rolands and Associates Corporation)
 **/
public abstract class EnumBase implements Comparable<EnumBase> {

    public static final String _VERSION_ = "$Id$";
    public static final Logger log = Logger.getLogger("simkit.util");
    /**
     * The name that this enum is identified by.
     **/
    protected String name;
    /**
     * The order in which this enum was created. (zero based)
     * The serial will be unique accross all enums. 
     **/
    protected int serial;
    /**
     * The serial of the next enum to be created.
     **/
    protected static int nextSerial = 0;
    /**
     * Holds the list of different types of enums 
     **/
    protected static HashMap<Class, HashMap<String, EnumBase>> 
            types = new LinkedHashMap<Class, HashMap<String, EnumBase>>();

    /**
     * Constructs a new enum and adds it to the members list.
     * 
     * @throws IllegalArgumentException if an enum of the same type already 
     * exists
     * with the same name.
     **/
    public EnumBase(String theName) {
        this.name = theName;
        this.serial = nextSerial;
        nextSerial++;
        put(this);
    }

    /**
     * Returns the name of this enum.
     **/
    public String getName() {
        return name;
    }

    /**
     * Returns the serial of this enum.
     **/
    public int getSerial() {
        return serial;
    }

    /**
     * Returns the next serial.
     **/
    public static int getNextSerial() {
        return nextSerial;
    }

    /**
     * Returns a Collection containing the enums of the given Class.
     **/
    public static <T> Collection<T> getMembers(Class<T> clazz) {
        Collection<T> ret = new LinkedHashSet<T>();
        HashMap<String, EnumBase> members = types.get(clazz);
        if (members != null) {
            for (EnumBase v : members.values()) {
                ret.add((T) v);
            }
        }
        return ret;
    }

    /**
     * Adds a new enum as a member of the enum class.
     * 
     * @throws IllegalArgumentException if an enum of the same name and 
     * type already exists.
     **/
    protected void put(EnumBase member) {
        Class clazz = this.getClass();
        if (clazz != member.getClass()) {
            throw new Error(
                    "Bad assumption about what this.getClass() should return");
        }
        HashMap<String, EnumBase> members = types.get(clazz);
        if (members == null) {
            members = new LinkedHashMap<String, EnumBase>();
            types.put(clazz, members);
        }
        if (members.put(member.getName(), member) != null) {
            throw new IllegalArgumentException("The enum of type " + clazz + 
                    " named " + member.getName() + " already exists.");
        }
    }

    /**
     * Finds the enum with the given name and Class. Returns null if there
     * are no enums of the given Class or if one with the given name is not 
     * found.
     **/
    public static <T> T find(String name, Class<T> clazz) {
        HashMap<String, EnumBase> members = types.get(clazz);
        if (members == null) {
            return null;
        }
        return (T) members.get(name);
    }

    /**
     * Returns true if the serials are the same. (Which should also mean
     * that the names and Class are the same.)
     **/
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EnumBase)) {
            return false;
        }
        EnumBase that = (EnumBase) o;
        if (this.serial == that.getSerial()) {
            return true;
        }
        return false;
    }

    /**
     * Returns the hash code of the name of this enum.
     **/
    @Override
    public int hashCode() {
        return name.hashCode();
    }

    /**
     * Compares two EnumBases of the same Class.
     * @throws ClassCastException if they are not the same Class.
     **/
//    @Override
    public int compareTo(EnumBase e) {
        if (this.getClass() != e.getClass()) {
            throw new ClassCastException();
        }
        if (e.equals(this)) {
            return 0;
        } else if (this.serial < e.getSerial()) {
            return -1;
        } else {
            return 1;
        }
    }

    @Override
    public String toString() {
        return "[class = " + getClass().getName() + " name = " + name + "]";
    }

    /**
     * Resets EnumBase back to its original, just loaded condition.
     * Primarily to support testing.
     **/
    public static void _reset() {
        log.warning("Reset should only be used to support testing");
        clear();
    }

    /**
     * Resets EnumBase back to its original, just loaded condition.
     **/
    public static void reset() {
        clear();
    }

    /**
     * Resets EnumBase back to its original, just loaded condition.
     **/
    public static void clear() {
        nextSerial = 0;
        types = new LinkedHashMap<Class, HashMap<String, EnumBase>>();
    }

    /**
     * Clears only instances of the given Class
     */
    public static void clear(Class clazz) {
        types.remove(clazz);
    }
}
            
