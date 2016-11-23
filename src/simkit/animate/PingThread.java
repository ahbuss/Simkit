package simkit.animate;

import simkit.Priority;
import simkit.Schedule;
import simkit.SimEntityBase;

/**
 * <P>An extremely simple way to animate Simkit programs.
 * a Ping event occurs every deltaT units of simulated time, which
 * correspond roughly to millisPerSimTime milliseconds of real time 
 * (your mileage may vary).  Any listeners to Ping may do as they 
 * wish, such as updating the position of units drawn on a screen.
 *
 * @version $Id$
 * @author abuss
 **/

public class PingThread extends SimEntityBase 
        implements Runnable, VCRController {
    
    // Defaults
    
    public static final double DEFAULT_DELTA_T = 0.1;
    public static final double DEFAULT_MILLIS_PER_SIM_TIME = 10.0;
    public static final double DEFAULT_ADJUST_FACTOR = 0.5;
    
    /** 
     * Time between Ping events
     */
    private double deltaT;
    /**
     * Real time per simulated time
     */
    private double millisPerSimtime;   
    /**
     * Adjusts sleep time to try to match target rate
     */
    private double adjustFactor;
    
    /**
     * Actual amount of time taken by a time step
     */
    private long realTimeStep;
    
    /**
     * Clock time a step was started.  Used to adjust sleep time
     */
    protected long startStep;
    
    /**
     * True if currently active
     */
    protected boolean pinging;         
    
    /**
     * True if Schedule.reset() has been called by this instance.
     */
    protected boolean resetOnce;
    
    public PingThread(double deltaT, double millisPerSimtime, 
            boolean pinging) {
        this.setMillisPerSimtime(millisPerSimtime);
        this.setDeltaT(deltaT);
        this.setAdjustFactor(DEFAULT_ADJUST_FACTOR);
    }
    
    public PingThread(double deltaT, double millisPerSimTime) {
        this(deltaT, millisPerSimTime, false);
    }
    
    public PingThread() {
        this.setDeltaT(DEFAULT_DELTA_T);
        this.setMillisPerSimtime(DEFAULT_MILLIS_PER_SIM_TIME);
    }
    
    /**
     * Reset state: pinging and resetOnce to false
     */
    @Override
    public void reset() {
        pinging = false;
        resetOnce = false;
    }
    
    /**
     *  Start pinging and and wait forever (or until the Thread is 
     * stopped).  If Schedule.reset() has not been called, call it.
     **/
    public void startPinging() {
        if (!this.isResetOnce()) {
            Schedule.reset();
            resetOnce = true;
        }
        
        if (!this.isPinging()) {
            new Thread(this).start();
        }
    }
    
    /**
     * Thread entry point.  
     */
    @Override
    public void run() {
        pinging = true;
        if(Schedule.getPauseAfterEachEvent()) {
            Schedule.setPauseAfterEachEvent(false);
        }
        waitDelay("Ping", 0.0);
        startStep = System.currentTimeMillis();
        if (!Schedule.isRunning()) {
            Schedule.startSimulation();
        }
    }
    
    @Override
    public void step() {
        pinging = true;
        resetOnce=true;
        if(!Schedule.getPauseAfterEachEvent()) {
            Schedule.setPauseAfterEachEvent(true);
        }
        waitDelay("Ping", 1.0, Priority.LOW);
        startStep = System.currentTimeMillis();
        if (!Schedule.isRunning()) {
            Schedule.startSimulation();
            this.stop();
        }
    }
    
    /**
     *  Stops Pinging and cancels next Ping event
     **/
    public void stopPinging() {
        pinging = false;
        this.interrupt("Ping");
    }
    
    /**
     * The main point of the class is the Ping event, which 
     * actually does nothing in and of itself other than 
     * schedule the next Ping event.  Note that the sleep time 
     * is the number of milliseconds equivalent to deltaT, as 
     * specified by the user.
     **/
    @SuppressWarnings("SleepWhileHoldingLock")
    public synchronized void doPing() {
//        This is just some stuff to see about better 
//        synchronization with the "real"
//        clock.  The idea is that the PingThread will determine 
//        how far off it is at each Ping and adjust the sleep time
//        accordingly.
        if (isPinging()) {
            waitDelay("Ping", deltaT);
            if (millisPerSimtime > 0) {
                realTimeStep = System.currentTimeMillis() - startStep;
                long offBy = (long) (realTimeStep - getDesiredPingTime());
                firePropertyChange("realTimeStep", new Long(realTimeStep));
                firePropertyChange("offby", new Long(offBy));
                startStep = System.currentTimeMillis();
                
                try {
                    Thread.sleep((long) Math.abs(getDesiredPingTime() - 
                            getAdjustFactor() * offBy));
                } catch (InterruptedException e) {}
            }
        }
//        Save the current time to adjust time step on next Ping
    }
    
    @Override
    public void stop() {
        Schedule.pause();
        interruptAll("Ping");
        pinging = false;
    }
    
    public void resume() { this.startPinging(); }
    
    @Override
    public void rewind() {
        this.pinging = false;
        Schedule.stopSimulation();
        Schedule.reset();
    }
    
    @Override
    public void setDeltaT(double dt) {deltaT = dt;}
    
    @Override
    public void setMillisPerSimtime(double mpst) {
        millisPerSimtime = mpst;
    }
    
    public double getDeltaT() {return deltaT;}
    public double getMillisPerSimtime() {return millisPerSimtime;}
    
    public boolean isPinging() {return pinging;}
    public boolean isResetOnce() { return resetOnce; }
    
    public double getDesiredPingTime() { 
        return deltaT * millisPerSimtime; 
    }
    
    @Override
    public void start() {
        startPinging();
    }
    
    @Override
    public void pause() {
        //stopPinging();
        Schedule.getEventList(0).pause();
        interruptAll("Ping");
        pinging = false;
        
    }
    
    /**
     * Go at full speed (no "Pings")
     */
    @Override
    public void fastForward() { 
        interruptAll("Ping");
        pinging = false;
    }
    
    public void setAdjustFactor(double factor) { 
        adjustFactor = factor; 
    }
    
    public double getAdjustFactor() { 
        return adjustFactor; 
    }
}