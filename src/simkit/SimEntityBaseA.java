package simkit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import simkit.util.LogString;

/**
 * SimEntityBaseA (A for Annotations)
 * <p>
 * Another implementation of SimEntity.  This one follows the approach of
 * SimEntityBase, but rather than requiring a naming convention for
 * SimEvent-completion methods, annotations are used to map methods of any
 * name to an event name.
 * <p>
 * The annotation type is {@link SimEventMethod} which must contain
 * one argument {@code "<Name of the event>"}.  This name establishes
 * the mapping, and is used along with the {@code eventName} property of a
 * {@code SimEvent} to find the appropriate method.
 * <p>
 * Methods are also referenced by signature.  In a subclass, a method annotated
 * with the same event name as a method in a superclass will override that method
 * if the signature of the subclass method is the same as that superclass
 * method.  This is true even if the method names differ.  For example:
<pre>

class A extends SimEntityA {
...
&#64;SimEventMethod("Arrival") public void foo(int x) { ...}
}

class B extends A {
...
&#64;SimEventMethod("Arrival") public void bar(int x) { ...}
}
</pre>
 * <p>
 * Will result in class B's bar method being called when it receives a
 * SimEvent who's eventName is "Arrival" with an int argument.  So, somewhat
 * strangely, foo is effectively overridden by bar from the perspective
 * of the EventList.
 * <p>
 * However, if the signature doesn't match, then both methods will be available
 * in B.
 * <pre>
 * class A extends SimEntityA {
 *  ...
 *  &#64;SimEventMethod("Arrival") public void foo() { ...}
 * }
 * class B extends A {
 *  ...
 *  &#64;SimEventMethod("Arrival") public void bar(int x) { ...}
 * }
 * </pre>
 * <p>
 * In the above case foo or bar might be called for an arrival event on an instance
 * of class B, depending on the argument.  If there is no argument then foo
 * is called, if there is an int argument then bar is called.
 * <p>
 * Currently, based on {@code BasicSimEntity}, so most of the {@code SimEntityBase} behaviors
 * operate the same.  However many of the the added things in {@code SimEntityBase} are purposefully
 * omitted (like the debug property and some of the dump methods).  You can
 * get a string description similar to the ones provided in {@code SimEntityBase}
 * with the method {@code} describeSimEventMethods()}.  The debugging and
 * verbosity is replaced with our more recent practice of using the logging
 * package and setting logging levels to control such output.
 *
 * @author Kirk Stork. The MOVES Institute, NPS
 */
public class SimEntityBaseA extends BasicSimEntity {

    public static final String _VERSION_ =
            "$Id$";
    private static final Logger LOGGER = Logger.getLogger(SimEntityBaseA.class.getName());
    private boolean eventNamesProcessed = false;
    //  Slightly deep dictionary to look up methods
    // the name field comes from the name argument of the
    // annotation.
    //
    // {class { event name { signature array { method }}}}
    //
    private static Map<Class<?>, Map<String, Map<Class<?>[], Method>>> eventMethodMap;


    static {
        eventMethodMap = new HashMap<>();
    }

    public SimEntityBaseA(String name, Priority priority) {
        super(name, priority);
        mapEventNames();
    }

    public SimEntityBaseA() {
        this(DEFAULT_ENTITY_NAME, Priority.DEFAULT);
        setName(getClass().getSimpleName() + '.' + getSerial());
    }

    public SimEntityBaseA(String name) {
        this(name, Priority.DEFAULT);
    }

    public SimEntityBaseA(Priority priority) {
        this(DEFAULT_ENTITY_NAME, priority);
        setName(getClass().getSimpleName() + '.' + getSerial());
    }

    protected void mapEventNames() {

        if (this.eventNamesProcessed) {
            return;
        }

        // every method introspection can find for this instance
        Method[] allObjectMethods = getClass().getMethods();

        Map<String, Map<Class<?>[], Method>> annotationKeyedMethods;
        Map<Class<?>[], Method> signatureKeyedMethods;

        for (Method m : allObjectMethods) {
//            System.out.println(this.getClass().getSimpleName() + " : " + m.getName());
            // skip any method not annotated as a SimEventMethod, but
            // keep the annotation is a SimEventMethod to get the key
            SimEventMethod annotation = m.getAnnotation(SimEventMethod.class);
            if (null == annotation) {
                continue;
            }

            String eventName = annotation.value();
            annotationKeyedMethods = eventMethodMap.get(this.getClass());
            if (null == annotationKeyedMethods) {
                annotationKeyedMethods = new HashMap<>();
            }

            signatureKeyedMethods = annotationKeyedMethods.get(annotation.value());
            if (null == signatureKeyedMethods) {
                signatureKeyedMethods = new HashMap<>();
            }

            signatureKeyedMethods.put(m.getParameterTypes(), m);
            annotationKeyedMethods.put(annotation.value(), signatureKeyedMethods);
            eventMethodMap.put(this.getClass(), annotationKeyedMethods);
        }
        eventNamesProcessed = true;
    }

