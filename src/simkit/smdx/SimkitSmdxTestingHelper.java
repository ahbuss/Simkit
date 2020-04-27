
package simkit.smdx;

import simkit.smdx.*;
import java.awt.geom.Point2D;
/**
* Used to gain access to protected things for testing.
*
* Renamed and moved from JDAFS project at Id: simkit.smdx.Helper.java 1544
* 
* 
* @author John Ruck (Rolands and Associates Corporation 8-23-06)
**/
public class SimkitSmdxTestingHelper {

    public static Point2D getDestination(UniformLinearMover ulm) {
        return ulm.destination;
    }
}
