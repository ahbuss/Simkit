/*
 * MovementState.java
 *
 * Created on May 28, 2002, 12:32 PM
 */

package simkit.smdx;
import java.util.LinkedHashMap;

/**
 * A java enumeration for the various states of motion for a Mover.<BR/>
 * The available states are:<UL>
 * <LI>PAUSED</LI>
 * <LI>PAUSING</LI>
 * <LI>STOPPED</LI>
 * <LI>STOPPING</LI>
 * <LI>STARTING</LI>
 * <LI>ACCELERATING</LI>
 * <LI>CRUISING (Moving at a constant velocity)</LI>
 * </UL>
 * @author  Arnold Buss
 * @version $Id$
 */
public class MovementState {
    
    public static final MovementState PAUSED = new MovementState("Paused");
    public static final MovementState PAUSING = new MovementState("Pausing");
    public static final MovementState STOPPED = new MovementState("Stopped");
    public static final MovementState STOPPING = new MovementState("Stopping");
    public static final MovementState STARTING = new MovementState("Starting");
    public static final MovementState ACCELERATING = new MovementState("Accelerating");
    public static final MovementState CRUISING = new MovementState("Cruising");
    
/**
* A HashMap of all of the allowed MovementStates keyed by name.
**/
    private static LinkedHashMap states;

/**
* Get the MovementState for the given name. Returns <CODE>null</CODE> if
* the name is not a valid state.
**/
    public static MovementState getState(String name) { return (MovementState) states.get(name); }
        
/**
* The name of this MovementState. (Case sensitive.)
**/
    private String name;
    
/**
* Constructs a new MovementState with the given name. Should only by used
* during static initialization of this class.
**/
    protected MovementState(String name) {
        this.name = name;
        if (states == null) {
            states = new LinkedHashMap(8);
        }
        states.put(name, this);
    }
    
/**
* Returns the name of this MovementState.
**/
    public String toString() { return name; }
    
}
