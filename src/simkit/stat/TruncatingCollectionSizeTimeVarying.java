package simkit.stat;

/**
 * @version $Id: TruncatingCollectionSizeTimeVarying.java 1000 2007-02-15 19:43:11Z ahbuss $
 * @author ahbuss
 */
public class TruncatingCollectionSizeTimeVarying extends CollectionSizeTimeVaryingStats {
    
    protected boolean truncated;
    
    private double truncationPoint;

    public TruncatingCollectionSizeTimeVarying(String name, double truncationPoint) {
        super(name);
        setTruncationPoint(truncationPoint);
        truncated = false;
    }

    public TruncatingCollectionSizeTimeVarying(double truncationPoint) {
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
