package simkit;
/**
 *  An abstract basic implementation of <CODE>SimEntity</CODE> that does not use
 *  reflection but rather
 *  relies on the user subclassing and implementing the <CODE>SimEventListener</CODE> method
 *  <CODE>processSimEvent(SimEvent)</CODE>.  The easiest way is for the user to directly dispatch the
 *  event based on the <CODE>SimEvent</CODE> name and paramters. For eexample, if there is an
 *  Event method called "<CODE>doThis()</CODE>", then a simple implementation of
 *  <CODE>processSimEvent(SimEvent)</CODE> is:
 * <PRE>
 * public void processSimEvent(SimEvent event) {
 * if (event.getMethodName().equals("doThis")) {this.doThis();}
 * }
 * </PRE>
 *  The primary disadvantage of this class (vice <CODE>SimEntityBase</CODE>) is
 *  that the user must remember to invoke each "do" method and pass it the
 *  correct parameters, if necessary.  Any further subclasses must remember to
 *  override <CODE>processSimEvent(SimEvent)</CODE> to
 *  add all additional "do" methods (or else they will be omitted altogether without
 *  comment).  The advantages are that it is slightly easier to understand, since
 *  it doesn't rely on the complex reflection of <CODE>SimEntityBase</CODE>.  This
 *  also runs about twice as fast as <CODE>SimEntityBase</CODE> based on very
 *  small-sample ad-hoc tests.
 *
 *  <P>One extremely important point about writing a <CODE>processSimEvent(SimEvent)</CODE>
 *  method: <CODE>processSimEvent(SimEvent)</CODE> <STRONG>must not</STRONG>
 *  execute a <CODE>doRun())</CODE> method if it is not the "owner" (i.e. if it
 *  gets a "Run" event by through listening).  Failure to do this will result in
 *  multiple "Run" events for the same object at the start of a simulation.
 *  @author Arnold Buss
 *  @version 0.5
 **/

import java.util.*;
import java.lang.reflect.*;
import java.beans.*;
import simkit.util.IndexedPropertyChangeEvent;

public abstract class BasicSimEntity extends BasicSimEventSource implements SimEntity {
    
    private static int nextSerial;
    static {
        nextSerial = 0;
    }
    
    private String name;
    private double priority;
    private int serial;
    protected PropertyChangeDispatcher property;
    private int warnLevel;
    
    public BasicSimEntity(String name) {
        this(name, DEFAULT_PRIORITY);
    }
    
    public BasicSimEntity(String name, double priority) {
        super();
        serial = ++nextSerial;
        setName(name);
        setPriority(priority);
        setWarnLevel(0);
        property = new PropertyChangeDispatcher(this);
        Schedule.addRerun(this);
    }
    
    public BasicSimEntity(double priority) {
        this(DEFAULT_ENTITY_NAME, priority);
        setName(getClass().getName() + '.' + getSerial());
    }
    
    public BasicSimEntity() {
        this(DEFAULT_ENTITY_NAME, DEFAULT_PRIORITY);
        setName(getClass().getName() + '.' + getSerial());
    }
    
    public void setWarnLevel(int level) { warnLevel = level; }
    
    public int getWarnLevel() { return warnLevel; }
    
    public boolean isReRunnable() {
        boolean reRunnable = false;
        try {
            this.getClass().getMethod("doRun", null);
            reRunnable = true;
        }
        catch (NoSuchMethodException e) {}
        return reRunnable;
    }
    
    public void reset() {
        interruptAll();
    }
/*
  Four-parameter methods
 */
    public SimEvent waitDelay(
    String      methodName,
    double      delay,
    Object[]    parameters,
    double      eventPriority) {
        
        SimEvent event = SimEventFactory.createSimEvent(this, methodName, delay, parameters, eventPriority);
        attemptSchedule(event);
        return event;
    }
    
