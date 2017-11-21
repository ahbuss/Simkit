package simkit;

import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import simkit.stat.SavedStats;
import simkit.stat.SampleStatistics;
import simkit.stat.SimpleStatsTally;
/**
 * <p>Abstract Base class for creating Simkit scenarios.</p>
 *
 * <p>A simkit Assembly represents a complete simulation initialized with the 
 * parameters from a single design point of some notional design of experiments.
 * The simulation can be run at that design point any number of times, with
 * statistical data gathered and reported semi-automatically.</p>
 * 
 * <p><code>BasicAssembly</code> is intended to be subclassed, and provides many
 * template methods and hooks that allow your sublcass to provide customized
 * behavior, while also providing the basic functionality of managing multiple
 * simulation runs.</p>
 * 
 * <p>After instantiating your subclass, the design point it represents is run
 * by calling <code>init()</code> and then <code>run</code>.</p>
 * 
 * <p><code>init</code> instantiates all the objects in the simulation and connects
 * the listener pattern connections needed by the model.  You
 * specify what the simulation entities will be by implementing the required template
 * method <code>createSimEntities()</code>.  You also have the opportunity to
 * create instruments for the model in the form of
 * <code>simkit.stat.SampleStatistics</code> instances that you create in the
 * optional methods <code>createReplicationStats()</code> and (less frequently)
 * <code>createDesignPointStats()</code>.  By default, anything you put into
 * the <code>replicationStats</code> array will be summarized without doing
 * anything in <code>createDesignPointStats</code>.</p>
 * 
 * <p>After all your creation methods are called, the hookup methods are called.
 * In the hookup methods, you operate on all the objects previously created
 * to set up the listening pattern.  You do this for two distinct reasons: First,
 * to make the connections that implement your event graph, and second, for data
 * collection (all the stats objects you created need to listen for property
 * changes in order to gather data from the model when it runs.)</p> 
 * 
 * <p>Note. You can add a stats object (or any other PropertyChangeListener as
 * a listener to this object via the protected instance variable
 * <code>propertyChangeSupport</code>. Instead of saying</p>
 * 
 * <p><code>
 * this.addPropertyChangeListener(somePropertychangeListener);
 * </code><br>
 * you say<br>
 * <code>
 * propertyChangeSupport.addPropertyChangeListener(somePropertychangeListener);
 * </code><br></p>
 * 
 * <p>Later, if you want to fire a PropertyChange, you invoke the firing method
 * on <code>propertyChangeSupport</code></p>
 * 
 * <p><code>
 * propertyChangeSupport.firePropertyChange(&lt;args&gt;);
 * </code></p>
 *
 * @version $Id$
 * @author ahbuss
 * @author Kirk Stork, The MOVES Institute
 */
public abstract class BasicAssembly implements Runnable {
    
    public static final Logger log = Logger.getLogger("simkit");
    
    protected LinkedHashMap<Integer, List<SampleStatistics>> replicationData;
    
    protected SampleStatistics[] replicationStats;
    protected SampleStatistics[] designPointStats;
    protected SimEntity[] simEntity;
    protected PropertyChangeListener[] propertyChangeListener;
    
    protected boolean hookupsCalled;
    
    protected boolean stopped;
    
    private double stopTime;
    //same meaning as in simkit.Schedule.
    private boolean reallyVerbose;
    private boolean singleStep;
    private int numberReplications;
    
    private boolean printReplicationReports;
    private boolean printSummaryReport;
    private boolean saveReplicationData;
    
    private int designPointID;
    
    protected PropertyChangeSupport propertyChangeSupport;
    
