package simkit.test;

/*
 * TypedBasicMover.java
 *
 * Created on December 6, 2001, 5:25 PM
 */

import simkit.smd.BasicMover;
import simkit.smd.Coordinate;
import simkit.smd.Mover;
import simkit.Schedule;
/**
 *
 * @author  ahbuss
 * @version $Id$
 */
public class TypedBasicMover extends BasicMover{
    
    protected PlatformType type;

    /** Creates new TypedBasicMover */
    public TypedBasicMover(String name, Coordinate starting, double maxSpeed, PlatformType myType) {
        super(name, starting, maxSpeed);
        type = myType;
    }
    
    public PlatformType getType() { return type; }
    
    public String toString() {
        return "[" + type + "] " + super.toString();
    }
    
    public static void main(String[] args) {
        TypedBasicMover mover = new TypedBasicMover("Osama", new Coordinate(10, 20), 30.0, 
            PlatformType.BAD_GUY);
        if (mover.getType() == PlatformType.LOITERER) {
            mover.moveTo(new Coordinate(40, 60), 20);
        }
        else if (mover.getType() == PlatformType.BAD_GUY) {
            mover.moveTo(new Coordinate(0, 0), 25);
        }
        Schedule.setVerbose(true);
        Schedule.startSimulation();
        
    }

}
