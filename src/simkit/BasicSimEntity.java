package simkit;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import simkit.util.IndexedPropertyChangeEvent;

/**
 *  An abstract basic implementation of <CODE>SimEntity</CODE> that does not use
 *  reflection but rather
 *  relies on the user subclassing and implementing the <CODE>SimEventListener</CODE> method
 *  <CODE>processSimEvent(SimEvent)</CODE>.  The easiest way is for the user to directly dispatch the
 *  event based on the <CODE>SimEvent</CODE> name and parameters. For example, if there is an
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
 *  @version $Id$
 **/
public abstract class BasicSimEntity extends BasicSimEventSource implements SimEntity {
    
/**
* The unique number to be assigned to the next BasicSimEntity created.
**/
    private static int nextSerial;
    static {
        nextSerial = 0;
    }
    
/**
* The name of this SimEntity.
**/
    private String name;

/**
* The priority given to this SimEntity's events. 
* If two events are scheduled at the same time with 
* the same event priority, the one whose owner has a higher
* priority will occur first.
**/
    private double priority;

/**
* A unique identifier for this SimEntity.
**/
    private int serial;

/**
* If true causes debug/trace information to be output.
**/
    private boolean verbose;
    
/*
 * If true (default), then SimEntity is persistent.
 */
    private boolean persistant;
/**
* Manages Properties for the entity. 
* @see PropertyChangeDispatcher
**/
    protected PropertyChangeDispatcher property;

    protected EventList eventList;
    
   /**
   * Construct a new BasicSimEntity with the given name and a default priority.
   * @param name The name of the BasicSimEntity.
   **/
    public BasicSimEntity(String name) {
        this(name, DEFAULT_PRIORITY);
    }
    
   /**
   * Construct a new BasicSimEntity with the given name and priority.
   * @param name The name of the BasicSimEntity.
   * @param priority The priority assigned this BasicSimEntity. 
   * @see #setPriority(double)
   **/
    public BasicSimEntity(String name, double priority, int eventListID) {
        super();
        serial = ++nextSerial;
        setName(name);
        setPriority(priority);
        property = new PropertyChangeDispatcher(this);
        setPersistant(true);                    //TODO add constructor with persistant
        eventList = Schedule.getEventList(eventListID);
        eventList.addRerun(this);
    }
    
    public BasicSimEntity(String name, double priority) {
        this(name, priority, Schedule.getDefaultEventList().getID());
    }
    
   /**
   * Construct a new BasicSimEntity with a default name and the given priority.
   * The name will be the class name plus a unique serial number.
   * @param priority The priority assigned this BasicSimEntity. 
   * @see #setPriority(double)
   **/
    public BasicSimEntity(double priority) {
        this(DEFAULT_ENTITY_NAME, priority);
        setName(getClass().getName() + '.' + getSerial());
    }
    
   /**
   * Construct a new BasicSimEntity with a default name and priority.
   * The name will be the class name plus a unique serial number.
   **/
    public BasicSimEntity() {
        this(DEFAULT_ENTITY_NAME, DEFAULT_PRIORITY);
        setName(getClass().getName() + '.' + getSerial());
    }
    
   /**
   * A BasicSimEntity is "ReRunnable" if it has a doRun method.
   * @return True if this BasicSimEntity has a doRun method, false otherwise.
   **/
    public boolean isReRunnable() {
        boolean reRunnable = false;
        try {
            this.getClass().getMethod("doRun", (Class[])null);
            reRunnable = true;
        }
        catch (NoSuchMethodException e) {}
        return reRunnable;
    }
    
