package simkit.smdx;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import junit.framework.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

public class Math2DTest extends TestCase {

    public static final double SMALL = 1.0E-9;
    public static final Logger log = Logger.getLogger("simkit.smdx");

    public void setUp() {
    }

    public void tearDown() {
    }

    /**
     * This test is to look for numerical instabilities that may be
     * causing DAFS bug 730.
     **/
    public void testBug730() {
//        System.out.println("Starting bug 730");
        Mover pursuer = new UniformLinearMover(new Point2D.Double(1.0E-9, 1.0E-9), 40000);
        Mover target = new UniformLinearMover(new Point2D.Double(0.0, 0.0), 35000);
        Point2D intercept = Math2D.getIntercept(pursuer, pursuer.getMaxSpeed(), 0.0, target);
//        log.info("intercept=" + intercept);
        assertEquals(Double.NEGATIVE_INFINITY, intercept.getX());
        assertEquals(Double.NEGATIVE_INFINITY, intercept.getY());
//        System.out.println("Finished bug 730");
    }

    public void testBug730a() {
//        System.out.println("Starting bug 730a");
        Mover pursuer = new UniformLinearMover(new Point2D.Double(26706.12518150173, 33110.032031361014), 40000.0);
        Mover target = new UniformLinearMover(new Point2D.Double(21731.08839436993, 36463.98625983303), 40000.0);
        assertNotNull(pursuer);
        assertNotNull(target);
        target.moveTo(new Point2D.Double(20324.44658389988, 37151.932931404095));
        target.doStartMove(target);
        Point2D intercept = Math2D.getIntercept(pursuer, pursuer.getMaxSpeed(), 0.95, target);
//        System.out.println("intercept=" + intercept);
        assertNull(intercept);
//          System.out.println("Finished bug 730a");
    }

