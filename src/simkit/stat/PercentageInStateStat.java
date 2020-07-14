package simkit.stat;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import simkit.Schedule;

/**
 * Listens for states of a given name and computes the percentage of time spent
 * in each state. The assumption is that the name of the property can be in one,
 * and only one, of a number of discrete states at a time. The actual states are
 * determined on-the-fly, but an initial state is required to be given.
 *
 * @author ahbuss
 */
public class PercentageInStateStat implements PropertyChangeListener {

    private NumberFormat numberFormat;

    private Object initialState;

    private String propertyName;

    protected Map<Object, Double> stateStats;

    protected Object currentState;

    protected double lastTime;

    protected int stateNameLength;

    /**
     * Instantiate a PercentageInState with given name and initial state
     *
     * @param propertyName Name of the property to be listened for
     * @param initialState Initial state, set in reset()
     */
    public PercentageInStateStat(String propertyName, Object initialState) {
        setPropertyName(propertyName);
        setNumberFormat(AbstractSimpleStats.DEFAULT_NUMBER_FORMAT);
        stateStats = new TreeMap<>();
        setInitialState(initialState);
        reset();
    }

    /**
     * Does not set the initial state. It is the responsibility of the source
     * component to fire a property change with the initial state at time 0.0
     *
     * @param propertyName Name of the property to be listened for
     */
    public PercentageInStateStat(String propertyName) {
        setPropertyName(propertyName);
        setNumberFormat(AbstractSimpleStats.DEFAULT_NUMBER_FORMAT);
        stateStats = new TreeMap<>();
        reset();
    }

    /**
     * Should be called after Schedule.reset(). Sets currentState to
     * initialState, total time to 0.0, and last time to simTime
     */
    public final void reset() {
        stateStats.clear();
        currentState = getInitialState();
        lastTime = Schedule.getSimTime();
        if (currentState != null) {
            stateStats.put(currentState, 0.0);
        }
        stateNameLength = 5;
    }

    /**
     * If this is my state name, call newObservation() with newValue
     *
     * @param propertyChangeEvent Heard PropertyChangeEvent
     */
    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (propertyChangeEvent.getPropertyName().equals(getPropertyName())) {
            newObservation(propertyChangeEvent.getNewValue());
        }
    }

    /**
     * Update statistics on old state; set newState and lastTime
     *
     * @param newState New State
     */
    public void newObservation(Object newState) {
        Object oldState = getCurrentState();
        if (oldState != null) {
            double oldTimeInState = stateStats.get(oldState);
            double timeInThisState = Schedule.getSimTime() - getLastTime();
            stateStats.put(oldState, oldTimeInState + timeInThisState);
        }
        lastTime = Schedule.getSimTime();

        currentState = newState;
        Double newTimeInState = stateStats.get(currentState);
        if (newTimeInState == null) {
            newTimeInState = 0.0;
            stateStats.put(currentState, newTimeInState);
            stateNameLength = Math.max(stateNameLength, currentState.toString().length());
        }
    }

    public Object getInitialState() {
        return initialState;
    }

    public final void setInitialState(Object initialState) {
        this.initialState = initialState;
    }

    /**
     * @return a String representation of all the data so far
     */
    public String stateString() {
        newObservation(getCurrentState());
        StringBuilder builder = new StringBuilder("State");
        for (int i = 0; i < getStateNameLength() / 8; ++i) {
            builder.append('\t');
        }
        builder.append("% In State");
        builder.append(System.getProperty("line.separator"));
        double sum = 0.0;

        for (Iterator i = stateStats.keySet().iterator(); i.hasNext();) {
            Object state = i.next();
            builder.append(state);
            int extra = getStateNameLength() - state.toString().length();
            int extraTabs = (int) Math.ceil(extra / 8.0);
            builder.append('\t');
            for (int j = 0; j < extraTabs; ++j) {
                builder.append('\t');
            }
            double mean = getPercentageFor(state);
            builder.append(numberFormat.format(mean));
            builder.append(System.getProperty("line.separator"));
            sum += mean;
        }
        builder.append("Total");
        int extra = getStateNameLength() - 5;
        int extraTabs = (int) Math.ceil(extra / 8.0);
        builder.append('\t');
        for (int j = 0; j < extraTabs; ++j) {
            builder.append('\t');
        }
        builder.append(numberFormat.format(sum));
        return builder.toString();
    }

    public Map<Object, Double> getStateStats() {
        return new LinkedHashMap<>(stateStats);
    }

    public Object getCurrentState() {
        return currentState;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public final void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public double getLastTime() {
        return lastTime;
    }

    public Object[] getObservedStates() {
        return stateStats.keySet().toArray();
    }

    public double getPercentageFor(Object state) {
        if (state.equals(getCurrentState())) {
            newObservation(state);
        }
        double percentage = 0.0;
        Double timeInThisState = stateStats.get(state);
        if (timeInThisState != null) {
            percentage = timeInThisState / Schedule.getSimTime();
        }
        return percentage;
    }

    public int getStateNameLength() {
        return stateNameLength;
    }

    public NumberFormat getNumberFormat() {
        return numberFormat;
    }

    public final void setNumberFormat(NumberFormat numberFormat) {
        this.numberFormat = numberFormat;
    }

}
