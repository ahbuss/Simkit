package simkit.smdx;

import simkit.*;
import java.awt.*;
import java.awt.geom.*;
import java.beans.*;
/**
 *
 * @author  Arnold Buss
 */
public interface Munition extends Moveable, SimEntity {
    
    public Point2D getAimPoint() ;
    
    public double getImpactRange() ;
    
    public Shape getImpact() ;
    
    public boolean isExpended();
}