    public SimEvent waitDelay(
    String      methodName,
    double      delay,
    Object      parameter,
    double      eventPriority) {
        SimEvent event = SimEventFactory.createSimEvent(this, methodName, delay, new Object[] {parameter}, eventPriority);
        attemptSchedule(event);
        return event;
    }
    
/*
  Three parameter methods
 */
    
    public SimEvent waitDelay(
    String      methodName,
    double      delay,
    Object[]    parameters) {
        SimEvent event =  SimEventFactory.createSimEvent(this, methodName, delay, parameters, DEFAULT_PRIORITY);
        attemptSchedule(event);
        return event;
    }
    
    public SimEvent waitDelay(
    String      methodName,
    double      delay,
    double      eventPriority) {
        SimEvent event = SimEventFactory.createSimEvent(this, methodName, delay, new Object[] {}, eventPriority);
        attemptSchedule(event);
        return event;
    }
    
    public SimEvent waitDelay(
    String      methodName,
    double      delay,
    Object      parameter ) {
        
        SimEvent event = SimEventFactory.createSimEvent(this, methodName, delay, new Object[] {parameter});
        attemptSchedule(event);
        return event;
    }
    
/*
  Two parameter method
 */
    
    public SimEvent waitDelay(
    String      methodName,
    double      delay )  {
        SimEvent event = SimEventFactory.createSimEvent(this, methodName, delay, new Object[] {}, DEFAULT_PRIORITY);
        attemptSchedule(event);
        return event;
    }
    
/*
   From previous version -- deprecated
   @deprecated
 */
    
    public SimEvent waitDelay(
    String      methodName,
    Object      parameter,
    double      delay,
    String      eventName ) {
        SimEvent event = SimEventFactory.createSimEvent(this, methodName, delay, new Object[] {parameter}, DEFAULT_PRIORITY);
        attemptSchedule(event);
        return event;
    }
    
    protected void attemptSchedule(SimEvent event) {
        Schedule.scheduleEvent(event);
    }
    
    /**
     *  Implements simkit.SimEntity.interrupt(String, Object[]).
     **/
    public void interrupt(String eventName, Object[] parameters) {
        Schedule.interrupt(this, eventName, parameters);
    }
    
    /**
     * Convenience interrupt methods
     **/
    
    public void interrupt(String eventName, Object parameter) {
        interrupt(eventName, new Object[] {parameter});
    }
    
    public void interrupt(String eventName) {
        Schedule.interrupt(this, eventName);
    }
    
    public void interrupt() {
        Schedule.interrupt(this);
    }
    
    /**
     * Implement simkit.SimEntity.interruptAll(String, Object[]).
     **/
    public void interruptAll(String eventName, Object[] parameters) {
        Schedule.interruptAll(this, eventName, parameters);
    }
    
    /**
     * Convenience interruptAll methods
     **/
    
    public void interruptAll(String eventName, Object parameter) {
        interruptAll(eventName, new Object[] {parameter});
    }
    
    public void interruptAll(String eventName) {
        Schedule.interruptAll(this, eventName);
    }
    
    public void interruptAll() {
        Schedule.interruptAll(this);
    }
    
    public void setName(String name) {this.name = name;}
    public String getName() {return name;}
    
    public void setPriority(double p) {priority = p;}
    public double getPriority() {return priority;}
    
    
    public static boolean parametersMatch(Object[] fromEvent, Object[] fromInterrupt) {
        boolean match = true;
        if ( (fromEvent == null && fromInterrupt != null) ||
        (fromEvent == null && fromInterrupt != null) ) {
            match = false;
        }
        else if ((fromEvent == null) && (fromInterrupt == null)) {
            match = true;
        }
        else if (fromEvent.length != fromInterrupt.length) {
            match = false;
        }
        else {
            for (int i = 0; i < fromEvent.length; i++) {
                if (! (fromEvent[i].equals(fromInterrupt[i])) ) {
                    match = false;
                    break;
                }
            }
        }
        return match;
    }
    
    public abstract void handleSimEvent(SimEvent event) ;
    
