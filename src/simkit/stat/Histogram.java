package simkit.stat;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class Histogram implements PropertyChangeListener {

    private String name;
    private int[] frequency;
    private int total;

    public Histogram(String name, int number) {
        this.name = name;
        frequency = new int[number + 1];
    }

    public void propertyChange(PropertyChangeEvent e) {
        if (e.getPropertyName().equals(name) ){
            frequency[ (Math.min(((Number) e.getNewValue()).intValue(), frequency.length - 1))]++;
            total++;
        }
    }

    public void reset() {
        frequency = new int[frequency.length];
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
        StringBuffer buf = new StringBuffer(name + "\nindex\tFrequencies\tProportions\n");
        double[] prop = getProportions();
        for (int i = 0; i < frequency.length; i++ ) {
            buf.append(i);
            buf.append('\t');
            buf.append(frequency[i]);
            buf.append('\t');
            buf.append(prop[i]);
            buf.append(SampleStatistics.EOL);
        }
        return buf.toString();

    }

}
