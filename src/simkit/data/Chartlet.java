 // FILE: Chartlet.java
package simkit.data;

import java.awt.*;
import graph.*;
import java.util.*;

/**
* @deprecated Use simkit.stat.Chartlet instead.
*
* @author Kirk A. Stork
* @version 1.0 (1.17.97)
*/
public class  Chartlet
	extends    Graph2D
//	implements Runnable
	
{
   private static final int UPDATE_INTERVAL = 1;
   private static final int UPDATE_DATA_INTERVAL = UPDATE_INTERVAL * 2;
   
   private String xLabel, yLabel;
   public static final Font theLabelFont = new Font("Helvetica", Font.PLAIN, 12);
   
//	protected G2Dint        myGraph;
   protected DataSet       theData;
   protected Axis          xAxis, yAxis;
   protected double        dataBuffer[];
   
   protected Thread        runner;
   protected Image         osi;
   protected Graphics      osg;
   protected int           iwidth;
   protected int           iheight;
   protected boolean       changed;
   protected boolean       animating;
   protected boolean       showing;
   protected boolean       chartmade;
   protected int           dataCount;
   protected Label         title;
   
   public Chartlet( String FrameTitle, String ChartTitle, boolean animate ) 
	{
      dataCount = 0;
		runner = null;
		animating = animate;
		changed = false;
      osi = null;
      osg = null;
      iwidth = 0;
      iheight = 0;
      title = new Label(ChartTitle, Label.CENTER);
      title.setFont(new Font("TimesRoman", Font.PLAIN, 18));
      theData = new DataSet();
      chartmade = false;
      showing = false;
	}
	
	private void makeChart() {
      this.framecolor = new Color(0,0,0);
      this.borderRight=50;
      xAxis = this.createAxis(Axis.BOTTOM);
      yAxis = this.createAxis(Axis.LEFT);
      yAxis.setLabelFont(theLabelFont);
      xAxis.setLabelFont(theLabelFont);
      xAxis.attachDataSet(theData);
      yAxis.attachDataSet(theData);
      xAxis.setManualRange(true);
      yAxis.setManualRange(true);
      yAxis.force_end_labels = true;
      this.attachDataSet(theData);
      theData.xaxis.setTitleFont(theLabelFont);
      theData.yaxis.setTitleFont(theLabelFont);
      theData.xaxis.minimum = 0.0;
      theData.yaxis.setTitleText(yLabel);
      theData.yaxis.setTitleRotation(0);
      theData.xaxis.setTitleText(xLabel);
      chartmade = true;
	}
	
	public void setXLabel(String s) {
	   xLabel = s;
	   if (xAxis != null) {
	      
	   }
	}
	
	public void setYLabel(String s) {
	   yLabel = s;
	   if (yAxis != null) {
	   }
	}
	
	public void show() {
      showing = true;
      chartmade = false;
      flushPlot();
	}

	public synchronized void repaint( ) {
	   int i = 0;
	   Graphics g;
//      System.out.println("Chartlet.repaint() called");
      if ( theData.getYmax() <= theData.getYmin() ) return;
      if ( theData.getXmax() <= theData.getXmin() ) return;
      if ( !chartmade ) return;
      if ( !showing ) return;
	   if ( isShowing() ) {
		   if ( changed ) {
	         changed = false;
	         g = this.getGraphics();
	         if ( osi == null || 
	              iwidth != this.getSize().width ||         //AB
	              iheight != this.getSize().height ) {      //AB
	            if ( osg != null ) {
	               osg.dispose();
	               osg = null;
	            }
	            iwidth = this.getSize().width;  //AB
	            iheight = this.getSize().height; //AB
	            osi = this.createImage(iwidth, iheight);
	            osg = osi.getGraphics();
	         }
	         osg.setColor(this.getBackground());
	         osg.fillRect(0,0,iwidth, iheight);
	         osg.setColor(g.getColor());
	         osg.clipRect(0,0,iwidth,iheight);
	         this.update(osg);
	         g.drawImage(osi,0,0,this);
	         g.dispose();
	         try {
	           Thread.sleep(20);
	         }
	         catch( Exception ex) {
	         }
		    }
		 } 
	}
	
   public synchronized void addDataPoint(double x, double y) {
      
      if ( dataCount == 0 ) {
         dataBuffer = new double[UPDATE_DATA_INTERVAL];
      }
      
      dataBuffer[dataCount++] = x;
      dataBuffer[dataCount++] = y;
                            
      if (dataCount == UPDATE_DATA_INTERVAL) {
         flushPlot();
      }
      changed = true;
      if ( theData.getYmax() <= theData.getYmin() ) return;
      if ( theData.getXmax() <= theData.getXmin() ) return;
//      if ( isShowing() ) {
//         System.out.println("Chartlet thinks it is visible");
         this.repaint();
         this.getParent().repaint();
//      }
   }
	
	public synchronized void replaceData(double[] data) {
	   if( xAxis != null ) {
         if ( !chartmade ) makeChart();
	      this.detachAxes();
	      this.detachDataSets();
	   }
	   try{
	      theData = new DataSet(data, data.length/2);
	   } catch(Exception e) {
	      System.err.println(e);
	   }
	   
      if ( theData.getYmax() <= theData.getYmin() ) return;
      if ( theData.getXmax() <= theData.getXmin() ) return;
      if ( !showing ) return;
	   xAxis = this.createAxis(Axis.BOTTOM);
      yAxis = this.createAxis(Axis.LEFT);
      yAxis.setLabelFont(theLabelFont);
      xAxis.setLabelFont(theLabelFont);
      yAxis.force_end_labels = true;
      xAxis.attachDataSet(theData);
      yAxis.attachDataSet(theData);

      theData.yaxis.setTitleText(yLabel);
      theData.yaxis.setTitleRotation(0);
      theData.xaxis.setTitleText(xLabel);
      theData.xaxis.setTitleFont(theLabelFont);
      theData.yaxis.setTitleFont(theLabelFont);
      this.attachDataSet(theData);
      changed = true;
      this.repaint();
   }
   
   public synchronized void replaceData(DataSet newData) {
	   if( xAxis != null ) {
         if ( !chartmade ) makeChart();
	      this.detachAxes();
	      this.detachDataSets();
	   }
	   try{
	      theData = newData;
	   } catch(Exception e) {
	      System.err.println(e);
	   }
	   
      if ( theData.getYmax() <= theData.getYmin() ) return;
      if ( theData.getXmax() <= theData.getXmin() ) return;
      if ( !showing ) return;
	   xAxis = this.createAxis(Axis.BOTTOM);
      yAxis = this.createAxis(Axis.LEFT);
      yAxis.setLabelFont(theLabelFont);
      xAxis.setLabelFont(theLabelFont);
      yAxis.force_end_labels = true;
      xAxis.attachDataSet(theData);
      yAxis.attachDataSet(theData);

      theData.yaxis.setTitleText(yLabel);
      theData.yaxis.setTitleRotation(0);
      theData.xaxis.setTitleText(xLabel);
      theData.xaxis.setTitleFont(theLabelFont);
      theData.yaxis.setTitleFont(theLabelFont);
      this.attachDataSet(theData);
      changed = true;
      this.repaint();
   }
	
	public synchronized void updateDisplay() {
     if ( !chartmade ) makeChart();
      changed=true;
//      this.invalidate();
      this.repaint();
   }
   	
	public synchronized void flushPlot() {
//	  System.out.println("Flushplot called on " + this);
     if ( !chartmade ) makeChart();
     if ( dataCount > 0 ) {
        try {
           theData.append(dataBuffer, ((dataCount)/2));
        } catch (Exception ex) {
           System.err.println("Error appending data: " + ex);
        }
        dataCount = 0;
        if ( theData.dataPoints() > 2 ) {
           changed = true;
        }
     }
	}
	
} // END CLASS Chartlet
