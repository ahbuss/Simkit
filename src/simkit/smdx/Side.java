package simkit.smdx;
import java.util.HashMap;

/**
 * 19 Oct 98 Started
 * <P> A Java "Enumeration" to represent the sides. The side's name is returned
 * in its toString() and getSide() methods.  Thus, an instance of Side has
 * a String property called "Side"
 * @author Arnold Buss
 **/

public class Side {
    
    /**
     *  These are the only instances of <CODE>Side</CODE> that can ever exist in an
     *  application.  To add more, subclass <CODE>Side</CODE>.
     **/
    public static final Side RED = new Side("Red");
    public static final Side BLUE = new Side("Blue");
    public static final Side ORANGE = new Side("Orange");
    public static final Side PURPLE = new Side("Purple");
    public static final Side GREEN = new Side("Green");
    public static final Side WHITE = new Side("White");
    
    private static HashMap allSides = new HashMap(6);
    /**
     *  Name of this Side
     **/
    private String side;
    
    /**
     *  Construct an instance of Side - note only subclasses can do this.
     **/
    protected Side(String side) {
        this.side = side;
        allSides.put(side, this);
    }
    /**
     *  @return the name of this Side
     **/
    public String getSide() {return side;}
    /**
     *  @return the name of this Side
     **/
    public String toString() {return side;}
    
    /**
     * @return Side corrseponding to the name of the side passed in
     * or null if there is no such side.
     */    
    public static Side getSideFor(String sideName) {
        return (Side) allSides.get(sideName);
    }
}
