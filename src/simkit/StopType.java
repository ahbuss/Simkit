/*
 * StopType.java
 *
 * Created on June 26, 2002, 11:48 AM
 */

package simkit;
import java.util.*;
/**
 * A Java enumeration for methods of stopping a simulation.
 *
 * @author  ahbuss
 * @version $Id$
 */
public class StopType {
    
/**
* The simulation will be stopped at a specific simulation time.
**/
    public static final StopType STOP_AT_TIME;
    
/**
* The simulation will be stopped after a certain number of
* a certain event has been processed.
**/
    public static final StopType STOP_ON_EVENT;
    
/**
* The simulation will end when there are no more events
* scheduled.
**/
    public static final StopType NO_STOP_TYPE;
   
/**
* A hash of the StopTypes indexed by the name of the StopType.
**/
    protected static Map types = new HashMap();
    
/**
* Add a StopType to the HashMap.
* @param The name of the StopType.
* @param The StopeType to add.
**/
    public static void addNameForType(String name, StopType type) {
        types.put(name, type);
    }
    
    static {
        STOP_AT_TIME = new StopType("Stop at Time");
        STOP_ON_EVENT = new StopType("Stop on Event");
        NO_STOP_TYPE = new StopType("No Stop Type");
        types.put("StopAtTime", STOP_AT_TIME);
        types.put("StopOnEvent", STOP_ON_EVENT);
        types.put("NoStopType", NO_STOP_TYPE);
    }
    
    
/**
* The name of the StopType.
**/ 
    private String name;
    
/**
* Construct a new StopType with the given name 
* and add it to the HashMap of StopTypes.
* This constructor should only be called during static
* initialization of this class or its children.
**/
    protected StopType(String name) {
        this.name = name;
        addNameForType(name, this);
    }
    
/**
* Returns the name of this StopType.
**/
    public String toString() { return name; }
    
}
