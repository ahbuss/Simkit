package simkit.stat;

/**
 *	A class for collecting data on the fly and reporting basic 
 *	summary statistics.  
 * 
 *      <P>Interface and formatting added 12-05-97 (ab)
 *      <P>PropertyChangeListener methods added 12-15-97 (ab)
 *      <P> 02 June 1999 - synchronized getter methods ("What took so long??)
 *
 *	@author Arnie Buss
**/

import java.text.*;
import simkit.*;
import java.beans.*;

public class SimpleStats implements Named,
                                    SampleStatistics, 
                                    PropertyChangeListener {

// private variables
   private int count;			// # observations
   private double sum;			// running sum of observations
   private double sumOfSquares;		// running sum^2 of observations
   private double minObs;		// running min of observations
   private double maxObs;		// runing max of observations
   protected double lastObs;              // last observation
   protected double startTime;
   protected double lastTime;
   
   private SamplingType myType;         // determines type of collecting
   protected DecimalFormat df;          // format for output
   private boolean verbose;

   private String name;
// constructors

   public SimpleStats(SamplingType type) {
      super();
      reset();
      setSamplingType(type);
      setFormat("0.0000");
   }

   public SimpleStats() {
      this(SamplingType.TALLY);
   }

   public SimpleStats(String name, SamplingType type) {
     this(type);
     this.setName(name);
   }

// instance methods
   /**
     *	Reset all counters; note values of minObs and maxObs
   **/
   public void reset() {
      count = 0;
      sum = 0.0;
      sumOfSquares = 0.0;
      minObs = Double.POSITIVE_INFINITY;
      maxObs = Double.NEGATIVE_INFINITY;
//      lastObs = Double.NaN;   // not sure about this...
      lastTime = Schedule.simTime();
      startTime = Schedule.simTime();
   }
   
   /**
     *	Update all counters.
   **/
   public synchronized void newObservation(double newObs) {
      count++;
      minObs = (newObs < minObs) ? newObs : minObs;
      maxObs = (newObs > maxObs) ? newObs : maxObs;
      if (myType == SamplingType.TALLY) {
         sum += newObs;
         sumOfSquares += newObs * newObs;
      }
      else if (myType == SamplingType.TIME_VARYING) {
         double elapsedTime = Schedule.simTime() - lastTime;
         sum += elapsedTime * lastObs;
         sumOfSquares += elapsedTime * lastObs * lastObs;
      }
      else {
         System.err.println("Unidentified type!");
      }
      lastTime = Schedule.simTime();
      lastObs = newObs;

      if (verbose) {System.out.println(toString());}
   }
   
   public synchronized void newObservation(Number newObs) {
      newObservation(newObs.doubleValue());
   }
   
   public synchronized void newObservation(int newObs) {
      newObservation((double) newObs);
   }

   
   /**
     *	Gives mean of observations collected so far
   **/
   public synchronized double getMean() {
      if (myType == SamplingType.TALLY) {
         return sum / count;
      }
      else if (myType == SamplingType.TIME_VARYING) {
         double elapsedTime = Schedule.simTime() - lastTime;
         double totalTime = Schedule.simTime() - startTime;
         return (sum + lastObs * elapsedTime)/ totalTime;
      }
      else {
         return Double.NaN;
      }
   }
      
   /**
     *	Gives variance of observations so far
   **/
   public synchronized double getVariance() {
      if (count > 0) {
         if (myType == SamplingType.TALLY) {
            return (sumOfSquares - sum * sum / count) / (count - 1);
	 }
         else if (myType == SamplingType.TIME_VARYING) {
            double elapsedTime = Schedule.simTime() - lastTime;
            double totalTime = Schedule.simTime() - startTime;
            double updatedSum = sum + lastObs * elapsedTime;
            double updatedSS = sumOfSquares + lastObs * lastObs * elapsedTime;
            return (updatedSS - updatedSum * updatedSum / totalTime) / totalTime;
         }
      }
      return Double.NaN;
   }

   /**
     *  Compute standard deviation of observations so far
     *	@see #getVariance
   **/
   public synchronized double getStandardDeviation() {
      return Math.sqrt(getVariance());
   }

// Getter methods
   public synchronized int getCount() {return count;}
   public synchronized double getMinObs() {return minObs;}
   public synchronized double getMaxObs() {return maxObs;}

// toString method
   public String toString() {
       StringBuffer buf = new StringBuffer();
       buf.append(getName());
       buf.append('(');
       buf.append(this.getSamplingType());
       buf.append(")\n");
       buf.append(getCount());
       buf.append("  ");
       buf.append(df.format(getMinObs()));
       buf.append("  ");
       buf.append(df.format(getMaxObs()));
       buf.append("  ");
       buf.append(df.format(getMean()));
       buf.append("  ");
       buf.append(df.format(getVariance()));
       buf.append("  ");
       buf.append(df.format(getStandardDeviation()));

       return buf.toString();

   }

   public void setFormat(String format) {
      df = new DecimalFormat(format);
   }

   public synchronized void setSamplingType(SamplingType type) { 
      myType = type; 
      reset();
   }
   
   public synchronized SamplingType getSamplingType() { return myType; }

   public void setVerbose(boolean v) {verbose = v;}
   public boolean getVerbose() {return verbose;}

// implements PropertyChangeListener

   public void propertyChange(PropertyChangeEvent event) {
      if (event.getPropertyName().equals(this.getName())) {
        newObservation( Double.valueOf(event.getNewValue().toString() ) );
      }
   }

// implements Named interface
   public void setName(String name) {this.name = name;}
   
   public String getName() {return name;}

// main method
   public static void main(String[] args) {
      SimpleStats myStats = new SimpleStats();
      
   /*
    *  This is the initial condition of myStats
   */
      System.out.println("At first:");
      System.out.println(myStats);
      
   /*
    *  Compute statistics for the first 1000 integers.
   */
      for (int i = 1; i <= 1000; i++ ) {
         myStats.newObservation(i);
      }
      System.out.println("Statistics for first 1000 integers:");
      System.out.println(myStats);

   /*
    *  Restore to initial condition
   */
      myStats.reset();
      System.out.println("After resetting:");
      System.out.println(myStats);
   /*
    * You should get the following output:
    * At first:
    * 0  ?  -?  ?  ?  ?
    * Statistics for first 1000 integers:
    * 1000  1.00  1000.00  500.50  83416.67  288.82
    * After resetting:
    * 0  ?  -?  ?  ?  ?
   */
   }
     
}