package simkit.smdx.test;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import simkit.smdx.Math2D;
import simkit.smdx.Mover;
import simkit.smdx.UniformLinearMover;

public class TestShapeTransformIntersectionTests {

    public static final Point2D NORTH = new Point2D.Double(0.0, 1.0);
    public static final Point2D EAST = new Point2D.Double(1.0, 0.0);
    public static final Point2D SOUTH = new Point2D.Double(0.0, -1.0);
    public static final Point2D WEST = new Point2D.Double(-1.0, 0.0);

    public class ThisTestMover extends UniformLinearMover {
        public ThisTestMover(Point2D loc, double maxSpeed) {
            super(loc, maxSpeed);
        }
        public void setVelocity(Point2D vel) {
            this.velocity = vel;
        }
    }

    Set<ThisTestMover> movers;
    Shape pie;

    public void makeMovers() {
        double[] pointGen = new double[]{-50.0, -25.0, 0.0, 25.0, 50.0};
        movers = new LinkedHashSet();

        for (double x : pointGen) {
            for (double y : pointGen) {
                ThisTestMover m = new ThisTestMover(
                        new Point2D.Double(x, y), 1.0);
                movers.add(m);
            }
        }

//        System.out.println(Arrays.toString(pointGen));
//        System.out.println(movers);
    }

    public static double normalizeDegrees(double degrees) {
        while (360.0 <= degrees) {
            degrees -= 360.0;
        }
        while ( 0.0 > degrees) {
            degrees += 360.0;
        }
        return degrees;
    }

    public void makePieWithMapValues(double arcCentralBearingDegrees,
            double extentDegrees, double radius) {

        Arc2D fp = new Arc2D.Double(Arc2D.PIE);
        Point2D loc = new Point2D.Double(0.0, 0.0);
        double arcExtent = extentDegrees;

        // sign of angles is reversed
        double arcStartAngle = (arcCentralBearingDegrees - extentDegrees / 2.0);
        // ensure within compass headings
        arcStartAngle = normalizeDegrees(arcStartAngle);

        System.out.println("Pie is using starting angle " + arcStartAngle);

        fp.setArcByCenter(
                loc.getX(),
                loc.getY(),
                radius,
                arcStartAngle,
                arcExtent,
                Arc2D.PIE);

        pie = fp;
    }

    public void reportIntersections() {
        for (Mover m : movers) {
            Point2D loc = m.getLocation();
            Point2D vel = m.getVelocity();
            Point2D[] intersections;
            AffineTransform transform = new AffineTransform();
            transform.concatenate(new AffineTransform(0.0, 1.0, -1.0, 0.0, 0.0, 0.0));
            Shape hitZone = transform.createTransformedShape(pie);

            intersections = Math2D.findIntersection(loc, vel, hitZone);
            System.out.println("\n" + m);
            System.out.println(Arrays.toString(intersections));
        }
    }

    public boolean testPoint(Mover m) {

        Shape   hitZone;
        Point2D target = m.getLocation();

        AffineTransform transform = new AffineTransform();
        transform.concatenate(new AffineTransform(0.0, 1.0, -1.0, 0.0, 0.0, 0.0));
        hitZone = transform.createTransformedShape(pie);

        return hitZone.contains(target);
    }

    public void setMoverVelocities(Point2D vel) {
        for (ThisTestMover m : movers) {
            m.setVelocity(vel);
        }
    }

    public static void main(String[] args) {
        TestShapeTransformIntersectionTests test = new TestShapeTransformIntersectionTests();

        test.makeMovers();

        double openingSize = 15.0;
        String openingDesc = "Narrow";
        double radius = 10.0;

        System.out.println("\n" + openingDesc + " look NORTH");
        test.makePieWithMapValues(0.0, openingSize, radius);
        test.setMoverVelocities(SOUTH);
        test.reportIntersections();

        System.out.println("\n" + openingDesc + " look EAST");
        test.makePieWithMapValues(90.0, openingSize, radius);
        test.setMoverVelocities(WEST);
        test.reportIntersections();

        System.out.println("\n" + openingDesc + " look SOUTH");
        test.makePieWithMapValues(180.0, openingSize, radius);
        test.setMoverVelocities(NORTH);
        test.reportIntersections();

        System.out.println("\n" + openingDesc + " look WEST");
        test.makePieWithMapValues(270.0, openingSize, radius);
        test.setMoverVelocities(EAST);
        test.reportIntersections();
    }
}
