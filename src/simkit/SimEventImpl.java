package simkit;
/////////////////////////// Copyright Notice //////////////////////////
//                                                                   //
// This simkit package or sub-package and this file is Copyright (c) //
// 1997, 1998, 1999 by Kirk A. Stork and Arnold H. Buss.             //
//                                                                   //
// Please forward any changes, comments or suggestions to:           //
//   abuss@nps.navy.mil                                              //
//                                                                   //
///////////////////////////////////////////////////////////////////////

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
/**
* Base class for simulation events. It is more efficient to use the
* SimEventFactory than to construct SimEvents directly.
*
* 
* @author K. A. Stork
* @author Arnold Buss 
* @since Simkit 1.0
* @see SimEventFactory
* @version $Id$
*
**/
public class SimEventImpl implements SimEvent, Serializable {

// static variables
/**
* Holds the format for output of numbers.
* The format is 0.000.
**/
   private static NumberFormat tsf;

// static initializer
   static {
     tsf =  new DecimalFormat("0.000"); 
   }

// instance variables
/**
* The SimEntity that will process this SimEvent.
**/
    private SimEntity source;

/**
* Caches the signature of the event method.
**/
    private Class[] signature;

/**
* Holds the parameters to be passed to the event
* method when called.
**/
    private Object[] parameters;

/**
* The event priority is used to determine which event
* is processed first if the scheduled times are the same.
* A smaller number is a higher priority and will be processed
* first.
**/
    private double eventPriority;

/**
* The current state of this event. An
* event is either WAITING or INTERRUPTED.
* @see SimEventState
**/
    private SimEventState waitState;

/**
* The simulation time at which this event is 
* scheduled to occur.
**/
    private double scheduledTime;

/**
* The simulation time at which this event was 
* created.
**/
    private double creationTime;

/**
* The name of the event associated with this
* SimEvent. The event name is the method
* name with or without the "do"
**/
    private String eventName;

/**
* The name of the method that will be called
* when this event is processed. It must start with "do"
**/
    private String methodName;

/**
* The methodName plus the signature.
**/
    private String fullMethodName;

/**
* A unique identifier for this SimEntity. 
**/
    private int serial;

/**
* A unique identifier for this SimEntity.
**/
    private int id;
   
// constructors

// New master constructor

/**
* Construct a new SimEvent. After construction, the parameters must
* be set by calls to the appropriate setters. The SimEvent
* returned by this constructor is not in a usable state.
**/
    public SimEventImpl() {
        setSource(null);
        setWaitState(SimEventState.UNUSED);
    }

// Master constructor  (original)
/*
    protected SimEventImpl (SimEntity source,
                       String theMethodName,
                       Object[] params,
                       double schTime,
                       double priority,
                       String name) {
        super();
        setSource(source);
        setWaitState(SimEventState.UNUSED);
        scheduledTime = schTime;
        creationTime = Schedule.simTime();
        parameters = (params != null) ? (Object[])params.clone() : new Object[]{};
        setEventPriority(priority);
        if (theMethodName.startsWith("do") ) {
            methodName = theMethodName;
        } //if 
        else {
            methodName = "do" + theMethodName;
        } // if
        setName(name);
        StringBuffer fmn = new StringBuffer(methodName);
        fmn.append('(');

        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i] != null) {
                fmn.append(parameters[i].getClass().getName());
            } // if
            else {
                fmn.append(java.lang.Object.class.getName());
            } // else
            if (i < parameters.length - 1) {fmn.append(',');}
        }  // for
        fmn.append(')');
        fullMethodName = fmn.toString();
        id = ++nextID;
    }       // SimEventImpl constructor
*/

// setters
/**
* The SimEntity that will process this SimEvent.
**/
    public void setSource(SimEntity s) {source = s;}
/**
* Sets both the eventName and the methodName. 
* The event name is the method
* name with or without the "do"
* If the event name is supplied without the "do", this
* method will prepend the "do" when setting the methodName.
**/
    public void setEventName(String name) {
        eventName = name;
        methodName = adjustEventName(eventName);
    }

/**
* The simulation time at which this event is 
* scheduled to occur.
**/
    public void setScheduledTime(double time) { scheduledTime = getSource().getEventList().getSimTime() + time; }

/**
* The event priority is used to determine which event
* is processed first if the scheduled times are the same.
* A smaller number is a higher priority and will be processed
* first.
**/
    public void setEventPriority(double eventPriority) { this.eventPriority = eventPriority; }

/**
* The current state of this event. An
* event is either WAITING or INTERRUPTED.
* @see SimEventState
**/
    public void setWaitState(SimEventState state) {waitState = state;}

/**
* A unique identifier for this SimEntity.
**/
    public void setSerial(int serial) {this.serial = serial;}

/**
* A unique identifier for this SimEntity.
**/
    public void setID(int id) {this.id = id;}

/**
* Sets the parameters to be passed to the event
* method when called.
**/
    public void setParameters(Object[] params) { parameters = (params != null) ? params : new Object[]{};}

/**
* The event priority is used to determine which event
* is processed first if the scheduled times are the same.
* A smaller number is a higher priority and will be processed
* first.
**/
    public void setPriority(double priority) { eventPriority = priority; }

/**
* The simulation time at which this event was 
* created.
**/
    public void setCreationTime(double time) {creationTime = time;}

// getters
/**
* The SimEntity that will process this SimEvent.
**/
    public SimEntity getSource() {return source;}

/**
* The simulation time at which this event is 
* scheduled to occur.
**/
    public double getScheduledTime() { return scheduledTime; }

/**
* The simulation time at which this event was 
* created.
**/
    public double getCreationTime() { return creationTime; }

/**
* The event priority is used to determine which event
* is processed first if the scheduled times are the same.
* A smaller number is a higher priority and will be processed
* first.
**/
    public double getEventPriority() { return eventPriority; }

/**
* The current state of this event. An
* event is either WAITING or INTERRUPTED.
* @see SimEventState
**/
    public SimEventState getWaitState() {return waitState; }

/**
* The name of the event associated with this
* SimEvent. The event name is the method
* name with or without the "do"
**/
    public String getEventName() { return getName(); }

/**
* The name of the event associated with this
* SimEvent. The event name is the method
* name with or without the "do"
**/
    public String getName() { return eventName; }

/**
* The name of the method that will be called
* when this event is processed. It must start with "do"
**/
    public String getMethodName() { return methodName; }

/**
* The methodName plus the signature.
**/
    public String getFullMethodName() {
        if (fullMethodName == null) {
            fullMethodName = this.getMethodName() + this.getSignatureAsString();
        }
        return fullMethodName;
    }

/**
* Gets the priority of the SimEntity who owns this event.
**/
    public double getOwnerPriority() { return ((SimEntity)getSource()).getPriority();  }

/**
* A unique identifier for this SimEntity.
**/
    public int getSerial() {return serial;}

/**
* A unique identifier for this SimEntity.
**/
    public int getID() {return id;}

/**
* The signature for the method associated with this event.
* The signature is determined from the parameters.
* @return An array of Class objects that make up the event method's signature.
**/
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
                signature = new Class[] {};
	        } // else
        }
        return signature;
    }
   
