package simkit.stat;

import java.text.DecimalFormat;
import simkit.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class SimpleStatsTally implements SampleStatistics, PropertyChangeListener {

    private static String DEFAULT_NAME = "%unnamed%";
    private static String DEFAULT_FORMAT = " 0.0000;-0.0000";

    private int count;
    private double mean;
    private double variance;
    private double minObs;
    private double maxObs;
    private double diff;

    private double lastTime;
    private double startTime;

    private String name;
    private DecimalFormat df;

    public SimpleStatsTally() {
        this.setFormat(DEFAULT_FORMAT);
        this.setName(DEFAULT_NAME);
        this.reset();
    }

    public void newObservation(double x) {

        minObs = (x < minObs) ? x : minObs;
        maxObs = (x > maxObs) ? x : maxObs;

        count++;
        diff = x - mean;
        variance += (count == 1) ? 0.0 : diff * diff / count - 1.0 / (count - 1) * variance;
        mean += diff / count;
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

    public SamplingType getSamplingType() { return SamplingType.TALLY; }

    public void setSamplingType(SamplingType type) {  }

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
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(getName());
        buf.append(' ');
        buf.append('(');
        buf.append(this.getSamplingType());
        buf.append(")\n");
        buf.append(getCount());
        buf.append(' ');
        buf.append(df.format(getMinObs()));
        buf.append(' ');
        buf.append(df.format(getMaxObs()));
        buf.append(' ');
        buf.append(df.format(getMean()));
        buf.append(' ');
        buf.append(df.format(getVariance()));
        buf.append(' ');
        buf.append(df.format(getStandardDeviation()));

        return buf.toString();
   }

}