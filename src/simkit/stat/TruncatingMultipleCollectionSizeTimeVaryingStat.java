package simkit.stat;

import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.util.Collection;

/**
 *
 * @author ahbuss
 */
public class TruncatingMultipleCollectionSizeTimeVaryingStat extends TruncatingMultipleSimpleStatsTimeVarying {
    
    public TruncatingMultipleCollectionSizeTimeVaryingStat(String name, int truncationPoint) {
        super(name, truncationPoint);
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        if (pce.getPropertyName().equals(getName()) && pce.getNewValue() instanceof Collection &&
                pce instanceof IndexedPropertyChangeEvent) {
            this.newObservation(((Collection)pce.getNewValue()).size(), ((IndexedPropertyChangeEvent)pce).getIndex());
        }
    }
    
    @Override
    public void newObservation(double x, int index) {
        if (!indexedStats.containsKey(index)) {
            indexedStats.put(index, new TruncatingSimpleStatsTimeVarying(getTruncationPoint()));
        }
        indexedStats.get(index).newObservation(x);
    }
}
