/*
 * ArrivalProcess2.java
 *
 * Created on April 25, 2002, 1:36 PM
 */

package simkit.examples;
import simkit.*;
import simkit.random.*;
/**
 *
 * @author  Arnold Buss
 * @version 
 */
public class ArrivalProcess2 extends BasicSimEntity {

    protected int numberArrivals;
    
    protected RandomVariate interarrivalTime;
    
    /** Creates new ArrivalProcess2 */
    public ArrivalProcess2(RandomVariate rv) {
        super();
        setInterarrivalTime(rv);        
    }

    public RandomVariate getInterarrivalTime() { return interarrivalTime; }
    
    public void setInterarrivalTime(RandomVariate rv) { interarrivalTime = rv; }
    
    public int getNumberArrivals() { return numberArrivals; }
    
    public void reset() {
        super.reset();
        numberArrivals = 0;
    }
    
    public void doRun() {
        waitDelay("Arrival", interarrivalTime.generate());
    }
    
    public void doArrival() {
        property.firePropertyChange("numberArrivals", numberArrivals, ++numberArrivals);
        waitDelay("Arrival", interarrivalTime.generate());
    }

    public void processSimEvent(SimEvent event) {
    }
    
    public void handleSimEvent(SimEvent event) {
        if (event.getEventName().equals("Run")) {
            doRun();
        }
        
        else if(event.getEventName().equals("Arrival")) {
            doArrival();
        }
    }
    
    public String paramString() {
        return toString();
    }
    
    public String toString() {
        return "Arrival Process " + interarrivalTime;
    }
}