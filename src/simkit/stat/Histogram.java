package simkit.stat;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.text.DecimalFormat;

public class Histogram extends SimpleStatsTally {

    private int[] frequency;
    private int total;
    private double lowValue;
    private double cellWidth;

    public Histogram(String name, int number) {
        this(name, 0, (double) number, number);
    }

    public Histogram(String name, double lowVal, double hiVal, int number) {
        super(name);
        frequency = new int[number + 1];
        lowValue = lowVal;
        cellWidth = (hiVal - lowVal) /  number;
    }
    
    public void newObservation(double x) {
        super.newObservation(x);
        int index = (int) Math.floor(( x - lowValue) / cellWidth);
        index = Math.min( Math.max(index, 0), frequency.length - 1);
        frequency[index]++;
        total++;
    }
    
    public void reset() {
        if (frequency != null) {
            frequency = new int[frequency.length];
        }
        total = 0;
    }

    public int[] getFrequencies() { return (int[]) frequency.clone(); }

    public double[] getProportions() {
        double[] temp = new double[frequency.length];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = frequency[i] / (double) total;
        }
        return temp;
    }

    public String toString() {
        DecimalFormat form = new DecimalFormat("0.000");
        StringBuffer buf = new StringBuffer(super.toString());
        buf.append("\nvalue\tFrequencies\tProportions\n");
        double[] prop = getProportions();
        for (int i = 0; i < frequency.length; i++ ) {
//            buf.append(i);
//            buf.append('\t');
            buf.append(form.format(lowValue + cellWidth * i));
            buf.append('\t');
            buf.append(frequency[i]);
            buf.append('\t');
            buf.append(prop[i]);
            buf.append(SampleStatistics.EOL);
        }
        return buf.toString();
    }

}
