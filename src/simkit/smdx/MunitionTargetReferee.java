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
 * A referee for Munition-Target interactions. The referee is responsible
 * for determining which {@link Target Targets} are affected by the impact of a 
 * {@link Munition}.
 * The results of the interaction is determined by an {@link Adjudicator} that
 * is obtained from the {@link MunitionTargetAdjudicatorFactory}.
 * <P>After the MunitionTargetReferee is constructed, it must be added
 * as a SimEventListener to each Munition for which it is to referee.
 * </P>
 * @author  Arnold Buss
 * @version $Id$
 */
public class MunitionTargetReferee extends SimEntityBase {
    
/**
* The Targets that are registered with this referee.
**/
    private Set targets;

/**
* If true registered Targets will be cleared if this referee is reset.
**/
    private boolean clearOnRest;
    
    /** Creates a new instance of MunitionTargetReferee */
    public MunitionTargetReferee() {
        targets = Collections.synchronizedSet(new HashSet());
    }
    
/**
* Cancels all pending events for this referee and if clearOnReset is true,
* clears the list of registered Targets.
**/
    public void reset() {
        super.reset();
        if (isClearOnReset()) {
            targets.clear();
        }
    }
    
/**
* Notifies this referee that the given Munition has impacted.
* This referee checks each registered Target to see if it is located inside the
* impact Shape of the Munition. If it is, then it gets an Adjudicator 
* that determines the effect of the Munition on the
* Target.
**/
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
    
/**
* Adds a Target to the list of Targets this referee will check when a Munition Impact
* occurs.
**/
    public void addTarget(Target target) {
        synchronized(targets) {
            targets.add(target);
        }
    }
    
/**
* Removes a Target from the list of registered Targets.
**/
    public void removeTarget(Target target) {
        synchronized(targets) {
            targets.remove(target);
        }
    }
    
/**
* Returns a copy of the list of registered Targets.
**/
    public Set getTargets() {
        Set copy = null;
        synchronized(targets) {
            copy = new HashSet(targets);
        }
        return copy;
    }
    
/**
* If true registered Targets will be cleared if this referee is reset.
**/
    public void setClearOnReset(boolean clear) { clearOnRest = clear; }
    
/**
* If true registered Targets will be cleared if this referee is reset.
**/
    public boolean isClearOnReset() { return clearOnRest; }
    
/**
* Returns a String containing the list of registered Targets.
**/
    public String toString() {
        StringBuffer buf = new StringBuffer("Targets:\n");
        for (Iterator i = targets.iterator(); i.hasNext(); ) {
            buf.append(i.next());
            buf.append('\n');       
        }       
        return buf.toString();
    }
    
}
