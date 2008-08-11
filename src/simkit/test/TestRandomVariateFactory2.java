package simkit.test;

import java.util.HashMap;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;

/**
 * @version $Id$
 * @author abuss
 */
public class TestRandomVariateFactory2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String name = "Gamma";
        double alpha = 2.6;
        double beta = 1.2;
        RandomVariate rv = RandomVariateFactory.getInstance(
                name, alpha, beta);
        System.out.println(rv);
        for (int i = 0; i < 3; ++i) {
            System.out.println(rv.generate());
        }
        
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("alpha", 3.4);
        params.put("beta", 1.3);
        params.put("foo", "bar");
        
        rv = RandomVariateFactory.getInstance(name, params);
        System.out.println(rv);
        
        double[] values = new double[] { 2, 4, 6};
        double[] frequencies = new double[] { 30, 40, 50};
        
        params.put("values", values);
        params.put("probabilities", frequencies);
        rv = RandomVariateFactory.getInstance("Discrete", params);
        System.out.println(rv);
    }

}
