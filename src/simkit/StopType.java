/*
 * StopType.java
 *
 * Created on June 26, 2002, 11:48 AM
 */

package simkit;
import java.util.*;
/**
 *
 * @author  ahbuss
 */
public class StopType {
    
    public static final StopType STOP_AT_TIME;
    
    public static final StopType STOP_ON_EVENT;
    
    public static final StopType NO_STOP_TYPE;
   
    protected static Map types = new HashMap();
    
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
    
    
 
    private String name;
    
    protected StopType(String name) {
        this.name = name;
        addNameForType(name, this);
    }
    
    public String toString() { return name; }
    
}
