
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
        Mover pursuer = new UniformLinearMover(new Point2D.Double(100.0, 100.0), 1001);
        Mover target = new UniformLinearMover(new Point2D.Double(100.0 + SMALL, 100.0 + SMALL), 1000);
        Point2D intercept = Math2D.getIntercept(pursuer, 1001, 0.0, target);
        log.info("intercept=" + intercept);
        log.info("Finished bug 730");
    }
}
