package simkit.random;
import simkit.random.*;
import java.awt.geom.*;
/**
 * Generates a random Point2D as prescribed by a pair of RandomVariates.
 * @author  Arnold Buss
 */
public class RandomPointGenerator {
    
/**
* A 2 element array that holds the instance of the x and y RandomVariate.
**/
    private RandomVariate[] rv;
    
/**
* Creates a new RandomPointGenerator with the specified corner points.
* By default the x and y values will be uniformly distributed between
* the min and max x (or y) values of the 2 points.
* @param cornerPts A two element array. The first point is the minimum x and y;
* the second point is the maximum x and y.
**/
    public RandomPointGenerator(Point2D[] cornerPts) {
        this(new double[] {cornerPts[0].getX(), cornerPts[0].getY(),
            cornerPts[1].getX(), cornerPts[1].getY()});
    }
    
/**
* Creates a new RandomPointGenerator with the x and y points uniformly distributed
* between the minimum and maximum x and y specified.
* @param corners A four element array containing: the minimum x, the minimum y,
* the maximum x, and the maximum y.
* @throws IllegalArgumentException If the array does not have exactly 4 elements.
**/
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
    
/**
* Sets the same seed for both supporting RandomNumbers.
**/
    public void setSeed(long seed) {
        rv[0].getRandomNumber().setSeed(seed);
        rv[1].setRandomNumber(rv[0].getRandomNumber());
    }
    
/** 
* Creates a new instance of RandomPointGenerator with the given RandomVariates.
* @param rv A 2 element array containing the RandomVariate for x and y.
* @throws IllegalArgumentException If the array is not exactly 2 elements.
 */
    public RandomPointGenerator(RandomVariate[] rv) {
        setRandomVariate(rv);
    }
    
/**
* Sets the RandomVariates for this RandomPointGenerator.
* @param rv A 2 element array containing the RandomVariate for x and y.
* @throws IllegalArgumentException If the array is not exactly 2 elements.
**/
    public void setRandomVariate(RandomVariate[] rv) {
        if (rv.length != 2) {
            throw new IllegalArgumentException("Need array of length 2: " + rv.length);
        }
        this.rv = (RandomVariate[]) rv.clone();
    }
    
/**
* Returns a copy of a two element array containing the x and y RandomVariates.
**/
    public RandomVariate[] getRandomVariate() { return (RandomVariate[]) rv.clone(); }
    
/**
* Generates the next point.
**/
    public Point2D generatePoint() {
        return new Point2D.Double(rv[0].generate(), rv[1].generate());
    }
    
/**
* Returns a String with the name of this RandomVariate and information
* about the x and y RandomVariate.
**/
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
