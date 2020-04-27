package simkit.stat;

/**
 * A class that collects statistics that can be used to produce a histogram.
 * This class does not draw the histogram, it simply calculates frequency and
 * proportion data.
 *
 * 
 *
 */
public class Histogram extends SimpleStatsTally {

    private double[] values;

    /**
     * Contains the number of observations that fall into each bin.
     *
     */
    private int[] frequency;

    /**
     * Contains the total number of observations.
     *
     */
    private int total;

    /**
     * Contains the lower bound of the first bin.
     *
     */
    private double lowValue;

    /**
     * Contains the bin width.
     *
     */
    private double cellWidth;

    /**
     * Creates a new Histogram with a lower bound of 0 and a bin width of 1.
     * This implies that the upper bound is equal to the number of bins.
     *
     * @param name The name of the property for which this Histogram collects
     * statistics.
     * @param number The number of bins.
     *
     */
    public Histogram(String name, int number) {
        this(name, 0, number, number);
    }

    /**
     * Creates a new Histogram.
     *
     * @param name The name of the property for which this Histogram collects
     * statistics.
     * @param lowVal The lower bound of the first bin.
     * @param hiVal The upper bound of the last bin.
     * @param number The number of bins.
     *
     */
    public Histogram(String name, double lowVal, double hiVal, int number) {
        super(name);
        frequency = new int[number + 1];
        lowValue = lowVal;
        cellWidth = (hiVal - lowVal) / number;
        values = new double[frequency.length];
        for (int i = 0; i < frequency.length; ++i) {
            values[i] = lowVal + i * cellWidth;
        }
    }

    @Override
    public void newObservation(double x) {
        super.newObservation(x);
        int index = (int) Math.floor((x - lowValue) / cellWidth);
        index = Math.min(Math.max(index, 0), frequency.length - 1);
        frequency[index]++;
        total++;
    }

    @Override
    public void reset() {
        if (frequency != null) {
            frequency = new int[frequency.length];
        }
        total = 0;
    }

    /**
     * @return a copy of the array of bin frequencies
     */
    public int[] getFrequencies() {
        return frequency.clone();
    }

    /**
     * Note that if any observations were less than the lower bound or greater
     * than the upper bound, then the proportions will not add up to 1.
     *
     * @return an array containing the proportion of observations in each bin.
     */
    public double[] getProportions() {
        double[] temp = new double[frequency.length];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = frequency[i] / (double) total;
        }
        return temp;
    }

    public double[] getScaledProportions() {
        double[] scaled = getProportions();
        for (int i = 0; i < scaled.length; ++i) {
            scaled[i] /= cellWidth;
        }
        return scaled;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder(super.toString());
        buf.append(EOL);
        buf.append("value\tFrequencies\tProportions");
        double[] scaledProportions = getScaledProportions();
        for (int i = 0; i < frequency.length; i++) {
            buf.append(EOL);
            buf.append(String.format("%.3f\t", values[i]));
            buf.append(String.format("%d\t\t", frequency[i]));
            buf.append(String.format("%.3f", scaledProportions[i]));
        }
        return buf.toString();
    }

    /**
     * @return the values
     */
    public double[] getValues() {
        return values.clone();
    }

    /**
     * @return the frequency
     */
    public int[] getFrequency() {
        return frequency.clone();
    }

    /**
     * @return the total
     */
    public int getTotal() {
        return total;
    }

    /**
     * @return the lowValue
     */
    public double getLowValue() {
        return lowValue;
    }

    /**
     * @return the cellWidth
     */
    public double getCellWidth() {
        return cellWidth;
    }

}
