package simkit;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import simkit.random.RandomVariate;
import simkit.util.IndexedPropertyChangeEventX;
import simkit.util.PropertyChangeEventX;

/**
 * An abstract basic implementation of <CODE>SimEntity</CODE> that does not use
 * reflection but rather relies on the user subclassing and implementing the
 * <CODE>SimEventListener</CODE> method <CODE>processSimEvent(SimEvent)</CODE>.
 * The easiest way is for the user to directly dispatch the event based on the
 * <CODE>SimEvent</CODE> name and parameters. For example, if there is an Event
 * method called "<CODE>doThis()</CODE>", then a simple implementation of
 * <CODE>processSimEvent(SimEvent)</CODE> is:
 * <PRE>
 * public void processSimEvent(SimEvent event) {
 * if (event.getMethodName().equals("doThis")) {this.doThis();}
 * }
 * </PRE> The primary disadvantage of this class (vice
 * <CODE>SimEntityBase</CODE>) is that the user must remember to invoke each
 * "do" method and pass it the correct parameters, if necessary. Any further
 * subclasses must remember to override <CODE>processSimEvent(SimEvent)</CODE>
 * to add all additional "do" methods (or else they will be omitted altogether
 * without comment). The advantages are that it is slightly easier to
 * understand, since it doesn't rely on the complex reflection of
 * <CODE>SimEntityBase</CODE>. This also runs about twice as fast as
 * <CODE>SimEntityBase</CODE> based on very small-sample ad-hoc tests.
 *
 * <P>
 * One extremely important point about writing a
 * <CODE>processSimEvent(SimEvent)</CODE> method:
 * <CODE>processSimEvent(SimEvent)</CODE> <STRONG>must not</STRONG>
 * execute a <CODE>doRun())</CODE> method if it is not the "owner" (i.e. if it
 * gets a "Run" event by through listening). Failure to do this will result in
 * multiple "Run" events for the same object at the start of a simulation.
 *
 * @author Arnold Buss
 *
 */
