/*
 * StopType.java
 *
 * Created on June 26, 2002, 11:48 AM
 */
package simkit;

/**
 * A Java enumeration for methods of stopping a simulation.
 * <b>Note</b>Converted to an actual enum type, since original was written
 * prior to this facility in Java (AB)
 *
 * @author ahbuss
 * @version $Id$
 */
public enum StopType {

    /**
     * The simulation will be stopped at a specific simulation time.
     */
    STOP_AT_TIME,
    
    /**
     * The simulation will be stopped after a certain number of a certain event
     * has been processed.
     */
    STOP_ON_EVENT,
    
    /**
     * The simulation will end when there are no more events scheduled.
     */
    NO_STOP_TYPE
    
}
