package simkit.util;

import java.beans.*;
import javax.swing.*;

/**
 *  A simple listener whose only job is to update a JButton's Enabled property from
 *  a PropertyChangeEvent of the "enabled" property.
 *  @author Arnold Buss
**/
public class JButtonListener implements PropertyChangeListener {

    private JButton button;

/**
 *  @param b The button to be enabled/disabled
**/    public  JButtonListener(JButton b) {
        super();
        button = b;
    }

/**
 *  @param e The PropertyChangeEvent that may enable/disable the JButton.
**/
    public void propertyChange(PropertyChangeEvent e) {
        if (e.getPropertyName().equals("enabled")) {
            button.setEnabled(((Boolean) e.getNewValue()).booleanValue());
        }
    }

}