    /**
     * This test automates the visual/manual test performed by 
     * {@link simkit.smdx.test.TestArcIntersection}.  That file still exists
     * in the repository in  the event this test fails -- it may be useful
     * for hunting down the problem.
     */
    public void testArcIntersection() {

        final double testTolerance = 1.0E-12;

        Shape[] arc;
        Line2D line[];
        ArrayList<Point2D> intersections;
        final int SIZE = 9;
        arc = new Shape[]{
                    new Arc2D.Double(50.0, 50.0, 300.0, 300.0, 30.0, 120.0, Arc2D.PIE),
                    null
                };
        AffineTransform trans = AffineTransform.getTranslateInstance(200, 200);
        Shape transArc = trans.createTransformedShape(arc[0]);
        Area area = new Area(transArc);
        Area obstacle = new Area(new Rectangle2D.Double(360, 230, 100, 70));

        area.subtract(obstacle);
        arc[ 1] = new GeneralPath(area);
        intersections = new ArrayList<Point2D>();

        line = new Line2D[]{
                    new Line2D.Double(80, 30, 300, 180),
                    new Line2D.Double(70, 160, 300, 140),
                    new Line2D.Double(40, 90, 225, 225),
                    new Line2D.Double(175, 30, 50, 330),
                    new Line2D.Double(275, 260, 530, 310)
                };
        for (int k = 0; k < arc.length; k++) {
            for (int i = 0; i < line.length; i++) {
                Point2D velocity = Math2D.subtract(line[i].getP2(), line[i].getP1());
                Point2D start = line[i].getP1();
                Point2D[] inter = Math2D.findIntersection(start, velocity, arc[k], null);
                for (int j = 0; j < inter.length; j++) {
                    double t = Math2D.innerProduct(velocity, Math2D.subtract(inter[j], start)) / Math2D.normSq(velocity);
//                    System.out.println("ORIG: " + t);
//                    System.out.println("ORIG: " + inter[j]);
                    if (t >= 0.0 && t <= 1.0) {
//                        System.out.println("ADDING: " + inter[j]);
                        intersections.add(inter[j]);
                    }
                }

            }
        }

        Line2D testLine = line[0];
        Shape testShape = arc[0];
        Point2D velocity = Math2D.subtract(testLine.getP2(), testLine.getP1());
        Point2D start = testLine.getP1();
        Point2D[] inter = Math2D.findIntersection(start, velocity, testShape, null);
        double t = Math2D.innerProduct(velocity, Math2D.subtract(inter[0], start)) / Math2D.normSq(velocity);

        assertTrue(t >= 0.0 && t <= 1.0);
        if (t >= 0.0 && t <= 1.0) {
//            System.out.println("TEST: " + inter[0]);
            assertEquals(132.71095620168882, inter[0].getX(), testTolerance);
            assertEquals(65.93928831933323, inter[0].getY(), testTolerance);
        }

        t = Math2D.innerProduct(velocity, Math2D.subtract(inter[1], start)) / Math2D.normSq(velocity);
        assertTrue(t >= 0.0 && t <= 1.0);
        if (t >= 0.0 && t <= 1.0) {
//            System.out.println("TEST: " + inter[1]);
            assertEquals(270.0317881306823, inter[1].getX(), testTolerance);
            assertEquals(159.56712827091974, inter[1].getY(), testTolerance);
        }

        testLine = line[1];

        velocity = Math2D.subtract(testLine.getP2(), testLine.getP1());
        start = testLine.getP1();
        inter = Math2D.findIntersection(start, velocity, testShape, null);
        t = Math2D.innerProduct(velocity, Math2D.subtract(inter[0], start)) / Math2D.normSq(velocity);

        assertTrue(t >= 0.0 && t <= 1.0);
        if (t >= 0.0 && t <= 1.0) {
//            System.out.println("TEST: " + inter[0]);
            assertEquals(122.77009880576537, inter[0].getX(), testTolerance);
            assertEquals(155.4112957560204, inter[0].getY(), testTolerance);
        }

        t = Math2D.innerProduct(velocity, Math2D.subtract(inter[1], start)) / Math2D.normSq(velocity);
        //System.out.println(t);
        assertTrue(t < 0.0 || t > 1.0);

        testLine = line[2];

        velocity = Math2D.subtract(testLine.getP2(), testLine.getP1());
        start = testLine.getP1();
        inter = Math2D.findIntersection(start, velocity, testShape, null);
        t = Math2D.innerProduct(velocity, Math2D.subtract(inter[0], start)) / Math2D.normSq(velocity);

        assertTrue(t >= 0.0 && t <= 1.0);
        if (t >= 0.0 && t <= 1.0) {
//            System.out.println("TEST: " + inter[0]);
            assertEquals(75.69335417761167, inter[0].getX(), testTolerance);
            assertEquals(116.04650169717603, inter[0].getY(), testTolerance);
        }

        t = Math2D.innerProduct(velocity, Math2D.subtract(inter[1], start)) / Math2D.normSq(velocity);
        assertTrue(t >= 0.0 && t <= 1.0);
        if (t >= 0.0 && t <= 1.0) {
//            System.out.println("TEST: " + inter[1]);
            assertEquals(155.6583496699117, inter[1].getX(), testTolerance);
            assertEquals(174.39933624561124, inter[1].getY(), testTolerance);
        }

        testLine = line[3];

        velocity = Math2D.subtract(testLine.getP2(), testLine.getP1());
        start = testLine.getP1();
        inter = Math2D.findIntersection(start, velocity, testShape, null);
        t = Math2D.innerProduct(velocity, Math2D.subtract(inter[0], start)) / Math2D.normSq(velocity);

        assertTrue(t >= 0.0 && t <= 1.0);
        if (t >= 0.0 && t <= 1.0) {
//            System.out.println("TEST: " + inter[0]);
            assertEquals(164.93665415735938, inter[0].getX(), testTolerance);
            assertEquals(54.15203002233762, inter[0].getY(), testTolerance);
        }

        t = Math2D.innerProduct(velocity, Math2D.subtract(inter[1], start)) / Math2D.normSq(velocity);
        assertTrue(t >= 0.0 && t <= 1.0);
        if (t >= 0.0 && t <= 1.0) {
//            System.out.println("TEST: " + inter[1]);
            assertEquals(122.75010354673475, inter[1].getX(), testTolerance);
            assertEquals(155.39975148783657, inter[1].getY(), testTolerance);
        }

        testLine = line[4];

        velocity = Math2D.subtract(testLine.getP2(), testLine.getP1());
        start = testLine.getP1();
        inter = Math2D.findIntersection(start, velocity, testShape, null);
        assertTrue(inter.length == 0);

        testLine = line[0];
        testShape = arc[1];
        velocity = Math2D.subtract(testLine.getP2(), testLine.getP1());
        start = testLine.getP1();
        inter = Math2D.findIntersection(start, velocity, testShape, null);
        t = Math2D.innerProduct(velocity, Math2D.subtract(inter[0], start)) / Math2D.normSq(velocity);
        assertTrue(t < 0.0 || t > 1.0);

        t = Math2D.innerProduct(velocity, Math2D.subtract(inter[1], start)) / Math2D.normSq(velocity);
        assertTrue(t < 0.0 || t > 1.0);

        

        testLine = line[1];
        velocity = Math2D.subtract(testLine.getP2(), testLine.getP1());
        start = testLine.getP1();
        inter = Math2D.findIntersection(start, velocity, testShape, null);
//        System.out.println(Arrays.toString(inter));
        assertTrue(inter.length == 0);

        testLine = line[2];
        velocity = Math2D.subtract(testLine.getP2(), testLine.getP1());
        start = testLine.getP1();
        inter = Math2D.findIntersection(start, velocity, testShape, null);
        t = Math2D.innerProduct(velocity, Math2D.subtract(inter[0], start)) / Math2D.normSq(velocity);
        assertTrue(t < 0.0 || t > 1.0);

        t = Math2D.innerProduct(velocity, Math2D.subtract(inter[1], start)) / Math2D.normSq(velocity);
        assertTrue(t < 0.0 || t > 1.0);

        testLine = line[3];
        velocity = Math2D.subtract(testLine.getP2(), testLine.getP1());
        start = testLine.getP1();
        inter = Math2D.findIntersection(start, velocity, testShape, null);
        assertTrue(inter.length == 0);

        testLine = line[4];
        velocity = Math2D.subtract(testLine.getP2(), testLine.getP1());
        start = testLine.getP1();
        inter = Math2D.findIntersection(start, velocity, testShape, null);
        t = Math2D.innerProduct(velocity, Math2D.subtract(inter[0], start)) / Math2D.normSq(velocity);
        if (t >= 0.0 && t <= 1.0) {
//            System.out.println("TEST: " + inter[0]);
            assertEquals(325.3788369928872, inter[0].getX(), testTolerance);
            assertEquals(269.8782033319386, inter[0].getY(), testTolerance);
        }

        t = Math2D.innerProduct(velocity, Math2D.subtract(inter[1], start)) / Math2D.normSq(velocity);
        if (t >= 0.0 && t <= 1.0) {
//            System.out.println("TEST: " + inter[1]);
            assertEquals(518.238726318594, inter[1].getX(), testTolerance);
            assertEquals(307.6938679056068, inter[1].getY(), testTolerance);
        }
        t = Math2D.innerProduct(velocity, Math2D.subtract(inter[2], start)) / Math2D.normSq(velocity);
        if (t >= 0.0 && t <= 1.0) {
//            System.out.println("TEST: " + inter[2]);
            assertEquals(460.0, inter[2].getX(), testTolerance);
            assertEquals(296.27450980392155, inter[2].getY(), testTolerance);
        }
        t = Math2D.innerProduct(velocity, Math2D.subtract(inter[3], start)) / Math2D.normSq(velocity);
        if (t >= 0.0 && t <= 1.0) {
//            System.out.println("TEST: " + inter[3]);
            assertEquals(360.0, inter[3].getX(), testTolerance);
            assertEquals(276.6666666666667, inter[3].getY(), testTolerance);
        }
    }

