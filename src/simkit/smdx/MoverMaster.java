/*
 * MoverMaster.java
 *
 * Created on March 30, 2002, 5:49 PM
 */

package simkit.smdx;
import simkit.Schedule;
/**
 * A delegate for determining if a magic move of a Mover should be allowed.
 * Magic moves are only allowed at the start of the simulation.
 * @author  Arnold Buss
 * @version $Id$
 */
public class MoverMaster {

/**
* Always false.
**/
    public static boolean allowsMagicMoves = false;
    
/**
* Always true.
**/
    public static boolean allowsMagicMovesIfStartSimulation = true;

/**
* Should never be constructed.
**/
   private MoverMaster() {
   }
    
/**
* Returns true if it is the start of the simulation and therefore magic moves
* should be allowed.
**/
    public static boolean allowsMagicMove() {
        return allowsMagicMoves || 
        ( allowsMagicMovesIfStartSimulation && Schedule.getSimTime() == 0.0 );
    }
    
}
