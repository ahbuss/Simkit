package simkit;

import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import simkit.BasicSimEntity;
import simkit.Schedule;
import simkit.SimEntity;
import simkit.SimEvent;
import simkit.stat.SavedStats;
import simkit.stat.SampleStatistics;
import simkit.stat.SimpleStatsTally;
/**
 * Base class for creating Simkit scenarios.
 *
 * @version $Id$
 * @author ahbuss
 */
public abstract class BasicAssembly extends BasicSimEntity implements Runnable{
    
    protected LinkedHashMap replicationData;
    
    protected SampleStatistics[] replicationStats;
    protected SampleStatistics[] designPointStats;
    protected SimEntity[] simEntity;
    protected PropertyChangeListener[] propertyChangeListener;
    
    protected boolean hookupsCalled;
    
    private double stopTime;
    private boolean verbose;
    private boolean singleStep;
    private int numberReplications;
    
    private boolean printReplicationReports;
    private boolean printSummaryReport;
    private boolean saveReplicationData;
    
    private int designPointID;
    
    private DecimalFormat form;
    
    /**
     * Default constructor sets paameters of BasicAssembly to their
     * default values.  These are:
     *<pre>
     * printReplicationReports = false
     * printSummaryReport = true
     * saveReplicationData = false
     * numberReplications = 1
     *</pre>
     * 
     */
    public BasicAssembly() {
        form = new DecimalFormat("0.0000");
        setPrintReplicationReports(false);
        setPrintSummaryReport(true);
        replicationData = new LinkedHashMap();
        simEntity= new SimEntity[0];
        replicationStats = new SampleStatistics[0];
        designPointStats = new SampleStatistics[0];
        propertyChangeListener = new PropertyChangeListener[0];
        setNumberReplications(1);
        hookupsCalled = false;
    }
    
    /** <p>Resets all inner stats.  State resetting for SimEntities is their
     * responsibility.  Outer stats are not reset.
     */
    public void reset() {
        super.reset();
        for (int i = 0; i < replicationStats.length; ++i) {
            replicationStats[i].reset();
        }
    }
    
    protected void performHookups() {
        hookupSimEventListeners();
        hookupReplicationListeners();
        createDesignPointStats();
        hookupOuterStatsListeners();
        hookupPropertyChangeListeners();
        hookupsCalled = true;
    }
   
    protected abstract void hookupSimEventListeners();
    
    protected abstract void hookupReplicationListeners();
    
    /** This method is left concrete so subclasses don't have to worry about
     * it if no additional PropertyChangeListeners are desired.
     */
    protected void hookupPropertyChangeListeners() {  }
    
    protected void createDesignPointStats() {
        designPointStats = new SampleStatistics[replicationStats.length];
        for (int i = 0; i < designPointStats.length; ++i) {
            designPointStats[i] = new SimpleStatsTally(replicationStats[i].getName() + ".mean");
        }
    }
    
    /** Set up all outer stats propertyChangeListeners
     */
    protected void hookupOuterStatsListeners() {
        for (int i = 0; i < designPointStats.length; ++i) {
            this.addPropertyChangeListener(designPointStats[i]);
        }
    }
    
    
    public void setStopTime(double time) {
        if (time < 0.0) {
            throw new IllegalArgumentException("Stop time must be >= 0.0: " + time);
        }
        stopTime = time;
    }
    
    public double getStopTime() { return stopTime; }
    
    public void setVerbose(boolean b) { verbose = b; }
    
    public boolean isVerbose() { return verbose; }
    
    public void setSingleStep(boolean b) { singleStep = b; }
    
    public boolean isSingleStep() { return singleStep; }
    
    public void setNumberReplications(int num) {
        if (num < 1) {
            throw new IllegalArgumentException("Number replications must be > 0: " + num);
        }
        numberReplications = num;
    }
    
    public int getNumberReplications() { return numberReplications; }
    
    public void setPrintReplicationReports(boolean b) { printReplicationReports = b; }
    
    public boolean isPrintReplicationReports() { return printReplicationReports; }
    
    public void setPrintSummaryReport(boolean b) { printSummaryReport = b; }
    
    public boolean isPrintSummaryReport() { return printSummaryReport; }
    
    public void setDesignPointID(int id) { designPointID = id; }
    
    public int getDesignPointID() { return designPointID; }
    
    public void setSaveReplicationData(boolean b) { saveReplicationData = b; }
    
    public boolean isSaveReplicationData() { return saveReplicationData; }
    
    /** Empty, needed to implement SimEntity
     */
    public void handleSimEvent(SimEvent simEvent) {
    }
    /** Empty, needed to implement SimEntity
     */
    public void processSimEvent(SimEvent simEvent) {
    }
    
    public SampleStatistics[] getDesignPointStats() { 
        return (SampleStatistics[]) designPointStats.clone();
    } 
    