   /**
   * Resets this BasicSimEntity by canceling all of its pending SimEvents.
   **/
    public void reset() {
        interruptAll();
    }
/*
  Four-parameter methods
 */
   /**
   * Schedules a SimEvent for an event that has multiple parameters.
   * @param methodName The name of the event to be scheduled. (The "do" is optional.
   * "doArrive" and "Arrive" are equivalent.)
   * @param delay The amount of time between now and when the event will occur.
   * @param parameters The parameters for the event. Primitives must be wrapped in an Object.
   * @param eventPriority If two events occur at the same time, the one with the highest number
   * will be executed first.
   **/
    public SimEvent waitDelay(
    String      methodName,
    double      delay,
    Object[]    parameters,
    double      eventPriority) {
        
        SimEvent event = SimEventFactory.createSimEvent(this, methodName, delay, parameters, eventPriority);
        attemptSchedule(event);
        return event;
    }
    
   /**
   * Schedules a SimEvent for an event that has a single parameter.
   * @param methodName The name of the event to be scheduled. (The "do" is optional.
   * "doArrive" and "Arrive" are equivalent.)
   * @param delay The amount of time between now and when the event will occur.
   * @param parameter The parameter for the event. Primitives must be wrapped in an Object.
   * @param eventPriority If two events occur at the same time, the one with the highest number
   * will be executed first.
   **/
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
    
   /**
   * Schedules a SimEvent for an event that has multiple parameters with a default priority. 
   * @param methodName The name of the event to be scheduled. (The "do" is optional.
   * "doArrive" and "Arrive" are equivalent.)
   * @param delay The amount of time between now and when the event will occur.
   * @param parameters The parameters for the event. Primitives must be wrapped in an Object.
   **/
    public SimEvent waitDelay(
    String      methodName,
    double      delay,
    Object[]    parameters) {
        SimEvent event =  SimEventFactory.createSimEvent(this, methodName, delay, parameters, DEFAULT_PRIORITY);
        attemptSchedule(event);
        return event;
    }
    
   /**
   * Schedules a SimEvent for an event that has no parameters.
   * @param methodName The name of the event to be scheduled. (The "do" is optional.
   * "doArrive" and "Arrive" are equivalent.)
   * @param delay The amount of time between now and when the event will occur.
   * @param eventPriority If two events occur at the same time, the one with the highest number
   * will be executed first.
   **/
    public SimEvent waitDelay(
    String      methodName,
    double      delay,
    double      eventPriority) {
        SimEvent event = SimEventFactory.createSimEvent(this, methodName, delay, new Object[] {}, eventPriority);
        attemptSchedule(event);
        return event;
    }
    
   /**
   * Schedules a SimEvent for an event that has a single parameter with a default priority.
   * @param methodName The name of the event to be scheduled. (The "do" is optional.
   * "doArrive" and "Arrive" are equivalent.)
   * @param delay The amount of time between now and when the event will occur.
   * @param parameter The parameter for the event. Primitives must be wrapped in an Object.
   **/
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
    
   /**
   * Schedules a SimEvent for an event that has no parameters with a default priority.
   * @param methodName The name of the event to be scheduled. (The "do" is optional.
   * "doArrive" and "Arrive" are equivalent.)
   * @param delay The amount of time between now and when the event will occur.
   **/
    public SimEvent waitDelay(
    String      methodName,
    double      delay )  {
        SimEvent event = SimEventFactory.createSimEvent(this, methodName, delay, new Object[] {}, DEFAULT_PRIORITY);
        attemptSchedule(event);
        return event;
    }
    
/**
* @deprecated No replacement.
* Allowed the method name and event name to be different.
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

/**
* Schedules an event.
**/    
    protected void attemptSchedule(SimEvent event) {
        eventList.scheduleEvent(event);
    }
    
    /**
     * Cancels the next event for this entity that matches the event name and
     * value of the parameters. 
     **/
    public void interrupt(String eventName, Object[] parameters) {
        eventList.interrupt(this, eventName, parameters);
    }
    
    /**
     * Cancels the next event for this entity that matches the event name and
     * value of the parameter. 
     **/
    public void interrupt(String eventName, Object parameter) {
        interrupt(eventName, new Object[] {parameter});
    }
    
    /**
     * Cancels the next event for this entity that matches the event name.
     **/
    public void interrupt(String eventName) {
        eventList.interrupt(this, eventName);
    }
    
