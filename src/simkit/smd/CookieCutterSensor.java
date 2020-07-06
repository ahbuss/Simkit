package simkit.smd;

/**
 * Simplest sensor type. This simply relies on the functionality of BasicSensor.
 *
 * @author ahbuss
 */
public class CookieCutterSensor extends BasicSensor {

    public CookieCutterSensor() {
    }

    /**
     * Instantiate a CookieCutterSensor on the Mover with the given maxRange.
     *
     * @param mover The Mover this Sensor is on.
     * @param maxRange Maximum range.
     */
    public CookieCutterSensor(Mover mover, double maxRange) {
        super(mover, maxRange);
    }

    /**
     * Replace "BasicSensor" with "CookieCutterSensor"
     *
     * @return String description
     */
    @Override
    public String toString() {
        return super.toString().replaceFirst("BasicSensor", "CookieCutterSensor");
    }

}