    public void testBug1413_east() {

        Shape fp = new Arc2D.Double(Arc2D.PIE);
        Point2D loc = new Point2D.Double(0.0, 0.0);
        double radius = 25.0;
        double arcStartAwtAngle = 270.0;
        double arcExtent = 180.0;

        ((Arc2D)fp).setArcByCenter(
                loc.getX(),
                loc.getY(),
                radius,
                arcStartAwtAngle,
                arcExtent,
                Arc2D.PIE);

//        System.out.println("Arc startpoint in AWT space is " + ((Arc2D)fp).getStartPoint());
//        System.out.println("Arc endpoint in AWT space is " + ((Arc2D)fp).getEndPoint());

        Point2D targetStart = new Point2D.Double(100.0, 0.0);
        Point2D targetVelocity = new Point2D.Double(-1.0, 0.0);

        Point2D[] intersections =
                Math2D.findIntersection(targetStart, targetVelocity, fp);

        System.out.println(Arrays.toString(intersections));

        assertEquals(2, intersections.length);
        assertEquals(25.0, intersections[0].getX(),1E-6);
        assertEquals(0.0, intersections[0].getY(),1E-6);
        assertEquals(0.0, intersections[1].getX(),1E-6);
        assertEquals(0.0, intersections[1].getY(),1E-6);
    }

