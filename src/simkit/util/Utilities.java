package simkit.util;

import javax.swing.*;
import java.beans.*;
import java.util.*;
import java.lang.reflect.*;

public class Utilities {

/**
 *  Creates and returns a JButton with the given action listening.  Additionally,
 *  an instance of  actions.JButtonListener is added as a PropertyChangeListener
 *  to enable and disable the button from its action.
 *  @param action The action from which to create the JButton.
**/
    public static JButton createActionButton(Action action) {
        JButton button = new JButton((String) action.getValue(Action.NAME),
                                      (Icon) action.getValue(Action.SMALL_ICON));
        button.setEnabled(action.isEnabled());
        button.addActionListener(action);
        action.addPropertyChangeListener( new JButtonListener(button) );
        return button;
    }
}
