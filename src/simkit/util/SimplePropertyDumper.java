package simkit.util;

import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import simkit.Named;

/**
* Prints property changes for all properties of any PropertyChangeSources
* it registers with to stdout. The format of the output is: The index
* of the property (if it is an IndexedProperyChangeEvent), the source
* of the property (if dumpSource is true; the default is false), the
* name of the property, the previous value of the property (if not null), the
* new value of the property. Note: changed to only print the new value if the
* old value is null.
* @see PropertyChangeFrame
* @version $Id$
**/
public class SimplePropertyDumper implements PropertyChangeListener {
    
/**
* If true, the source of the property change will be included in
* the output.
**/
    private boolean dumpSource;

/**
* Creates a new SimplePropertyDumper that will not include the
* source of the change in the output.
**/
    public SimplePropertyDumper() {
        this(false);
    }
    
/**
* Creates a new SimplePropertyDumper.
* @param dumpSource If true, the source of the property change is
* included in the output.
**/
    public SimplePropertyDumper(boolean dumpSource) {
        setDumpSource(dumpSource);
    }
    
/**
* Outputs information about the property contained in the event.
**/
    public void propertyChange(PropertyChangeEvent e) {
        String index = "";
        if (e instanceof IndexedPropertyChangeEvent) {
            index = "[" + ((IndexedPropertyChangeEvent) e).getIndex() + "]";
        }
        if (isDumpSource()) {
            Object source = e.getSource();
            if (source instanceof Named) {
                System.out.print("<" + ((Named)source).getName() + "> ");
            } else {
                System.out.print("<" + source + "> ");
            }
        }
        System.out.println( e.getPropertyName() + index + ": " + 
                 (e.getOldValue() != null ? e.getOldValue() + " => " : "") +
            e.getNewValue());
    }
    
/**
* If true, the source of the property change will be included in
* the output.
**/
    public void setDumpSource(boolean ds) { dumpSource = ds; }
    
/**
* If true, the source of the property change will be included in
* the output.
**/
    public boolean isDumpSource() { return dumpSource; }
}
