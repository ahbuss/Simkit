package simkit.stat;

import java.text.DecimalFormat;
import simkit.Schedule;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class SimpleStatsTimeVarying extends AbstractSimpleStats {
    
    private double mean;
    private double variance;
    private double diff;
    
    private double lastTime;
    private double startTime;
    
    public SimpleStatsTimeVarying(String name) {
        super(name);
    }
    
    public SimpleStatsTimeVarying() {
        this(DEFAULT_NAME);
    }
    public void newObservation(double x) {
        super.newObservation(x);
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
   
    public SamplingType getSamplingType() { return SamplingType.TIME_VARYING; }
    
    public void setSamplingType(SamplingType type) {  }
    
    public void reset() {
        super.reset();
        diff = 0.0;
        mean = Double.NaN;
        variance = Double.NaN;
        lastTime = Schedule.getSimTime();
        startTime = Schedule.getSimTime();        
    }
}