public abstract class BasicSimEntity extends BasicSimEventSource
        implements SimEntity {

    /**
     * The unique number to be assigned to the next BasicSimEntity created.
     *
     */
    private static int nextSerial;

    public static final void resetNextSerial() {
        nextSerial = 0;
    }

    static {
        nextSerial = 0;
    }

    /**
     * The name of this SimEntity.
     *
     */
    private String name;

    /**
     * The priority given to this SimEntity's events. If two events are
     * scheduled at the same time with the same event priority, the one whose
     * owner has a higher priority will occur first.
     *
     */
    private Priority priority;

    /**
     * A unique identifier for this SimEntity.
     *
     */
    private int serial;

    /**
     * If true causes debug/trace information to be output.
     *
     */
    private boolean verbose;

    /*
 * If true (default), then SimEntity is persistent.
     */
    private boolean persistant;
    /**
     * Manages Properties for the entity.
     *
     * @see PropertyChangeDispatcher
     *
     */
    protected PropertyChangeDispatcher property;

    protected BasicEventList eventList;

    private boolean justDefinedProperties;

    /**
     * If true (default) then all added properties are cleared in the reset()
     * method.
     */
    private boolean clearAddedPropertiesOnReset;

    /**
     * Construct a new BasicSimEntity with the given name and a default
     * priority.
     *
     * @param name The name of the BasicSimEntity.
     *
     */
    public BasicSimEntity(String name) {
        this(name, Priority.DEFAULT);
    }

    /**
     * Construct a new BasicSimEntity with the given name and priority.
     *
     * @param name The name of the BasicSimEntity.
     * @param priority The priority assigned this BasicSimEntity.
     * @param eventListID id of this BasicSimEntity's EventList
     * @see #setPriority(Priority)
     */
    public BasicSimEntity(String name, Priority priority, int eventListID) {
        super();
        serial = ++nextSerial;
        setName(name);
        setPriority(priority);
        Class<?> stopClass = (this instanceof SimEntityBase) ? SimEntityBase.class : BasicSimEntity.class;
        property = new PropertyChangeDispatcher(this, stopClass);
        setPersistant(true);                    //TODO add constructor with persistant
        eventList = Schedule.getEventList(eventListID);
        eventList.addRerun(this);
        setJustDefinedProperties(true);
        this.setClearAddedPropertiesOnReset(true);
    }

    public BasicSimEntity(String name, Priority priority) {
        this(name, priority, Schedule.getDefaultEventList().getID());
    }

    /**
     * Construct a new BasicSimEntity with a default name and the given
     * priority. The name will be the class name plus a unique serial number.
     *
     * @param priority The priority assigned this BasicSimEntity.
     * @see #setPriority(Priority)
     *
     */
    public BasicSimEntity(Priority priority) {
        this(DEFAULT_ENTITY_NAME, priority);
        setName(getClass().getSimpleName() + '.' + getSerial());
    }

    /**
     * Construct a new BasicSimEntity with a default name and priority. The name
     * will be the class name plus a unique serial number.
     *
     */
    public BasicSimEntity() {
        this(DEFAULT_ENTITY_NAME, Priority.DEFAULT);
        setName(getClass().getSimpleName() + '.' + getSerial());
    }

    /**
     * A BasicSimEntity is "ReRunnable" if it has a doRun method.
     *
     * @return True if this BasicSimEntity has a doRun method, false otherwise.
     */
    @Override
    public boolean isReRunnable() {
        boolean reRunnable;
        try {
            this.getClass().getMethod("doRun", (Class<?>[]) null);
            reRunnable = true;
        } catch (NoSuchMethodException e) {
            //  If we are here, it is ok, the class simply isn't,
            // in fact, rerunnable.
            reRunnable = false;
        }
        return reRunnable;
    }

    /**
     * Resets this BasicSimEntity by canceling all of its pending SimEvents.
     * Clears all added properties in the PropertyChangeDispatcher instance
     * Remove all SimEventListeners that are transient (not persistent)
     */
    @Override
    public void reset() {
        interruptAll();
        if (isClearAddedPropertiesOnReset()) {
            property.clearAddedProperties();
        }
        SimEventListener[] listeners = getSimEventListeners();
        for (SimEventListener listener : listeners) {
            if (listener instanceof BasicSimEntity
                    && !((BasicSimEntity) listener).isPersistant()) {
                this.removeSimEventListener(listener);
            }
        }
    }

    /*
  Four-parameter methods
     */
    /**
     * Schedules a SimEvent for an event that has multiple parameters.
     *
     * @param name The name of the event to be scheduled. (The "do" is optional.
     * "doArrive" and "Arrive" are equivalent.)
     * @param delay The amount of time between now and when the event will
     * occur.
     * @param priority If two events occur at the same time, the one with the
     * highest number will be executed first.
     * @param parameters The parameters for the event.
     * @return The SimEvent that is put on the Event List
     */
    @Override
    public SimEvent waitDelay(String name, double delay, Priority priority, Object... parameters) {

        SimEvent event = new SimEvent(this, name, parameters, delay, priority);
        attemptSchedule(event);
        return event;
    }

    /**
     * Schedules an event with the default priority /
     *
     * @param name Event name of event
     * @param delay simTime until event is to occur
     * @param parameters optional parameters for event
     * @return The SimEvent that is put on the Event List
     */
    @Override
    public SimEvent waitDelay(String name, double delay, Object... parameters) {
        return this.waitDelay(name, delay, Priority.DEFAULT, parameters);
    }

    /**
     * Schedules an event using a RandomVariate instance to generate the delay.
     * Priority is default
     *
     * @param name Event name of event
     * @param delayGenerator RandomVariate that is used to generate the time
     * until the event will occur
     * @param parameters optional parameters for event
     * @return The SimEvent that is put on the Event List
     */
    public SimEvent waitDelay(String name, RandomVariate delayGenerator, Object... parameters) {
        return waitDelay(name, delayGenerator.generate(), parameters);
    }

    /**
     * Schedules an event using a RandomVariate instance to generate the delay
     *
     * @param name Event name of event
     * @param delayGenerator RandomVariate that is used to generate the time
     * until the event will occur
     * @param priority If two events occur at the same time, the one with the
     * highest number will be executed first.
     * @param parameters optional parameters for event
     * @return The SimEvent that is put on the Event List
     */
    public SimEvent waitDelay(String name, RandomVariate delayGenerator,
            Priority priority, Object... parameters) {
        return waitDelay(name, delayGenerator.generate(), priority, parameters);
    }

    /**
     * Schedules an event.
     *
     * @param event The event to be scheduled
     */
    protected void attemptSchedule(SimEvent event) {
        eventList.scheduleEvent(event);
    }

    /**
     * Cancels the next event for this entity that matches the event name and
     * value of the parameters.
     *
     * @param eventName name of event to cancel
     * @param parameters Parameters of event to cancel
     *
     */
    @Override
    public void interrupt(String eventName, Object... parameters) {
        eventList.interrupt(this, eventName, parameters);
    }

    /**
     * Cancels the next event for this entity that matches the event name.
     *
     * @param eventName Name of event to cancel
     *
     */
    @Override
    public void interrupt(String eventName) {
        eventList.interrupt(this, eventName);
    }

    /**
     * Cancels all events for this entity that match the event name and value of
     * the parameters.
     *
     */
    @Override
    public void interruptAll(String eventName, Object... parameters) {
        eventList.interruptAll(this, eventName, parameters);
    }

    /**
     * Cancels all events for this entity that match the event name.
     */
    @Override
    public void interruptAll(String eventName) {
        eventList.interruptAll(this, eventName);
    }

    /**
     * Cancel all events for this entity.
     */
    @Override
    public void interruptAll() {
        eventList.interruptAll(this);
    }

    /**
     * Sets the name of this entity.
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

// javadoc inherited from Named
    @Override
    public String getName() {
        return name;
    }

    /**
     * Sets the scheduling priority for this entity. If two events are scheduled
     * at the same time and have the same event priority, the one whose owner
     * has the highest priority will be processed first.
     *
     * @param p Given Priority of this BasicSimEntity
     */
    @Override
    public void setPriority(Priority p) {
        priority = p;
    }

    /**
     * The scheduling priority for this entity. If two events are scheduled at
     * the same time and have the same event priority, the on whose owner has
     * the highest priority will be processed first.
     */
    @Override
    public Priority getPriority() {
        return priority;
    }

    /**
     *
     * @param verbose If true causes debug/trace information to be output.
     */
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    /**
     *
     * @return true if debug/trace information is to be output.
     */
    public boolean isVerbose() {
        return this.verbose;
    }

    /**
     *
     * TODO: replace this with Arrays.equals(Object[], Object[]).
     *
     * Determine if the two arrays contain equal Objects. Equality is determined
     * by the equals method of the Objects.
     *
     * @param fromEvent Parameters from event on Event List
     * @param fromInterrupt Parameters from interrupt call
     * @return true if arrays are equal (i.e. each element in one is equal to
     * the corresponding one in the other).
     */
    public static boolean parametersMatch(Object[] fromEvent, Object[] fromInterrupt) {
        boolean match = true;
        if ((fromEvent == null && fromInterrupt != null)
                || (fromEvent == null && fromInterrupt != null)) {
            match = false;
        } else if ((fromEvent == null) && (fromInterrupt == null)) {
            match = true;
        } else if (fromEvent.length != fromInterrupt.length) {
            match = false;
        } else {
            for (int i = 0; i < fromEvent.length; i++) {
                if (!(fromEvent[i].equals(fromInterrupt[i]))) {
                    match = false;
                    break;
                }
            }
        }
        return match;
    }

// Javadoc inherited from SimEventListener    
    @Override
    public abstract void handleSimEvent(SimEvent event);

    /**
     * Process the given SimEvent. Entity's should not process other entities'
     * doRun events. See the discussion in the description of this class for
     * information on implementing this method.
     *
     */
    @Override
    public abstract void processSimEvent(SimEvent event);

// Javadoc inherited from SimEntity
    @Override
    public int getSerial() {
        return serial;
    }

// Javadoc inherited from PropertyChangeSource
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        property.addPropertyChangeListener(listener);
    }

