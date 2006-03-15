package simkit.stat;

/**
 * @version $Id$
 * @author ahbuss
 */
public class TruncatingSimpleStatsTimeVarying extends SimpleStatsTimeVarying {
    
    protected boolean truncated;
    
    private double truncationPoint;

    public TruncatingSimpleStatsTimeVarying(String name, double truncationPoint) {
        super(name);
        setTruncationPoint(truncationPoint);
        truncated = false;
    }

    public TruncatingSimpleStatsTimeVarying(double truncationPoint) {
        this(DEFAULT_NAME, truncationPoint);
    }
    
    public void reset() {
        super.reset();
        truncated = false;
    }
    
    public void newObservation(double x) {
        if (!isTruncated() && eventList.getSimTime() > getTruncationPoint()) {
            reset();
            truncated = true;
            startTime = getTruncationPoint();
        }
        super.newObservation(x);
    }
    
    public boolean isTruncated() {
        return truncated;
    }

    public double getTruncationPoint() {
        return truncationPoint;
    }

    public void setTruncationPoint(double truncationPoint) {
        this.truncationPoint = truncationPoint;
    }
    
}
