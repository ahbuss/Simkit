/*
 * MoverMaster.java
 *
 * Created on March 30, 2002, 5:49 PM
 */

package simkit.smdx;
import simkit.Schedule;
/**
 *
 * @author  Arnold Buss
 * @version 
 */
public class MoverMaster {

    public static boolean allowsMagicMoves = false;
    
    public static boolean allowsMagicMovesIfStartSimulation = true;
    
    public static boolean allowsMagicMove() {
        return allowsMagicMoves || 
        ( allowsMagicMovesIfStartSimulation && Schedule.getSimTime() == 0.0 );
    }
    
}