    public void testBug1413_south() {

        Shape fp = new Arc2D.Double(Arc2D.PIE);
        Point2D loc = new Point2D.Double(0.0, 0.0);
        double radius = 25.0;
        double arcStartAwtAngle = 180.0;
        double arcExtent = 180.0;

        ((Arc2D)fp).setArcByCenter(
                loc.getX(),
                loc.getY(),
                radius,
                arcStartAwtAngle,
                arcExtent,
                Arc2D.PIE);

//        System.out.println("Arc startpoint in AWT space is " + ((Arc2D)fp).getStartPoint());
//        System.out.println("Arc endpoint in AWT space is " + ((Arc2D)fp).getEndPoint());
//        System.out.println("Arc bounding rectangle in AWT space is " + fp.getBounds2D());

        Point2D targetStart = new Point2D.Double(0.0, 100.0);
        Point2D targetVelocity = new Point2D.Double(0.0, -1.0);

        Point2D[] intersections =
                Math2D.findIntersection(targetStart, targetVelocity, fp);

//        System.out.println(Arrays.toString(intersections));

        assertEquals(2, intersections.length);
        assertEquals(0.0, intersections[0].getX(),1E-6);
        assertEquals(25.0, intersections[0].getY(),1E-6);
        assertEquals(0.0, intersections[1].getX(),1E-6);
        assertEquals(0.0, intersections[1].getY(),1E-6);
    }

    public void testBug1413_west() {

        Shape fp = new Arc2D.Double(Arc2D.PIE);
        Point2D loc = new Point2D.Double(0.0, 0.0);
        double radius = 25.0;
        double arcStartAwtAngle = 90.0;
        double arcExtent = 180.0;

        ((Arc2D)fp).setArcByCenter(
                loc.getX(),
                loc.getY(),
                radius,
                arcStartAwtAngle,
                arcExtent,
                Arc2D.PIE);

//        System.out.println("Arc startpoint in AWT space is " + ((Arc2D)fp).getStartPoint());
//        System.out.println("Arc endpoint in AWT space is " + ((Arc2D)fp).getEndPoint());
//        System.out.println("Arc bounding rectangle in AWT space is " + fp.getBounds2D());

        Point2D targetStart = new Point2D.Double(-100.0, 0.0);
        Point2D targetVelocity = new Point2D.Double(1.0, 0.0);

        Point2D[] intersections =
                Math2D.findIntersection(targetStart, targetVelocity, fp);

//        System.out.println(Arrays.toString(intersections));

        assertEquals(2, intersections.length);
        assertEquals(-25.0, intersections[0].getX(),1E-6);
        assertEquals(0.0, intersections[0].getY(),1E-6);
        assertEquals(0.0, intersections[1].getX(),1E-6);
        assertEquals(0.0, intersections[1].getY(),1E-6);
    }

    public void testBug1413_north() {

        Shape fp = new Arc2D.Double(Arc2D.PIE);
        Point2D loc = new Point2D.Double(0.0, 0.0);
        double radius = 25.0;
        double arcStartAwtAngle = 0.0;
        double arcExtent = 180.0;

        ((Arc2D)fp).setArcByCenter(
                loc.getX(),
                loc.getY(),
                radius,
                arcStartAwtAngle,
                arcExtent,
                Arc2D.PIE);

//        System.out.println("Arc startpoint in AWT space is " + ((Arc2D)fp).getStartPoint());
//        System.out.println("Arc endpoint in AWT space is " + ((Arc2D)fp).getEndPoint());
//        System.out.println("Arc bounding rectangle in AWT space is " + fp.getBounds2D());

        Point2D targetStart = new Point2D.Double(0.0, -100.0);
        Point2D targetVelocity = new Point2D.Double(0.0, 1.0);

        Point2D[] intersections =
                Math2D.findIntersection(targetStart, targetVelocity, fp);

//        System.out.println(Arrays.toString(intersections));

        assertEquals(2, intersections.length);
        assertEquals(0.0, intersections[0].getX(),1E-6);
        assertEquals(-25.0, intersections[0].getY(),1E-6);
        assertEquals(0.0, intersections[1].getX(),1E-6);
        assertEquals(0.0, intersections[1].getY(),1E-6);
    }

