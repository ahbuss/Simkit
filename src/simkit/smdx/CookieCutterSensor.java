/*
 * CookieCutterSensor.java
 *
 * Created on March 6, 2002, 8:50 PM
 */

package simkit.smdx;
import simkit.*;
import java.awt.*;
import java.awt.geom.*;

/**
 *
 * @author  Arnold Buss
 * @version 
 */
public class CookieCutterSensor extends SimEntityBase implements Sensor {

    /** Creates new CookieCutterSensor */
    public CookieCutterSensor() {
    }

    public void doDetection(Moveable contact) {
    }
    
    public void doUndetection(Moveable contact) {
    }
    
    public Point2D getVelocity() {
        return null;
    }
    
    public double getMaxRange() {
        return 0;
    }
    
    public Shape getFootprint() {
        return null;
    }
    
    public Point2D getAcceleration() {
        return null;
    }
    
    public void doEndMove(Sensor sensor) {
    }
    
    public void doStartMove(Sensor sensor) {
    }
    
    public Point2D getCurrentLocation() {
        return null;
    }
    
    public Mover getMover() {
        return null;
    }
    
    public void setMover(Mover mover) {
    }    
}
