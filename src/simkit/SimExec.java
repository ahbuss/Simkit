package simkit;

/**
 *
 * @author ahbuss
 */
public class SimExec implements Runnable {

    private boolean verbose;
    private boolean singleStep;
    private double stopTime;
    private int numberRuns;
    
    private BasicEventList eventList;

    public SimExec() {
        this(Schedule.getDefaultEventList());
    }
    
    public SimExec(BasicEventList eventList) {
        this.eventList = eventList;
        setVerbose(false);
        setSingleStep(false);
        setStopTime(100.0);
        setNumberRuns(1);
    }

    @Override
    public void run() {
        this.eventList.stopAtTime(getStopTime());
        this.eventList.setSingleStep(isSingleStep());
        this.eventList.setVerbose(isVerbose());
        for (int i = 0; i < getNumberRuns(); ++i) {
            this.eventList.reset();
            this.eventList.startSimulation();
        }
    }

    public void setVerbose(boolean b) {
        verbose = b;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setSingleStep(boolean b) {
        singleStep = b;
    }

    public boolean isSingleStep() {
        return singleStep;
    }

    public void setStopTime(double time) {
        if (time >= 0.0) {
            stopTime = time;
        } else {
            throw new IllegalArgumentException("Stop time must be >= 0.0: " + time);
        }
    }

    public double getStopTime() {
        return stopTime;
    }

    public void setNumberRuns(int n) {
        if (n > 0) {
            numberRuns = n;
        } else {
            throw new IllegalArgumentException("Must have at least 1 run: " + n);
        }
    }

    public int getNumberRuns() {
        return numberRuns;
    }

    @Override
    public String toString() {
        return simkit.util.DefaultToStringer.getToString(this);
    }
}