    /**
     * verifies java.awt.geom.CubicCurve2D hasn't changed since we coded
     * to it.
     */
    public void testCubic() {
        double[] coeff = new double[4];
        coeff[0] = -1.0;
        coeff[1] = 1.0;
        coeff[2] = -1.0;
        coeff[3] = 1.0;

        double[] sol = new double[3];

        int num = CubicCurve2D.solveCubic(coeff, sol);

        assertEquals(1, num);
        assertEquals(1.0, sol[0], 1E-9);
        assertEquals(0.0, sol[1], 1E-9);
        assertEquals(0.0, sol[2], 1E-9);

        coeff = new double[]{1.0, 3.0, 3.0, 1.0};
        sol = new double[3];
        num = CubicCurve2D.solveCubic(coeff, sol);
        assertEquals(1, num);
        assertEquals(-1.0, sol[0], 1E-9);
        assertEquals(0.0, sol[1], 1E-9);
        assertEquals(0.0, sol[2], 1E-9);

        coeff = new double[]{2.0, -1.0, -2.0, 1.0};
        sol = new double[3];
        num = CubicCurve2D.solveCubic(coeff, sol);
        assertEquals(3, num);
        assertEquals(-1.0, sol[0], 1E-9);
        assertEquals(2.0, sol[1], 1E-9);
        assertEquals(1.0, sol[2], 1E-9);

    }
    
    public void testSmallestPositive() {

        double[] ta;
        
        // simple
        assertEquals(0.8, simkit.smdx.Math2D.smallestPositive(
                new double[]{1.0, 0.9, 0.8}));
        assertEquals(0.8, simkit.smdx.Math2D.smallestPositive(
                new double[]{-1.0, 0.9, 0.8}));
        
        // smallest in the first n
        ta = new double[]{1.0, 0.0, 0.0};
        
        assertEquals(1.0, simkit.smdx.Math2D.smallestPositive(ta, 1));
        assertEquals(1.0, simkit.smdx.Math2D.smallestPositive(ta, 2));
        assertEquals(1.0, simkit.smdx.Math2D.smallestPositive(ta, 3));

        ta = new double[]{1.0, 0.9, 0.8};

        assertEquals(1.0, simkit.smdx.Math2D.smallestPositive(ta, 1));
        assertEquals(0.9, simkit.smdx.Math2D.smallestPositive(ta, 2));
        assertEquals(0.8, simkit.smdx.Math2D.smallestPositive(ta, 3));

        ta = new double[]{-1.0, 0.9, 0.8};
        
        assertEquals(Double.POSITIVE_INFINITY, simkit.smdx.Math2D.smallestPositive(ta, 1));
        assertEquals(0.9, simkit.smdx.Math2D.smallestPositive(ta, 2));
        assertEquals(0.8, simkit.smdx.Math2D.smallestPositive(ta, 3));

        ta = new double[]{1.0, 2.0, 3.0};
        // smallest larger than x
        assertEquals(3.0, simkit.smdx.Math2D.smallestPositive(ta, 2.0));
        
        // smallest in the first n that are at least as large as x
        assertEquals(Double.POSITIVE_INFINITY, simkit.smdx.Math2D.smallestPositive(ta, 1, 2.0));
        assertEquals(Double.POSITIVE_INFINITY, simkit.smdx.Math2D.smallestPositive(ta, 2, 2.0));
        assertEquals(3.0, simkit.smdx.Math2D.smallestPositive(ta, 3, 2.0));
        
    }
    
    class UnitTestingMover extends UniformLinearMover {
        
        public UnitTestingMover(String name, Point2D location, double maxSpeed) {
            super(name, location, maxSpeed);
        }
        
        public void setLastStopLocation_TestHook(Point2D loc){
            this.lastStopLocation = loc;
        }
        
        public void setVelocity_TestHook(Point2D vel) {
            this.velocity = vel;
        }
        
    }
    
