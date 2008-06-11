package simkit.util;

import java.beans.*;
import javax.swing.*;

/**
 *  A simple listener whose only job is to update a JButton's Enabled property from
 *  a PropertyChangeEvent of the "enabled" property.
 *  @author Arnold Buss
 *  @version $Id: JButtonListener.java 476 2003-12-09 00:27:33Z jlruck $
**/
public class JButtonListener implements PropertyChangeListener {

/**
* The JButton that this JButtonListener controls the enabled property of.
**/
    private JButton button;

/**
 *  Creates a new listener to enable/disable the given JButton.
 *  @param b The button to be enabled/disabled
**/    public  JButtonListener(JButton b) {
        super();
        button = b;
    }

/**
 *  If the Property is "enabled" enables or disables the JButton based
 *  on its value.
 *  @param e The PropertyChangeEvent that may enable/disable the JButton.
**/
    public void propertyChange(PropertyChangeEvent e) {
        if (e.getPropertyName().equals("enabled")) {
            button.setEnabled(((Boolean) e.getNewValue()).booleanValue());
        }
    }

}
