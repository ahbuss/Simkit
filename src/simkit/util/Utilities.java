package simkit.util;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

/**
* Holds a static method used to create an "action button".
* @version $Id: Utilities.java 643 2004-04-25 22:59:33Z kastork $
**/
public class Utilities {

/**
 *  Creates and returns a JButton with the given action set as a listener.  Additionally,
 *  an instance of  JButtonListener is added as a PropertyChangeListener
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
