package simkit.random;
import java.awt.geom.Point2D;
/**
 * Generates a random Point2D as prescribed by a pair of RandomVariates.
 * @author  Arnold Buss
 * @version $Id$
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
* @param cornerPoints A two element array. The first point is the minimum x and y;
* the second point is the maximum x and y.
**/
    public RandomPointGenerator(Point2D[] cornerPoints) {
        setCornerPoints(cornerPoints);
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
        setCorners(corners);
    }
    
    /**
     * Sets both supporting RandomNumbers to the same instance.
     * @param seed new seed for the supporting RandomNumber
     */
    public void setSeed(long seed) {
        rv[0].getRandomNumber().setSeed(seed);
        rv[1].setRandomNumber(rv[0].getRandomNumber());
    }
    
/** 
 * Creates a new instance of RandomPointGenerator with the given RandomVariates.
 * @param rv A 2 element array containing the RandomVariate for x and y.
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
     * 
     * @return a copy of a two element array containing the x and y RandomVariates.
     */
    public RandomVariate[] getRandomVariate() { 
        return rv.clone(); 
    }
    
/**
 * Generates the next point.
 * @return randomly generated Point2D
 */
    public Point2D generatePoint() {
        return new Point2D.Double(rv[0].generate(), rv[1].generate());
    }
    
    /**
     * @param corners 2-dimentional array specifying corners of region
     * @throws IllegalArgumentException If the array is not exactly 2 elements.
     */
    public void setCornerPoints(Point2D[] corners) {
        if (rv.length != 2) {
            throw new IllegalArgumentException("Need array of length 2: " + 
                    corners.length);
        }
        setCorners(new double[] {corners[0].getX(), corners[0].getY(),
            corners[1].getX(), corners[1].getY()});
    }
    
    /**
     * Values are {minX, minY, maxX, maxY}
     * @param corners 4-dimensional array specifying corners
     * @throws IllegalArgumentException If the array is not exactly 4 elements.
     */
    public void setCorners(double[] corners) {
        if (rv.length != 4) {
            throw new IllegalArgumentException("Need array of length 4: " + 
                    corners.length);
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
* @return a String with the name of this RandomVariate and information
* about the x and y RandomVariate.
*/
    public String toString() {
        return "RandomPointGenerator " + rv[0] + " - " + rv[1];
    }
}