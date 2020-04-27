package simkit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Default implementation of a SimEntity using reflection. Consequently, the
 * callback methods don't have to be implemented by the modeler using this
 * class. When the processSimEvent method is called, reflection is used to
 * attempt to find the method for the SimEvent. If the method is not found, it
 * is silently ignored.
 *
 * @author Arnold Buss
 * @author K. A. Stork
 *
 *
 */
public abstract class SimEntityBase extends BasicSimEntity {

    private static final Logger LOGGER = Logger.getLogger(SimEntityBase.class.getName());

    /**
     * A two dimensional Hash table used to cache doMethods for all
     * SimEntityBases. Keyed by Class and Method.
*
     */
    private static LinkedHashMap<Class<?>, Map<String, Method>> allDoMethods;

    /**
     * A two dimensional Hash table used to hold the names and signatures of all
     * doMethods of all SimEntityBases. Keyed by Class and Method name.
*
     */
    private static LinkedHashMap<Class<?>, Map<String, List<Class<?>[]>>> allNamesAndSignatures;

    static {
        allDoMethods = new LinkedHashMap<>();
        allNamesAndSignatures = new LinkedHashMap<>();
    }

    /**
     * If true, print debug information.
*
     */
    private static boolean debug;

    /**
     * Construct a new SimEntityBase with the given name and event priority.
     *
     * @param name The name of the entity.
     * @param priority The default priority for processing this entity's events.
    *
     */
    public SimEntityBase(String name, Priority priority) {
        super(name, priority);

        Map<String, Method> doMethods = allDoMethods.get(this.getClass());
        if (doMethods == null) {
            doMethods = new LinkedHashMap<>();
            Method[] methods = this.getClass().getMethods();
            for (Method method : methods) {
                if (method.getName().startsWith(EVENT_METHOD_PREFIX)) {
                    doMethods.put(getFullMethodName(method), method);
                    String methodName = method.getName();
                } // if
            } // for
            allDoMethods.put(this.getClass(), doMethods);
        } // if

        Map<String, List<Class<?>[]>> namesAndSignatures = allNamesAndSignatures.get(this.getClass());
        if (namesAndSignatures == null) {
            namesAndSignatures = new LinkedHashMap<>();
            for (Method nextDoMethod : doMethods.values()) {
                List<Class<?>[]> v;
                if (namesAndSignatures.containsKey(nextDoMethod.getName())) {
                    v = namesAndSignatures.get(nextDoMethod.getName());
                } else {
                    v = new ArrayList<>();
                    namesAndSignatures.put(nextDoMethod.getName(), v);
                }
                v.add(nextDoMethod.getParameterTypes());
            }
            allNamesAndSignatures.put(this.getClass(), namesAndSignatures);
        }
    }

    /**
     * Construct a new SimEntityBase with a default name and priority. The name
     * is the class name plus a unique serial number.
*
     */
    public SimEntityBase() {
        this(DEFAULT_ENTITY_NAME, Priority.DEFAULT);
        setName(getClass().getSimpleName() + '.' + getSerial());
    }

    /**
     * Construct a new SimEntityBase with given name and a default priority.
     *
     * @param name The name of the entity.
*
     */
    public SimEntityBase(String name) {
        this(name, Priority.DEFAULT);
    }

    /**
     * Construct a new SimEntityBase with a default name and the given priority.
     * The name is the class name plus a unique serial number.
     *
     * @param priority The priority for processing this entity's events.
*
     */
    public SimEntityBase(Priority priority) {
        this(DEFAULT_ENTITY_NAME, priority);
        setName(getClass().getSimpleName() + '.' + getSerial());
    }

    /**
     * Process the given SimEvent. If the Method signature does not match any
     * for this entity, the event is ignored. Also other entity's doRun events
     * are ignored. Just calls processSimEvent.
     *
     * @param event Given SimEvent
     */
    @Override
    public synchronized void handleSimEvent(SimEvent event) {
        processSimEvent(event);
    }