    /**
     * Replaces {@code simkit.smdx.test.TestIntercept}.  Needs
     * improved coverage of corner cases.
     */
    public void testIntercept() {
        UnitTestingMover pursuer;
        UnitTestingMover target;
        Point2D computedIntercept;
        Point2D testIntercept;
        double tolerance = 1.0E-6;
        
        // stationary target at origin
        
        target = new UnitTestingMover("Target", new Point2D.Double(0.0, 0.0), 10.0);
        target.setVelocity_TestHook(new Point2D.Double(0.0, 0.0));
        
        // pursuer also at origin
        pursuer = new UnitTestingMover("Pursuer", new Point2D.Double(-0.0, 0.0), 10.0);
        testIntercept = new Point2D.Double(0.0, 0.0);

        computedIntercept = Math2D.getIntercept(pursuer, target);
        assertEquals(testIntercept, computedIntercept);
        
        // stationary target at origin
        // approach along axis from the left
        pursuer = new UnitTestingMover("Pursuer", new Point2D.Double(-50.0, 0.0), 10.0);
        testIntercept = new Point2D.Double(0.0, 0.0);

        computedIntercept = Math2D.getIntercept(pursuer, target);
        assertEquals(testIntercept, computedIntercept);
        
        computedIntercept = Math2D.getIntercept(pursuer, 1.0, target);
        assertEquals(testIntercept, computedIntercept);
        
        computedIntercept = Math2D.getIntercept(pursuer, 1.0, 0.0, target);
        assertEquals(testIntercept, computedIntercept);
        
        testIntercept = new Point2D.Double(-1.0, 0.0);
        computedIntercept = Math2D.getIntercept(pursuer, 1.0, 1.0, target);
        assertEquals(testIntercept, computedIntercept);

        // stationary target at origin
        // approach along axis from the right
        pursuer = new UnitTestingMover("Pursuer", new Point2D.Double(50.0, 0.0), 10.0);
        testIntercept = new Point2D.Double(0.0, 0.0);

        computedIntercept = Math2D.getIntercept(pursuer, target);
        assertEquals(testIntercept, computedIntercept);
        
        computedIntercept = Math2D.getIntercept(pursuer, 1.0, target);
        assertEquals(testIntercept, computedIntercept);
        
        computedIntercept = Math2D.getIntercept(pursuer, 1.0, 0.0, target);
        assertEquals(testIntercept, computedIntercept);
        
        testIntercept = new Point2D.Double(1.0, 0.0);
        computedIntercept = Math2D.getIntercept(pursuer, 1.0, 1.0, target);
        assertEquals(testIntercept, computedIntercept);

        // stationary target at origin
        // approach along axis from above
        pursuer = new UnitTestingMover("Pursuer", new Point2D.Double(0.0, 50.0), 10.0);
        testIntercept = new Point2D.Double(0.0, 0.0);

        computedIntercept = Math2D.getIntercept(pursuer, target);
        assertEquals(testIntercept, computedIntercept);
        
        computedIntercept = Math2D.getIntercept(pursuer, 1.0, target);
        assertEquals(testIntercept, computedIntercept);
        
        computedIntercept = Math2D.getIntercept(pursuer, 1.0, 0.0, target);
        assertEquals(testIntercept, computedIntercept);
        
        testIntercept = new Point2D.Double(0.0, 1.0);
        computedIntercept = Math2D.getIntercept(pursuer, 1.0, 1.0, target);
        assertEquals(testIntercept, computedIntercept);

        // stationary target at origin
        // approach along axis from below
        pursuer = new UnitTestingMover("Pursuer", new Point2D.Double(0.0, -50.0), 10.0);
        testIntercept = new Point2D.Double(0.0, 0.0);

        computedIntercept = Math2D.getIntercept(pursuer, target);
        assertEquals(testIntercept, computedIntercept);
        
        computedIntercept = Math2D.getIntercept(pursuer, 1.0, target);
        assertEquals(testIntercept, computedIntercept);
        
        computedIntercept = Math2D.getIntercept(pursuer, 1.0, 0.0, target);
        assertEquals(testIntercept, computedIntercept);
        
        testIntercept = new Point2D.Double(0.0, -1.0);
        computedIntercept = Math2D.getIntercept(pursuer, 1.0, 1.0, target);
        assertEquals(testIntercept, computedIntercept);

        //===
        // half-speed target at origin moving to the right
        
        target = new UnitTestingMover("Target", new Point2D.Double(0.0, 0.0), 1.0);
        target.setVelocity_TestHook(new Point2D.Double(1.0, 0.0));
        
        // half-speed target at origin moving to the right (fleeing)
        // approach along axis from the left
        pursuer = new UnitTestingMover("Pursuer", new Point2D.Double(-50.0, 0.0), 2.0);
        testIntercept = new Point2D.Double(50.0, 0.0);

        computedIntercept = Math2D.getIntercept(pursuer, target);
        assertEquals(testIntercept, computedIntercept);
        
        computedIntercept = Math2D.getIntercept(pursuer, 2.0, target);
        assertEquals(testIntercept, computedIntercept);
        
        computedIntercept = Math2D.getIntercept(pursuer, 2.0, 0.0, target);
        assertEquals(testIntercept, computedIntercept);
        
        testIntercept = new Point2D.Double(48.0, 0.0);
        computedIntercept = Math2D.getIntercept(pursuer, 2.0, 1.0, target);
        assertEquals(testIntercept, computedIntercept);

        // half-speed target at origin moving to the right (approaching)
        // approach along axis from the right (head-on)
        pursuer = new UnitTestingMover("Pursuer", new Point2D.Double(300.0, 0.0), 2.0);
        testIntercept = new Point2D.Double(100.0, 0.0);

        computedIntercept = Math2D.getIntercept(pursuer, target);
        assertEquals(testIntercept, computedIntercept);
        
        computedIntercept = Math2D.getIntercept(pursuer, 2.0, target);
        assertEquals(testIntercept, computedIntercept);
        
        computedIntercept = Math2D.getIntercept(pursuer, 2.0, 0.0, target);
        assertEquals(testIntercept, computedIntercept);
        
        testIntercept = new Point2D.Double(102.0, 0.0);
        computedIntercept = Math2D.getIntercept(pursuer, 2.0, 3.0, target);
        assertEquals(testIntercept, computedIntercept);
        
        // equal-speed target at origin moving to the right (approaching)
        // approach along axis from the right (head-on)
        
        target = new UnitTestingMover("Target", new Point2D.Double(0.0, 0.0), 2.0);
        target.setVelocity_TestHook(new Point2D.Double(2.0, 0.0));
        testIntercept = new Point2D.Double(150.0, 0.0);

        computedIntercept = Math2D.getIntercept(pursuer, target);
        assertEquals(testIntercept, computedIntercept);
        
        computedIntercept = Math2D.getIntercept(pursuer, 2.0, target);
        assertEquals(testIntercept, computedIntercept);
        
        computedIntercept = Math2D.getIntercept(pursuer, 2.0, 0.0, target);
        assertEquals(testIntercept, computedIntercept);
        
        testIntercept = new Point2D.Double(151.0, 0.0);
        computedIntercept = Math2D.getIntercept(pursuer, 2.0, 2.0, target);
        assertEquals(testIntercept, computedIntercept);
        
        // equal speed fleeing target (no solution situation)
        target = new UnitTestingMover("Target", new Point2D.Double(0.0, 0.0), 2.0);
        target.setVelocity_TestHook(new Point2D.Double(2.0, 0.0));

        pursuer = new UnitTestingMover("Pursuer", new Point2D.Double(-50.0, 0.0), 2.0);

        computedIntercept = Math2D.getIntercept(pursuer, target);
//        System.out.println(computedIntercept);
        assertNull(computedIntercept);
        
        computedIntercept = Math2D.getIntercept(pursuer, 2.0, target);
        assertNull(computedIntercept);
        
        computedIntercept = Math2D.getIntercept(pursuer, 2.0, 0.0, target);
        assertNull(computedIntercept);
        
        testIntercept = new Point2D.Double(48.0, 0.0);
        computedIntercept = Math2D.getIntercept(pursuer, 2.0, 1.0, target);
        assertNull(computedIntercept);
        
        // faster speed fleeing target (no solution situation)
        target = new UnitTestingMover("Target", new Point2D.Double(0.0, 0.0), 2.0);
        target.setVelocity_TestHook(new Point2D.Double(2.0, 0.0));

        pursuer = new UnitTestingMover("Target", new Point2D.Double(-1.0, 0.0), 1.0);

        computedIntercept = Math2D.getIntercept(pursuer, target);
        assertNull(computedIntercept);
        
        computedIntercept = Math2D.getIntercept(pursuer, 1.0, target);
        assertNull(computedIntercept);
        
        computedIntercept = Math2D.getIntercept(pursuer, 1.0, 0.0, target);
        assertNull(computedIntercept);
        
        testIntercept = new Point2D.Double(-1.0, 0.0);
        computedIntercept = Math2D.getIntercept(pursuer, 1.0, 1.0, target);
        assertEquals(testIntercept, computedIntercept);

        computedIntercept = Math2D.getIntercept(pursuer, 1.0, 0.5, target);
        assertNull(computedIntercept);
    }
    
