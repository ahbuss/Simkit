/*
 * TestMunition.java
 *
 * Created on July 29, 2002, 3:34 PM
 */

package simkit.smdx.test;
import java.awt.geom.Point2D;

import simkit.Schedule;
import simkit.smdx.CircularImpactMunition;
import simkit.smdx.MunitionTargetAdjudicatorFactory;
import simkit.smdx.MunitionTargetReferee;
import simkit.smdx.SimpleTarget;
import simkit.smdx.SureFireKillAdjudicator;
import simkit.smdx.Target;
import simkit.util.SimplePropertyDumper;
/**
 *
 * @author  Arnold Buss
 */
public class TestMunition {
    
    static {
//        MunitionTargetAdjudicatorFactory.addAdjudicator(simkit.smdx.CircularImpactMunition.class,
//            simkit.smdx.SimpleTarget.class, simkit.smdx.SureFireKillAdjudicator.class);
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MunitionTargetAdjudicatorFactory.setDefaultAdjudicator(new SureFireKillAdjudicator());
        
        MunitionTargetReferee ref = new MunitionTargetReferee();
        
        Target[] target = new Target[2];
        target[0] = new SimpleTarget("Fred", new Point2D.Double(30, 40), 0.0);        
        target[1] = new SimpleTarget("Barney", new Point2D.Double(0, 0), 0.0);
        
        for (int i = 0; i < target.length; i++) {
            ref.addTarget(target[i]);
        }
        System.out.println(ref);
        
        CircularImpactMunition munition = new CircularImpactMunition("Da Bomb", new Point2D.Double(-50, -50), 100.0, 30.0);
        munition.addSimEventListener(ref);        
        
        SimplePropertyDumper dump = new SimplePropertyDumper(true);
        munition.addPropertyChangeListener(dump);
        for (int i = 0; i < target.length; i++) {
            target[i].addPropertyChangeListener(dump);
        }
        
        Schedule.setVerbose(true);
        Schedule.reset();
        munition.waitDelay("Fire", 0.0, new Point2D.Double(25.0, 35.0));
        Schedule.startSimulation();
        
        System.out.println(ref);
    }
    
}
