package simkit.examples;
import simkit.Schedule;
import java.text.DecimalFormat;
/**
 * Represents a generic minimal entity.  Instances of Customer can be 
 * instantiated and used to represent customers (or jobs) being proccessed
 * in a system.  Each instance keeps track of when it was created and also
 * has the capability of marking points in time for later retreival via the
 * stampTime() method and the timeStamp instance variable.
 * @author  Arnold Buss
 */
public class Customer {
    
    public static final DecimalFormat form = new DecimalFormat("0.000");
    
    private double createdTime;
    private double timeStamp;
    
    /** Creates a new instance of Customer */
    public Customer() {
        reset();
    }
    
    /** This method is so that instances of Customer may be
     * reused.
     */    
    public void reset() {
        createdTime = Schedule.getSimTime();
        stampTime();
    }
    
    /** Mark the current time.
     */    
    public void stampTime() { timeStamp = Schedule.getSimTime(); }
    
    /**
     * @return The value of the last time stamp.
     */    
    public double getTimeStamp() { return timeStamp; }
    
    /**
     * @return The time the Customer was created
     */    
    public double getCreatedTime() { return createdTime; }
    
    /**
     * @return Elapsed simTime since this instance was created
     */    
    public double getTimeSinceCreated() { 
        return Schedule.getSimTime() - getCreatedTime();
    }
    
    /**
     * @return Elapsed simTime since last time stamp.
     */    
    public double getTimeSinceStamp() {
        return Schedule.getSimTime() - getTimeStamp();
    }
    
    /**
     * @return Short description of this instance
     */    
    public String toString() {
        return "Customer [" + form.format(getCreatedTime()) + ", " +
            form.format(getTimeStamp()) +"]";
    }
    
}
