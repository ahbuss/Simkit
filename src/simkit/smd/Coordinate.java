package simkit.smd;

/**
  *  A class for modeling a two-dimensional coordinate.  
  *  <P>
  *  The functionality is the ability to change the coordinates,
  *  to compute the distance between one Coordinate instance and another,
  *  add, and subtract Coordinates as vector.
  *
  * @author Arnold Buss
  * @version 0.2
**/

import java.text.*;
import java.util.*;

public class Coordinate implements java.io.Serializable {

// instance variables
   /**
     *  The current values of the coordinates
   **/
   private double xCoord;
   private double yCoord;
   private DecimalFormat form;
   
// constructors
   /**
     *  Default constructor is at the origin
   **/
   public Coordinate() {
      this(0.0, 0.0);
   }

   public Coordinate(String s) {
     StringTokenizer tokens = new StringTokenizer(s, " \t(),");
     if (tokens.countTokens() == 2) {
       setCoordinates(
          Double.valueOf(tokens.nextToken()).doubleValue(),
          Double.valueOf(tokens.nextToken()).doubleValue()
       );
     }
     else {
       throw new IllegalArgumentException("Non-coordinate String passed to Coordinate constructor:" +
                            s );
     }
     setFormat("0.00");
   }

   /**
     *  Construct coordinate at (x, y) <BR>
     *  @param double x, double y
   **/
     public Coordinate(double x, double y) {
        setCoordinates(x, y);
        setFormat("0.00");
   }
   
   /**
     *  Construct coordinate at same location as oldCoord <BR>
     *  @param Coordinate oldCoord
   **/
     public Coordinate(Coordinate oldCoord) {
        this(oldCoord.getXCoord(), oldCoord.getYCoord());
   }
   
// accessor methods

   public double getXCoord() {return xCoord; }
   public double getYCoord() {return yCoord; }

   /**
     *  Note that <I>both</I> coordinates must be set together.
     * @param double x, double y
   **/
   public void setCoordinates(double x, double y) {
      xCoord = x;
      yCoord = y;
   }
// instance methods
   
   /**
     *  An additional setCoordinates method that uses another Coordinate for
     *  its data.
     * @param Coordinate oldCoord
   **/
   public void setCoordinates(Coordinate oldCoord) {
      setCoordinates(oldCoord.getXCoord(), oldCoord.getYCoord());
   }
   
   /**
     *  Increment coordinates by (deltaX, deltaY).
     * @param double deltaX, double deltaY
   **/
     public Coordinate incrementBy(double deltaX, double deltaY) {
        setCoordinates( xCoord + deltaX, yCoord + deltaY);
        return this;
   }
 
   /**
     *  Increment coordinates by Coordinate delta.
     * @param Coordinate delta
   **/
     public Coordinate incrementBy(Coordinate delta) {
        incrementBy(delta.getXCoord(), delta.getYCoord());
        return this;
   }

   /**
     *  Decrement coordinates by Coordinate delta.
     * @param Coordinate delta
   **/
     public Coordinate decrementBy(Coordinate delta) {
        incrementBy(- delta.getXCoord(), - delta.getYCoord());
        return this;
   }

/**
  *  Multiply both coordinates by scalar alpha
  * @param double alpha
**/

   public Coordinate scalarMultiply(double alpha) {
      setCoordinates( alpha * getXCoord(), alpha * getYCoord()); 
      return this;
   }

/**
  *  Compute the inner product between this coordinate and another
  * @param Coordinate other
**/   

   public double dotBy(Coordinate other) {
      return xCoord * other.getXCoord() + yCoord * other.getYCoord();
   }
 
/**
  *  Compute the Euclidean distance between current instance and
  *  (x, y). 
  *  @param double x, double y
**/
   public double distanceFrom(double x, double y) {
      return Math.sqrt( 
	   (xCoord - x) * (xCoord - x) + (yCoord - y) * (yCoord - y) );
   }

/**
  *  Compute the Euclidean distance between current instance and
  *  Coordinate there. <BR>
  *  @param Coordinate there
**/
   public double distanceFrom(Coordinate there) {
      return distanceFrom(there.getXCoord(), there.getYCoord() );
   }

   public double getNorm() {return distanceFrom(new Coordinate());} 
     
/**
  *  Sets the DecimalFormat String
  *  @param String newFormatString
**/
   public void setFormat(String newFormatString) {
      form = new DecimalFormat(newFormatString);
   }

   public Coordinate getDirection() {
     return new Coordinate(this).scalarMultiply(1.0 / this.getNorm());
   }
   
/**
  * Return the values of the current coordinates.
**/
   public String toString() {
      return "(" + form.format(xCoord) + ", " + form.format(yCoord) + ")";
   }
   
// main method -- unit test of Coodinate
   public static void main(String[] args) {
      Coordinate first = new Coordinate(1.0, 2.0);
      Coordinate second = new Coordinate(4.0, 6.0);
      Coordinate third = new Coordinate(second);
      Coordinate fourth = new Coordinate(first.toString());
      
      System.out.println("first Coordinate = " + first);
      System.out.println("second Coordinate = " + second);
      System.out.println("distance = " + first.distanceFrom(second) );
      
      System.out.println("added : " + third.incrementBy(second) );
      third.setCoordinates(second);
      System.out.println("subtracted: " + third.decrementBy(first) );
      System.out.println("first * 1.5 = " + first.scalarMultiply(1.5) );
      
      System.out.println("first = " + first + " second = " + second);
      System.out.println("first . second = " + first.dotBy(second) );

      System.out.println("fourth Coordinate = " + fourth);
      System.out.println("Directions: " + first.getDirection() + " " + second.getDirection());
   }
 /*
  * Output:
first Coordinate = (1, 2)
second Coordinate = (4, 6)
distance = 5.0
added : (8, 12)
subtracted: (3, 4)
first * 1.5 = (1.5, 3)
first = (1.5, 3) second = (4, 6)
first . second = 24.0
 */
}