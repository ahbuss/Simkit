package simkit;

import simkit.util.*;
import simkit.util.IndexedPropertyChangeEvent;
import java.lang.reflect.*;
import java.util.*;

/**
 *  Default implementation of a SimEntity using reflection.  Consequently, the
 *  callback methods don't have to be implemented by the modeler using
 *  this class.
 *
 *  @author Arnold Buss
 *  @author K. A. Stork
 *
 **/

public abstract class SimEntityBase extends BasicSimEntity {
    
    private static Hashtable2 allDoMethods;
    private static Hashtable2 allNamesAndSignatures;
    private static Map allImpossibleEvents;
    
    static {
        allDoMethods = new Hashtable2();
        allNamesAndSignatures = new Hashtable2();
        allImpossibleEvents = new HashMap();
    }
    
    private  String stopEventName;
    private static boolean debug;
    
    /**
     * Base constructor.
     **/
    public SimEntityBase(String name, double priority) {
        super(name, priority);
        
        Map doMethods = (Map) allDoMethods.get(this.getClass());
        if (doMethods == null) {
            doMethods = new HashMap();
            Method[] methods = this.getClass().getMethods();
            for (int i = 0; i < methods.length; i++) {
                if (methods[i].getName().startsWith(EVENT_METHOD_PREFIX)) {
                    doMethods.put(getFullMethodName(methods[i]), methods[i]);
                    String methodName = methods[i].getName();
                } // if
            }  // for
            doMethods = new HashMap(doMethods);
            allDoMethods.put(this.getClass(), doMethods);
        } // if
        
        Map namesAndSignatures = (Map) allNamesAndSignatures.get(this.getClass());
        if (namesAndSignatures == null) {
            namesAndSignatures = new HashMap();
            for (Iterator i = doMethods.keySet().iterator(); i.hasNext(); ) {
                Method nextDoMethod = (Method) doMethods.get(i.next());
                List v = null;
                if (namesAndSignatures.containsKey(nextDoMethod.getName())) {
                    v = (List)namesAndSignatures.get(nextDoMethod.getName());
                }
                else {
                    v = new ArrayList();
                    namesAndSignatures.put(nextDoMethod.getName(), v);
                }
                v.add(nextDoMethod.getParameterTypes());
            }
            namesAndSignatures = new HashMap(namesAndSignatures);
            allNamesAndSignatures.put(this.getClass(), namesAndSignatures);
        }
    }
    
    /**
     * Convenience constructors.
     **/
    public SimEntityBase() {
        this(DEFAULT_ENTITY_NAME, DEFAULT_PRIORITY);
        setName(getClass().getName() + '.' + getSerial());
    }
    
    public SimEntityBase(String name) {
        this(name, DEFAULT_PRIORITY);
    }
    
    public SimEntityBase(double priority) {
        this(DEFAULT_ENTITY_NAME, priority);
        setName(getClass().getName() + '.' + getSerial());
    }
    
    // implements SimEventListener
    
    public synchronized void handleSimEvent(SimEvent event) {
        processSimEvent(event);
    }
    
