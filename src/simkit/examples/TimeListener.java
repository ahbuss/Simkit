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
 *
 * @author  Arnold Buss
 */
public class TimeListener extends SimEntityBase implements PropertyChangeListener {
    
    protected HashMap arrivalTimes;
    
    public TimeListener() {
        arrivalTimes = new HashMap();
    }
    
    public void reset() {
        super.reset();
        arrivalTimes.clear();
    }
    
    public void doArrival(Integer customer) {
        arrivalTimes.put(customer, new Double(eventList.getSimTime()));
    }
    
    public void doEndService(Integer customer) {
        Double arrivalTime = (Double) arrivalTimes.remove(customer);
        if (arrivalTime != null) {
            firePropertyChange("timeInSystem", 
                eventList.getSimTime() - arrivalTime.doubleValue() );
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
