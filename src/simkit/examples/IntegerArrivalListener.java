/*
 * IntegerArrivalListener.java
 *
 * Created on February 27, 2004, 1:15 PM
 */

package simkit.examples;

import simkit.Priority;

/**
 * @version $Id: IntegerArrivalListener.java 1000 2007-02-15 19:43:11Z ahbuss $
 * @author  Arnold Buss
 */
public class IntegerArrivalListener extends simkit.SimEntityBase {
    
    protected int nextCustomer;
    
    public void reset() {
        super.reset();
        nextCustomer = 0;
    }
    
    public void doArrival() {
        int oldNextCustomer = getNextCustomer();
        nextCustomer += 1;
        firePropertyChange("nextCustomer", oldNextCustomer, getNextCustomer());
        waitDelay("Arrival", 0.0, Priority.HIGH, getNextCustomer());
    }

    public int getNextCustomer() {
        return nextCustomer;
    }
    
}
