package simkit;

/**
 * Class for simulation events. We will now let the garbage collector do its job
 * - i.e. no more object pooling with the SimEventFactory.
 *
 * @author K. A. Stork
 * @author Arnold Buss
 * @since Simkit 1.0
 *
 *
 */
public class SimEvent implements Comparable<SimEvent> {

    // static variables
    public static final double DEFAULT_PRIORITY = 0.0;

    // static initializer
    static {
        resetID();
    }

    protected static int nextID;

    // instance variables
    /**
     * The SimEntity that will process this SimEvent.
     *
     */
    private SimEventScheduler source;

    /**
     * Caches the signature of the event method.
     *
     */
    private Class<?>[] signature;

    /**
     * Holds the parameters to be passed to the event method when called.
     *
     */
    private Object[] parameters;

    /**
     * The event priority is used to determine which event is processed first if
     * the scheduled times are the same. A smaller number is a higher priority
     * and will be processed first.
     *
     */
    protected Priority priority;

    /**
     * The current state of this event. An event is either WAITING or
     * INTERRUPTED.
     *
     * @see SimEventState
     *
     */
    private SimEventState waitState;

    /**
     * The simulation time at which this event is scheduled to occur.
     *
     */
    private double scheduledTime;

    /**
     * The simulation time at which this event was created.
     *
     */
    private double creationTime;

    /**
     * The name of the event associated with this SimEvent. The event name is
     * the method name with or without the "do"
     *
     */
    private String eventName;

    /**
     * The name of the method that will be called when this event is processed.
     * It must start with "do"
     *
     */
    private String methodName;

    /**
     * The methodName plus the signature.
     *
     */
    private String fullMethodName;

    /**
     * A unique identifier for this SimEntity - the # of times an event of this
     * name has occured.
     *
     */
    private int serial;

    /**
     * A unique identifier for this SimEntity.
     *
     */
    private int id;

    /**
     * A hash value that depends on the owner, event name, and parameters.
     *
     */
    protected Integer eventHash = null;

