package simkit.actions;

import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.swing.AbstractAction;

/**
 * <P>
 * The simplest GenericAction. Instances should be added as ActionListeners to
 * "Action" Controls -- Buttons, Menu Items, and Toolbar Buttons only. This
 * class keeps a reference to a Model
 *
 * @author Arnold Buss
 *
 */
public class GenericAction extends AbstractAction {

    private Object model;
    private Method actionMethod;

    /**
     * Construct with given target object and method name.
     *
     * @param theModel the target object
     * @param methodName the name of the method to be invoked
     *
     */
    public GenericAction(Object theModel, String methodName) {
        super(methodName);
        model = theModel;
        try {
            actionMethod = theModel.getClass().getMethod(methodName, (Class<?>[]) null);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(e.toString() + " " + methodName);
        }
    }

    public GenericAction(Object theModel, Method method) {
        super(method.getName());
        model = theModel;
        actionMethod = method;
    }

    /**
     * <P>
     * When the ActionEvent is heard, invoke the "action" method on the
     * corresponding object (model). Note that no information contained in the
     * ActionEvent is actually required.
     *
     * @param event The ActionEvent that is heard.
     *
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        try {
            actionMethod.invoke(model, (Object[]) null);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            System.err.println(e);
            e.printStackTrace(System.err);
        } catch (InvocationTargetException e) {
            System.err.println(e.getTargetException());
            e.getTargetException().printStackTrace(System.err);
        }
    }

    /**
     * <P>
     * This "fixes" AbstractAction so that the enabled property can be treated
     * like any other. Note that AbstractAction.setEnabled() fires a
     * PropertyChangeEvent, as does AbstractAction.putValue().
     *
     * @param key The key of the property to be set
     * @param value The value of the property.
     *
     */
    @Override
    public void putValue(String key, Object value) {
        if ("enabled".equals(key)) {
            this.setEnabled(((Boolean) value));
        } else {
            super.putValue(key, value);
        }
    }

    @Override
    public String toString() {
        return actionMethod != null ? actionMethod.getName() : "null";
    }
}
