// FILE : SummaryStat.java

package simkit.stat;

import java.awt.Color;
import java.awt.Label;

/**
*  Base class for time based statistical data accumulation.
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
   
   public SummaryStat( String  dataName,
                       double  creationTime) {
      this.reset(creationTime);
      name_            = dataName;
   }         
   
   public String name() {
      return name_;
   }
   
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
   
   public DataLog getLog() {
      return samples;
   }
   
   public void setDisplayUpdateInterval ( int x ) {
      updateInterval = x;
   }
   
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
      


   public double mean(){
      return sum_ / n;
   }

   public double sum(){
      return sum_;
   }

   public long count() {
      return n;
   }
   
   public double variance(){
      return   ( sumsq - 
            ( sum_ * sum_ ) / 
            (double)n )/ 
            (double)(n);
   }
   
   public double sampleVar(){
      return   ( sumsq - 
            ( sum_ * sum_ ) / 
            (double)n )/ 
            (double)(n);
   }
   
   public double std(){
      return Math.sqrt( variance() );
   }
   
   public double wtdMean(double now){
      return  cumValTime  / ( now - creationTime  );
   }
   
   public double wtdVariance( double now ){
      return    ( cumValTimeSq  - 
            ( cumValTime * cumValTime ) / 
            ( now - creationTime  ) ) /
            ( now - creationTime  );
   }

   public double wtdStd(double now) {
      return Math.sqrt(wtdVariance(now));
   }

   public double timeWtdCIHalfWidth( double now) {
      double hw;
      
      hw = Math.sqrt( wtdVariance(now) / ( now - creationTime  ));
      hw *= 1.959963984540053;
      return hw;
   }
   
   public double CI95HalfWidth( ) {
      double hw;
      
      hw = Math.sqrt( sampleVar() / ( n ));
      hw *= 1.959963984540053;
      return hw;
   }
   
   public double CI90HalfWidth( ) {
      double hw;
      
      hw = Math.sqrt( sampleVar() / ( n ));
      hw *= 1.645;
      return hw;
   }
   
   public double rel90Err() {
      return CI90HalfWidth() / mean();
   }
   
   public double rel95Err() {
      return CI95HalfWidth() / mean();
   }
   
   public double min(){
      return minimum;
   }
   
   public double max(){
      return maximum;
   }

   public synchronized String toString() {
      return this.toString(lastChangeTime);
   }

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
                  "95% CI Half Width:           " + CI90HalfWidth() + "\n" +
                  "       TIME WEIGHTED STATISTICS\n" +
                  "Mean:                        " + wtdMean(now) + "\n" +
                  "Variance:                    " + wtdVariance(now) + "\n" +
                  "Std Dev:                     " + wtdStd(now) + "\n" +
                  "CI Half Width:               " + timeWtdCIHalfWidth( now );
      return result;
   }
   
   
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
