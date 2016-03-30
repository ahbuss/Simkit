package simkit.smd;

/**
 * Simplest sensor type. This simply relies on the functionality
 * of BasicSensor.
 * @version $Id: CookieCutterSensor.java 81 2009-11-16 22:28:39Z ahbuss $
 * @author ahbuss
 */
public class CookieCutterSensor extends BasicSensor {

    /**
     * Instantiate a CookieCutterSensor on the Mover with the
     * given maxRange.
     * @param mover The Mover this Sensor is on.
     * @param maxRange Maximum range.
     */
    public CookieCutterSensor(Mover mover, double maxRange) {
        super(mover, maxRange);
    }

    /**
     * Replace "BasicSensor" with "CookieCutterSensor"
     * @return String description
     */
    @Override
    public String toString() {
        return super.toString().replaceFirst("BasicSensor", "CookieCutterSensor");
    }

}
