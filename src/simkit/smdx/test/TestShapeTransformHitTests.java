package simkit.smdx.test;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;
import java.util.LinkedHashSet;
import java.util.Set;
import simkit.smdx.Mover;
import simkit.smdx.UniformLinearMover;

public class TestShapeTransformHitTests {

    Set<Mover> movers;
    Shape pie;

    public void makeMovers() {
        double[] pointGen = new double[]{-50.0, -25.0, 0.0, 25.0, 50.0};
        movers = new LinkedHashSet();

        for (double x : pointGen) {
            for (double y : pointGen) {
                UniformLinearMover m = new UniformLinearMover(
                        new Point2D.Double(x, y), 0);
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

    public void reportContainment() {
        for (Mover m : movers) {
            boolean hit = testPoint(m);

            if (hit) {
                System.out.println("Map coord " + m.getLocation() +
                        " is in the transformed pie");
            }

//            if(pie.contains(m.getLocation())){
//                System.out.println("Map coord " + m.getLocation() +
//                        " is in the raw awt pie");
//            }
        }
    }

    public boolean testPoint(Mover m) {

        Shape   hitZone;
        Point2D target = m.getLocation();

        AffineTransform transform = new AffineTransform();
//        transform.concatenate(new AffineTransform(1.0, 0.0, 0.0, -1.0, 0.0, 0.0));
        transform.concatenate(new AffineTransform(0.0, 1.0, -1.0, 0.0, 0.0, 0.0));

        hitZone = transform.createTransformedShape(pie);
        return hitZone.contains(target);
    }

    public static void main(String[] args) {
        TestShapeTransformHitTests test = new TestShapeTransformHitTests();

        test.makeMovers();

        double openingSize = 15.0;
        String openingDesc = "Narrow";
        double radius = 50.0;

        System.out.println("\n" + openingDesc + " look NORTH");
        test.makePieWithMapValues(0.0, openingSize, radius);
        test.reportContainment();

        System.out.println("\n" + openingDesc + " look EAST");
        test.makePieWithMapValues(90.0, openingSize, radius);
        test.reportContainment();

        System.out.println("\n" + openingDesc + " look SOUTH");
        test.makePieWithMapValues(180.0, openingSize, radius);
        test.reportContainment();

        System.out.println("\n" + openingDesc + " look WEST");
        test.makePieWithMapValues(270.0, openingSize, radius);
        test.reportContainment();

        System.out.println("\n" + openingDesc + " look SOUTH EAST");
        test.makePieWithMapValues(135.0, openingSize, radius);
        test.reportContainment();

        System.out.println("\n" + openingDesc + " look NORTH EAST");
        test.makePieWithMapValues(45.0, openingSize, radius);
        test.reportContainment();

        System.out.println("\n" + openingDesc + " look NORTH WEST");
        test.makePieWithMapValues(315.0, openingSize, radius);
        test.reportContainment();

        System.out.println("\n" + openingDesc + " look SOUTH WEST");
        test.makePieWithMapValues(225.0, openingSize, radius);
        test.reportContainment();

        //
        openingSize = 180.0;
        openingDesc = "Half-Circle";

        System.out.println("\n" + openingDesc + " look NORTH");
        test.makePieWithMapValues(0.0, openingSize, radius);
        test.reportContainment();

        System.out.println("\n" + openingDesc + " look EAST");
        test.makePieWithMapValues(90.0, openingSize, radius);
        test.reportContainment();

        System.out.println("\n" + openingDesc + " look SOUTH");
        test.makePieWithMapValues(180.0, openingSize, radius);
        test.reportContainment();

        System.out.println("\n" + openingDesc + " look WEST");
        test.makePieWithMapValues(270.0, openingSize, radius);
        test.reportContainment();

        System.out.println("\n" + openingDesc + " look SOUTH EAST");
        test.makePieWithMapValues(135.0, openingSize, radius);
        test.reportContainment();

        System.out.println("\n" + openingDesc + " look NORTH EAST");
        test.makePieWithMapValues(45.0, openingSize, radius);
        test.reportContainment();

        System.out.println("\n" + openingDesc + " look NORTH WEST");
        test.makePieWithMapValues(315.0, openingSize, radius);
        test.reportContainment();

        System.out.println("\n" + openingDesc + " look SOUTH WEST");
        test.makePieWithMapValues(225.0, openingSize, radius);
        test.reportContainment();

    }
}