/**
* The parameters to be passed to the event
* method when called.
**/
    public Object[] getParameters() { return parameters; }

/**
* Converts the array of parameters to a String for display.
**/
    public String getParametersAsString() {
        StringBuffer s = new StringBuffer();
        if (parameters != null) {
            if (parameters.length > 0) {
                s.append(' '); s.append('\t'); s.append('{');
                for (int i = 0; i < parameters.length; i++) {
                    s.append(parameters[i]);
                    if (i < parameters.length - 1) {s.append(',');s.append(' ');}
                } // for
	            s.append('}');
            } // if
        } // if
        return s.toString();
    }

/**
* Interrupts (cancels) this event. Sets the waitState flag to INTERRUPTED.
* This causes Schedule to ignore this event instead of processing it.
**/
    public void interrupt() {
        setWaitState(SimEventState.INTERRUPTED);
    }
   
/**
* Returns the event name plus the parameters as a String.
**/
    public String paramString() {
        return eventName + getParametersAsString();
    }
   
/**
* Returns a String containing information about the source of this SimEvent,
* the event name and parameters, and the serial of this event.
* @see #paramString()
**/
    public String countString() {
        return "(" + getSource().toString() + ") " + paramString() + " [" + serial + "]" ;
    }
   
/**
* Returns the scheduled time, the event name and parameters, and the waitState of
* this SimEvent.
**/
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(tsf.format( scheduledTime ));
        buf.append('\t');
        buf.append(paramString());
        buf.append('\t');
        buf.append(getWaitStateStr());
        return buf.toString();
    }
      
/**
* If the state is WAITING, returns an empty String, otherwise returns the name of the waitState.
**/
    private String getWaitStateStr() { return waitState == SimEventState.WAITING ? "" : waitState.toString(); }

/**
* Determine if this SimEvent refers to the same event as the given Object. Events match
* if the event method, signature, and the value of the parameters are the same.
**/
    public boolean eventEquals(Object obj) {
        if (!(obj instanceof SimEvent))  return false;
        return (((SimEvent) obj).getFullMethodName().equals(getFullMethodName()))
             && interruptParametersMatch(((SimEvent) obj).getParameters());
    }  

/**
* Determines if the given array of parameters match the parameters
* for this event.
* @return true if the parameters match, false otherwise.
**/
    public boolean interruptParametersMatch(Object[] params) {
        boolean isMatch = true;
        if (params.length != parameters.length) {
            isMatch = false;
        }
        else {
            for (int i = 0; i < parameters.length; i++) {
                isMatch &= parameters[i].equals(params[i]);
            }
        }
        return isMatch;
    }

/**
* Returns true if this event is pending and has not been interrupted.
* @return true if the wait state is WAITING.
**/
    public boolean isPending() { return waitState == SimEventState.WAITING; }

/**
* Resets the state of this SimEvent so that it can be reused.
**/
    public void reset() {
        setSource(null);
        setParameters(null);
        fullMethodName = null;
        signature = null;
        setWaitState(SimEventState.UNUSED);
        setEventName(null);
        setEventPriority(SimEvent.DEFAULT_PRIORITY);
        setSerial(Integer.MIN_VALUE);
        
    }
    
/**
* Converts an event name to the correct method name. Prepends "do"
* to the event name if it doesn't already start with "do".
*/
    public static String adjustEventName(String name) {
        if (name != null) {
            return (name.startsWith(SimEntity.EVENT_METHOD_PREFIX) ) ?
                name : SimEntity.EVENT_METHOD_PREFIX + name;
        }
        else {
            return null;
        }
    }

/**
* Returns a String containing the names of the classes that
* make up the signature of the event method.
**/
    public String getSignatureAsString() {
        StringBuffer buf = new StringBuffer();
        buf.append('(');
        if (parameters != null) {
            for (int i = 0; i < parameters.length; i++) {
                if (parameters[i] != null) {
                    buf.append(parameters[i].getClass().getName());
                }
                else {
                    buf.append("null");
                }
                if (i < parameters.length - 1) {buf.append(',');}
            }
        }
        buf.append(')');
        return buf.toString();
    }

} // class SimEvent

