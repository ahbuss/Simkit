/*
 * TestMoverManager.java
 *
 * Created on July 29, 2002, 11:39 AM
 */

package simkit.smdx.test;
import java.awt.geom.Point2D;

import simkit.Schedule;
import simkit.random.RandomNumber;
import simkit.random.RandomNumberFactory;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;
import simkit.smdx.Mover;
import simkit.smdx.MoverManager;
import simkit.smdx.MoverManagerFactory;
import simkit.smdx.RandomLocationMoverManager;
import simkit.smdx.UniformLinearMover;
/**
 *
 * @author  Arnold Buss
 */
public class TestMoverManager {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        RandomNumber rng = RandomNumberFactory.getInstance(12345L);
        RandomVariate[] location = new RandomVariate[2];
        location[0] = RandomVariateFactory.getInstance("Uniform", 0.0, 100.0);
        location[1] = RandomVariateFactory.getInstance("Uniform",  0.0, 100.0);
        
        for (RandomVariate loc: location) {
            loc.setRandomNumber(rng);
        }
        Point2D start = new Point2D.Double(0.0, 0.0);
        Mover[] mover = new Mover[3];
        mover[0] = new UniformLinearMover("Fred", start, 20.0);
        mover[1] = new UniformLinearMover("Barney", start, 30.0);
        mover[2] = new UniformLinearMover("Wilma", start, 40.0);
        
        RandomVariate speed = RandomVariateFactory.getInstance("Uniform", 0.0, mover[2].getMaxSpeed());
        speed.getRandomNumber().setSeed(12345L);
        
        MoverManager[] manager = new MoverManager[mover.length];
        manager[0] = new RandomLocationMoverManager(mover[0], location);
        manager[1] = MoverManagerFactory.getInstance("simkit.smdx.RandomLocationMoverManager",
            new Object[] { mover[1], location });
        manager[2] = MoverManagerFactory.getInstance("simkit.smdx.RandomLocationMoverManager",
            new Object[] { mover[2], location, speed });
        for (int i = 0; i < manager.length; i++) {
            manager[i].setStartOnReset(true);
        }
        
        Schedule.setSingleStep(true);
        Schedule.reset();
        Schedule.startSimulation();
    }
    
}