// Javadoc inherited from PropertyChangeSource
    @Override
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        property.addPropertyChangeListener(propertyName, listener);
    }

// Javadoc inherited from PropertyChangeSource
    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        property.removePropertyChangeListener(listener);
    }

// Javadoc inherited from PropertyChangeSource
    @Override
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        property.removePropertyChangeListener(propertyName, listener);
    }

    /**
     * Notify all PropertyChangeListeners of the PropertyChangeEvent.
     *
     * @param e The event with all the information about what property has
     * changed and to what value.
     */
    @Override
    public void firePropertyChange(PropertyChangeEvent e) {
        property.firePropertyChange(e);
    }

    public void setJustDefinedProperties(boolean b) {
        justDefinedProperties = b;
    }

    public boolean isJustDefinedProperties() {
        return justDefinedProperties;
    }

    /**
     * Get the value of the given property.
     *
     * @param name The name of the property to be retrieved
     * @return The value of the property
     * @throws NullPointerException If the property doesn't exist
     */
    @Override
    public Object getProperty(String name) {
        return property.getProperty(name);
    }

    /**
     * Get the value of the given property or return the supplied default.
     *
     * @param name The name of the property to be retrieved.
     * @param defaultValue The default value -- returned if property's value
     * cannot be returned or is null.
     * @return The value of the property.
     * @throws NullPointerException If the property doesn't exist
     */
    @Override
    public Object getProperty(String name, Object defaultValue) {
        return property.getProperty(name, defaultValue);
    }

    /**
     * Set the property to the given value.
     *
     * @param name The name of the property to be set
     * @param value The new value of the property
     * @throws NullPointerException If the property doesn't exist
     */
    @Override
    public void setProperty(String name, Object value) {
        property.setProperty(name, value);
    }

    /**
     * Notify registered PropertChangeListeners that the given Object property
     * has changed.
     *
     * <p>
     * Note: uses <code>PropertyChangeEventX</code> rather than
     * <code>PropertyChangeEvent</code>
     *
     * @see PropertyChangeEventX
     * @param propertyName The property that changed.
     * @param oldValue The value of the property prior to this change.
     * @param newValue The value of the property after this change.
     * @param extraData Extra data to be added to fired PropertyChangeEvent
     */
    public void firePropertyChange(String propertyName, Object oldValue, Object newValue, Object... extraData) {
        property.firePropertyChange(new PropertyChangeEventX(this, propertyName, oldValue, newValue, extraData));
    }

    /**
     * Notify registered PropertChangeListeners that the given Object property
     * has changed. This version does not report the previous value.
     *
     * @param propertyName The property that changed.
     * @param newValue The value of the property after this change.
     */
    public void firePropertyChange(String propertyName, Object newValue) {
        property.firePropertyChange(new PropertyChangeEventX(this, propertyName, null, newValue));
    }

    /**
     * Notify registered PropertyChangeListeners that an element of an indexed
     * property has changed.
     * <p>
     * Note: instantiates <code>IndexedPropertyChangeEventX</code> instead of
     * <code>IndexedPropertyChangeEvent</code>
     *
     * @see IndexedPropertyChangeEventX
     * @param index The element that has changed.
     * @param propertyName The Indexed property containing the changed element.
     * @param oldValue The value of the property prior to this change.
     * @param newValue The value of the property after this change.
     * @param extraData Extra data added to IndexedPropertyChangeEventX
     *
     */
    public void fireIndexedPropertyChange(int index, String propertyName, Object oldValue,
            Object newValue, Object... extraData) {
        property.firePropertyChange(new IndexedPropertyChangeEventX(this, propertyName, oldValue, newValue, index, extraData));
    }

    /**
     * Notify registered PropertyChangeListeners that an element of an indexed
     * property has changed. This version doesn't report the previous value.
     *
     * @param index The element that has changed.
     * @param propertyName The Indexed property containing the changed element.
     * @param newValue The value of the property after this change.
     *
     */
    public void fireIndexedPropertyChange(int index, String propertyName, Object newValue) {
        property.firePropertyChange(new IndexedPropertyChangeEventX(this, propertyName, null, newValue, index));
    }

    /**
     * A default string description of this entity, name (Entity Priority)
     * &lt;list of all properties as key = value pairs&gt;
     *
     * @return String description of object's name and properties
     *
     */
    @Override
    public String toString() {
        if (isJustDefinedProperties()) {
            return getName() + getPropertiesString();
        } else {
            return getName() + property;
        }
    }

    /**
     * Gets an array of all registered PropertyChangeListeners.
     *
     * @return array of PropertyChangeListeners
     */
    @Override
    public PropertyChangeListener[] getPropertyChangeListeners() {
        return property.getPropertyChangeListeners();
    }

    @Override
    public boolean isPersistant() {
        return persistant;
    }

    /**
     *
     * @param persist if true, this instance is persistent (i.e. a ReRunnable)
     */
    @Override
    public void setPersistant(boolean persist) {
        persistant = persist;
    }

    @Override
    public void setEventListID(int id) {
        if (eventList != null) {
            eventList.removeRerun(this);
        }
        eventList = Schedule.getEventList(id);
        if (isPersistant()) {
            eventList.addRerun(this);
        }
    }

    public void setEventList(BasicEventList el) {
        if (eventList != null) {
            eventList.removeRerun(this);
        }
        eventList = el;
        if (isPersistant()) {
            eventList.addRerun(this);
        }
    }

    @Override
    public int getEventListID() {
        return eventList.getID();
    }

    @Override
    public BasicEventList getEventList() {
        return eventList;
    }

    @Override
    public String[] getAddedProperties() {
        return property.getAddedProperties();
    }

    protected String getPropertiesString() {
        return property.paramString();
    }

    /**
     * Clears the added property of the given name.
     *
     * @param propertyName the name of the added property to be cleared.
     */
    public void clearAddedProperty(String propertyName) {
        property.clearAddedProperty(propertyName);
    }

    /**
     * This SimEntity has higher priority if its Priority instance has higher
     * priority or it has a smaller serial.
     *
     * @param other Other SimEntity to compare to
     */
    @Override
    public int compareTo(SimEntity other) {
        int priorityComp = this.getPriority().compareTo(other.getPriority());
        return priorityComp != 0 ? priorityComp
                : this.getSerial() - other.getSerial();
    }

    public boolean isClearAddedPropertiesOnReset() {
        return clearAddedPropertiesOnReset;
    }

    public void setClearAddedPropertiesOnReset(boolean clearAddedPropertiesOnReset) {
        this.clearAddedPropertiesOnReset = clearAddedPropertiesOnReset;
    }

    @Override
    public void interruptAllWithArgs(String eventName, Object parameter) {
        getEventList().interruptAllWithArg(this, eventName, parameter);
    }

    @Override
    public void interruptAllWithArgs(Object parameter) {
        getEventList().interruptAllWithArg(parameter);
    }
}
