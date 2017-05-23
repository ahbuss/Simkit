package simkit.smd;

/**
 * Holds parameter of constant delay from when the sensor's range is
 * entered (EnterRange) and when the target is detected (timeToDetect).
 *
 * @version $Id: ConstantTimeSensor.java 81 2009-11-16 22:28:39Z ahbuss $
 * @author ahbuss
 */
public class ConstantTimeSensor extends BasicSensor {

    private double timeToDetect;

    public ConstantTimeSensor() { }
    
    public ConstantTimeSensor(Mover mover, double maxRange, double timeToDetect) {
        super(mover, maxRange);
        this.setTimeToDetect(timeToDetect);
    }

    /**
     * @return the timeToDetect
     */
    public double getTimeToDetect() {
        return timeToDetect;
    }

    /**
     * @param timeToDetect the timeToDetect to set
     */
    public void setTimeToDetect(double timeToDetect) {
        this.timeToDetect = timeToDetect;
    }

    @Override
    public String toString() {
        return super.toString().replaceFirst("BasicSensor", "ConstantTimeSensor") +
                " " + getTimeToDetect();
    }
}
