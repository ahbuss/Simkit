package simkit.test;

/*
 * PlatformTypes.java
 *
 * Created on December 6, 2001, 5:17 PM
 */
/**
 *
 * @author ahbuss
 * 
 */
public class PlatformType {

    public static final PlatformType PATROL_BOAT = new PlatformType("Patrol Boat");
    public static final PlatformType PASSERBY = new PlatformType("Passerby");
    public static final PlatformType LOITERER = new PlatformType("Loiterer");
    public static final PlatformType BAD_GUY = new PlatformType("Bad Guy");

    private final String name;

    /**
     * Creates new PlatformTypes
     *
     * @param name Name of this PlatformType
     */
    protected PlatformType(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }

}
