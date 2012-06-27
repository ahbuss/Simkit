package simkit.actions;

import java.beans.*;
import java.lang.reflect.*;
import java.util.*;
import javax.swing.*;

/**
 *  <P>A simple listener whose only job is to update a JButton's Enabled property from
 *  a PropertyChangeEvent of the "enabled" property.
 *
 *  <P> Extended to have the received <I>all</I> PropertyChangeEvents change
 *  the corresponding property on the button.  This uses JavaBeans Introspection,
 *  but could have just as easily used "ordinary" reflection...
 *  
 * @version $Id$
 * @author ahbuss
**/
public class JButtonListener implements PropertyChangeListener {

    private Object button;
    private Map<String, Method> setters;

/**
 *  @param b The button to be enabled/disabled
**/    public  JButtonListener(Object b) {
        super();
        button = b;
        setters = new WeakHashMap<String, Method>();
        try {
            BeanInfo bi = Introspector.getBeanInfo(button.getClass());
            PropertyDescriptor[] pd = bi.getPropertyDescriptors();
            for (int i = 0; i < pd.length; i++) {
                Method set = pd[i].getWriteMethod();
                if (set != null) {
                    setters.put(pd[i].getName(), set);
                }
            }
        }
        catch (IntrospectionException e) {e.printStackTrace(System.err);}
    }

/**
 *  @param e The PropertyChangeEvent that may enable/disable the JButton.
**/
    public void propertyChange(PropertyChangeEvent e) {
/*
        if (e.getPropertyName().equals("enabled")) {
            button.setEnabled(((Boolean) e.getNewValue()).booleanValue());
        }
*/
        Method setter = setters.get(e.getPropertyName());
        if (setter != null) {
            try {
                setter.invoke(button, new Object[] {e.getNewValue()});
                return;
            }
            catch (InvocationTargetException ex) {ex.getTargetException().printStackTrace(System.err);}
            catch (IllegalAccessException ex) {ex.printStackTrace(System.err);}
        }
    }
    
    public Object getObject() { return button ; }

}
