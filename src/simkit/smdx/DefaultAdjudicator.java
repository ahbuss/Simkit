/*
 * DefaultAdjudicator.java
 *
 * Created on July 29, 2002, 3:37 PM
 */

package simkit.smdx;

/**
 * An Adjudicator whose result of adjudicate is to do nothing.
 * @author  Arnold Buss
 */
public class DefaultAdjudicator implements Adjudicator {
    
    /** 
     * Does nothing.  Use as the default in many cases.
     * @param munition The Munition instance for the impact
     * @param target The affected target
     */    
    public void adjudicate(Munition munition, Target target) {
    }
    
}
