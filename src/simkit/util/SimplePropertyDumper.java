package simkit.util;

/**
 *  A simple property dumper - instantiate one, add it as a PropertyChangeListener,
 *  and it prints all property changes to the command line.
**/

import java.beans.*;

public class SimplePropertyDumper implements PropertyChangeListener {
    
    private boolean dumpSource;

    public SimplePropertyDumper() {
        this(false);
    }
    
    public SimplePropertyDumper(boolean dumpSource) {
        setDumpSource(dumpSource);
    }
    
    public void propertyChange(PropertyChangeEvent e) {
        String index = "";
        if (e instanceof IndexedPropertyChangeEvent) {
            index = "[" + ((IndexedPropertyChangeEvent) e).getIndex() + "]";
        }
        if (isDumpSource()) {
            System.out.print("{" + e.getSource() + "} " );
        }
        System.out.println( e.getPropertyName() + index + ": " + e.getOldValue() + " => " +
            e.getNewValue());
    }
    
    public void setDumpSource(boolean ds) { dumpSource = ds; }
    
    public boolean isDumpSource() { return dumpSource; }
}