    /**
     * Adapted from {@code simkit.smdx.TestIntersectionTime}
     * by Arnie Buss, which this test replaces.
     */
    public void testIntersectionTime() {
        double tolerance = 1.0E-6;
        
        Point2D start = new Point2D.Double(20.0, 30.0);
        Point2D velocity = new Point2D.Double(40.0, 50.0);
        Line2D line = new Line2D.Double(0.0, 100.0, 80.0, 0.0);
        double maxSpeed = 100.0;
        
        Point2D[] intersect = Math2D.findIntersection(start, velocity, line);
        double[] time = Math2D.findIntersectionTime(start, velocity, line);

        assertEquals(38.0, intersect[0].getX(), tolerance);
        assertEquals(52.5, intersect[0].getY(), tolerance);
        assertEquals(0.45, time[0], tolerance);
        
        Point2D destination = new Point2D.Double(-20.0, -50.0);
        Point2D velTemp = Math2D.subtract(destination, start);
        velocity = Math2D.scalarMultiply( maxSpeed / Math2D.norm(velTemp), velTemp );
        intersect = Math2D.findIntersection(start, velocity, line);
        time = Math2D.findIntersectionTime(start, velocity, line);
        assertEquals(0, intersect.length);
        
        Shape cookie = new Ellipse2D.Double(0.0, -20.0, 40.0, 40.0);
        intersect = Math2D.findIntersection(start, velocity, cookie);
        time = Math2D.findIntersectionTime(start, velocity, cookie);

        assertEquals(14.6332495807108, intersect[0].getX(), tolerance);
        assertEquals(19.2664991614216, intersect[0].getY(), tolerance);
        assertEquals(0.1200041875580615, time[0], tolerance);

        assertEquals(1.3667504192892004, intersect[1].getX(), tolerance);
        assertEquals(-7.266499161421599, intersect[1].getY(), tolerance);
        assertEquals(0.416652127041888, time[1], tolerance);

    }