    private final DecimalFormat form;
    private boolean verbose;
    
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
        propertyChangeSupport = new PropertyChangeSupport(this);
        setPrintReplicationReports(false);
        setPrintSummaryReport(true);
        replicationData = new LinkedHashMap<>();
        simEntity= new SimEntity[0];
        replicationStats = new SampleStatistics[0];
        designPointStats = new SampleStatistics[0];
        propertyChangeListener = new PropertyChangeListener[0];
        setNumberReplications(1);
        hookupsCalled = false;
    }
    
    public void init() {
        createObjects();
        performHookups();
    }
    
    /** <p>Resets all inner stats.  State resetting for SimEntities is their
     * responsibility.  Outer stats are not reset.
     */
    public void reset() {
//        super.reset();
        for (int i = 0; i < replicationStats.length; ++i) {
            replicationStats[i].reset();
        }
        stopped = true;
    }
    
    /**
     * Create all the objects used.  This is called from the constructor.
     * The <code>createSimEntities()</code> method is abstract and will
     * be implemented in the concrete subclass.  The others are empty by
     * default.  The <code>createReplicationStats()</code> method must be
     * overridden if any replications stats are needed.
     */
    protected void createObjects() {
        // template method
        createSimEntities();
        
        // optional template method
        createReplicationStats();
        
        // implemented locally, should not be overridden by subclasses
        createDesignPointStats();
        
        // optional template method
        createPropertyChangeListeners();
    }
    
    /**
     * Call all the hookup methods.
     */
    protected void performHookups() {
        // template method
        hookupSimEventListeners();
        
        // optional template method
        hookupReplicationListeners();
        
        // template method
        hookupDesignPointListeners();
        
        //optional template method
        hookupPropertyChangeListeners();

        hookupsCalled = true;
    }
   
    /**
     * Template method for creating the SimEntities in the model.
     * 
     * This is the first creation method called by createObjects().
     */
    protected abstract void createSimEntities();
    
    /**
     * Template method for hooking up the listening pattern in
     * already-instantiated SimEntities.  This is the first hookup method
     * called by performHookups().
     */
    protected abstract void hookupSimEventListeners();
    
    /**
     * Template method for hooking up listening in replications.  This is
     * usually used for instrumentation of the model.
     * 
     * Called by performHookups after hookupSimEventListeners().
     * 
     */
    protected abstract void hookupReplicationListeners();
    
    /** 
     * Optional method.  Implement if your Assembly has its own PropertyChangeListeners
     * that you want to register with SimEntities that were instantiated in
     * createSimEntities()
     */
    protected void hookupPropertyChangeListeners() {  }
    
    /**
     * The default behavior is to create a <code>SimpleStatsTally</code>
     * instance for each element in <code>replicationStats</code> with the
     * corresponding name + "mean".
     * 
     * When overriding this method, if you call super to retain this
     * behavior, be sure to also call super in <code>hookupDesignPointListeners()</code>
     * or the default hookups won't be made.
     * 
     */
    protected void createDesignPointStats() {
        designPointStats = new SampleStatistics[replicationStats.length];
        for (int i = 0; i < designPointStats.length; ++i) {
            designPointStats[i] = new SimpleStatsTally(replicationStats[i].getName() + ".mean");
        }
    }
    
    /**
     * This hook method allows sub-classes to add instrumentation that 
     * is accumulated during a replication.  The usual practice is to 
     * create stats objects and add them to the protected instance
     * vaiable <code>replicationsStats</code>.  By doing this, your stats object
     * here will be summarized in the output data for each design point (by default).
     * 
     * Any object you create here should be hooked up (i.e. registered to listen
     * for the approprate state changes) in the corresponding hook method
     * <code>hookupReplicationListeners()</code>
     * 
     * Thid method is called from <code>createObjects()</code> after 
     * <code>createSimEntities()</code> and before  <code>createDesignPointStats()</code>
     */
    protected void createReplicationStats() { }
    
    /** 
     * Optional method.  Implement if your Assembly has its own PropertyChangeListeners
     * that you want to register with SimEntities that were instantiated in
     * createSimEntities(). 
     * 
     * This is the last method called by <code>createObjects()</code>
     */
    protected void createPropertyChangeListeners() { }
    
    /** 
     * Set up all outer stats propertyChangeListeners.
     * 
     * Caution.  The default implementation is tied to the default implementation
     * of <code>createDesignPointListeners()</code>.  Therefore, if you override
     * <code>createDesignPointListeners()</code>, you will likely have to override
     * <code>hookupDesignPointListeners()</code>.  If your implementation of
     * <code>createDesignPointListeners()</code> calls super, then be sure to
     * call super here as well.
     */
    protected void hookupDesignPointListeners() {
        for (int i = 0; i < designPointStats.length; ++i) {
            propertyChangeSupport.addPropertyChangeListener(designPointStats[i]);
        }
    }
    
    
    public void setStopTime(double time) {
        if (time < 0.0) {
            throw new IllegalArgumentException("Stop time must be >= 0.0: " + time);
        }
        stopTime = time;
    }
    
    public double getStopTime() { return stopTime; }
        
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
    
    
    /**
    * Determines the value of Schedule.reallyVerbose. If true
    * prints information about scheduling, interrupting, and handling events.
     * @param value Given value
    **/
    public void setReallyVerbose(boolean value) {reallyVerbose = value;}

    /**
    * The value of Schedule.reallyVerbose. 
     * @return  true, if it prints information about scheduling, interrupting, 
     * and handling events.
    **/
    public boolean isReallyVerbose() {return reallyVerbose;}

    /**
     * Empty, implemented here so subclasses don't have to implement
     * (abstract method of BasicSimEntity)
     * @param simEvent Given SimEvent
     */
    public void handleSimEvent(SimEvent simEvent) {
    }
    /**
     * Empty, implemented here so subclasses don't have to implement
     * (abstract method of BasicSimEntity)
     * @param simEvent Given SimEvent
     */
    public void processSimEvent(SimEvent simEvent) {
    }
    
    public SampleStatistics[] getDesignPointStats() { 
        return (SampleStatistics[]) designPointStats.clone();
    } 
    
    public SampleStatistics[] getReplicationStats(int id) {
        SampleStatistics[] stats = null;
        List<SampleStatistics> reps = replicationData.get(id);
        if (reps != null) {
            stats = reps.toArray(new SampleStatistics[0]);
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
    
    public Map<Integer, List<SampleStatistics>> getReplicationData() {
        return new LinkedHashMap<>(replicationData);
    }
    
    /** Save all replicationStats for a given iteration.  This assumes that the
     * replicationData map has been instntaied and initialized.
     */
    protected void saveReplicationStats() {
        for (int i = 0; i < replicationStats.length; ++i) {
            List<SampleStatistics> reps = replicationData.get(i);
            reps.add(new SavedStats(replicationStats[i]));
        }
    }
    
    /**   
     * This can be done generically.
     * @param rep The replication number for this report
     * @return For each inner stats, the name, count, min, max, mean, variance, and
     * standard deviation.
     */
    protected String getReplicationReport(int rep) {
        StringBuilder buf = new StringBuilder("Output Report for Replication #");
        buf.append(rep);
        for (int i = 0; i < replicationStats.length; ++i) {
            buf.append(System.getProperty("line.separator"));
            buf.append(replicationStats[i].getName());
            buf.append('[');
            buf.append(i);
            buf.append(']');
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
    /** This can be done generically.
     * @return For each outer stats,  name, count, min, max, mean, variance, and
     * standard deviation.  
     */
    protected String getSummaryReport() {
         StringBuilder buf = new StringBuilder("Summary Output Report:");
        buf.append(System.getProperty("line.separator"));
        buf.append(super.toString());
        for (int i = 0; i < designPointStats.length; ++i) {
            buf.append(System.getProperty("line.separator"));
            buf.append(designPointStats[i]);
        }
       return buf.toString();
    }
    
    /**
     * These are the actual SimEnties in the array, but the array itself is 
     * a copy.
     * @return the SimEntities in this scenario in a copy of the array.
     */
    public SimEntity[] getSimEntities() { 
        return (SimEntity[]) simEntity.clone(); 
    }
    
    /**
     * @return true of simulation is stopped, false otherwise.
     * 
     * If this method  returns true, the model cannont be restarted, but must
     * be started over from scratch.
     */
    public boolean isStopped() { return stopped; }
    
    /**
     * Stops simulation wherever it is in the loop, providing a means to stop
     * the simulation in mid-run.  This is irreversable and the simulation must
     * be restarted from scratch.
     * 
     * Before this method does anything, the hook method simulationWillBeStopped()
     * is called, giving sub-classes of BasicAssembly the opportunity to
     * clean-up, dump data, close files, notify a gui,  etc.
     */
    public void stop() {
        simulationWillBeStopped();
        stopped = true;
        Schedule.stopSimulation();
    }
    
    /**
     * Execute the simulation for the desired number of replications.
     */
    @Override
    public void run() {
        if (!hookupsCalled) {
            throw new RuntimeException("performHookups() hasn't been called!");
        }
        
        Schedule.stopAtTime(getStopTime());
        if (isVerbose()) {
            Schedule.setVerbose(isVerbose());
        }
        Schedule.setReallyVerbose(isReallyVerbose());
        if (isSingleStep()) {
            Schedule.setSingleStep(isSingleStep());
        }
        
        if (isSaveReplicationData()) {
            replicationData.clear();
            for (int i = 0; i < replicationStats.length; ++i) {
                replicationData.put(i, new ArrayList<>());
            }
        }
        
        stopped = false;
        double startTime = System.currentTimeMillis(); 
        for (int replication = 0; replication < getNumberReplications() && 
                !stopped ; ++replication) {
            scheduleWillReset();
            Schedule.reset();
            scheduleDidReset();
            log.log(Level.INFO, "Starting replication {0}", replication);
            Schedule.startSimulation();
            for (int i = 0; i < replicationStats.length; ++i) {
                propertyChangeSupport.firePropertyChange(new IndexedPropertyChangeEvent(this, replicationStats[i].getName(), null, replicationStats[i], i));
                propertyChangeSupport.firePropertyChange(new IndexedPropertyChangeEvent(this, replicationStats[i].getName()+".mean", null, replicationStats[i].getMean(), i));
            }
            if (isPrintReplicationReports()) {
                System.out.println(getReplicationReport(replication));
            }
            if (isSaveReplicationData()) {
                saveReplicationStats();
            }
            replicationDidFinish();
        }
        if (isPrintSummaryReport()) {
            System.out.println(getSummaryReport());
        }
        double endTime = System.currentTimeMillis();
        log.log(Level.INFO, "Execution time for {0} replications: {1}", 
                new Object[]{getNumberReplications(), (endTime - startTime) * 1.0E-3});
        simulationDidFinish();
    }

    /**
     * Hook method that will be called prior to calling Schedule.reset().
     * 
     * Subclasses should place any custom resetting needed in this method
     */
    protected void scheduleWillReset() {}

    /**
     * Hook method that will be called just after Schedule.reset() and just
     * prior to calling <code>Schedule.startSimlulation()</code>
     * 
     * This method should rarely be needed because simkit idiom is to handle all
     * preparation for a simulation run in the various reset methods.  The
     * notable exception is if there are objects that hold state information
     * in static variables, or use static variables as the way to keep a
     * strong reference to a re-usable SimEntity.  Such objects may need to
     * have a static resetter called on them between replications.
     * 
     * Subclasses may use this place for any custom code that should run just
     * before time begins to advance.
     */
    protected void scheduleDidReset() {}

    /**
     * Hook method called after the event list runs to completion for a replication.
     * 
     * (That is, when startSimulation returns).  Note that this includes situations
     * where the event queue gets stopped and emptied by some external intervention,
     * such as by calling the stop() method on an EventList, Schedule or on an instance
     * of this class.
     */          
    protected void replicationDidFinish() {}
    
    /**
     * Hook method called after all replications have completed.
     */          
    protected void simulationDidFinish() {}
    
    /**
     * Hook method that is called when <code>stop()</code> is called on an
     * Assembly. After this method has had a chance to run, the model will be
     * stoped where ever it is in the loop/replication.
     */
    protected void simulationWillBeStopped() {}

    public boolean isVerbose() {
        return verbose;
    }
    public void setVerbose(boolean yn) {
        this.verbose = yn;
    }
    

    
}
