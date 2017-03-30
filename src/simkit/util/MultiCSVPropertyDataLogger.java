package simkit.util;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.logging.Logger;

/**
 *
 * @author ahbuss
 */
public class MultiCSVPropertyDataLogger implements PropertyChangeListener {

    private static final Logger LOGGER = Logger.getLogger(MultiCSVPropertyDataLogger.class.getName());
    
    private static final String HEADER = 
            "Replication,SimTime,SimEntity,SimEvent,State,OldValue,NewValue";
    
    private File outputFile;
    
    protected int currentReplication;
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
