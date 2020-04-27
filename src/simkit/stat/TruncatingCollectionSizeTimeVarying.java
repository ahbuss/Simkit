package simkit.stat;

/**
 * 
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
    
    @Override
    public void reset() {
        super.reset();
        truncated = false;
    }
    
    @Override
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