    /**
     * Cancels all events for this entity that match the event name and
     * value of the parameters. 
     **/
    public void interruptAll(String eventName, Object[] parameters) {
        eventList.interruptAll(this, eventName, parameters);
    }
    
    /**
     * Cancels all events for this entity that match the event name and
     * value of the parameter. 
     **/
    public void interruptAll(String eventName, Object parameter) {
        interruptAll(eventName, new Object[] {parameter});
    }
    
    /**
     * Cancels all events for this entity that match the event name.
     **/
    public void interruptAll(String eventName) {
        eventList.interruptAll(this, eventName);
    }
    
/**
* Cancel all events for this entity.
**/
    public void interruptAll() {
        eventList.interruptAll(this);
    }
    
/**
* Sets the name of this entity.
**/
    public void setName(String name) {this.name = name;}

// javadoc inherited from Named
    public String getName() {return name;}
    
/**
* Sets the scheduling priority for this entity. If two events
* are scheduled at the same time and have the same event priority,
* the one whose owner has the highest priority will be processed first.
**/
    public void setPriority(double p) {priority = p;}

/**
* The scheduling priority for this entity. If two events
* are scheduled at the same time and have the same event priority,
* the on whose owner has the highest priority will be processed first.
**/
    public double getPriority() {return priority;}
    
/**
* If true causes debug/trace information to be output.
**/
    public void setVerbose(boolean b) { verbose = b; }

/**
* If true causes debug/trace information to be output.
**/
    public boolean isVerbose() { return verbose; }    
    
/**
* Determine if the two arrays contain equal Objects. Equality is determined by the 
* equals method of the Objects.
**/
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

// Javadoc inherited from SimEventListener    
    public abstract void handleSimEvent(SimEvent event) ;
    
/**
* Process the given SimEvent. Entity's should not process other
* entities' doRun events. See the discussion in the description of this
* class for information on implementing this method.
**/
    public abstract void processSimEvent(SimEvent event);
    
// Javadoc inherited from SimEntity
    public int getSerial() { return serial; }
    
// Javadoc inherited from PropertyChangeSource
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        property.addPropertyChangeListener(listener);
    }
    
// Javadoc inherited from PropertyChangeSource
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        property.addPropertyChangeListener(propertyName, listener);
    }
    
// Javadoc inherited from PropertyChangeSource
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        property.removePropertyChangeListener(listener);
    }
    
// Javadoc inherited from PropertyChangeSource
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        property.removePropertyChangeListener(propertyName, listener);
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
     * Get the value of the given property.
     * @param name The name of the property to be retrieved
     * @return The value of the property
     * @throws NullPointerException If the property doesn't exist
     */
    public Object getProperty(String name) {
        return property.getProperty(name);
    }
    
    /**
     * Get the value of the given property or return the supplied default.
     * @param name The name of the property to be retrieved.
     * @param defaultValue The default value -- returned if property's value cannot
     *        be returned or is null.
     * @return The value of the property.
     * @throws NullPointerException If the property doesn't exist
     */
    public Object getProperty(String name, Object defaultValue) {
        return property.getProperty(name, defaultValue);
    }
    
    /**
     * Set the property to the given value.
     * @param name The name of the property to be set
     * @param value The new value of the property
     * @throws NullPointerException If the property doesn't exist
     */
    public void setProperty(String name, Object value) {
        property.setProperty(name, value);
    }
    
/**
* Notify registered PropertChangeListeners that the given Object property has changed.
* @param propertyName The property that changed.
* @param oldValue The value of the property prior to this change.
* @param newValue The value of the property after this change.
**/
    public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        property.firePropertyChange(propertyName, oldValue, newValue);
    }

/**
* Notify registered PropertChangeListeners that the given int property has changed.
* @param propertyName The property that changed.
* @param oldValue The value of the property prior to this change.
* @param newValue The value of the property after this change.
**/
    public void firePropertyChange(String propertyName, int oldValue, int newValue) {
        property.firePropertyChange(propertyName, oldValue, newValue);
    }
    

