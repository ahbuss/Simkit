package simkit.test;

import java.net.URL;
import simkit.random.DataVariate;
import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;

/**
 *
 * @author ahbuss
 */
public class TestDataVariate {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        URL url = TestDataVariate.class.getResource("output.txt");
        String file = url.getFile();
        RandomVariate dataVariate = RandomVariateFactory.getInstance("Data", file);
        System.out.println(dataVariate);
        
        for (int i = 0; i < 20; ++i) {
            System.out.println(dataVariate.generate());
        }
        System.out.println("-------------------");
        ((DataVariate) dataVariate).reset();
        for (int i = 0; i < 20; ++i) {
            System.out.println(dataVariate.generate());
        }
        
        ((DataVariate) dataVariate).close();
    }

}
