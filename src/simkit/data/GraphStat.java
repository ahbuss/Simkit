// FILE : GraphStat.java

package simkit.data;

//import jgl.Array;
import graph.Graph2D;
import graph.Axis;
import graph.DataSet;
import graph.HistogramDataSet;

import java.awt.Color;
import java.awt.Label;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.BorderLayout;
import java.awt.Font;

/**
*  Base class for time based statistical data accumulation.
*
*  @author    Kirk A. Stork
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
   
   public GraphStat( String dataName,
	                  double creationTime ) {
	   super(dataName, creationTime);
	}
	
   
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
         histogramChart.show();

      }
      return histogramChart;
   }
   
      
   public Graph2D initAveChart( boolean animate) {
      collectingAverage = true;
      animatingAverage  = animate;
      aveChart = new Chartlet( name(), "Simple Average", animate);
      aveChart.setXLabel("Time");
      aveChart.setYLabel("Value");

      if ( animatingAverage ) {
         aveChart.show();
      }
      
      return aveChart;
   }
      
   public Graph2D initTimeAveChart( boolean animate) {
      collectingTimeAverage = true;
      animatingTimeAverage  = animate;
      
      timeAveChart = new Chartlet(name(), "Time Average", animate);
      timeAveChart.setXLabel("Time");
      timeAveChart.setYLabel("Value");
      if ( animatingTimeAverage ) {
         timeAveChart.show();
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
	
   private synchronized void updateChartData() {
         histogramChart.updateDisplay();
//         histogramChart.repaint();
   }
   
   public synchronized void sample( double now, double val ) {
      
      boolean counted = false;
      
      if ( histogramChart != null ) {
         theHistData.addCount(val);
      }
      super.sample(now, val);
   }
   

} // class data.GraphStat
