package simkit.test;

import simkit.random.RandomVariate;
import simkit.random.RandomVariateFactory;

/**
 * 
 * @author ahbuss
 */
public class TestTriangle {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        RandomVariate rv = RandomVariateFactory.getInstance(
                "Triangle", 2.0, 2.0, 3.0);
        System.out.println(rv);
    }

}
