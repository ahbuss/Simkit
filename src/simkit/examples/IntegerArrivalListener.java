/*
 * IntegerArrivalListener.java
 *
 * Created on February 27, 2004, 1:15 PM
 */

package simkit.examples;

/**
 *
 * @author  Arnold Buss
 */
public class IntegerArrivalListener extends simkit.SimEntityBase {
    
    protected int nextCustomer;
    
    public void reset() {
        super.reset();
        nextCustomer = 0;
    }
    
    public void doArrival() {
        waitDelay("Arrival", 0.0,  new Integer(++nextCustomer), 1.0);
    }
    
}
