package simkit.smd;
import simkit.*;

/**
 *  The basic Mediator interface.  Every Mediator needs at least these
 *  methods to operate.
 *  @author Arnold Buss
 *  @version 1.0.2
**/

public interface Mediator extends SimEntity {
    public void setTargetAndSensor(Mover target, Sensor sensor);
    public Mover getTarget();
    public Sensor getSensor();
    public boolean isTargetInRange();
    public double getDetectionTime();
    public double getUndetectionTime();
    public void disconnect();
//    public void connect();
}
