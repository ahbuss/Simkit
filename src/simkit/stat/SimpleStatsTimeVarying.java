package simkit.stat;

import java.text.DecimalFormat;
import simkit.Schedule;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class SimpleStatsTimeVarying implements SampleStatistics, PropertyChangeListener {
    
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
    
    public SimpleStatsTimeVarying(String name) {
        this.setFormat(DEFAULT_FORMAT);
        this.setName(name);
        this.reset();
    }
    
    public SimpleStatsTimeVarying() {
        this(DEFAULT_NAME);
    }
    public void newObservation(double x) {
        minObs = (x < minObs) ? x : minObs;
        maxObs = (x > maxObs) ? x : maxObs;
        count++;
        if (count == 1 ) {
            mean = diff;
            variance = 0.0;
        } else if (Schedule.getSimTime() > lastTime) {
            double factor = 1.0 - (lastTime - startTime) / (Schedule.getSimTime() - this.startTime);
            mean += diff * factor;
            variance +=  factor * ( (1.0 - factor) * diff * diff - variance );
        }
        diff = x - mean;
        this.lastTime = Schedule.getSimTime();
    }
    
    public void newObservation(Number x) { this.newObservation( x.doubleValue() ); }
    
    public void newObservation(boolean b) { this.newObservation(b ? 1.0 : 0.0); }
    
    public void newObservation(Boolean b) { this.newObservation(b.booleanValue() ? 1.0 : 0.0); }
    
    public double getMinObs() { return minObs; }
    public double getMaxObs() { return maxObs; }
    public double getMean() {
        if (Schedule.getSimTime() > this.lastTime) {
            newObservation(diff + mean);
        }
        return mean;
    }
    public double getVariance() {
        if (Schedule.getSimTime() > this.lastTime) {
            newObservation(diff + mean);
        }
        return variance;
    }
    public double getStandardDeviation() { return Math.sqrt(getVariance()); }
    public int getCount() { return count; }
    
    public void setFormat(String formatString) {
        df = new DecimalFormat(formatString);
    }
    
    // Implements Named interface
    
    public void setName(String newName) { this.name = newName; }
    public String getName() { return name; }
    
    public SamplingType getSamplingType() { return SamplingType.TIME_VARYING; }
    
    public void setSamplingType(SamplingType type) {  }
    
    public void propertyChange(PropertyChangeEvent event) {
        if (this.getName().equals(event.getPropertyName()) ){
            Object value = event.getNewValue();
            if (value instanceof Number) {
                this.newObservation((Number)event.getNewValue());
            }
            else if (value instanceof Boolean) {
                this.newObservation((Boolean) value);
            }
        }
        else if (event.getPropertyName().equals("reset") &&
                this.getName().equals(event.getNewValue())) {
            this.reset();
        }
    }
    
    public void reset() {
        count = 0;
        diff = 0.0;
        mean = Double.NaN;
        variance = Double.NaN;
        minObs = Double.POSITIVE_INFINITY;
        maxObs = Double.NEGATIVE_INFINITY;
        lastTime = Schedule.getSimTime();
        startTime = Schedule.getSimTime();        
    }
    
    public synchronized Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {}
        return null;
    }
    
    public String getDataLine() {
        StringBuffer buf = new StringBuffer();
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
    
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(getName());
        buf.append(' ');
        buf.append('(');
        buf.append(this.getSamplingType());
        buf.append(')');
        buf.append(EOL);
        
        return buf.toString() + getDataLine();
    }
}
