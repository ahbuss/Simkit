package simkit.smd;

import java.awt.Shape;
import java.util.LinkedHashSet;
import java.util.Iterator;
import java.util.Set;

import simkit.SimEntityBase;

/**
 * A referee for Munition-Target interactions. The referee is responsible for
 * determining which {@link Target Targets} are affected by the impact of a
 * {@link Munition}. The results of the interaction is determined by an
 * {@link Adjudicator} that is obtained from the
 * {@link MunitionTargetAdjudicatorFactory}.
 * <P>
 * After the MunitionTargetReferee is constructed, it must be added as a
 * SimEventListener to each Munition for which it is to referee.
 * </P>
 *
 * @author Arnold Buss
 * @version $Id$
 */
public class MunitionTargetReferee extends SimEntityBase {

    /**
     * The Targets that are registered with this referee.
     *
     */
    private final Set<Target> targets;

    /**
     * If true registered Targets will be cleared if this referee is reset.
     *
     */
    private boolean clearOnRest;

    /**
     * Creates a new instance of MunitionTargetReferee
     */
    public MunitionTargetReferee() {
        targets = new LinkedHashSet<>();
    }

    /**
     * If clearOnReset is true, clears the list of registered Targets.
     *
     */
    @Override
    public void reset() {
        super.reset();
        if (isClearOnReset()) {
            targets.clear();
        }
    }

    /**
     * This referee is notified that the given Munition has impacted. This
     * referee checks each registered Target to see if it is located inside the
     * impact Shape of the Munition. If it is, then it gets an Adjudicator that
     * determines the effect of the Munition on the Target.
     *
     * @param munition Given Munition making the impact
     *
     */
    public void doImpact(Munition munition) {
        Shape impact = munition.getImpact();
        for (Target target : targets) {
            if (impact.contains(target.getCurrentLocation())) {
                Adjudicator adjudicator = MunitionTargetAdjudicatorFactory.getAdjudicator(munition, target);
                adjudicator.adjudicate(munition, target);
            }
        }
    }

    /**
     * Adds a Target to the list of Targets this referee will check when a
     * Munition Impact occurs.
     *
     * @param target Given Target to be added
     */
    public void addTarget(Target target) {
        targets.add(target);
    }

    /**
     * 
     * @param target Given target to be removed from the list of 
     * registered Targets.
     */
    public void removeTarget(Target target) {
            targets.remove(target);
    }

    /**
     *
     * @return a copy of the list of registered Targets.
     */
    public Set<Target> getTargets() {
        Set<Target> copy;
        copy = new LinkedHashSet<>(targets);
        return copy;
    }

    /**
     * 
     * @param clear If true registered Targets will be cleared if this referee is reset.
     */
    public void setClearOnReset(boolean clear) {
        clearOnRest = clear;
    }

    /**
     * 
     * @return true if registered Targets will be cleared if this referee is reset.
     */
    public boolean isClearOnReset() {
        return clearOnRest;
    }

    /**
     *
     * @return a String containing the list of registered Targets.
     */
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder("Targets:\n");
        for (Iterator i = targets.iterator(); i.hasNext();) {
            buf.append(i.next());
            buf.append('\n');
        }
        return buf.toString();
    }

}