    public synchronized void processSimEvent(SimEvent event) {
        Method m = null;
        String methodName =  event.getMethodName();
        // Do not process other SimEntityBase's "doRun()" methods
        if ( !event.getSource().equals(this) && event.getFullMethodName().equals("doRun()")) {
            return;
        } // if
        // If no method of that name, then there is no hope.
        Map namesAndSignatures = (Map) allNamesAndSignatures.get(this.getClass());
        if (!namesAndSignatures.containsKey(methodName)) {
            if (debug) {
                System.out.println("No method of name " + methodName + " -- giving up...");
            } // if
            return;
        } // if
        if (isVerbose()) {
            System.out.println("Event processed by " + this + ": " + event);
        } // if
        
        try {
            Map doMethods = (Map) allDoMethods.get(this.getClass());
            // This method has either happened before or matches one in method exactly
            if (isDebug()) {
                System.out.println("doMethods hashcode = " + doMethods.hashCode());
                System.out.println("namesAndSignatures hashcode = " + namesAndSignatures.hashCode());
                System.out.println("doMethods: " + doMethods);
            }
            if (doMethods.containsKey(event.getFullMethodName())) {
                m = (Method) doMethods.get(event.getFullMethodName());
                m.invoke(this, event.getParameters());
                //                updateEventCounts(event);
            } // if
            else {
                if (isVerbose()) {
                    System.out.println(
                    "Master lookup failed, trying namesAndSignatures..." +
                    " Method Name = " + event.getFullMethodName());
                }
                // Now, we are here only because there is some chance that there will be a match.
                // First
                Object[] params = event.getParameters();
                for (Iterator iter = ( (List) namesAndSignatures.get(methodName)).iterator(); iter.hasNext(); ) {
                    if (isDebug()) {
                        System.out.println("namesAndSignatures: " + namesAndSignatures.hashCode());
                    }
                    Class[] signature = (Class[]) iter.next();
                    if (debug) {
                        System.out.print("  Signature: (");
                        for (int k = 0; k < signature.length; k++) {
                            System.out.print(signature[k]);
                            if (k < signature.length - 1) {System.out.print(", ");}
                        } // for
                        System.out.println(")");
                    }  // if
                    if (signature.length == params.length) {
                        boolean match = true;
                        if (debug) {
                            System.out.println("There are " + signature.length + " arguments to check...");
                        } // if
                        for (int i = 0; i < signature.length; i++) {
                            if (debug) {
                                System.out.println("\tChecking: " + signature[i]);
                            }  // if
                            if (signature[i].isPrimitive()) {
                                if (signature[i].equals(Float.TYPE)) {
                                    match &= params[i].getClass().equals(java.lang.Float.class);
                                }  // if
                                if (signature[i].equals(Integer.TYPE)) {
                                    match &= params[i].getClass().equals(java.lang.Integer.class);
                                }  // if
                                if (signature[i].equals(Double.TYPE)) {
                                    match &= params[i].getClass().equals(java.lang.Double.class);
                                }  // if
                                if (signature[i].equals(Long.TYPE)) {
                                    match &= params[i].getClass().equals(java.lang.Long.class);
                                }  // if
                                if (signature[i].equals(Boolean.TYPE)) {
                                    match &= params[i].getClass().equals(java.lang.Boolean.class);
                                }  // if
                                if (signature[i].equals(Byte.TYPE)) {
                                    match &= params[i].getClass().equals(java.lang.Byte.class);
                                }  // if
                                if (signature[i].equals(Short.TYPE)) {
                                    match &= params[i].getClass().equals(java.lang.Short.class);
                                }  // if
                                if (signature[i].equals(Character.TYPE)) {
                                    match &= params[i].getClass().equals(java.lang.Character.class);
                                }  // if
                            }  // if
                            else {
                                match = match && signature[i].isAssignableFrom(params[i].getClass());
                            }  // else
                            if (isVerbose()) {
                                System.out.println(signature[i].getName() + " ?=? " +
                                params[i].getClass().getName());
                            } // if
                        } // for
                        if (match) {
                            try {
                                if (debug) {
                                    System.out.println("Match found: " + event.getFullMethodName());
                                }
                                m = getClass().getMethod(methodName, signature);
                                m.invoke(this, params);
                                //                                updateEventCounts(event);
                                doMethods.put(event.getFullMethodName(), m);
                            }
                            catch (NoSuchMethodException f) {f.printStackTrace(System.err);}
                        }  //if
                    }
                }
            }
        }
        catch (NullPointerException e) {
            System.out.println("Attempted method: " + event.getFullMethodName());
            e.printStackTrace();
        }
        catch(IllegalAccessException e) {
            System.out.println("Attempted method: " + m );
            System.out.println("  [key = " + event.getFullMethodName() +"]");
            System.out.println("  [name = " + event.getMethodName() + "]");
            System.out.print  ("  [params = (");
            for (int i = 0; i < event.getParameters().length; i++) {
                System.out.print(event.getParameters()[i].getClass().getName());
                if (i < event.getParameters().length - 1) {System.out.print(",");}
            }
            System.out.println(") ]");
            System.out.println("This object: " +
            Integer.toHexString(this.hashCode()) );
            System.out.print("This class: " );
            Class c = this.getClass();
            while(!c.equals(SimEntityBase.class)) {
                System.out.print(c);
                c = c.getSuperclass();
            }
            System.out.println(NL + "Method's object: " +
            Integer.toHexString(event.getSource().hashCode()) );
            System.out.println("Method's class: " + m.getDeclaringClass());
            e.printStackTrace();
        }  //shouldn't happen
        catch(IllegalArgumentException e) {e.printStackTrace();} //shouldn't happen
        catch(InvocationTargetException e) {
            e.getTargetException().printStackTrace();
        }
        finally {
        }
    }
    
    /**
     * <P>This method is added by TRAC-WSMR, Authot Lt Col Olson, USMC.
     * <code>dumpDoMethodsStr</code> returns a String containing the same information as
     * <code>dumpDoMethods</code>.  This method allows a developer to place the information
     * in a graphical user interface, textbox, or similar output device.
     *
     * <P> Method rewritten by A. Buss to use newer Iterator (vice Enumeration) and
     * to use <CODE>StringBuffer</CODE> for speed.  The underscores are also made to
     * exactly match the length of the heading.
     *
     * @return String representation of this entity's "doMethods"
     **/
    public String dumpDoMethodsStr() {
        String name = getName();
        StringBuffer buf = new StringBuffer();
        buf.append("Event Methods for ");
        buf.append(name);
        buf.append(NL);
        for (int i = 0; i < 18 + name.length(); i++) {
            buf.append('=');
        }
        buf.append(NL);
        Map doMethods = (Map) allDoMethods.get(this.getClass());
        for (Iterator i = doMethods.keySet().iterator(); i.hasNext(); ) {
            buf.append(i.next());
            buf.append(NL);
        }
        return buf.toString();
    }
    
