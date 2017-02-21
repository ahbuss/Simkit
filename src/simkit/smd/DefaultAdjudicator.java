package simkit.smd;

/**
 * An Adjudicator whose result of adjudicate is to do nothing.
 * @author  Arnold Buss
 * @version $Id$
 */
public class DefaultAdjudicator implements Adjudicator {
    
    /** 
     * Does nothing.  Use as the default in many cases.
     * @param munition The Munition instance for the impact
     * @param target The affected target
     */    
    @Override
    public void adjudicate(Munition munition, Target target) {
    }
    
}
