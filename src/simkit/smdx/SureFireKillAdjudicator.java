/*
 * SureFireKillMediator.java
 *
 * Created on February 20, 2002, 5:01 PM
 */

package simkit.smdx;

/**
 * An Adjudicator that kills the target if the actual Target 
 * location is contained inside the impact Shape of the Munition.
 * If the Target is inside the impact Shape, the probability of kill
 * if 1, if not the probability of kill is 0.
 * @author  Arnold Buss
 * 
 */
public class SureFireKillAdjudicator implements Adjudicator {

/**
* Calls <CODE>kill</CODE> on the Target if the actual location
* of the Target is inside the impact Shape of the Munition.
**/
    public void adjudicate(Munition munition, Target target) {
        if (munition.getImpact().contains(target.getLocation())) {
            target.kill();
        }
    }
    
}
