/*
 * MunitionTargetReferee.java
 *
 * Created on July 29, 2002, 12:52 PM
 */

package simkit.smdx;

import simkit.*;
import java.util.*;
import java.awt.*;
/**
 *
 * @author  Arnold Buss
 */
public class MunitionTargetReferee extends SimEntityBase {
    
    private Set targets;
    private boolean clearOnRest;
    
    /** Creates a new instance of MunitionTargetReferee */
    public MunitionTargetReferee() {
        targets = Collections.synchronizedSet(new HashSet());
    }
    
    public void reset() {
        super.reset();
        if (isClearOnReset()) {
            targets.clear();
        }
    }
    
    public void doImpact(Munition munition) {
        Shape impact = munition.getImpact();
        for (Iterator i = targets.iterator(); i.hasNext(); ) {
            Target target = (Target) i.next();
            if (impact.contains( target.getLocation())) {
                Adjudicator adjudicator = MunitionTargetAdjudicatorFactory.getAdjudicator(munition, target);
                adjudicator.adjudicate(munition, target);
            }                    
        }
    }
    
    public void addTarget(Target target) {
        synchronized(targets) {
            targets.add(target);
        }
    }
    
    public void removeTarget(Target target) {
        synchronized(targets) {
            targets.remove(target);
        }
    }
    
    public Set getTargets() {
        Set copy = null;
        synchronized(targets) {
            copy = new HashSet(targets);
        }
        return copy;
    }
    
    public void setClearOnReset(boolean clear) { clearOnRest = clear; }
    
    public boolean isClearOnReset() { return clearOnRest; }
    
    public String toString() {
        StringBuffer buf = new StringBuffer("Targets:\n");
        for (Iterator i = targets.iterator(); i.hasNext(); ) {
            buf.append(i.next());
            buf.append('\n');       
        }       
        return buf.toString();
    }
    
}