/**
* Notify registered PropertChangeListeners that the given boolean property has changed.
* This version does not report the previous value.
* @param propertyName The property that changed.
* @param newValue The value of the property after this change.
**/
    public void firePropertyChange(String propertyName, boolean newValue) {
        property.firePropertyChange(propertyName, null, new Boolean(newValue));
    }
    
/**
* Notify registered PropertChangeListeners that the given int property has changed.
* This version does not report the previous value.
* @param propertyName The property that changed.
* @param newValue The value of the property after this change.
**/
    public void firePropertyChange(String propertyName, int newValue) {
        property.firePropertyChange(propertyName, null, new Integer(newValue));
    }
    
/**
* Notify registered PropertChangeListeners that the given double property has changed.
* This version does not report the previous value.
* @param propertyName The property that changed.
* @param newValue The value of the property after this change.
**/
    public void firePropertyChange(String propertyName, double newValue) {
        property.firePropertyChange(propertyName, null, new Double(newValue));    }
    
/**
* Notify registered PropertChangeListeners that the given double property has changed.
* @param propertyName The property that changed.
* @param oldValue The value of the property prior to this change.
* @param newValue The value of the property after this change.
**/
    public void firePropertyChange(String propertyName, double oldValue, double newValue) {
		property.firePropertyChange(propertyName, new Double(oldValue), new Double(newValue));
    }
    
/**
* Notify registered PropertChangeListeners that the given boolean property has changed.
* @param propertyName The property that changed.
* @param oldValue The value of the property before this change.
* @param newValue The value of the property after this change.
**/    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
        firePropertyChange(
            propertyName, 
            oldValue ? Boolean.TRUE : Boolean.FALSE,
            newValue ? Boolean.TRUE : Boolean.FALSE
        );
    }
    
/**
* Notify registered PropertChangeListeners that the given Object property has changed.
* This version does not report the previous value.
* @param propertyName The property that changed.
* @param newValue The value of the property after this change.
**/
    public void firePropertyChange(String propertyName, Object newValue) {
        property.firePropertyChange(propertyName, null, newValue);
    }
    
/**
* Notify registered PropertyChangeListeners that an element of an indexed property has changed.
* @param index The element that has changed.
* @param propertyName The Indexed property containing the changed element.
* @param oldValue The value of the property prior to this change.
* @param newValue The value of the property after this change.
**/
    public void fireIndexedPropertyChange(int index, String propertyName, Object oldValue,
    Object newValue) {
        property.firePropertyChange(new IndexedPropertyChangeEvent(this, propertyName, oldValue, newValue, index));
    }
    
/**
* Notify registered PropertyChangeListeners that an element of an indexed property has changed.
* This version doesn't report the previous value.
* @param index The element that has changed.
* @param propertyName The Indexed property containing the changed element.
* @param newValue The value of the property after this change.
**/
    public void fireIndexedPropertyChange(int index, String propertyName, Object newValue) {
        property.firePropertyChange(new IndexedPropertyChangeEvent(this, propertyName, null, newValue, index));
    }
    
/**
* Notify registered PropertyChangeListeners that an element of an indexed property has changed.
* This version doesn't report the previous value and is for int values.
* @param index The element that has changed.
* @param propertyName The Indexed property containing the changed element.
* @param newValue The value of the property after this change.
**/
    public void fireIndexedPropertyChange(int index, String propertyName, int newValue) {
        property.firePropertyChange(new IndexedPropertyChangeEvent(this, propertyName, null, new Integer(newValue), index));
    }
    
/**
* Notify registered PropertyChangeListeners that an element of an indexed property has changed.
* This version is for int values.
* @param index The element that has changed.
* @param propertyName The Indexed property containing the changed element.
* @param oldValue The value of the property prior to this change.
* @param newValue The value of the property after this change.
**/
    public void fireIndexedPropertyChange(int index, String propertyName, int oldValue, int newValue) {
        property.firePropertyChange(new IndexedPropertyChangeEvent(this, propertyName, new Integer(oldValue), new Integer(newValue), index));
    }
    
