package simkit.util;

/**
 *  A simple property dumper - instantiate one, add it as a PropertyChangeListener,
 *  and it prints all property changes to the command line.
**/

import java.beans.*;

public class SimplePropertyDumper implements PropertyChangeListener {

    public void propertyChange(PropertyChangeEvent e) {
        String index = "";
        if (e instanceof IndexedPropertyChangeEvent) {
            index = "[" + ((IndexedPropertyChangeEvent) e).getIndex() + "]";
        }

        System.out.println(e.getPropertyName() + index + ": " + e.getOldValue() + " => " +
            e.getNewValue());
    }
}
