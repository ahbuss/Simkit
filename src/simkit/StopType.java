/*
 * StopType.java
 *
 * Created on June 26, 2002, 11:48 AM
 */

package simkit;

/**
 *
 * @author  ahbuss
 */
public class StopType {
    
    public static final StopType STOP_AT_TIME = new StopType("Stop at Time");
    
    public static final StopType STOP_ON_EVENT = new StopType("Stop on Event");
    
    public static final StopType NO_STOP_TYPE = new StopType("No Stop Type");
    
    private String name;
    
    protected StopType(String name) {
        this.name = name;
    }
    
    public String toString() { return name; }
    
}