/**
* Notify registered PropertyChangeListeners that an element of an indexed property has changed.
* This version doesn't report the previous value and is for double values.
* @param index The element that has changed.
* @param propertyName The Indexed property containing the changed element.
* @param newValue The value of the property after this change.
**/
    public void fireIndexedPropertyChange(int index, String propertyName, double newValue) {
        property.firePropertyChange(new IndexedPropertyChangeEvent(this, propertyName, null, new Double(newValue), index));
    }
    
/**
* Notify registered PropertyChangeListeners that an element of an indexed property has changed.
* This version is for double values
* @param index The element that has changed.
* @param propertyName The Indexed property containing the changed element.
* @param oldValue The value of the property prior to this change.
* @param newValue The value of the property after this change.
**/
    public void fireIndexedPropertyChange(int index, String propertyName, double oldValue, double newValue) {
        property.firePropertyChange(new IndexedPropertyChangeEvent(this, propertyName, new Double(oldValue), new Double(newValue), index));
    }
    
/**
* Notify registered PropertyChangeListeners that an element of an indexed property has changed.
* This version doesn't report the previous value and is for boolean values.
* @param index The element that has changed.
* @param propertyName The Indexed property containing the changed element.
* @param newValue The value of the property after this change.
**/
    public void fireIndexedPropertyChange(int index, String propertyName, boolean newValue) {
        property.firePropertyChange(new IndexedPropertyChangeEvent(this, propertyName, null, new Boolean(newValue), index));
    }
    
/**
* Notify registered PropertyChangeListeners that an element of an indexed property has changed.
* This version is for boolean values.
* @param index The element that has changed.
* @param propertyName The Indexed property containing the changed element.
* @param oldValue The value of the property prior to this change.
* @param newValue The value of the property after this change.
**/
    public void fireIndexedPropertyChange(int index, String propertyName, boolean oldValue, boolean newValue) {
        property.firePropertyChange(new IndexedPropertyChangeEvent(this, propertyName, new Boolean(oldValue), new Boolean(newValue), index));
    }
    
    /**
     * A default string description of this entity,
     * name (Entity Priority)
     **/ 
    public String toString() {
        StringBuffer buf = new StringBuffer(this.getName());
        try {
            BeanInfo info = Introspector.getBeanInfo( this.getClass(), simkit.BasicSimEntity.class );
            PropertyDescriptor[] descriptors = info.getPropertyDescriptors();
            for (int i = 0; i < descriptors.length; ++i) {
                Method readMethod = descriptors[i].getReadMethod();
                Method writeMethod = descriptors[i].getWriteMethod();
                if (writeMethod != null && readMethod != null) {
                    buf.append(System.getProperty("line.separator"));
                    buf.append('\t');
                    buf.append( descriptors[i].getName() );
                    buf.append(" = ");
                    buf.append( readMethod.invoke(this, (Object[])null));
                }
            }
        } 
        catch (IntrospectionException e) { e.printStackTrace(System.err);  }
        catch (IllegalAccessException e) { e.printStackTrace(System.err);  }
        catch (InvocationTargetException e) { e.getTargetException().printStackTrace(System.err);  }
        return buf.toString();
    }        
    /**  
     * Gets an array of all registered PropertyChangeListeners.
     * @return array of PropertyChangeListeners
     */
    public PropertyChangeListener[] getPropertyChangeListeners() {
        return property.getPropertyChangeListeners();
    }
    
    public boolean isPersistant() {
        return  persistant;
    }    
    
    public void setPersistant(boolean persist) {
        persistant = persist;
        if (!persistant) {
            eventList.removeRerun(this);
        }
    }
    
    public void setEventListID(int id) {
        if (eventList != null) {
            eventList.removeRerun(this);
        }
        eventList = Schedule.getEventList(id);
        if (isPersistant()) {
            eventList.addRerun(this);
        }
    }
    
    public int getEventListID() { return eventList.getID(); }
    
    public EventList getEventList() {
        return eventList;
    }
    
}