    public abstract void processSimEvent(SimEvent event);
    
    public int getSerial() { return serial; }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        property.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        property.removePropertyChangeListener(listener);
    }
    
    /**
     * Notify all PropertyChangeListeners of the PropertyChangeEvent.
     * @param e The event with all the information about what property has changed
     * and to what value.
     */
    public void firePropertyChange(PropertyChangeEvent e) {
        property.firePropertyChange(e);
    }
    
    /**
     * @param name The name of the property to be retrieved
     * @return The value of the property
     */
    public Object getProperty(String name) {
        return property.getProperty(name);
    }
    
    /**
     * @param name The name of the property to be retrieved.
     * @param defaultValue The default value -- returned if property's value cannot
     *        be returned or is null.
     * @return The value of the property.
     */
    public Object getProperty(String name, Object defaultValue) {
        return property.getProperty(name, defaultValue);
    }
    
    /**
     * @param name The name of the property to be set
     * @param value The new value of the property
     */
    public void setProperty(String name, Object value) {
        property.setProperty(name, value);
    }
    
    public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        property.firePropertyChange(propertyName, oldValue, newValue);
    }
    
    public void firePropertyChange(String propertyName, int oldValue, int newValue) {
        property.firePropertyChange(propertyName, oldValue, newValue);
    }
    
    public void firePropertyChange(String propertyName, boolean newValue) {
        property.firePropertyChange(propertyName, null, new Boolean(newValue));
    }
    
    public void firePropertyChange(String propertyName, int newValue) {
        property.firePropertyChange(propertyName, null, new Integer(newValue));
    }
    
    public void firePropertyChange(String propertyName, double newValue) {
        property.firePropertyChange(propertyName, null, new Double(newValue));    }
    
    public void firePropertyChange(String propertyName, Object newValue) {
        property.firePropertyChange(propertyName, null, newValue);
    }
    
    public void fireIndexedPropertyChange(int index, String propertyName, Object oldValue,
    Object newValue) {
        property.firePropertyChange(new IndexedPropertyChangeEvent(this, propertyName, oldValue, newValue, index));
    }
    
    public void fireIndexedPropertyChange(int index, String propertyName, Object newValue) {
        property.firePropertyChange(new IndexedPropertyChangeEvent(this, propertyName, null, newValue, index));
    }
    
    public void fireIndexedPropertyChange(int index, String propertyName, int newValue) {
        property.firePropertyChange(new IndexedPropertyChangeEvent(this, propertyName, null, new Integer(newValue), index));
    }
    
    public void fireIndexedPropertyChange(int index, String propertyName, int oldValue, int newValue) {
        property.firePropertyChange(new IndexedPropertyChangeEvent(this, propertyName, new Integer(oldValue), new Integer(newValue), index));
    }
    
    public void fireIndexedPropertyChange(int index, String propertyName, double newValue) {
        property.firePropertyChange(new IndexedPropertyChangeEvent(this, propertyName, null, new Double(newValue), index));
    }
    
    public void fireIndexedPropertyChange(int index, String propertyName, double oldValue, double newValue) {
        property.firePropertyChange(new IndexedPropertyChangeEvent(this, propertyName, new Double(oldValue), new Double(newValue), index));
    }
    
    public void fireIndexedPropertyChange(int index, String propertyName, boolean newValue) {
        property.firePropertyChange(new IndexedPropertyChangeEvent(this, propertyName, null, new Boolean(newValue), index));
    }
    
    public void fireIndexedPropertyChange(int index, String propertyName, boolean oldValue, boolean newValue) {
        property.firePropertyChange(new IndexedPropertyChangeEvent(this, propertyName, new Boolean(oldValue), new Boolean(newValue), index));
    }
    
    /**
     * A default string description of this entity,
     *
     * name (Entity Priority)
     **/ 
    public String toString() {
        return this.getName() + ' ' + '(' + getPriority() + ')';
    }
        
}