    // constructors
    // New master constructor
    /**
     * Construct a new SimEvent.
     *
     * @param source SimEntity that scheduled this event
     * @param name Name of scheduled event
     * @param params given parameters
     * @param delay given delay
     * @param priority give Priority
     *
     */
    public SimEvent(SimEventScheduler source,
            String name,
            Object[] params,
            double delay,
            Priority priority) {
        super();
        this.source = source;
        this.eventName = name;
        setScheduledTime(delay);
        creationTime = Schedule.getSimTime();
        parameters = (params != null) ? (Object[]) params.clone() : new Object[]{};
        if (priority == null) {
            throw new NullPointerException("Null Priority: "
                    + source + " " + name);
        }
        this.priority = priority;
        methodName = adjustEventName(name);
        StringBuilder fmn = new StringBuilder(methodName);
        fmn.append('(');

        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i] != null) {
                fmn.append(parameters[i].getClass().getName());
            } // if
            else {
                fmn.append(java.lang.Object.class.getName());
            } // else
            if (i < parameters.length - 1) {
                fmn.append(',');
            }
        }  // for
        fmn.append(')');
        fullMethodName = fmn.toString();
        setWaitState(SimEventState.WAITING);
        id = ++nextID;
    }       // SimEvent constructor

    public SimEvent(SimEventScheduler source, String name, double delay) {
        this(source, name, null, delay, Priority.DEFAULT);
    }

    public SimEvent(SimEventScheduler source, String name, Object[] params, double delay) {
        this(source, name, params, delay, Priority.DEFAULT);
    }

    // setters
    public void setWaitState(SimEventState state) {
        waitState = state;
    }

    /**
     * The simulation time at which this event is scheduled to occur is the
     * current simTime + delay.
     *
     * @param delay Amount of delay
     */
    protected void setScheduledTime(double delay) {
        scheduledTime = getSource().getEventList().getSimTime() + delay;
    }

    /**
     *
     * @param serial Given serial number for this SimEvent
     */
    public void setSerial(int serial) {
        this.serial = serial;
    }

    // getters
    /**
     * The SimEventScheduler that will process this SimEvent.
     *
     * @return scheduling instance
     */
    public SimEventScheduler getSource() {
        return source;
    }

    /**
     *
     * @return The simulation time at which this event is scheduled to occur.
     */
    public double getScheduledTime() {
        return scheduledTime;
    }

    /**
     *
     * @return The simulation time at which this event was created.
     */
    public double getCreationTime() {
        return creationTime;
    }

    /**
     * The event priority is used to determine which event is processed first if
     * the scheduled times are the same. A smaller number is a higher priority
     * and will be processed first.
     *
     * @return The event priority is used to determine which event is processed
     * first if the scheduled times are the same.
     */
    public double getEventPriority() {
        return priority.getPriority();
    }

    /**
     * An event is either WAITING or INTERRUPTED.
     *
     * @return The current state of this event.
     * @see SimEventState
     *
     */
    public SimEventState getWaitState() {
        return waitState;
    }

    /**
     * The event name is the method name with or without the "do"
     *
     * @return The name of the event associated with this SimEvent.
     */
    public String getEventName() {
        return getName();
    }

    /**
     * The event name is the method name with or without the "do"
     *
     * @return The name of the event associated with this SimEvent.
     */
    public String getName() {
        return eventName;
    }

    /**
     *
     * It must start with "do"
     *
     * @return The name of the method that will be called when this event is
     * processed.
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     *
     * @return The method Name plus the signature.
     */
    public String getFullMethodName() {
        if (fullMethodName == null) {
            fullMethodName = this.getMethodName() + this.getSignatureAsString();
        }
        return fullMethodName;
    }

    /**
     *
     * @return the priority of the SimEventScheduler who owns this event.
     */
    public Priority getOwnerPriority() {
        return ((SimEventScheduler) getSource()).getPriority();
    }

    /**
     *
     * @return A unique identifier for this SimEntity.
     */
    public int getSerial() {
        return serial;
    }

    /**
     *
     * @return A unique identifier for this SimEntity.
     */
    public int getID() {
        return id;
    }

    /**
     * The signature for the method associated with this event. The signature is
     * determined from the parameters.
     *
     * @return An array of Class objects that make up the event method's
     * signature.
     *
     */
    public Class[] getSignature() {
        if (signature == null) {

            if (parameters.length > 0) {
                signature = new Class[parameters.length];
                for (int i = 0; i < signature.length; i++) {
                    if (parameters[i] != null) {
                        signature[i] = parameters[i].getClass();
                    } // if
                    else {
                        signature[i] = Object.class;
                        System.out.println("Signature for " + i + " is " + signature[i]);
                    } // else
                } // for
            } // if
            else {
                signature = new Class[]{};
            } // else
        }
        return signature;
    }

    /**
     *
     * @return The parameters to be passed to the event method when called.
     */
    public Object[] getParameters() {
        return parameters;
    }

    /**
     *
     * @return the array of parameters to a String for display.
     */
    public String getParametersAsString() {
        StringBuilder s = new StringBuilder();
        if (parameters != null) {
            if (parameters.length > 0) {
                s.append(' ');
                s.append('\t');
                s.append('{');
                for (int i = 0; i < parameters.length; i++) {
                    s.append(parameters[i]);
                    if (i < parameters.length - 1) {
                        s.append(',');
                        s.append(' ');
                    }
                } // for
                s.append('}');
            } // if
        } // if
        return s.toString();
    }

    /**
     * Interrupts (cancels) this event. Sets the waitState flag to INTERRUPTED.
     * This causes Schedule to ignore this event instead of processing it.
     *
     */
    public void interrupt() {
        setWaitState(SimEventState.INTERRUPTED);
    }

    /**
     *
     * @return the event name plus the parameters as a String.
     */
    public String paramString() {
        return eventName + getParametersAsString();
    }

    /**
     *
     * @return a String containing information about the source of this
     * SimEvent, the event name and parameters, and the serial of this event.
     * @see #paramString()
     *
     */
    public String countString() {
        return "(" + getSource().toString() + ") " + paramString() + " [" + serial + "]";
    }

    /**
     * Returns the scheduled time, the event name and parameters, and the
     * waitState of this SimEvent.
     */
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
//        buf.append(tsf.format(scheduledTime));
        buf.append(String.format("%,.4f", scheduledTime));
        buf.append('\t');
        buf.append(paramString());
        buf.append('\t');
        buf.append(getWaitStateStr());
        return buf.toString();
    }

    /**
     * If the state is WAITING, returns an empty String, otherwise returns the
     * name of the waitState. TODO: find out why
     *
     * @return the name of the waitState or "" if state is WAITING.
     */
    private String getWaitStateStr() {
        return waitState == SimEventState.WAITING ? "" : waitState.toString();
    }

    /**
     * Determine if this SimEvent refers to the same event as the given Object.
     * Events match if the event method, signature, and the value of the
     * parameters are the same.
     *
     * @param obj Given Object
     * @return true if this SimEvent refers to the same event as the given
     * Object.
     */
    public boolean eventEquals(Object obj) {
        if (!(obj instanceof SimEvent)) {
            return false;
        }
        return (((SimEvent) obj).getFullMethodName().equals(getFullMethodName()))
                && interruptParametersMatch(((SimEvent) obj).getParameters());
    }

    /**
     * Determines if the given array of parameters match the parameters for this
     * event.
     *
     * @param params given array of parameters
     * @return true if the parameters match, false otherwise.
     *
     */
    public boolean interruptParametersMatch(Object[] params) {
        if (params.length != parameters.length) {
            return false;
        }
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i] == null ^ params[i] == null) {//xor
                return false;
            }
            if ((parameters[i] == null && params[i] == null)
                    || (parameters[i].equals(params[i]))) {
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if this event is pending and has not been interrupted.
     *
     * @return true if the wait state is WAITING.
     *
     */
    public boolean isPending() {
        return waitState == SimEventState.WAITING;
    }

    /**
     * Converts an event name to the correct method name. Prepends "do" to the
     * event name if it doesn't already start with "do".
     *
     * @param name Given event name
     * @return Method name for given event name
     */
    public static String adjustEventName(String name) {
        if (name != null) {
            return (name.startsWith(SimEventScheduler.EVENT_METHOD_PREFIX))
                    ? name : SimEventScheduler.EVENT_METHOD_PREFIX + name;
        } else {
            return null;
        }
    }

    /**
     *
     * @return a String containing the names of the classes that make up the
     * signature of the event method.
     */
    public String getSignatureAsString() {
        StringBuilder buf = new StringBuilder();
        buf.append('(');
        if (parameters != null) {
            for (int i = 0; i < parameters.length; i++) {
                if (parameters[i] != null) {
                    buf.append(parameters[i].getClass().getName());
                } else {
                    buf.append("null");
                }
                if (i < parameters.length - 1) {
                    buf.append(',');
                }
            }
        }
        buf.append(')');
        return buf.toString();
    }

    public static void resetID() {
        nextID = 0;
    }

    /**
     *
     * @return a hash based on owner, event name, and the parameters.
     */
    public int getEventHash() {
        if (eventHash == null) {
            eventHash = calculateEventHash(getSource(), getEventName(), parameters);
        }
        return eventHash;
    }

    /**
     * Returns a hash based on owner, event name, and the parameters.
     *
     * @param source Given source
     * @param eventName Given event name
     * @param params Given parameters
     * @return a hash based on owner, event name, and the parameters.
     */
    public static int calculateEventHash(SimEventScheduler source,
            String eventName,
            Object[] params) {
        int temp = source.hashCode();
        temp = 31 * temp + eventName.hashCode();
        for (Object param : params) {
            if (param != null) {
                temp = 31 * temp + param.hashCode();
            }
        }
        return temp;
    }

    public Priority getPriority() {
        return priority;
    }

    /**
     * Compares this SimEvent to another SimEvent. The order of importance
     * regarding scheduling is as follows:
     *
     * <br>
     *
     * 1. Scheduled time of the events (Earlier time is a higher priority.) <br>
     * 2. Priority of the SimEvents. (A larger number is a higher priority.)
     * <BR>
     * 3. Priorities of the SimEntities that own the SimEvents (A larger number
     * is a higher priority.) <br>
     * 4. The SimTime the SimEvents were instantiated. (An earlier creation time
     * is a higher priority.) <br>
     * 5. The ID number of the event. (A lower ID number is a higher priority.)
     * <BR>
     *
     * @param other other SimEvent
     */
    @Override
    public int compareTo(SimEvent other) {
//Copied from SimEventComp 1.6
        if (this.equals(other) || other.equals(this)) {
            return 0;
        }

        if (this.getScheduledTime() > other.getScheduledTime()) {
            return 1;
        }
        if (this.getScheduledTime() < other.getScheduledTime()) {
            return -1;
        }
        if (this.getEventPriority() < other.getEventPriority()) {
            return 1;
        }
        if (this.getEventPriority() > other.getEventPriority()) {
            return -1;
        }
        if (this.getOwnerPriority().getPriority() > other.getOwnerPriority().getPriority()) {
            return -1;
        }
        if (this.getOwnerPriority().getPriority() < other.getOwnerPriority().getPriority()) {
            return 1;
        }
        if (this.getCreationTime() > other.getCreationTime()) {
            return 1;
        }
        if (this.getCreationTime() < other.getCreationTime()) {
            return -1;
        }
        if (this.getID() > other.getID()) {
            return 1;
        }
        if (this.getID() < other.getID()) {
            return -1;
        }
        return 0;
    }

} // class SimEvent