    /**
     * Process the given SimEvent. If the Method signature does not match any
     * for this entity, the event is ignored. Also other entity's doRun events
     * are ignored.
     * @param event Given SimEvent
     */
    @Override
    public synchronized void processSimEvent(SimEvent event) {
        if (event == null) {
            return;
        }
        Method m = null;
        String methodName = event.getMethodName();
        // Do not process other SimEntityBase's "doRun()" methods
        if (!event.getSource().equals(this) && event.getFullMethodName().equals("doRun()")) {
            return;
        } // if
        // If no method of that name, then there is no hope.
        Map<String, List<Class<?>[]>> namesAndSignatures
                = allNamesAndSignatures.get(this.getClass());
        if (!namesAndSignatures.containsKey(methodName)) {
            if (debug) {
                LOGGER.log(Level.INFO, "No method of name {0} -- giving up...", methodName);
            } // if
            return;
        } // if
        if (isVerbose()) {
            LOGGER.log(Level.INFO, "Event processed by {0}: {1}", new Object[]{this, event});
        } // if

        try {
            Map<String, Method> doMethods = allDoMethods.get(this.getClass());
            // This method has either happened before or matches one in method exactly
//            TODO: put logging here
            if (isDebug()) {
                LOGGER.log(Level.INFO, 
                        "doMethods hashcode = {0}{1}namesAndSignatures hashcode = "
                                + "{2}{3}doMethods: {4}", 
                        new Object[]{doMethods.hashCode(), NL, 
                            namesAndSignatures.hashCode(), NL, doMethods});
            }
            if (doMethods.containsKey(event.getFullMethodName())) {
                m = doMethods.get(event.getFullMethodName());
                if (isDebug()) {
                    LOGGER.log(Level.FINE, 
                            "SimEntityBase will invoke {0} on {1} which was "
                                    + "found in the allDoMethods cache.", 
                            new Object[]{m.toGenericString(), this.toString()});
                }
                m.invoke(this, event.getParameters());
            } // if
            else {
//                TODO: put logging here
                if (isVerbose()) {
                    LOGGER.log(Level.INFO,
                            "Master lookup failed, trying namesAndSignatures..." + 
                                    " Method Name = {0}", event.getFullMethodName());
                }
                // Now, we are here only because there is some chance that there will be a match.
                // First
                Object[] params = event.getParameters();
                for (Class<?>[] signature : namesAndSignatures.get(methodName)) {
                    if (isDebug()) {
                        LOGGER.log(Level.INFO, "namesAndSignatures: {0}", 
                                namesAndSignatures.hashCode());
                    }
                    if (debug) {
                        String msg = "  Signature: (";
                        for (int k = 0; k < signature.length; k++) {
                            msg += signature[k];
                            if (k < signature.length - 1) {
                                msg += ", ";
                            }
                        } // for
                        msg += ")";
                        LOGGER.info(msg);
                    }  // if
                    if (signature.length == params.length) {
                        boolean match = true;
                        if (debug) {
                            LOGGER.log(Level.INFO, "There are {0} arguments to check...", 
                                    signature.length);
                        } // if
                        for (int i = 0; i < signature.length; i++) {
                            if (debug) {
                                LOGGER.log(Level.INFO, "\tChecking: {0}", signature[i]);
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
                            } // if
                            else {
                                match = match && (params[i] == null || signature[i].isAssignableFrom(params[i].getClass()));
                            }  // else
                            if (isVerbose()) {
                                LOGGER.log(Level.INFO, "{0} ?=? {1}", 
                                        new Object[]{signature[i].getName(), 
                                            params[i].getClass().getName()});
                            } // if
                        } // for
                        if (match) {
                            try {
                                if (debug) {
                                    LOGGER.log(Level.INFO, "Match found: {0}", 
                                            event.getFullMethodName());
                                }
                                m = getClass().getMethod(methodName, signature);
                                if (isDebug()) {
                                    LOGGER.log(Level.FINE, 
                                            "SimEntityBase will invoke {0} on {1} "
                                                    + "and keep the method for later", 
                                            new Object[]{m.toGenericString(), this.toString()});
                                    LOGGER.log(Level.FINE, 
                                            "Placing method {0} with signature {1} "
                                                    + "under key {2}", 
                                            new Object[]{m.toString(), 
                                                Arrays.toString(signature), 
                                                event.getFullMethodName()});
                                }
                                m.invoke(this, params);
                                doMethods.put(event.getFullMethodName(), m);
                            } catch (NoSuchMethodException f) {
                                LOGGER.log(Level.SEVERE, "", f);
                                throw (new RuntimeException(f));
                            }
                        }  //if
                    }
                }
            }
        } catch (NullPointerException e) {
            LOGGER.log(Level.SEVERE, "Attempted method: " + event.getFullMethodName(), e);
            throw (new RuntimeException(e));
        } catch (IllegalAccessException e) {
            String msg = "Attempted method: " + m + NL;
            msg += "  [key = " + event.getFullMethodName() + "]" + NL;
            msg += "  [name = " + event.getMethodName() + "]" + NL;
            msg += "  [params = (";
            for (int i = 0; i < event.getParameters().length; i++) {
                msg += event.getParameters()[i].getClass().getName();
                if (i < event.getParameters().length - 1) {
                    msg += ",";
                }
            }
            msg += ") ]" + NL;
            msg += "This object: " + Integer.toHexString(this.hashCode()) + NL;
            msg += "This class: ";
            Class c = this.getClass();
            while (!c.equals(SimEntityBase.class)) {
                msg += c;
                c = c.getSuperclass();
            }
            msg += NL + "Method's object: "
                    + Integer.toHexString(event.getSource().hashCode()) + NL;
            msg += "Method's class: " + m.getDeclaringClass();
            LOGGER.log(Level.SEVERE, msg, e);
            //shouldn't happen
            // so if it does, die
            throw (new RuntimeException(e));
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, "IllegalArumentException ignored", e);
            //shouldn't happen
            // so if it does, die
            throw (new RuntimeException(e));
        } catch (InvocationTargetException e) {
            LOGGER.log(Level.SEVERE, "SimEntityBase.processSimEvent: "
                    + "The event method threw an exception during processing of"
                    + " events for a SimEventListener.  The event being processed is\n"
                    + event.toString() + "\nThe listener attempting to process the event is\n"
                    + this.toString(), e);
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            } else if (cause instanceof Error) {
                throw (Error) cause;
            } else {
                LOGGER.log(Level.SEVERE, "The cause was a checked Exception, "
                        + "rethrowing as RuntimeException.", cause);
                throw new RuntimeException(cause);
            }
        } finally {
        }
    }

    /**
     * Gets a String representation of this entity's event methods.
     * <P>
     * This method is added by TRAC-WSMR, Authot Lt Col Olson, USMC.
     * <code>dumpDoMethodsStr</code> returns a String containing the same
     * information as <code>dumpDoMethods</code>. This method allows a developer
     * to place the information in a graphical user interface, textbox, or
     * similar output device.
     *
     * <P>
     * Method rewritten by A. Buss to use newer Iterator (vice Enumeration) and
     * to use <CODE>StringBuilder</CODE> for speed. The underscores are also
     * made to exactly match the length of the heading.
     *
     * @return String representation of this entity's "doMethods"
     *
     */
    public String dumpDoMethodsStr() {
        String name = getName();
        StringBuilder buf = new StringBuilder();
        buf.append("Event Methods for ");
        buf.append(name);
        buf.append(NL);
        for (int i = 0; i < 18 + name.length(); i++) {
            buf.append('=');
        }
        buf.append(NL);
        Map<String, Method> doMethods = allDoMethods.get(this.getClass());
        for (String methodName : doMethods.keySet()) {
            buf.append(methodName);
            buf.append(NL);
        }
        return buf.toString();
    }

    /**
     * Prints out the "do" methods for this SimEntity
     *
     */
    public void dumpDoMethods() {
        System.out.println(this.dumpDoMethodsStr());
    }

    /**
     * Produces a String containing the names and signatures of this entity's
     * "do" methods.
     * <P>
     * This method is added by TRAC-WSMR, Authot Lt Col Olson, USMC.
     * <code>dumpNamesAndSignaturesStr()</code> returns a String containing the
     * same information as <code>dumpNamesAndSignatures()</code>. This method
     * allows a developer to place the information in a graphical user
     * interface, textbox, or similar output device.
     *
     * <P>
     * A. Buss modified this to utilize Iterators and StringBuilder. Also, the
     * corresponding old method now simply invokes this one.
     *
     * @return String representation of this entity's "NamesAndSignatures"
     *
     *
     */
    public String dumpNamesAndSignaturesStr() {
        String name = getName();
        StringBuilder buf = new StringBuilder();
        buf.append("Names and signatures for ");
        buf.append(name);
        buf.append(NL);
        for (int i = 0; i < 25 + name.length(); i++) {
            buf.append('=');
        }
        buf.append(NL);
        Map<String, List<Class<?>[]>> namesAndSignatures = allNamesAndSignatures.get(this.getClass());
        for (Iterator<String> i = namesAndSignatures.keySet().iterator(); i.hasNext();) {
            String methodName = i.next();
            buf.append(methodName);
            buf.append(':');
            buf.append('\t');
            buf.append('(');
            for (Class[] aClass : namesAndSignatures.get(methodName)) {
                for (int k = 0; k < aClass.length; k++) {
                    buf.append(aClass[k].getName());
                    if (k < aClass.length - 1) {
                        buf.append(',');
                    }
                }
                buf.append(')');
                if (i.hasNext()) {
                    buf.append(NL);
                }
            }
        }
        return buf.toString();
    }

    /**
     * Prints names and signatures of "do" methods to output specified by
     * Schedule.
     *
     */
    public void dumpNamesAndSignatures() {
        System.out.println(dumpNamesAndSignaturesStr());
    }

    /**
     * Interrupt all this SimEntity's events when a certain time is reached
     *
     * @deprecated - Use stopAtTime(double);
     * @param endingTime The ending time at which all this SimEntity's events
     * are interrupted.
     *
     */
    public void stopOnTime(double endingTime) {
        this.stopAtTime(endingTime);
    }

    /**
     * Interrupt all this SimEntity's events when a certain time is reached
     *
     * @param endingTime The ending time at which all this SimEntity's events
     * are interrupted.
     *
     */
    public void stopAtTime(double endingTime) {
        new Stop(eventList).waitDelay("StopSimEntity", endingTime, Priority.LOWEST, this);
    }

    /*
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
    /**
     *
     * @param b If true, print debug information.
     */
    public static void setDebug(boolean b) {
        debug = b;
    }

    /**
     *
     * @return If true, print debug information.
     */
    public static boolean isDebug() {
        return debug;
    }

    /**
     * Gets the method name plus signature as a String.
     *
     * @param m The method for which to get the full name (unfortunately the jdk
     * does not appear to provide this particular String...to my knowledge).
     * @return The full method name of m, including signature, as a String.
     *
     */
    public static String getFullMethodName(Method m) {
        String name = m.toString();
        return m.getName() + getSignatureString(m);
    }

    /**
     * Get the signature of the given Method as a String.
     *
     * @param m The method for which to get the signature as a String
     * (unfortunately the jdk does not appear to provide this particular String
     * either...to my knowledge).
     * @return The signature m as a String.
     *
     */
    public static String getSignatureString(Method m) {
        String name = m.toString();
        return name.substring(name.indexOf('('));
    }

    /**
     * Determines if a event signature is equivalent to the given arguments.
     *
     * @param signature An array of the method's Classes.
     * @param args An array containing the method's arguments as Objects.
     * @return True if the corresponding signatures are assignable from the
     * arguments and are therefore equivalent.
*
     */
    public static boolean isAssignableFrom(Class<?>[] signature, Object[] args) {
        boolean assignable = true;
        if (signature.length != args.length) {
            assignable = false;
        } else {
            for (int i = 0; i < signature.length; i++) {
                if (!signature[i].isAssignableFrom(args[i].getClass())) {
                    assignable = false;
                    break;
                }
            }
        }
        return assignable;
    }

    /**
     * Does <i>not</i> clear cache of doMethods and namesAndSignatures
     */
    public static void coldReset() {
//        allDoMethods.clear();
//        allNamesAndSignatures.clear();
    }

}
