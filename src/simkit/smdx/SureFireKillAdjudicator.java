/*
 * SureFireKillMediator.java
 *
 * Created on February 20, 2002, 5:01 PM
 */

package simkit.smdx;

/**
 *
 * @author  Arnold Buss
 * @version
 */
public class SureFireKillAdjudicator implements Adjudicator {

    public void adjudicate(Munition munition, Target target) {
        if (munition.getImpact().contains(target.getLocation())) {
            target.kill();
        }
    }
    
}
