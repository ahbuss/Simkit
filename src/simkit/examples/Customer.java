package simkit.examples;
import java.text.DecimalFormat;

import simkit.*;
/**
 * Represents a generic minimal entity.  Instances of Customer can be 
 * instantiated and used to represent customers (or jobs) being processed
 * in a system.  Each instance keeps track of when it was created and also
 * has the capability of marking points in time for later retrieval via the
 * stampTime() method and the timeStamp instance variable.
 * @author  Arnold Buss
 * @version $Id$
 */
public class Customer {
    
/**
* Holds formatting information for outputting times.
**/
    public static final DecimalFormat form = new DecimalFormat("0.000");
    
/**
* The simulation time when this Customer was created.
**/
    private double createdTime;

/**
* A saved time.
**/
    private double timeStamp;
    
    private SimEntity owner;
    
/**
* Creates a new Customer at the current simulation time.
**/
    public Customer(SimEntity owner) {
        setOwner(owner);
        reset();
    }
    
/** 
* Resets this Customer to its initial state setting the creation time to
* the current simulation time. This method is so that instances of Customer may be
* reused.
*/    
    public void reset() {
        createdTime = owner.getEventList().getSimTime();
        stampTime();
    }
    
    /** Saves the current time overwriting any previously saved time.
     */    
    public void stampTime() { timeStamp = owner.getEventList().getSimTime(); }
    
    /**
     * Returns the last saved time.
     * @return The value of the last time stamp.
     */    
    public double getTimeStamp() { return timeStamp; }
    
    /**
     * Returns the simulation time when this Customer was created.
     * @return The time the Customer was created
     */    
    public double getCreatedTime() { return createdTime; }
    
    /**
     * Returns the elapsed time since this Customer was created.
     * @return Elapsed simTime since this instance was created
     */    
    public double getTimeSinceCreated() { 
        return owner.getEventList().getSimTime() - getCreatedTime();
    }
    
    /**
     * Returns the elapsed time since the last time stamp.
     * @return Elapsed simTime since last time stamp.
     */    
    public double getTimeSinceStamp() {
        return owner.getEventList().getSimTime() - getTimeStamp();
    }
    
    /**
     * Returns a String containing the creation time and the last time stamp.
     * @return Short description of this instance
     */    
    public String toString() {
        return "Customer [" + form.format(getCreatedTime()) + ", " +
            form.format(getTimeStamp()) +"]";
    }
    
    public void setOwner(SimEntity se) {
        owner = se;
    }
    
}