    public void testBearingFromPointToPoint() {
        Point2D from = new Point2D.Double(2.0, 2.0);
        Point2D to = null;
        double angle = 0.0;
        double eps = 1E-9;
        // NORTH
        to = new Point2D.Double(2.0, 6.0);
        angle = 0.0;
        assertEquals(angle, Math2D.bearingFrom(from, to), eps);
        // EAST
        to = new Point2D.Double(6.0, 2.0);
        angle = 90.0;
        assertEquals(angle, Math2D.bearingFrom(from, to), eps);
        // SOUTH
        to = new Point2D.Double(2.0, -6.0);
        angle = 180.0;
        assertEquals(angle, Math2D.bearingFrom(from, to), eps);
        // WEST
        to = new Point2D.Double(-2.0, 2.0);
        angle = 270.0;
        assertEquals(angle, Math2D.bearingFrom(from, to), eps);
        // NORTHEAST
        to = new Point2D.Double(4.0, 4.0);
        angle = 45.0;
        assertEquals(angle, Math2D.bearingFrom(from, to), eps);
        // SOUTHEAST
        to = new Point2D.Double(4.0, 0.0);
        angle = 135.0;
        assertEquals(angle, Math2D.bearingFrom(from, to), eps);
        // NORTHWEST
        to = new Point2D.Double(0.0, 4.0);
        angle = 315.0;
        assertEquals(angle, Math2D.bearingFrom(from, to), eps);
        // SOUTHWEST
        to = new Point2D.Double(0.0, 0.0);
        angle = 225.0;
        assertEquals(angle, Math2D.bearingFrom(from, to), eps);
    }
}    

