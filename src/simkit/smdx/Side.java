package simkit.smdx;
import java.util.LinkedHashMap;

/**
 * A Java "Enumeration" to represent the sides. The side's name is returned
 * in its toString() and getSide() methods.  Thus, an instance of Side has
 * a String property called "Side"
 * 
 * @author Arnold Buss
 * @version $Id$
 **/

public class Side {
    
    /**
     *  These are the default instances of <CODE>Side</CODE>. 
     **/
    protected static LinkedHashMap<String, Side> allSides = 
            new LinkedHashMap<String, Side>(12);

/**
* Red
**/
    public static final Side RED = new Side("Red");

/**
* Blue
**/
    public static final Side BLUE = new Side("Blue");

/**
* Orange
**/
    public static final Side ORANGE = new Side("Orange");

/**
* Purple
**/
    public static final Side PURPLE = new Side("Purple");

/**
* Green
**/
    public static final Side GREEN = new Side("Green");

/**
* White
**/
    public static final Side WHITE = new Side("White");
    
    /**
     *  Name of this Side
     **/
    private String side;
    
    /**
     *  Constructs an instance of Side.
     *  @param side The name of this Side.
     *  @throws IllegalArgumentException if the Side already exists.
     **/
    public Side(String side) {
        this.side = side;
        if (allSides.containsKey(side)) {
            throw new IllegalArgumentException("A Side named " + side
                + " already exists.");
        }
        allSides.put(side, this);
        allSides.put(side.toLowerCase(), this);
    }
    /**
     * Gets the name of this Side.
     * 
     *  @return The name of this Side
     **/
    public String getSide() {return side;}

    /**
     * Returns a String containing the name of this Side.
     * 
     *  @return The name of this Side
     **/
    public String toString() {return side;}
    
    /**
     * Find the Side with the given name. The argument can either
     * be exactly as the Side was constructed or all lower case.
     * 
     * @param sideName Given name of Side
     * @return Side corresponding to the name of the side passed in
     * or null if there is no such side.
     */    
    public static Side getSideFor(String sideName) {
        return allSides.get(sideName.toLowerCase());
    }

}
