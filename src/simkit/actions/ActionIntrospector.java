package simkit.actions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;

/**
 *
 * 
 * @author ahbuss
 */
public class ActionIntrospector {

    protected static Map<Class<?>, ActionInfo> cache;
    protected static String[] searchList;

    static {
        setSearchList(new String[]{"actions"});
    }

    public static void setSearchList(String[] newSearchList) {
        searchList = (String[]) newSearchList.clone();
    }

    public static String[] getSearchList() {
        return (String[]) searchList.clone();
    }

    public static ActionInfo getActionInfo(String className) {
        try {
            return getActionInfo(Thread.currentThread().getContextClassLoader().loadClass(className));
        } catch (ClassNotFoundException e) {
            System.err.println(e);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static ActionInfo getActionInfo(Class<?> c) {
        if (cache == null) {
            cache = new WeakHashMap<>();
        }
        if (cache.containsKey(c)) {
            return cache.get(c);
        }
        Class<? extends ActionInfo> infoClass;
        ActionInfo infoInstance = null;
        try {
            if (c.isAssignableFrom(ActionInfo.class)) {
                infoInstance = ActionInfo.class.getConstructor().newInstance();
            } else {
                String infoClassName = c.getName() + "ActionInfo";
                infoClass = (Class<? extends ActionInfo>) Thread.currentThread().getContextClassLoader().loadClass(infoClassName);
                infoInstance = infoClass.getConstructor().newInstance();
            }
        } catch (ClassNotFoundException nope) {
            try {
                String infoClassName = c.getName() + "$ActionInfo";
                infoClass = (Class<? extends ActionInfo>) Thread.currentThread().getContextClassLoader().loadClass(infoClassName);
                infoInstance = infoClass.getConstructor().newInstance();
            } catch (IllegalAccessException | InstantiationException e) {
            } catch (ClassNotFoundException e) {
                String unqualifiedNameWithDot = "." + toUnqualifiedName(c) + "ActionInfo";
                for (String searchList1 : searchList) {
                    try {
                        String infoClassName = searchList1 + unqualifiedNameWithDot;
                        infoClass = (Class<? extends ActionInfo>) Thread.currentThread().getContextClassLoader().loadClass(infoClassName);
                        infoInstance = infoClass.getConstructor().newInstance();
                    } catch (NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                        Logger.getLogger(ActionIntrospector.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (NoSuchMethodException ex) {
                Logger.getLogger(ActionIntrospector.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(ActionIntrospector.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(ActionIntrospector.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(ActionIntrospector.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IllegalAccessException | InstantiationException e) {
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(ActionIntrospector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(ActionIntrospector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(ActionIntrospector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(ActionIntrospector.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (infoInstance == null) {
            infoInstance = new DefaultActionInfo(c);
        }
        cache.put(c, infoInstance);
        return infoInstance;

    }

    public static Method[] getActionMethods(String className) {
        try {
            return getActionMethods(Thread.currentThread().getContextClassLoader().loadClass(className));
        } catch (ClassNotFoundException e) {
            System.err.println(e);
        }
        return null;
    }

    public static Method[] getActionMethods(Class<?> c) {
        return getActionMethods(c, java.lang.Object.class);
    }

    public static Method[] getActionMethods(Class<?> fromClass, Class<?> toSuperClass) {
        if (toSuperClass != null && !toSuperClass.isAssignableFrom(fromClass)) {
            throw new IllegalArgumentException(fromClass.getName() + " is not a subclass of "
                    + toSuperClass.getName());
        }
        ArrayList<Method> actionMethods = new ArrayList<>();
        for (Class<?> thisClass = fromClass; thisClass != toSuperClass; thisClass = thisClass.getSuperclass()) {
            Method[] newMethods = thisClass.getDeclaredMethods();
            for (Method newMethod : newMethods) {
                if ((newMethod.getParameterTypes().length == 0) && (newMethod.getReturnType() == Void.TYPE) && (Modifier.isPublic(newMethod.getModifiers()))) {
                    actionMethods.add(newMethod);
                }
            }
        }
        return (Method[]) actionMethods.toArray(new Method[actionMethods.size()]);
    }

    public static Method getActionMethod(String name, Class<?> c) throws NoSuchActionMethodException {
        try {
            Method method = c.getMethod(name, (Class<?>[]) null);
            if (method.getReturnType() == Void.TYPE) {
                return method;
            }
        } catch (NoSuchMethodException e) {
        }
        throw new NoSuchActionMethodException("No Action method called " + name
                + " for " + c);
    }

    public static String[] getActionNames(Class fromClass, Class toClass) {
        Method[] method = getActionMethods(fromClass, toClass);
        String[] names = new String[method.length];
        for (int i = 0; i < names.length; i++) {
            names[i] = method[i].getName();
        }
        return names;
    }

    public static String[] getActionNames(Class c) {
        Method[] methods = getActionMethods(c);
        String[] names = new String[methods.length];
        for (int i = 0; i < methods.length; i++) {
            names[i] = methods[i].getName();
        }
        return names;
    }

    public static String[] getActionNames(Object obj) {
        return getActionNames(obj.getClass());
    }

    public static Action[] getActions(Object obj) {
        return getActionInfo(obj.getClass()).getActions(obj);
    }

    /**
     * First get or generate all the object's actions. Then filter out the ones
     * that are superClass or higher.
     *
     * @param obj Given Object
     * @param superClass Given super class
     * @return array of object's Actions
     */
    public static Action[] getActions(Object obj, Class<?> superClass) {
        if (!superClass.isAssignableFrom(obj.getClass())) {
            return new Action[0];
        } else {
            String[] actionNames = getActionNames(obj.getClass(), superClass);
            return getActionInfo(obj.getClass()).getActions(obj, actionNames);
        }
    }

    public static Action[] getActions(Object obj, String[] justThese) {
        return getActionInfo(obj.getClass()).getActions(obj, justThese);
    }

    public static String toUnqualifiedName(Class fromClass) {
        String unQualifiedName = fromClass.getName();
        Package p = fromClass.getPackage();
        if (p != null) {
            unQualifiedName = unQualifiedName.substring(p.getName().length() + 1);
        }
        return unQualifiedName;
    }

    public static Action getAction(Object obj, String actionName) {
        return getActionInfo(obj.getClass()).getAction(obj, actionName);
    }

    public static Action getAction(Object[] instances, String actionName) {
        Action action = null;
        for (Object instance : instances) {
            action = getAction(instance, actionName);
            if (action != null) {
                break;
            }
        }
        return action;
    }

    static class DefaultActionInfo implements ActionInfo {

        protected Class<?> myClass;
        protected String[] actionNames;
        protected WeakHashMap<Object, Action[]> objectActionCache;

        protected DefaultActionInfo(Class<?> theClass) {
            myClass = theClass;
            actionNames = ActionIntrospector.getActionNames(theClass);
            objectActionCache = new WeakHashMap<>();
        }

        @Override
        public String[] getActionNames() {
            return (String[]) actionNames.clone();
        }

        @Override
        public Action[] getActions(Object instance) {
            if (!myClass.isInstance(instance)) {
                throw new IllegalArgumentException("Instance is not from "
                        + myClass);
            }
            Action[] theActions = objectActionCache.get(instance);
            if (theActions == null) {
                theActions = new Action[actionNames.length];
                for (int i = 0; i < actionNames.length; i++) {
                    try {
                        theActions[i] = new GenericAction(instance, actionNames[i]);
                    } catch (IllegalArgumentException e) {
                        System.err.println(e);
                    }
                }
                objectActionCache.put(instance, theActions);
            }
            return theActions;
        }

        @Override
        public Class getActionClass() {
            return myClass;
        }

        @Override
        public boolean isActionMethod(String methodName) {
            boolean isActionMethod = false;
            try {
                Method method = ActionIntrospector.getActionMethod(methodName, myClass);
                isActionMethod = method != null;
            } catch (NoSuchActionMethodException e) {
            }
            return isActionMethod;
        }

        @Override
        public Action getAction(Object instance, String actionName) {
            if (!getActionClass().isInstance(instance)) {
                throw new IllegalArgumentException(instance.getClass() + " not a "
                        + getActionClass());
            }
            Action[] actions = getActions(instance);
            for (int i = 0; i < actionNames.length; i++) {
                if (actionNames[i].equals(actionName)) {
                    return actions[i];
                }
            }
            return null;
        }

        @Override
        public Action[] getActions(Object instance, String[] justThese) {
            ArrayList<Action> temp = new ArrayList<>();
            for (String justThese1 : justThese) {
                if (isActionMethod(justThese1)) {
                    temp.add(getAction(instance, justThese1));
                }
            }
            return temp.toArray(new Action[temp.size()]);
        }
    }
}
