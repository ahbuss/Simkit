package simkit.stat;

import simkit.EventListAware;
import simkit.BasicEventList;
import simkit.Schedule;

/**
 * Basic class for collecting statistics on time varying properties. The
 * statistics calculated are time weighted. Automatically gets the time of the
 * observations from <CODE>EventList</CODE>.
 * <P>
 * Getting the mean, variance, or standard deviation will cause a new
 * observation to be added to account for the time since the last observation,
 * therefore the value returned by <CODE>getCount</CODE> may not correspond to
 * the number of times <CODE>newObservation</CODE> has been called.
 * </P>
 *
 * @version $Id$
 */
public class SimpleStatsTimeVarying extends AbstractSimpleStats
        implements EventListAware {

    /**
     * The time weighted mean of the statistic.
     */
    protected double mean;

    /**
     * The time weighted variance of the statistic.
     */
    protected double variance;

    /**
     * The difference between the current observation and the mean.
     */
    protected double diff;

    /**
     * The time of the last observation.
     */
    protected double lastTime;

    /**
     * The time of the first observation.
     */
    protected double startTime;

    protected BasicEventList eventList;

    /**
     * Constructs a new SimpleStatsTimeVarying with the given name.
     *
     * @param name The name of the property to collect statistics for.
     */
    public SimpleStatsTimeVarying(String name) {
        super(name);
        setEventListID(Schedule.getDefaultEventList().getID());
        reset();
    }

    /**
     * Constructs a new SimpleStatsTimeVarying with the default name. Note: The
     * name can be set later by calling <CODE>setName()</CODE>.
     */
    public SimpleStatsTimeVarying() {
        this(DEFAULT_NAME);
    }
    
    public SimpleStatsTimeVarying(SimpleStatsTimeVarying original) {
        this.mean = original.getMean();
        this.variance = original.getVariance();
        this.count = original.getCount();
        this.diff = original.diff;
        this.minObs = original.getMinObs();
        this.maxObs = original.getMaxObs();
        this.numberFormat = original.numberFormat;
        this.name = original.getName();
        this.lastTime = original.lastTime;
        this.eventList = original.getEventList();
        this.startTime = original.getStartTime();
    }

    @Override
    public void newObservation(double x) {
        if (!Double.isNaN(x)) {
            super.newObservation(x);
            if (count == 1) {
                mean = diff;
                variance = 0.0;
            } else if (eventList.getSimTime() > lastTime) {
                double factor = 1.0 - (lastTime - getStartTime()) / (eventList.getSimTime() - this.getStartTime());
                mean += diff * factor;
                variance += factor * ((1.0 - factor) * diff * diff - variance);
            }
            diff = x - mean;
            this.lastTime = eventList.getSimTime();
        }
    }

    @Override
    public double getMean() {
        if (eventList.getSimTime() > this.lastTime) {
            newObservation(diff + mean);
        }
        return mean;
    }

    @Override
    public double getVariance() {
        if (eventList.getSimTime() > this.lastTime) {
            newObservation(diff + mean);
        }
        return variance;
    }

    @Override
    public double getStandardDeviation() {
        return Math.sqrt(getVariance());
    }

    /**
     * 
     * @return the SamplingType for this SampleStatistic which is always
     * <CODE>TIME_VARYING</CODE>.
     */
    @Override
    public SamplingType getSamplingType() {
        return SamplingType.TIME_VARYING;
    }

    /**
     * Does nothing since the SamplingType is always <CODE>TIME_VARYING</CODE>.
     * @param type Given type (ignored)
     */
    @Override
    public void setSamplingType(SamplingType type) {
    }

    @Override
    public void reset() {
        super.reset();
        diff = 0.0;
        mean = Double.NaN;
        variance = Double.NaN;
        lastTime = eventList.getSimTime();
        startTime = eventList.getSimTime();
    }

    @Override
    public void setEventListID(int id) {
        eventList = Schedule.getEventList(id);
    }

    @Override
    public void setEventList(BasicEventList list) {
        this.eventList = list;
    }

    @Override
    public int getEventListID() {
        return eventList.getID();
    }

    @Override
    public BasicEventList getEventList() {
        return eventList;
    }

    public double getStartTime() {
        return startTime;
    }
}
