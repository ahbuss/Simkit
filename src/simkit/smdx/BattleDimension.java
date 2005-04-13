
package simkit.smdx;

import java.util.Map;
import java.util.HashMap;
import java.util.logging.*;
/**
* Represents the battle dimension or operating medium of an object.<br/>
* Valid values are:<ul>
* <li>Space</li>
* <li>Air</li>
* <li>Ground</li>
* <li>Surface</li>
* <li>Subsurface</li>
* <li>SOF</li>
* </ul><br/>
* The BattleDimension is used to help determine which Mil-Std-2525 symbol to display.
* <br/>Note: This class is currently not implemented as a Java 1.5 enum, to allow use of 
* Java 1.4 JVM's.
* 
* @author John Ruck (Rolands and Associates Corporation)
* @version $Id$
**/
public class BattleDimension {

    public static Logger log = Logger.getLogger("simkit.smdx");
    
    public static final String _VERSION_ = "$Id$";

/**
* Holds a mapping from Strings to the values.
**/
    protected static Map validValues = new HashMap();

/**
* The name of this battle dimension.
**/
    protected String name;

/**
* The MIL-STD-2525 symbol for this dimension (Position 3)
**/
    protected char symbol;

    public static final BattleDimension SPACE = new BattleDimension("Space", "P");
    public static final BattleDimension AIR = new BattleDimension("Air", "A");
    public static final BattleDimension GROUND = new BattleDimension("Ground", "G");
    public static final BattleDimension SURFACE = new BattleDimension("Surface", "S");
    public static final BattleDimension SUBSURFACE = new BattleDimension("Subsurface", "U");
    public static final BattleDimension SOF = new BattleDimension("SOF", "F");

    static {
//Values are keyed by all lower case version.
        validValues.put("space", SPACE);
        validValues.put("air", AIR);
        validValues.put("ground", GROUND);
        validValues.put("surface", SURFACE);
        validValues.put("subsurface", SUBSURFACE);
        validValues.put("sof", SOF);
    }

/**
* Contructs a new instance with the given name of symbol.
* @param aName The name of the battle dimension
* @param aSymbol The character for position 3 of the Mil-Std-2525 ID code.
**/
    protected BattleDimension(String aName, char aSymbol) {
        this.name = aName;
        this.symbol = aSymbol;
    }

/**
* Contructs a new instance with the given name of symbol.
* @param aName The name of the battle dimension
* @param aSymbol The character for position 3 of the Mil-Std-2525 ID code.
**/
    protected BattleDimension(String aName, String aSymbol) {
        if (aSymbol.length() == 0) {
            throw new IllegalArgumentException("The symbol contained no character");
        } else if (aSymbol.length() > 1) {
            log.warning("A String with more than one character was passed as the Symbol."
                + " The symbol should be a single character. "
                + " Using the first character. The value passed was " + aSymbol);
        }
        this.name = aName;
        this.symbol = aSymbol.charAt(0);
    }
            

/**
* Finds the instance of BattleDimension corresponding to the given name (case insensative).
* @return The BattleDimension for the given name or null if it does not exist.
**/
    public static BattleDimension findBattleDimension(String name) {
        return (BattleDimension) validValues.get(name.toLowerCase());
    }

/**
* The name of the BattleDimension.
**/
    public String getName() {return name;}

/**
* The character for position 3 of the Mil-Std-2525 Symbol ID Code.
* (See Table A-I of MIL-STD-2525B.)
**/
    public char getSymbol() {return symbol;}

    public String toString() {
        return "BattleDimension[" + name + ", " + symbol + "]";
    }
}
