package simkit.smdx;
import java.util.HashMap;

/**
 * A Java "Enumeration" to represent the sides. The side's name is returned
 * in its toString() and getSide() methods.  Thus, an instance of Side has
 * a String property called "Side"
 * @author Arnold Buss
 * @version $Id$
 **/

public class Side {
    
    /**
     *  These are the only instances of <CODE>Side</CODE> that can ever exist in an
     *  application.  To add more, subclass <CODE>Side</CODE>.
     **/
    protected static HashMap allSides = new HashMap(12);

    public static final Side RED = new Side("Red");
    public static final Side BLUE = new Side("Blue");
    public static final Side ORANGE = new Side("Orange");
    public static final Side PURPLE = new Side("Purple");
    public static final Side GREEN = new Side("Green");
    public static final Side WHITE = new Side("White");
    
    /**
     *  Name of this Side
     **/
    private String side;
    
    /**
     *  Constructs an instance of Side.
     *  @param side The name of this Side.
     **/
    protected Side(String side) {
        this.side = side;
        allSides.put(side, this);
        allSides.put(side.toLowerCase(), this);
    }
    /**
     * Gets the name of this Side.
     *  @return The name of this Side
     **/
    public String getSide() {return side;}

    /**
     * Returns a String containing the name of this Side.
     *  @return The name of this Side
     **/
    public String toString() {return side;}
    
    /**
     * Find the Side with the given name. The argument can either
     * be exactly as the Side was constructed or all lower case.
     * @return Side corresponding to the name of the side passed in
     * or null if there is no such side.
     */    
    public static Side getSideFor(String sideName) {
        return (Side) allSides.get(sideName);
    }
}
