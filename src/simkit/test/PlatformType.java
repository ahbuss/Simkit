package simkit.test;

/*
 * PlatformTypes.java
 *
 * Created on December 6, 2001, 5:17 PM
 */

/**
 *
 * @author  ahbuss
 * @version $Id: PlatformType.java 451 2003-11-19 01:04:48Z jlruck $
 */
public class PlatformType {

    public static final PlatformType PATROL_BOAT = new PlatformType("Patrol Boat");
    public static final PlatformType PASSERBY = new PlatformType("Passerby");
    public static final PlatformType LOITERER = new PlatformType("Loiterer");
    public static final PlatformType BAD_GUY = new PlatformType("Bad Guy");
    
    private String name;
    /** Creates new PlatformTypes */
    protected PlatformType(String name) {
        this.name = name;
    }
    public String toString() { return name; }

}