    public SampleStatistics[] getReplicationStats(int id) {
        SampleStatistics[] stats = null;
        ArrayList reps = (ArrayList) replicationData.get(new Integer(id));
        if (reps != null) {
            stats = (SampleStatistics[]) reps.toArray(new SampleStatistics[0]);
        }
        return stats;
    }
    
    public SampleStatistics getReplicationStat(String name, int replication) {
        SampleStatistics stats = null;
        int id = getIDforReplicationStateName(name);
        if (id >= 0) {
            stats = getReplicationStats(id)[replication];
        }
        return stats;
    }
    
    public int getIDforReplicationStateName(String state) {
        int id = -1;
        for (int i = 0; i < replicationStats.length; ++i) {
            if (replicationStats[i].getName().equals(state)) {
                id = i;
                break;
            }
        }
        return id;
    }
    
    public Map getReplicationData() {
        return new LinkedHashMap(replicationData);
    }
    
    /** Save all replicationStats for a given iteration.  This assumes that the
     * replicationData map has been instntaied and initialized.
     */
    protected void saveReplicationStats() {
        for (int i = 0; i < replicationStats.length; ++i) {
            ArrayList reps = (ArrayList) replicationData.get(new Integer(i));
            reps.add(new SavedStats(replicationStats[i]));
        }
    }
    
    /** For each inner stats, print name, count, min, max, mean, variance, and
     * standard deviation.  This can be done generically.
     * @param rep The replication number for this report
     */
    protected String getReplicationReport(int rep) {
        StringBuffer buf = new StringBuffer("Output Report for Replication #");
        buf.append(rep);
        for (int i = 0; i < replicationStats.length; ++i) {
            buf.append(System.getProperty("line.separator"));
            buf.append(replicationStats[i].getName());
            buf.append('\t');
            buf.append(replicationStats[i].getCount());
            buf.append('\t');
            buf.append(form.format(replicationStats[i].getMinObs()));
            buf.append('\t');
            buf.append(form.format(replicationStats[i].getMaxObs()));
            buf.append('\t');
            buf.append(form.format(replicationStats[i].getMean()));
            buf.append('\t');
            buf.append(form.format(replicationStats[i].getVariance()));
            buf.append('\t');
            buf.append(form.format(replicationStats[i].getStandardDeviation()));
        }
        return buf.toString();
    }
    /** For each outer stats, print name, count, min, max, mean, variance, and
     * standard deviation.  This can be done generically.
     */
    protected String getSummaryReport() {
        StringBuffer buf = new StringBuffer("Summary Output Report");
        buf.append(System.getProperty("line.separator"));
        buf.append("StopTime: " + getStopTime());
        buf.append(System.getProperty("line.separator"));
        buf.append("Number Replications: " + getNumberReplications());
        for (int i = 0; i < designPointStats.length; ++i) {
            buf.append(System.getProperty("line.separator"));
            buf.append(designPointStats[i].getName());
            buf.append('\t');
            buf.append(designPointStats[i].getCount());
            buf.append('\t');
            buf.append(form.format(designPointStats[i].getMinObs()));
            buf.append('\t');
            buf.append(form.format(designPointStats[i].getMaxObs()));
            buf.append('\t');
            buf.append(form.format(designPointStats[i].getMean()));
            buf.append('\t');
            buf.append(form.format(designPointStats[i].getVariance()));
            buf.append('\t');
            buf.append(form.format(designPointStats[i].getStandardDeviation()));
        }
        return buf.toString();
    }
    
    public void run() {
        if (!hookupsCalled) {
            throw new RuntimeException("performHookups() hasn't been called!");
        }
        
        Schedule.stopAtTime(getStopTime());
        if (isVerbose()) {
            Schedule.setVerbose(isVerbose());
        }
        if (isSingleStep()) {
            Schedule.setSingleStep(isSingleStep());
        }
        
        if (isSaveReplicationData()) {
            replicationData.clear();
            for (int i = 0; i < replicationStats.length; ++i) {
                replicationData.put(new Integer(i), new ArrayList());
            }
        }
        
        for (int replication = 0; replication < getNumberReplications(); ++replication) {
            Schedule.reset();
            Schedule.startSimulation();
            for (int i = 0; i < replicationStats.length; ++i) {
                fireIndexedPropertyChange(i, replicationStats[i].getName(), replicationStats[i]);
                firePropertyChange( replicationStats[i].getName() + ".mean" , replicationStats[i].getMean());
            }
            if (isPrintReplicationReports()) {
                System.out.println(getReplicationReport(replication));
            }
            if (isSaveReplicationData()) {
                saveReplicationStats();
            }
        }
        if (isPrintSummaryReport()) {
            System.out.println(getSummaryReport());
        }
    }
    
}
