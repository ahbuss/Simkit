package simkit.test;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;
import simkit.stat.Histogram;
/**
 *
 * @author  Arnold Buss
 */
public class TestResample {
    
    public static void main(String[] args) {
        double[] data = new double[] { 5, 8, 3, 1, 10, 4 , -1, -2};
        RandomVariate rs = RandomVariateFactory.getInstance("Resample",
            new Object[] { data });
        System.out.println(rs);
        
        for (int i = 0; i < 10; ++i) {
            System.out.println(rs.generate());
        }
        
        Histogram hist = new Histogram("test", 10);
        Histogram hist2 = new Histogram("test2", -2, 10.0, 12);
        for (int i = 0; i < 500000; ++i) {
            double val = rs.generate();
            hist.newObservation(val);
            hist2.newObservation(val);
        }
        System.out.println("theoretical prob: " + (1.0 / data.length));
        System.out.println(hist);
        System.out.println(hist2);
        
        Double[] vals = {3.141, 7.2, 3.5, 8.9};
        RandomVariate rv = RandomVariateFactory.getInstance("Resample", new Object[] { vals });
        System.out.println(rv);
    }
    
}
