package graph;
@Deprecated
public class HistogramDataSet
       extends graph.DataSet
{
// Important inherrited variables
//
// data[]   Used here to contain the corners of the
//          histogram plot.
//
// Important method override information
//
// Several methods of DataSet are overridden for the
// purpose of disabling them.  For instance, appendData
// makes no sense for a histogram.  Methods overridden 
// for this reason are commented as such.  This is
// poor object oriented practice, but is done rather than
// redesigning the graph package with an interface
// for the DataSet type for expedience.
//
   private double   leftRange,
                    rightRange,
                    leftBinLimit,
                    rightBinLimit;
                    
   private double[] binLimits;
   private int      nBins;
   private double   binWidth;
   private boolean  defaultBins;
   
   public HistogramDataSet( double minLimit,
                            double maxLimit,
                            int    numBins ) {
      super();
      leftBinLimit  = minLimit;
      rightBinLimit = maxLimit;
      nBins         = numBins;
      
      binWidth = (rightBinLimit - leftBinLimit) /
                 ((double) nBins);
      buildSet();
   }
   
   public HistogramDataSet( double minLimit,
                            int    numBins,
                            double bin_width ) {
      super();
      leftBinLimit  = minLimit;
      nBins         = numBins;
      binWidth      = bin_width;
      rightBinLimit = minLimit + (double)numBins *
                      bin_width;
      buildSet();
   }
   
   
   private void buildSet() {
      int i;
      int points;
      
      leftRange  = leftBinLimit  - binWidth / 2.0;
      rightRange = rightBinLimit + binWidth / 2.0;
      points = ( nBins * 3 + 7 ) * 2;
      binLimits = new double[nBins+1];
      binLimits[0] = leftBinLimit;
      for ( i = 1; i < nBins; i++ ) {
         binLimits[i] = binLimits[ i-1 ] + binWidth;
      }
      binLimits[i] = rightBinLimit;
      
      data = new double[ points ];
      
      data[0] = leftRange;
      data[2] = leftRange;
      data[4] = leftBinLimit;
      data[6] = leftBinLimit;
      for ( i = 1; i < nBins+1; i++ ) {
         data [ 6 * i + 4 ] = binLimits[ i ];
         data [ 6 * i + 6 ] = binLimits[ i ];
         data [ 6 * i + 8 ] = binLimits[ i ];
      }
      data[data.length - 2] = rightRange;
      data[data.length - 4] = rightRange;
      length = data.length;
//      for ( int k = 0; k < data.length; k+=2) {
//        System.out.println(data[k] + "      " + data[k+1]);
//      }
   }

   public double getXmin() {  return leftRange; } 
/**
Add a data value to the histogram.  The value adds
a count to the bin in which it falls.
**/   
   public void addCount( double val) {
      
      boolean counted = false;
      for ( int i = 0; i < nBins+1; i++ ) {
         if ( val < binLimits[i] ) {
            data[ 6 * i + 3 ]++;
            data[ 6 * i + 5 ]++;
            counted = true;
            break;
         }
      }
   
      if ( !counted ) {
         data[data.length - 3 ]++;
         data[data.length - 5 ]++;
      }
      //     Calculate the data range.

           range(stride);
//           System.out.println(dxmin + "  " + dxmax +
//                              "  " + dymin + "  " + dymax);
//     Update the range on Axis that this data is attached to
           if(xaxis != null) xaxis.resetRange();
           if(yaxis != null) yaxis.resetRange();

//      System.out.println("\nAdding a count to histogram data: " +
//                         val + " The histogram data is now:\n" );
//      for ( int i = 0; i < data.length; i+=2) {
//         System.out.println(" " +
//                            data[i] + " " + data[i+1]);
//      }
//      Thread.yield();
   }

/**
Overridden to disable.
**/
   
   public void deletData() {
      System.err.println(
       "Attempt to delete data from HistogramDataSet, " +
       "Operation not performed.");
   }
   
/**
Overridden to disable.
**/
   
   public void  delete( int start, int end ) {   
      System.err.println(
       "Attempt to delete data from HistogramDataSet, " +
       "Operation not performed.");
   }

/**
Overridden to disable.
**/
   
   public void append( double d[], int n ) throws Exception {
      System.err.println(
       "Attempt to append data to HistogramDataSet, " +
       "Operation not performed.");
      throw new Exception("DataSet: Error in append data!");
   }

}