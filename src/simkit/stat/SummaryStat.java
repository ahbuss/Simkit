// FILE : SummaryStat.java

package simkit.stat;

import java.awt.Color;
import java.awt.Label;

/**
*  Base class for time based statistical data accumulation.
*  Accumulates samples, calculates statistics, and displays the statistics
* in a Panel. The confidence interval generating functions have not yet been 
* tested.
* <p/>Note: If the display features of this Class are not needed, then it is
* recommended that one of the children of 
* {@link AbstractSimpleStats} be used since they have a more numerically stable
* algorithm.
*
*  @author    Kirk A. Stork
*  @version $Id$
*/
public class      SummaryStat
       implements DataLogging

{   
   public static final int DEFAULT_UPDATE_INTERVAL = 50;
   private static final int NUM_TABLE_ENTRIES = 16;
   
   protected String           name_;
   protected long             n;
   protected int              updateInterval,
                              updateCount;
   protected double           creationTime ,
                              lastChangeTime,
                              cumValTime,
                              cumValTimeSq,
                              sum_,
                              sumsq,
                              lastValue,
                              maximum,
                              minimum;
   protected TablePanel       window;
   private   double[]         values;
   protected DataLog          samples;
   
/**
* Creates a new SummaryStat with the given name and creation time.
**/
   public SummaryStat( String  dataName,
                       double  creationTime) {
      this.reset(creationTime);
      name_            = dataName;
   }         
   
/**
* Returns a String containing the name of this SummaryStat.
**/
   public String name() {
      return name_;
   }
   
/**
* Clears this SummaryStat.
* @param now The new creation time.
**/
   public void reset(double now) {
      n              = 0;
      cumValTime     = 0.0;
      cumValTimeSq   = 0.0;
      sum_           = 0.0;
      sumsq          = 0.0;
      lastValue      = 0.0;
      maximum        = 0.0;
      minimum        = 0.0;
      creationTime   = now;
      lastChangeTime = now;
      updateCount    = 0;
      updateInterval = DEFAULT_UPDATE_INTERVAL;
      values         = new double[NUM_TABLE_ENTRIES];
      setLog(null);
   }

/**
* Sets the DataLog to which observations will be logged.
**/
   public void setLog(DataLog whichLog) {
      if (samples !=null) {
         samples.unregisterLogger(this);
      }
      samples = whichLog;
      
      if (samples !=null) {
         samples.registerLogger(this);
         samples.log("Log of sample data for " + name());
      }
   }
   
/**
* Gets the DataLog to which observations will be logged.
**/
   public DataLog getLog() {
      return samples;
   }
   
/**
* Sets how often the TablePanel will be updated (default is 50).
* The TablePanel is created by calling createTable.
**/
   public void setDisplayUpdateInterval ( int x ) {
      updateInterval = x;
   }
   
/**
* Adds a sample. Updates both time weighted and non-time statistics.
* Updates data on the TablePanel (if created) as determined by the value
* of the display update interval (default 50).
* @param now The time of the sample
* @param val The value of the sample.
**/
   public synchronized void sample(double now, double val) {

      if ( samples != null ) {
         samples.log(now,val);
      }
      if ( n == 0 ) {
         minimum = val;
      }
      n++;
      cumValTime      += lastValue * ( now - lastChangeTime);
      cumValTimeSq    += lastValue * lastValue * ( now - lastChangeTime);
      sum_            += val;
      sumsq           += val * val;
      
      if ( val > maximum ) maximum = val;
      if ( val < minimum ) minimum = val;
      lastChangeTime   = now;
      lastValue        = val;
      
      updateCount++;
      if ( updateInterval <= updateCount ) {
         flushDisplay(now);
         updateCount=0;
      }
   }
      

/**
* Returns the mean of the samples.
**/
   public double mean(){
      return sum_ / n;
   }

/**
* Returns the sum of the samples.
**/
   public double sum(){
      return sum_;
   }

/**
* Returns the number of samples.
**/
   public long count() {
      return n;
   }
   
/**
* Returns the variance of the samples.
**/
   public double variance(){
      return   ( sumsq - 
            ( sum_ * sum_ ) / 
            (double)n )/ 
            (double)(n);
   }
   
/**
* Returns the variance of the samples.
**/
   public double sampleVar(){
      return   ( sumsq - 
            ( sum_ * sum_ ) / 
            (double)n )/ 
            (double)(n);
   }
   
/**
* Returns the standard deviation of the samples.
**/
   public double std(){
      return Math.sqrt( variance() );
   }
   
/**
* Returns the time weighted mean of the samples.
**/
   public double wtdMean(double now){
      return  cumValTime  / ( now - creationTime  );
   }
   
/**
* Returns the time weighted variance of the samples.
**/
   public double wtdVariance( double now ){
      return    ( cumValTimeSq  - 
            ( cumValTime * cumValTime ) / 
            ( now - creationTime  ) ) /
            ( now - creationTime  );
   }

/**
* Returns the time weighted standard deviation of the samples.
**/
   public double wtdStd(double now) {
      return Math.sqrt(wtdVariance(now));
   }

/**
* Returns the half width of a time weighted 95% confidence interval.
**/
   public double timeWtdCIHalfWidth( double now) {
      double hw;
      
      hw = Math.sqrt( wtdVariance(now) / ( now - creationTime  ));
      hw *= 1.959963984540053;
      return hw;
   }
   
/**
* Returns the half-width of the 95% confidence interval.
**/
   public double CI95HalfWidth( ) {
      double hw;
      
      hw = Math.sqrt( sampleVar() / ( n ));
      hw *= 1.959963984540053;
      return hw;
   }
   
/**
* Returns the half-width of the 90% confidence interval.
**/
   public double CI90HalfWidth( ) {
      double hw;
      
      hw = Math.sqrt( sampleVar() / ( n ));
      hw *= 1.645;
      return hw;
   }
   
/**
* Returns the relative error at 90% confidence. It is the 90% CI half-width
* divided by the mean.
**/
   public double rel90Err() {
      return CI90HalfWidth() / mean();
   }
   
/**
* Returns the relative error at 95% confidence. It is the 95% CI half-width
* divided by the mean.
**/
   public double rel95Err() {
      return CI95HalfWidth() / mean();
   }
   
/**
* Returns the minimum observation for the samples.
**/
   public double min(){
      return minimum;
   }
   
/**
* Returns the maximum observation for the samples.
**/
   public double max(){
      return maximum;
   }

/**
* Returns a String containing the statistics for the samples as of the
* last sample time.
**/
   public synchronized String toString() {
      return this.toString(lastChangeTime);
   }

/**
* Returns a String containing the statistics for the samples as of the
* given time. It is assumed that the given time is later than the last sample time.
**/
   public synchronized String toString( double now ){
      String result = 
                  name() + " At time: " + now + "\n" +
                  "           SIMPLE STATISTICS\n" +
                  "Number of Samples:           " + n + "\n" +
                  "Min value:                   " + min() + "\n" +
                  "Max value:                   " + max() + "\n" +
                  "Mean value:                  " + mean() + "\n" +
                  "Variance:                    " + variance() + "\n" +
                  "Sample Variance:             " + sampleVar() + "\n" +
                  "Std Dev:                     " + std() + "\n" +
                  "90% CI Half Width:           " + CI90HalfWidth() + "\n" +
                  "95% CI Half Width:           " + CI95HalfWidth() + "\n" +
                  "       TIME WEIGHTED STATISTICS\n" +
                  "Mean:                        " + wtdMean(now) + "\n" +
                  "Variance:                    " + wtdVariance(now) + "\n" +
                  "Std Dev:                     " + wtdStd(now) + "\n" +
                  "CI Half Width:               " + timeWtdCIHalfWidth( now );
      return result;
   }
   
   
/**
* Creates a TablePanel with headers for the statistics.
* After adding to a Container ,call flushDisplay to fill in the values.
**/
   public TablePanel makeTable(double now) {
      if ( window == null ) {
         window = new TablePanel();
         window.addItem("Time");
         window.addItem("Number of Samples");
         window.addItem("Min Value");
         window.addItem("Max Value");
         window.addItem("Mean");
         window.addItem("Variance");
         window.addItem("Sample Variance");
         window.addItem("Std Deviation");
         window.addItem("90% Half Width");
         window.addItem("95% Half Width");
         window.addItem("Relative Error at 90% HW");
         window.addItem("Relative Error at 95% HW");
         window.addItem("Wtd Mean");
         window.addItem("Wtd Variance");
         window.addItem("Wtd Std Dev");
         window.addItem("Wtd 95% Half Width");
         window.init();
      }
         return window;
   
   }
   
/**
* Update the TablePanel with the statistics.
* The TablePanel is created by calling makeTable.
**/
   public void flushDisplay( double now ) {
      if ( window != null ) {
         int i = 0;
         values[i++] = now;
         values[i++] = n;
         values[i++] = min();
         values[i++] = max();
         values[i++] = mean();
         values[i++] = variance();
         values[i++] = sampleVar();
         values[i++] = std();
         values[i++] = CI95HalfWidth();
         values[i++] = CI90HalfWidth();
         values[i++] = rel90Err();
         values[i++] = rel95Err();
         values[i++] = wtdMean(now);
         values[i++] = wtdVariance(now);
         values[i++] = wtdStd(now);
         values[i++] = timeWtdCIHalfWidth( now );
         window.updateValues(values);
      }
  }
} // class data.SummaryStat
