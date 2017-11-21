package simkit.test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import simkit.SimEntity;
import simkit.util.PropertyChangeEventX;

/**
 *
 * @author ahbuss
 */
public class SimplePropertyDumperX implements PropertyChangeListener {

    /**
     * Outputs a CSV line on the console
     * @param pce Given PropertyChangeEvent
     */
    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        StringBuilder outputLine = new StringBuilder();
        if (pce.getSource() instanceof SimEntity) {
            outputLine.append(((SimEntity)pce.getSource()).getName());
        }
        outputLine.append(',').append(pce.getPropertyName()).append(',');
        outputLine.append(pce.getOldValue()).append(',').append(pce.getNewValue());
        if (pce instanceof PropertyChangeEventX) {
            Object[] extraData = ((PropertyChangeEventX)pce).getExtraData();
            for (Object o: extraData) {
                outputLine.append(',').append(o);
            }
        }
        System.out.println(outputLine);
    }
    
}
