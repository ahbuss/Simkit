package simkit.stat;

import simkit.stat.SimpleStatsTally;

/**
 * A SimpleStatsTally that resets after a truncation point has been reached.
 *
 * @version $Id$
 * @author ahbuss
 */
public class TruncatingSimpleStatsTally extends SimpleStatsTally {
    
    protected boolean truncated;
    private int truncationPoint;
    
    /**
     * Create a TruncatingSimpleStatsTally with given truncation point.
     * @param truncationPoint Truncation point.
     */
    public TruncatingSimpleStatsTally(int truncationPoint) {
        this(DEFAULT_NAME, truncationPoint);
    }
    
    /**
     * Create a TruncatingSimpleStatsTally with given property name and
     * with given truncation point.
     * @param propertyName Name of property to listen to
     * @param truncationPoint Truncation point
     */
    public TruncatingSimpleStatsTally(String propertyName, int truncationPoint) {
        super(propertyName);
        setTruncationPoint(truncationPoint);
        truncated = false;
    }
    /**
     * @return Truncation point
     */
    public int getTruncationPoint() { return truncationPoint; }
    
    /**
     * @throws IllegalArgumentException if truncationPoint < 0
     * @param truncationPoint Truncation point
     */
    public void setTruncationPoint(int truncationPoint) {
        if (truncationPoint < 0) {
            throw new IllegalArgumentException("Number transients must be >= 0: " +
                    truncationPoint);
        }
        this.truncationPoint = truncationPoint;
        reset();
    }
    
    /**
     * @return Whether truncation point has been reached or not
     */
    public boolean isTruncated() { return truncated; }
    
    /** Set to not "warmed up"
     */
    public void reset() {
        super.reset();
        truncated = false;    }

    /**
     * 
     * Update counters (in super class).  If truncation point has been reached,
     * reset all counters and set truncated to true.
     * @param x New observation value
     */
    public void newObservation(double x) {
        super.newObservation(x);
        if (!isTruncated() && this.getCount() >= this.getTruncationPoint()) {
            this.reset();
            truncated = true;
        }
    }
        
    /**
     * @return String desciption, including all stats.
     */
    public String toString() {        
        return getName() + " (TALLY) [truncation = " + getTruncationPoint() + 
            "]" + System.getProperty("line.separator") + getDataLine();
    }
}