    @Override
    public void handleSimEvent(SimEvent event) {
        processSimEvent(event);
    }

    public String describeSimEventMethods() {
        StringBuilder buff = new StringBuilder();
        Map<String, Map<Class<?>[], Method>> stringKeyedMethods =
                eventMethodMap.get(this.getClass());
        for (String key : stringKeyedMethods.keySet()) {
            buff.append(key).append(':').append(NL);
            for (Class[] sig : stringKeyedMethods.get(key).keySet()) {
                Method m = stringKeyedMethods.get(key).get(sig);
                buff.append('\t');
                buff.append(m.toString()).append(NL);
            }
        }
        return buff.toString();
    }

    protected Method lookupMethodForSimEvent(SimEvent event) {

        String requestedEvent = event.getEventName();
        Class<?>[] requestedSignature = new Class<?>[event.getSignature().length];

        // <ugh>

        Object[] args = event.getParameters();
        for (int i = 0; i < args.length; i++) {
            if (null == args[i]) {
                break;
            }
            Class<?> c = args[i].getClass();
            requestedSignature[i] = c;
        }
        // </ugh>

        Map<Class<?>[], Method> signatureKeyedMethods = eventMethodMap.get(getClass()).get(requestedEvent);

        Method requestedMethod = null;

        for (Class<?>[] sig : signatureKeyedMethods.keySet()) {
            if (callableArgTypes(sig, requestedSignature)) {
                requestedMethod = signatureKeyedMethods.get(sig);
            }
        }

        // special case, ignore other entities' run events
        if (requestedEvent.equalsIgnoreCase("Run") &&
                event.getSource() != this) {
            requestedMethod = null;
        }

        if (null == requestedMethod) {
            Level l = Level.WARNING;
            if (LOGGER.isLoggable(l)) {
                StringBuilder buff = new StringBuilder("(");
                int z = requestedSignature.length;
                if (z == 0) {
                    buff.append(")");
                } else {
                    for (Class c : requestedSignature) {
                        buff.append(c.getName());
                        if (z > 0) {
                            buff.append(", ");
                        } else {
                            buff.append(")");
                        }
                        z--;
                    }
                }
                LOGGER.log(l, "No method having signature compatible with {0} was "
                        + "found for event ''{1}'' in {2}", 
                        new Object[]{buff, requestedEvent, 
                            this.getClass().getSimpleName()});

            }
        }
        return requestedMethod;
    }

    @Override
    public void processSimEvent(SimEvent event) {

        Object[] requestedParameters = event.getParameters();
        Method requestedMethod = this.lookupMethodForSimEvent(event);

        // this is consistent with the behavior pattern set by SimEntityBase
        if (null == requestedMethod) {
            return;
        }

        try {
            Level l = Level.FINEST;
            if (LOGGER.isLoggable(l)) {
                LOGGER.log(l, LogString.format(eventList,
                        "Will invoke " + requestedMethod.toGenericString() +
                        " on " + this.toString() +
                        " which was found under the annotation sim event name \'" +
                        event.getEventName() + "\'"));
            }
            requestedMethod.invoke(this, requestedParameters);

        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean isReRunnable() {
        Class<?>[] f = new Class<?>[0];
        Map<Class<?>[], Method> signatureKeyedRunMethods = eventMethodMap.get(this.getClass()).get("Run");
        if (null == signatureKeyedRunMethods || signatureKeyedRunMethods.isEmpty()) {
            return false;
        }
        for (Class<?>[] sig : signatureKeyedRunMethods.keySet()) {
            if (Arrays.equals(sig, f)) {
                return true;
            }
        }
        return false;
    }

    /**
     * As in SimEntityBase#isAssignableFrom(), compares two arrays that represent method
     * signatures to establish whether one signature is call-compatible with another.
     * <p>
     * Naming convention of isXXX is not used because this is not a property of
     * this class, but a utility method.
     * 
     * @param argTypesForMethod The argument types in the method definition
     * @param argTypesForCall  The argument types in the proposed method call
     * @return true if baseArgTypes are a compatible signature when calling a method
     * with signature argTypes
     */
    private static boolean callableArgTypes(Class<?>[] argTypesForMethod, Class<?>[] argTypesForCall) {
        boolean callable = true;
        if (argTypesForCall.length != argTypesForMethod.length) {
            callable = false;
        } else {
            for (int i = 0; i < argTypesForCall.length; i++) {
                if (!argTypesForMethod[i].isAssignableFrom(argTypesForCall[i])) {
                    callable = false;
                    break;
                }
            }
        }
        return callable;
    }
}
