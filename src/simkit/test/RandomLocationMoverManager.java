package simkit.test;

/**
 *  This is an example of simple "behavior".  This MoverManager is responsible
 *  for directing a single Mover.  The behavior is that a random location is
 *  chosen in a rectangle determined by the instance variables lowerLeft and
 *  upperRight, then the Mover is directed to proceed to that location.  Upon
 *  arrival, another random point is chosen and the Mover directed to that.
 *
 *  @author Arnold Buss
 **/

import simkit.Schedule;
import simkit.SimEntityBase;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;
import simkit.random.CongruentialSeeds;
import simkit.smd.BasicMover;
import simkit.smd.Mover;
import simkit.smd.Coordinate;

public class RandomLocationMoverManager extends SimEntityBase {
    
    private RandomVariate[] nextLocationRV;
    private Mover myMover;
    private boolean cycling;
    
    public RandomLocationMoverManager(Mover m, RandomVariate[] loc) {
        myMover = m;
        myMover.addSimEventListener(this);
        this.setNextLocationRV(loc);
    }
    
    public void reset() {
        super.reset();
        cycling = false;        
    }
    
    public void startCycle() {
        cycling = true;
        myMover.moveTo(getNextLocation());
    }
    
    public void stopCycle() {
        cycling = false;
        myMover.stop();
    }
    
    public void doRun() {
        startCycle();
    }
    
    public void doEndMove(Mover m) {
        if (cycling) {
            startCycle();
        }
    }
    
    public void setNextLocationRV(RandomVariate[] loc) {
        if (loc.length != 2) {
            throw new IllegalArgumentException("Need two RandomVariates");
        }
        nextLocationRV = (RandomVariate[]) loc.clone();
    }
    
    protected Coordinate getNextLocation() {
        return new Coordinate(nextLocationRV[0].generate(), nextLocationRV[1].generate());
    }
    
    public static void main(String[] args) {
        Mover vm = new BasicMover(new Coordinate(20, 20), 25);
        
        RandomVariate[] ranLoc = new RandomVariate[2];
        ranLoc[0] = RandomVariateFactory.getInstance("simkit.random.UniformVariate",
            new Object[] { new Double(-100.0), new Double(100.0) },
            CongruentialSeeds.SEED[0]);
        
        ranLoc[1] = RandomVariateFactory.getInstance("simkit.random.UniformVariate",
            new Object[] { new Double(0.0), new Double(200.0) },
            CongruentialSeeds.SEED[1]);
        
        RandomLocationMoverManager rlmm =
        new RandomLocationMoverManager(vm, ranLoc);
        Schedule.setSingleStep(true);
        Schedule.stopOnTime(100.0);
        Schedule.startSimulation();
    }
}