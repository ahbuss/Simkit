// FILE : GraphStat.java

package simkit.stat;

//import jgl.Array;
import graph.Graph2D;
import graph.HistogramDataSet;

/**
* A Class to accumulate statistics and display them graphically. The data
* can be displayed as either a histogram, an average vs. time, or a time
* weighted average vs. time.</p>
*
*  @author    Kirk A. Stork
*  @version $Id$
*/

public class      GraphStat
       extends    SummaryStat
{
   
   protected Chartlet   aveChart,
                        timeAveChart,
                        histogramChart;
   protected boolean    collectingAverage,
                        collectingTimeAverage,
                        animatingAverage,
                        animatingTimeAverage,
                        animatingHistogram;
			
   private boolean          changed;
   
   private Graph2D          theChart;   
   private String           xLabel,
                            yLabel;

   private HistogramDataSet theHistData;
   
/**
* Creates a new GraphStat with the given name and given start time.
**/
   public GraphStat( String dataName,
	                  double creationTime ) {
	   super(dataName, creationTime);
	}
	
   
/**
* Resets the statistics and re-initializes this GraphStat at the given time.
**/
	public void reset(double now) {
	   super.reset(now);
      collectingAverage     = false;
      collectingTimeAverage = false;
      animatingAverage      = false;
      animatingTimeAverage  = false;
      animatingHistogram    = false;
      aveChart              = null;
      timeAveChart          = null;
      histogramChart        = null;
	}

/**
* Creates a histogram. The returned Graph2D should be added to an AWT Container
* for display.
* @param animate If true the Chart will be automatically updated.
* @param left The lowest value on the histogram
* @param right The highest value on the histogram
* @param numBins The number of histogram bins.
**/
   public Graph2D initHistogram( boolean  animate,
                                 double   left,
                                 double   right,
                                 int      numBins ) {
      animatingHistogram = animate;
      theHistData = new HistogramDataSet( left, right, numBins);

      histogramChart = new Chartlet(name(),"Histogram",animate);
      histogramChart.setXLabel("Value");
      histogramChart.setYLabel("Count");
      histogramChart.replaceData(theHistData);
      if ( animate ) {
         histogramChart.setVisible(true);

      }
      return histogramChart;
   }
   
      
/**
* Creates a graph of average vs. time. The returned Graph2D should be added
* to an AWT Container for display.
* @param animate If true the chart will be automatically updated.
**/
   public Graph2D initAveChart( boolean animate) {
      collectingAverage = true;
      animatingAverage  = animate;
      aveChart = new Chartlet( name(), "Simple Average", animate);
      aveChart.setXLabel("Time");
      aveChart.setYLabel("Value");

      if ( animatingAverage ) {
         aveChart.setVisible(true);
      }
      
      return aveChart;
   }
      
/**
* Creates a graph of time average vs. time. The returned Graph2D should be added
* to an AWT Container for display.
* @param animate If true the chart will be automatically updated.
**/
   public Graph2D initTimeAveChart( boolean animate) {
      collectingTimeAverage = true;
      animatingTimeAverage  = animate;
      
      timeAveChart = new Chartlet(name(), "Time Average", animate);
      timeAveChart.setXLabel("Time");
      timeAveChart.setYLabel("Value");
      if ( animatingTimeAverage ) {
         timeAveChart.setVisible(true);
      }
      return timeAveChart;
   }
		

	public void showCharts(double now){
      flushDisplay(now);
      if ( collectingAverage ) {
         aveChart.flushPlot();
         aveChart.repaint();
      }
      if ( collectingTimeAverage ) {
         timeAveChart.flushPlot();
      }
      timeAveChart.repaint();
	   if ( histogramChart != null ) {
	      updateChartData();
	      histogramChart.repaint();
	   }
	}
	
/**
* Updates the data on the chart(s).
**/
	public synchronized void flushDisplay( double now) {
		super.flushDisplay(now);
      if ( collectingAverage ) {
         aveChart.addDataPoint(now, mean());
      }
      if ( collectingTimeAverage ) {
         timeAveChart.addDataPoint(now, wtdMean(now));
      }
      
      if ( updateCount >= updateInterval ) {
         changed = true;
         if ( animatingHistogram) {
            updateChartData();
         }
      }
	}
	
/**
* Updates the histogram display
**/
   private synchronized void updateChartData() {
         histogramChart.updateDisplay();
//         histogramChart.repaint();
   }
   
/**
* Adds a new sample. Updates the data on the Charts as determined by the value
* of the display update interval (default is 50).
**/
   public synchronized void sample( double now, double val ) {
      
      boolean counted = false;
      
      if ( histogramChart != null ) {
         theHistData.addCount(val);
      }
      super.sample(now, val);
   }
   

} // class data.GraphStat
