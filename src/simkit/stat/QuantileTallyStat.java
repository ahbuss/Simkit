package simkit.stat;

import static java.lang.Double.NaN;
import static java.lang.Double.isNaN;
import static java.lang.Math.floor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author ahbuss
 */
public class QuantileTallyStat extends SimpleStatsTally {

    private List<Double> rawData;

    /**
     *
     * @param name
     */
    public QuantileTallyStat(String name) {
        super(name);
        this.rawData = new ArrayList<>();
    }

    public QuantileTallyStat() {
        this(DEFAULT_NAME);
    }

    @Override
    public void reset() {
        super.reset();
        if (this.rawData == null) {
            this.rawData = new ArrayList<>();
        }
        this.rawData.clear();
    }

    @Override
    public void newObservation(double x) {
        if (!isNaN(x)) {
            super.newObservation(x);
            this.rawData.add(x);
        }
    }

    /**
     * Uses "R8" method for calculating quantile of data. The raw data are
     * left unchanged - a copy is sorted and used to determine the quantile.
     * @param q Given quantile requested of data - must be ∈ [0,1]
     * @return q-th quantile
     * @throws IllegalArgumentException if q ∉ [0,1]
     */
    public double getQuantile(double q) {
        if (q < 0.0 || q > 1.0) {
            throw new IllegalArgumentException(String.format("p must be ∈ [0,1]: %f", q));
        }
        Double[] copy = rawData.toArray(new Double[0]);
        double quantile = NaN;
        double index;
        Arrays.sort(copy);
        if (copy.length > 0) {
            if (q == 0.0) {
                quantile = copy[0];
            } else if (q == 1.0) {
                quantile = copy[copy.length - 1];
            } else {
                double min = 2.0 * (1.0 / 3.0) / (copy.length + 1.0 / 3.0);
                double max = (copy.length - 1.0 / 3.0) / (copy.length + 1.0 / 3.0);
                if (q < min) {
                    index = 0;
                } else if (q >= max) {
                    index = copy.length;
                } else {
                    index = (copy.length + 1.0 / 3.0) * q + 1.0 / 3.0;
                }
                int pos = (int) floor(index);
                double delta = index - pos;
                if (pos < 1) {
                    quantile = copy[0];
                } else if (pos >= copy.length) {
                    quantile = copy[copy.length - 1];
                } else {
                    double low = copy[pos - 1];
                    double high = copy[pos];
                    quantile = low + delta * (high - low);
                }
            }

        }
        return quantile;
    }

    /**
     * @return the rawData
     */
    public List<Double> getRawData() {
        return new ArrayList<>(rawData);
    }

}
