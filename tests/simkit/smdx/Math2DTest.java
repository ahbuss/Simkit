
package simkit.smdx;

import junit.framework.*;

import java.util.logging.*;
import java.awt.geom.*;
public class Math2DTest extends TestCase {

    public static final double SMALL = 1.0E-9;

    public static Logger log = Logger.getLogger("simkit.smdx");
    
    public void setUp() {
    }

    public void tearDown() {
    }

/**
* This test is to look for numerical instabilities that may be
* causing DAFS bug 730.
**/
    public void testBug730() {
        log.info("Starting bug 730");
        Mover pursuer = new UniformLinearMover(new Point2D.Double(1.0E-9, 1.0E-9), 40000);
        Mover target = new UniformLinearMover(new Point2D.Double(0.0, 0.0), 35000);
        Point2D intercept = Math2D.getIntercept(pursuer, pursuer.getMaxSpeed(), 0.0, target);
        log.info("intercept=" + intercept);
        log.info("Finished bug 730");
    }

    public void testBug730a() {
        log.info("Starting bug 730a");
        Mover pursuer = new UniformLinearMover(new Point2D.Double(26706.12518150173, 33110.032031361014), 40000.0);
        Mover target = new UniformLinearMover(new Point2D.Double(21731.08839436993, 36463.98625983303), 40000.0);
        target.moveTo(new Point2D.Double(20324.44658389988, 37151.932931404095));
        Point2D intercept = Math2D.getIntercept(pursuer, pursuer.getMaxSpeed(), 0.95, target);
        log.info("intercept=" + intercept);
        log.info("Finished bug 730a");
    }
}
