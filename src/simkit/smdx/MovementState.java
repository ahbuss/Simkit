/*
 * MovementState.java
 *
 * Created on May 28, 2002, 12:32 PM
 */

package simkit.smdx;
import java.util.*;

/**
 *
 * @author  Arnold Buss
 */
public class MovementState {
    
    public static final MovementState PAUSED = new MovementState("Paused");
    public static final MovementState PAUSING = new MovementState("Pausing");
    public static final MovementState STOPPED = new MovementState("Stopped");
    public static final MovementState STOPPING = new MovementState("Stopping");
    public static final MovementState STARTING = new MovementState("Starting");
    public static final MovementState ACCELERATING = new MovementState("Accelerating");
    public static final MovementState CRUISING = new MovementState("Cruising");
    
    private static HashMap states;
    public static MovementState getState(String name) { return (MovementState) states.get(name); }
        
    private String name;
    
    protected MovementState(String name) {
        this.name = name;
        if (states == null) {
            states = new HashMap(8);
        }
        states.put(name, this);
    }
    
    public String toString() { return name; }
    
}