    /**
     *  Prints out the "do" methods for this SimEntity
     **/
    public void dumpDoMethods() {
        Schedule.getOutputStream().println(this.dumpDoMethodsStr());
    }
    
    /**
     * <P> This method is added by TRAC-WSMR, Authot Lt Col Olson, USMC.
     * <code>dumpNamesAndSignaturesStr()</code> returns a String containing the same information as
     * <code>dumpNamesAndSignatures()</code>.  This method allows a developer to place the information
     * in a graphical user interface, textbox, or similar output device.
     *
     * <P> A. Buss modified this to utilize Iterators and StringBuffer.  Also, the corresponding
     * old method now simply invokes this one.
     *
     * @return String representation of this entity's "NamesAndSignatures"
     *
     **/
    public String dumpNamesAndSignaturesStr() {
        String name = getName();
        StringBuffer buf = new StringBuffer();
        buf.append("Names and signatures for ");
        buf.append(name);
        buf.append(NL);
        for (int i = 0; i < 25 + name.length(); i++){
            buf.append('=');
        }
        buf.append(NL);
        Map namesAndSignatures = (Map) allNamesAndSignatures.get(this.getClass());
        for (Iterator i = namesAndSignatures.keySet().iterator(); i.hasNext(); ) {
            Object methodName = i.next();
            buf.append(methodName);
            buf.append(':');
            buf.append('\t');
            buf.append('(');
            for (Iterator j = ((List)namesAndSignatures.get(methodName)).iterator(); j.hasNext();) {
                Class[] aClass = (Class[]) j.next();
                for (int k = 0; k < aClass.length; k++) {
                    buf.append(aClass[k].getName());
                    if (k < aClass.length - 1) {buf.append(',');}
                }
                buf.append(')');
                if (i.hasNext()) {buf.append(NL);}
            }
        }
        return buf.toString();
    }
    
    /**
     *  Prints names and signatures of "do" methods to output specified by Schedule.
     **/
    public void dumpNamesAndSignatures() {
        Schedule.getOutputStream().println(dumpNamesAndSignaturesStr());
    }
    /**
     * Interrupt all this SimEntity's events when a certain time is reached
     * @deprecated - Use stopAtTime(double);
     * @param endingTime The ending time at which all this SimEntity's events are interrupted.
     **/
    public void stopOnTime(double endingTime) {
        this.stopAtTime(endingTime);
    }
    
    /**
     * Interrupt all this SimEntity's events when a certain time is reached
     * @param endingTime The ending time at which all this SimEntity's events are interrupted.
     **/
    public void stopAtTime(double endingTime) {
        new Stop().waitDelay("StopSimEntity", endingTime, this, -Double.MAX_VALUE);
    }
    
    /**
     * Interrupt all this SimEntity's events when a certain event count is reached
     * @param eventName The event to be used.
     * @param count The number of occurences of the event beyond which no events are
     *        scheduled.
     **/
/*
   public void stopOnEvent(String eventName, int count) {
      resetEventCounts();
      stopEventName = eventName;
      numberEvents = count;
   }
 */
    
    public static void setDebug(boolean b) {debug = b;}
    public static boolean isDebug() {return debug;}
    
    /**
     *  @param m The method for which to get the full name (unfortunately the jdk does
     *           not appear to provide this particular String...to my knowledge).
     *  @return The full method name of m, including signature, as a String.
     **/
    public static String getFullMethodName(Method m) {
        String name = m.toString();
        return m.getName() + getSignatureString(m);
    }
    
    /**
     *  @param m The method for which to get the signature as a String (unfortunately
     *  the jdk does not appear to provide this particular String either...to my knowledge).
     *  @return The signature m as a String.
     **/
    public static String getSignatureString(Method m) {
        String name = m.toString();
        return name.substring(name.indexOf('('));
    }
    
    public static boolean isAssignableFrom(Class[] signature, Object[] args) {
        boolean assignable = true;
        if (signature.length != args.length) {
            assignable = false;
        }
        else {
            for (int i = 0; i < signature.length; i++) {
                if (!signature[i].isAssignableFrom(args[i].getClass())) {
                    assignable = false;
                    break;
                }
            }
        }
        return assignable;
    }
    
}
