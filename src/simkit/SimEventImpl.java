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

import java.util.*;
import java.text.*;
import java.io.Serializable;
/**
* Base class for simulation events.
*
* 
* @author K. A. Stork
* @author Arnold Buss 
* @since Simkit 1.0
*
**/

public class SimEventImpl implements SimEvent, Serializable {

// static variables
   private static NumberFormat tsf;

// static initializer
   static {
     tsf =  new DecimalFormat("0.000"); 
   }

// instance variables
    private SimEntity source;
    private Class[] signature;
    private Object[] parameters;
    private double eventPriority;
    private SimEventState waitState;
    private double scheduledTime;
    private double creationTime;
    private String eventName;
    private String methodName;
    private String fullMethodName;
    private int serial;
    private int id;
   
// constructors

// New master constructor

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
    public void setSource(SimEntity s) {source = s;}
    public void setEventName(String name) {
        eventName = name;
        methodName = adjustEventName(eventName);
    }
    public void setScheduledTime(double time) { scheduledTime = Schedule.getSimTime() + time; }
    public void setEventPriority(double p) { eventPriority = p; }
    public void setWaitState(SimEventState state) {waitState = state;}
    public void setSerial(int serial) {this.serial = serial;}
    public void setID(int id) {this.id = id;}
    public void setParameters(Object[] params) { parameters = (params != null) ? params : new Object[]{};}
    public void setPriority(double priority) { eventPriority = priority; }
    public void setCreationTime(double time) {creationTime = time;}

// getters
    public SimEntity getSource() {return source;}
    public double getScheduledTime() { return scheduledTime; }
    public double getCreationTime() { return creationTime; }
    public double getEventPriority() { return eventPriority; }
    public SimEventState getWaitState() {return waitState; }
    public String getEventName() { return getName(); }
    public String getName() { return eventName; }
    public String getMethodName() { return methodName; }
    public String getFullMethodName() {
        if (fullMethodName == null) {
            fullMethodName = this.getMethodName() + this.getSignatureAsString();
        }
        return fullMethodName;
    }

    public double getOwnerPriority() { return ((SimEntity)getSource()).getPriority();  }
    public int getSerial() {return serial;}
    public int getID() {return id;}

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
   
    public Object[] getParameters() { return parameters; }

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

    public void interrupt() {
        setWaitState(SimEventState.INTERRUPTED);
    }
   

    public String paramString() {
        return eventName + getParametersAsString();
    }
   
    public String countString() {
        return "(" + getSource().toString() + ") " + paramString() + " [" + serial + "]" ;
    }
   
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(tsf.format( scheduledTime ));
        buf.append('\t');
        buf.append(paramString());
        buf.append('\t');
        buf.append(getWaitStateStr());
        return buf.toString();
    }
      
    private String getWaitStateStr() { return waitState == SimEventState.WAITING ? "" : waitState.toString(); }

    public boolean eventEquals(Object obj) {
        if (!(obj instanceof SimEvent))  return false;
        return (((SimEvent) obj).getFullMethodName().equals(getFullMethodName()))
             && interruptParametersMatch(((SimEvent) obj).getParameters());
    }  

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

    public boolean isPending() { return waitState == SimEventState.WAITING; }

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
    
    public static String adjustEventName(String name) {
        if (name != null) {
            return (name.startsWith(SimEntity.EVENT_METHOD_PREFIX) ) ?
                name : SimEntity.EVENT_METHOD_PREFIX + name;
        }
        else {
            return null;
        }
    }

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

