package simkit.smdx;
import simkit.*;
import simkit.smdx.*;
import simkit.random.*;
import java.awt.geom.*;
/**
 *
 * @author  Arnold Buss
 */
public class RandomLocationMoverManager extends SimEntityBase implements MoverManager {
    
    private Mover mover;
    private RandomVariate[] location;
    private RandomVariate speed;
    private boolean startOnReset;
    
    /** Creates a new instance of RandomLocationMoverManager */
    public RandomLocationMoverManager(Mover mover, RandomVariate[] location, RandomVariate speed) {
        this(mover, location);
        setSpeedGenerator(speed);
    }
    
    public RandomLocationMoverManager(Mover mover, RandomVariate[] location) {
        setMover(mover);
        setLocationGenerator(location);
        RandomVariate speed = RandomVariateFactory.getInstance("simkit.random.ConstantVariate",
            new Object[] { new Double(mover.getMaxSpeed()) } );
        setSpeedGenerator(speed);
    }
    
    public void reset() {
        if (isStartOnReset()) {
            waitDelay("Start", 0.0);
        }
    }
    
    public void doStart() {
        start();
    }
    
    public void doEndMove(Mover m) {
        if (m == mover) {
            mover.moveTo(getLocation(), speed.generate());
        }
    }    
    
    public Mover getMover() { return mover; }
    
    public boolean isMoving() { return mover.isMoving(); }
    
    public boolean isStartOnReset() { return startOnReset; }
    
    public void setStartOnReset(boolean b) { startOnReset = b; }
    
    public void start() {
        mover.moveTo(getLocation(), speed.generate());
    }
    
    public void stop() { mover.stop(); }
    
    public void setLocationGenerator(RandomVariate[] rv) {
        if (rv == null) {
            throw new NullPointerException("RandomVariate array is null");
        }
        else if (rv.length < 2) {
            throw new IllegalArgumentException("need array of at least length 2: " + rv.length); 
        }
        else if (rv[0] == null || rv[1] == null) {
            throw new NullPointerException("Null RandomVariate in array");
        }
        location = new RandomVariate[] { rv[0], rv[1] };
    }
    
    public void setSpeedGenerator(RandomVariate rv) { speed = rv; }
    
    protected Point2D getLocation() {
        return new Point2D.Double(location[0].generate(), location[1].generate());
    }
    
    public void setMover(Mover newMover) {
        if (mover != null) {
            mover.removeSimEventListener(this);
        }
        mover = newMover;
        mover.addSimEventListener(this);
    }
    
}
