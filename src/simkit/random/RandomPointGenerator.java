package simkit.random;
import simkit.random.*;
import java.awt.geom.*;
/**
 * Generates a random Point2D as prescribed by a pair of RandomVariates.
 * @author  Arnold Buss
 */
public class RandomPointGenerator {
    
    private RandomVariate[] rv;
    
    public RandomPointGenerator(Point2D[] cornerPts) {
        this(new double[] {cornerPts[0].getX(), cornerPts[0].getY(),
            cornerPts[1].getX(), cornerPts[1].getY()});
    }
    
    public RandomPointGenerator(double[] corners) {
        if (corners.length != 4) {
            throw new IllegalArgumentException("Need 4 corners: " + corners.length);
        }
        RandomVariate[] rand = new RandomVariate[2];
        rand[0] = RandomVariateFactory.getInstance("Uniform",
            new Object[] { new Double(corners[0]), new Double(corners[2]) });
        rand[1] = RandomVariateFactory.getInstance("Uniform",
            new Object[] { new Double(corners[1]), new Double(corners[3]) },
            rand[0].getRandomNumber());
        setRandomVariate(rand);
    }
    
    public void setSeed(long seed) {
        rv[0].getRandomNumber().setSeed(seed);
        rv[1].setRandomNumber(rv[0].getRandomNumber());
    }
    
    /** Creates a new instance of RandomPointGenerator */
    public RandomPointGenerator(RandomVariate[] rv) {
        setRandomVariate(rv);
    }
    
    public void setRandomVariate(RandomVariate[] rv) {
        if (rv.length != 2) {
            throw new IllegalArgumentException("Need array of length 2: " + rv.length);
        }
        this.rv = (RandomVariate[]) rv.clone();
    }
    
    public RandomVariate[] getRandomVariate() { return (RandomVariate[]) rv.clone(); }
    
    public Point2D generatePoint() {
        return new Point2D.Double(rv[0].generate(), rv[1].generate());
    }
    
    public String toString() {
        return "RandomPointGenerator " + rv[0] + " - " + rv[1];
    }
//    
//    public static void main(String[] args) {
//        double[] c = new double[] { 0, 0, 50, 250 };
//            
//        RandomPointGenerator rpg = new RandomPointGenerator(c);
//        
//        System.out.println(rpg);
//        for (int i = 0; i < 5; ++i) {
//            System.out.println(rpg.generatePoint());
//        }
//        
//        Point2D[] pts = new Point2D[2];
//        pts[0] = new Point2D.Double(0.0, 0.0);
//        pts[1] = new Point2D.Double(30.0, 350.0);
//        
//        rpg = new RandomPointGenerator(pts);
//        System.out.println(rpg);
//        for (int i = 0; i < 5; ++i) {
//            System.out.println(rpg.generatePoint());
//        }
//        
//    }
//    
}
