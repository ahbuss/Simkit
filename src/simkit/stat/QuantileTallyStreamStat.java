package simkit.stat;

import java.util.Collections;
import java.util.PriorityQueue;

/**
 *
 * @author arnoldbuss
 */
public class QuantileTallyStreamStat extends SimpleStatsTally implements QuantileSampleStatistics {

    private static final int DEFAULT_PERCENTILE = 50;

    private int percentile;

    private PriorityQueue<Double> lower;

    private PriorityQueue<Double> upper;

    public QuantileTallyStreamStat() {
        this(DEFAULT_NAME);
    }

    public QuantileTallyStreamStat(String name) {
        this(name, DEFAULT_PERCENTILE);
    }

    public QuantileTallyStreamStat(String name, int percentile) {
        super(name);
        if (percentile < 1 || percentile > 99) {
            throw new IllegalArgumentException("percentile must be ∈ [1,99]: " + percentile);
        }

        this.percentile = percentile;

        this.upper = new PriorityQueue<>();

        this.lower = new PriorityQueue<>(Collections.reverseOrder());
    }

    @Override
    public void reset() {
        super.reset();
        if (lower != null) {
            this.lower.clear();
        }
        if (upper != null) {
            this.upper.clear();
        }
    }

    @Override
    public void newObservation(double value) {
        super.newObservation(value);

        if (lower.isEmpty() || value < lower.peek()) {
            lower.add(value);
        } else {
            upper.add(value);
        }

        int targetRank = getTargetRank();

        while (lower.size() > targetRank) {
            double lowerRemove = lower.remove();
            upper.add(lowerRemove);
        }

        while (lower.size() < targetRank) {
            double upperRemove = upper.remove();
            lower.add(upperRemove);
        }

    }

    @Override
    public double getQuantile() {
        return lower.isEmpty() ? Double.NEGATIVE_INFINITY : lower.element();
    }

    private int getTargetRank() {
        long numerator = (long) (percentile * getCount());
        return (int) ((numerator + 99) / 100);
    }

    /**
     * @return the percentile
     */
    public int getPercentile() {
        return percentile;
    }

}
