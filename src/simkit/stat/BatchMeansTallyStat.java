package simkit.stat;

import simkit.stat.SavedStats;
import java.util.LinkedHashMap;
import java.util.Map;
import simkit.stat.AbstractSimpleStats;
import simkit.stat.SampleStatistics;
import simkit.stat.SamplingType;
import simkit.stat.SimpleStatsTally;

/**
 * Uses batch means method with initial transient to estimate the mean of a 
 * presumably steady-state (after the truncationpoint) measure.  
 * @version $Id: BatchMeansTallyStat.java 1000 2007-02-15 19:43:11Z ahbuss $
 * @author ahbuss
 */
public class BatchMeansTallyStat extends SimpleStatsTally {
    
    private int truncationPoint;
    private int batchSize;
    
    protected int nextBatchID;
    protected SampleStatistics currentBatch;
    protected SampleStatistics transientStats;
    protected Map<Integer, SampleStatistics> batches;
    
    /**
     * @param name The property to be listened to
     * @param truncationPoint The initial transient point
     * @param batchSize size of batches after truncation
     */
    public BatchMeansTallyStat(String name, int truncationPoint, int batchSize) {
        setName(name);
        setTruncationPoint(truncationPoint);
        setBatchSize(batchSize);
        currentBatch = new SimpleStatsTally("Transient");
        transientStats = currentBatch;
        batches = new LinkedHashMap<Integer, SampleStatistics>();
        nextBatchID = 0;
    }
    
    /**
     * Update the currentBatch stats.  If truncationPoint reached, save currentStats
     * in transientStats and start the first batch; else if another batch is reached,
     * save that in the batches Map and start another batch
     * @param x the new observation
     */
    public void newObservation(double x) {
        currentBatch.newObservation(x);
        if (getNextBatchID() == 0 && currentBatch.getCount() == getTruncationPoint()) {
            transientStats = new SavedStats(currentBatch);
            nextBatch();
        }
        else if (getNextBatchID() > 0 && currentBatch.getCount() == getBatchSize()) {
            batches.put(new Integer(getNextBatchID()), new SavedStats(currentBatch));
            super.newObservation(currentBatch.getMean());
            nextBatch();
        }
    }
    
    /**
     * Increment nextBatchID and set currentBact to a new SimpleStatsTally
     */
    protected void nextBatch() {
        nextBatchID += 1;
        currentBatch = new SimpleStatsTally("Batch-" + getNextBatchID());
    }
    
    /** @return the truncation point.
     */
    public int getTruncationPoint() {
        return truncationPoint;
    }
    
    /** 
     * The measure is assumed to be in "steady-state" after this many
     * observations
     * @param truncationPoint the truncation point
     * @throws IllegalArgumentException if truncationPoint < 0
     */
    public void setTruncationPoint(int truncationPoint) {
        if (truncationPoint < 0) {
            throw new IllegalArgumentException("truncationPoint must be >= 0: " +
                    truncationPoint);
        }
        this.truncationPoint = truncationPoint;
    }
    
    /** @return size of each batch after truncation
     */
    public int getBatchSize() {
        return batchSize;
    }
    
    /**
     * @param batchSize size of each batch after truncation
     * @throws IllegalArgumentException if batchSize <= 0
     */
    public void setBatchSize(int batchSize) {
        if (batchSize <= 0) {
            throw new IllegalArgumentException("batchSize must be > 0: " +
                    batchSize);
        }
        this.batchSize = batchSize;
    }

    /** @return next batch number
     */
    public int getNextBatchID() {
        return nextBatchID;
    }

    /** @return immutable copy of current batch stats
     */
    public SampleStatistics getCurrentBatch() {
        return new SavedStats(currentBatch);
    }

    /** @return immutable copy of transient stats
     */
    public SampleStatistics getTransientStats() {
        return new SavedStats(transientStats);
    }

    /** @return shallow copy of currently saved batch stats
     */
    public Map<Integer, SampleStatistics> getBatches() {
        return new LinkedHashMap<Integer, SampleStatistics>(batches);
    }   
}