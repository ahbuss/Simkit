package simkit.data;

/**
 *	<P> A class for collecting data on the fly and reporting basic
 *	summary statistics.
 *
 *  <P> 09 August 2000 - Modified per Paul Sanchez' algorithm
 *
 * @deprecated This class has been replaced by <CODE>simkit.random.SimpleStats2</CODE>
 * @see simkit.random.SimpleStats2
 *	@author Arnie Buss
**/

import java.text.*;
import simkit.*;
import java.beans.*;

public class SimpleStats2 implements Named,
                                    SampleStatistics, 
                                    PropertyChangeListener {

    private static String DEFAULT_NAME = "unnamed";
    private static SamplingType DEFAULT_SAMPLING_TYPE = SamplingType.TALLY;

    private int count;
    private double mean;
    private double variance;
    private double minObs;
    private double maxObs;
    private double diff;
    
    private double lastTime;
    private double startTime;

    private String name;
    private SamplingType samplingType;

    private DecimalFormat df;

    public SimpleStats2(String name, SamplingType type) {
        this.setName(name);
        this.setSamplingType(type);
        setFormat(" 0.0000;-0.0000");
    }

    public SimpleStats2(String name) {
        this(name, DEFAULT_SAMPLING_TYPE);
    }

    public SimpleStats2(SamplingType type) {
        this(DEFAULT_NAME, type);
    }

    public SimpleStats2() {
        this(DEFAULT_NAME, DEFAULT_SAMPLING_TYPE);
    }

    public void newObservation(double x) {

        minObs = (x < minObs) ? x : minObs;
        maxObs = (x > maxObs) ? x : maxObs;

        count++;
        if (samplingType == SamplingType.TIME_VARYING) {
            double ratio = Schedule.getSimTime() > 0.0 ? lastTime / Schedule.getSimTime() : 0.0;
            variance =  ratio * ( variance + (1.0 - ratio) * diff * diff);
            mean += (1.0 - ratio) * diff;
        }
        lastTime = Schedule.getSimTime();
        diff = x - mean;
        if (samplingType == SamplingType.TALLY) {
            variance += (count == 1) ? 0.0 : diff * diff / count - 1.0 / (count - 1) * variance;
            mean += diff / count;
        }
    }

    public void newObservation(Number x) { this.newObservation( x.doubleValue() ); }

    public double getMinObs() { return minObs; }
    public double getMaxObs() { return maxObs; }
    public double getMean() { return mean; }
    public double getVariance() { return variance; }
    public double getStandardDeviation() { return Math.sqrt(getVariance()); }
    public int getCount() { return count; } 


    public void setFormat(String formatString) {
        df = new DecimalFormat(formatString);
    }

// Implements Named interface

    public void setName(String newName) { this.name = newName; }
    public String getName() { return name; }

    public SamplingType getSamplingType() { return samplingType; }
    public void setSamplingType(SamplingType type) {
        samplingType = type;
        reset();
    }

    public void propertyChange(PropertyChangeEvent event) {
        if (this.getName().equals(event.getPropertyName()) ){
            newObservation(Double.valueOf(event.getNewValue().toString()));
        }
    }

    public void reset() {
        count = 0;
        diff = 0.0;
        mean = 0.0;
        variance = 0.0;
        minObs = Double.POSITIVE_INFINITY;
        maxObs = Double.NEGATIVE_INFINITY;
        lastTime = Schedule.getSimTime();
        startTime = Schedule.getSimTime();
    }

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

}
