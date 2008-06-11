/*
 * TimeListener.java
 *
 * Created on February 27, 2004, 1:28 PM
 */

package simkit.examples;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;

import simkit.SimEntityBase;

/**
 * @author  Arnold Buss
 * @version $Id: TimeListener.java 1000 2007-02-15 19:43:11Z ahbuss $
 */
public class TimeListener extends SimEntityBase implements PropertyChangeListener {
    
    protected HashMap<Integer, Double> arrivalTimes;
    
    public TimeListener() {
        arrivalTimes = new HashMap<Integer, Double>();
    }
    
    public void reset() {
        super.reset();
        arrivalTimes.clear();
    }
    
    public void doArrival(Integer customer) {
        arrivalTimes.put(customer, eventList.getSimTime());
    }
    
    public void doEndService(Integer customer) {
        Double arrivalTime = arrivalTimes.remove(customer);
        if (arrivalTime != null) {
            firePropertyChange("timeInSystem", 
                eventList.getSimTime() - arrivalTime.doubleValue());
        }
    }
    
    public void doRenege(Integer customer) {
        Double arrivalTime = (Double) arrivalTimes.remove(customer);
        if (arrivalTime != null) {
            firePropertyChange("delayInQueue", 
                eventList.getSimTime() - arrivalTime.doubleValue() );
            firePropertyChange("delayInQueueRenege", 
                eventList.getSimTime() - arrivalTime.doubleValue() );
        }
    }
    
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("customer")) {
            Double arrivalTime = (Double) arrivalTimes.get(evt.getNewValue());
            if (arrivalTime != null) {
                firePropertyChange("delayInQueue", eventList.getSimTime() -
                    arrivalTime.doubleValue());
                firePropertyChange("delayInQueueServed", eventList.getSimTime() -
                    arrivalTime.doubleValue());
            }
        }
    }
    
}
