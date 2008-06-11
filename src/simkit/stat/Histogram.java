package simkit.stat;

import java.text.DecimalFormat;

/**
* A class that collects statistics that can be used to produce a histogram.
* This class does not draw the histogram, it simply calculates frequency and
* proportion data.
* @version $Id$
**/
public class Histogram extends SimpleStatsTally {

/**
* Contains the number of observations that fall into each bin.
**/
    private int[] frequency;

/**
* Contains the total number of observations.
**/
    private int total;

/**
* Contains the lower bound of the first bin.
**/
    private double lowValue;

/**
* Contains the bin width.
**/
    private double cellWidth;

/**
* Creates a new Histogram with a lower bound of 0 and a
* bin width of 1. This implies that the upper bound is equal to
* the number of bins.
* @param name The name of the property for which this Histogram collects statistics.
* @param number The number of bins.
**/
    public Histogram(String name, int number) {
        this(name, 0, (double) number, number);
    }

/**
* Creates a new Histogram.
* @param name The name of the property for which this Histogram collects statistics.
* @param lowVal The lower bound of the first bin.
* @param hiVal The upper bound of the last bin.
* @param number The number of bins.
**/
    public Histogram(String name, double lowVal, double hiVal, int number) {
        super(name);
        frequency = new int[number + 1];
        lowValue = lowVal;
        cellWidth = (hiVal - lowVal) /  number;
    }

//Javadoc inherited.    
    public void newObservation(double x) {
        super.newObservation(x);
        int index = (int) Math.floor(( x - lowValue) / cellWidth);
        index = Math.min( Math.max(index, 0), frequency.length - 1);
        frequency[index]++;
        total++;
    }
    
//Javadoc inherited.    
    public void reset() {
        if (frequency != null) {
            frequency = new int[frequency.length];
        }
        total = 0;
    }

/**
* Returns a copy of the array of bin frequencies.
**/
    public int[] getFrequencies() { return (int[]) frequency.clone(); }

/**
* Returns an array containing the proportion of observations in each bin.
* Note that if any observations were less than the lower bound or 
* greater than the upper bound, then the proportions will not add
* up to 1.
**/
    public double[] getProportions() {
        double[] temp = new double[frequency.length];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = frequency[i] / (double) total;
        }
        return temp;
    }

/**
* Returns a table with the frequencies and proportions in each bin.
**/
    public String toString() {
        DecimalFormat form = new DecimalFormat("0.000");
        StringBuffer buf = new StringBuffer(super.toString());
        buf.append("\nvalue\tFrequencies\tProportions\n");
        double[] prop = getProportions();
        for (int i = 0; i < frequency.length; i++ ) {
//            buf.append(i);
//            buf.append('\t');
            buf.append(form.format(lowValue + cellWidth * i));
            buf.append('\t');
            buf.append(frequency[i]);
            buf.append('\t');
            buf.append(prop[i]);
            buf.append(SampleStatistics.EOL);
        }
        return buf.toString();
    }

}
