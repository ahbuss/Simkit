/*
 * DefaultAdjudicator.java
 *
 * Created on July 29, 2002, 3:37 PM
 */

package simkit.smdx;

/**
 * An Adjudicator whose result of adjudicate is to do nothing.
 * @author  Arnold Buss
 * @version $Id: DefaultAdjudicator.java 476 2003-12-09 00:27:33Z jlruck $
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
